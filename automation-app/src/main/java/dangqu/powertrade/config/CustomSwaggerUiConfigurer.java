package dangqu.powertrade.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class CustomSwaggerUiConfigurer {
    
    @Bean
    public Docket modelsApi() {
      return new Docket(DocumentationType.OAS_30)
          .groupName("model-resource")
          .apiInfo(new ApiInfoBuilder()
            .title("Automation Modeler API")
            .version("1.0")
            .build()
          )
          .select()
          .paths(regex(".*/rest/models.*"))
          .build();
    }

    @Bean
    public Docket processApi() {
      return new Docket(DocumentationType.OAS_30)
          .groupName("process-resource")
          .apiInfo(new ApiInfoBuilder()
            .title("Automation Process API")
            .description("Comming soon...")
            .version("1.0")
            .build()
          )
          .select()
          .paths(regex(".*/rest/process.*"))
          .build();
      }

}
