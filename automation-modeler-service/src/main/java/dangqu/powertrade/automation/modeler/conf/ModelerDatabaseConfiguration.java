package dangqu.powertrade.automation.modeler.conf;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.flowable.common.engine.api.FlowableException;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import dangqu.powertrade.automation.common.service.exception.InternalServerErrorException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Configuration(proxyBeanMethods = false)
public class ModelerDatabaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelerDatabaseConfiguration.class);

    protected static final String LIQUIBASE_CHANGELOG_PREFIX = "PT_DE_";


    @Autowired
    protected ResourceLoader resourceLoader;

    protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();

    public static final String DATABASE_TYPE_H2 = "h2";
    public static final String DATABASE_TYPE_POSTGRES = "postgres";

    public static Properties getDefaultDatabaseTypeMappings() {
        Properties databaseTypeMappings = new Properties();
        databaseTypeMappings.setProperty("H2", DATABASE_TYPE_H2);

        databaseTypeMappings.setProperty("PostgreSQL", DATABASE_TYPE_POSTGRES);
        
        return databaseTypeMappings;
    }

    @Bean
    @Qualifier("automationModeler")
    public SqlSessionFactory modelerSqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        String databaseType = initDatabaseType(dataSource);
        if (databaseType == null) {
            throw new FlowableException("couldn't deduct database type");
        }

        try {
            Properties properties = new Properties();
            properties.put("prefix", "");
            properties.put("blobType", "BLOB");
            properties.put("boolValue", "TRUE");

            properties.load(this.getClass().getClassLoader().getResourceAsStream("dangqu/powertrade/automation/db/properties/" + databaseType + ".properties"));

            sqlSessionFactoryBean.setConfigurationProperties(properties);
            sqlSessionFactoryBean
                    .setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath:/META-INF/modeler-mybatis-mappings/*.xml"));
            sqlSessionFactoryBean.afterPropertiesSet();
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            throw new FlowableException("Could not create sqlSessionFactory", e);
        }

    }

    @Bean(destroyMethod = "clearCache") // destroyMethod: see https://github.com/mybatis/old-google-code-issues/issues/778
    @Qualifier("automationModeler")
    public SqlSessionTemplate modelerSqlSessionTemplate(@Qualifier("automationModeler") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Qualifier("automationModeler")
    public Liquibase modelerLiquibase(DataSource dataSource) {
        LOGGER.info("Configuring Liquibase");

        Liquibase liquibase = null;
        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            database.setDatabaseChangeLogTableName(LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogTableName());
            database.setDatabaseChangeLogLockTableName(LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogLockTableName());

            liquibase = new Liquibase("META-INF/liquibase/automation-modeler-service-db-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("automation");
            return liquibase;

        } catch (Exception e) {
            throw new InternalServerErrorException("Error creating liquibase database", e);
        } finally {
            closeDatabase(liquibase);
        }
    }

    protected String initDatabaseType(DataSource dataSource) {
        String databaseType = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String databaseProductName = databaseMetaData.getDatabaseProductName();
            LOGGER.info("database product name: '{}'", databaseProductName);
            databaseType = databaseTypeMappings.getProperty(databaseProductName);
            if (databaseType == null) {
                throw new FlowableException("couldn't deduct database type from database product name '" + databaseProductName + "'");
            }
            LOGGER.info("using database type: {}", databaseType);

        } catch (SQLException e) {
            LOGGER.error("Exception while initializing Database connection", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Exception while closing the Database connection", e);
            }
        }

        return databaseType;
    }

    private void closeDatabase(Liquibase liquibase) {
        if (liquibase != null) {
            Database database = liquibase.getDatabase();
            if (database != null) {
                try {
                    database.close();
                } catch (DatabaseException e) {
                    LOGGER.warn("Error closing database", e);
                }
            }
        }
    }

}

