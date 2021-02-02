package dangqu.powertrade.automation.modeler.repository.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dangqu.powertrade.automation.common.repository.UuidIdGenerator;
import dangqu.powertrade.automation.modeler.domain.ModelHistory;
import dangqu.powertrade.automation.modeler.repository.ModelHistoryRepository;

@Component
public class ModelHistoryRepositoryImpl implements ModelHistoryRepository {

    private static final String NAMESPACE = "dangqu.powertrade.automation.modeler.domain.ModelHistory.";

    @Autowired
    @Qualifier("automationModeler")
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected UuidIdGenerator idGenerator;



    @Override
    public List<ModelHistory> findByModelId(String modelId) {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectModelHistoryByModelId", modelId);
    }

    @Override
    public void save(ModelHistory modelHistory) {
        if (modelHistory.getId() == null) {
            modelHistory.setId(idGenerator.generateId());
            sqlSessionTemplate.insert(NAMESPACE + "insertModelHistory", modelHistory);
        } else {
            sqlSessionTemplate.update(NAMESPACE + "updateModelHistory", modelHistory);
        }
    }

    @Override
    public void delete(ModelHistory modelHistory) {
        sqlSessionTemplate.delete(NAMESPACE + "deleteModelHistory", modelHistory);
    }

}

