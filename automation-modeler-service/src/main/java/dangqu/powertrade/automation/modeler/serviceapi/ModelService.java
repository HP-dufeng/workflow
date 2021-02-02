package dangqu.powertrade.automation.modeler.serviceapi;

import dangqu.powertrade.automation.modeler.domain.Model;
import dangqu.powertrade.automation.modeler.model.ModelKeyRepresentation;
import dangqu.powertrade.automation.modeler.model.ModelRepresentation;

public interface ModelService {
    
    Model getModel(String modelId);
    void deleteModel(String modelId);

    ModelRepresentation getModelRepresentation(String modelId);
    ModelKeyRepresentation validateModelKey(Model model, Integer modelType, String key);

    Model createModel(ModelRepresentation model, String createdBy);
    Model saveModel(ModelRepresentation model, String updatedBy);

}
