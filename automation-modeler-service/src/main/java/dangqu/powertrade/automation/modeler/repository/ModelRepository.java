package dangqu.powertrade.automation.modeler.repository;

import java.util.List;

import dangqu.powertrade.automation.modeler.domain.Model;

public interface ModelRepository {
    void save(Model model, Boolean isNew);

    void delete(Model model);

    Model get(String id);

    List<Model> findByKeyAndType(String key, Integer modelType);
}
