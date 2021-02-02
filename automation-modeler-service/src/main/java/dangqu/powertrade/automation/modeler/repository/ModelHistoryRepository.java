package dangqu.powertrade.automation.modeler.repository;

import java.util.List;

import dangqu.powertrade.automation.modeler.domain.ModelHistory;


public interface ModelHistoryRepository {

    void save(ModelHistory modelHistory);

    void delete(ModelHistory modelHistory);

    List<ModelHistory> findByModelId(String modelId);

}
