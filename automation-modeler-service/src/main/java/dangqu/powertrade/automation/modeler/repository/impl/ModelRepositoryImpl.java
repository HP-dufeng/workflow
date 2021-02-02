package dangqu.powertrade.automation.modeler.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dangqu.powertrade.automation.common.repository.UuidIdGenerator;

import dangqu.powertrade.automation.modeler.domain.Model;
import dangqu.powertrade.automation.modeler.repository.ModelRepository;
import dangqu.powertrade.security.conf.tenant.TenantProvider;

@Component
public class ModelRepositoryImpl implements ModelRepository {

    private static final String NAMESPACE = "dangqu.powertrade.automation.modeler.domain.Model.";

    @Autowired
    @Qualifier("automationModeler")
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected UuidIdGenerator idGenerator;

    @Autowired
    protected TenantProvider tenantProvider;


    @Override
    public void save(Model model, Boolean isNew) {
        if (isNew) {
            sqlSessionTemplate.insert(NAMESPACE + "insertModel", model);
        } else {
            sqlSessionTemplate.update(NAMESPACE + "updateModel", model);
        }

    }

    @Override
    public void delete(Model model) {
        sqlSessionTemplate.delete(NAMESPACE + "deleteModel", model);

    }

    @Override
    public Model get(String id) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectModel", id);
    }

    @Override
    public List<Model> findByKeyAndType(String key, Integer modelType) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        params.put("modelType", modelType);
        return findModelsByParameters(params);
    }

    protected List<Model> findModelsByParameters(Map<String, Object> parameters) {
        parameters.put("tenantId", tenantProvider.getTenantId());
        return sqlSessionTemplate.selectList(NAMESPACE + "selectModelByParameters", parameters);
    }
    
}
