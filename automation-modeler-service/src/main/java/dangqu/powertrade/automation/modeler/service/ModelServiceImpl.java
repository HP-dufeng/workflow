package dangqu.powertrade.automation.modeler.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dangqu.powertrade.automation.common.service.exception.InternalServerErrorException;
import dangqu.powertrade.automation.common.service.exception.NotFoundException;
import dangqu.powertrade.automation.modeler.domain.Model;
import dangqu.powertrade.automation.modeler.domain.ModelHistory;
import dangqu.powertrade.automation.modeler.model.ModelKeyRepresentation;
import dangqu.powertrade.automation.modeler.model.ModelRepresentation;
import dangqu.powertrade.automation.modeler.repository.ModelHistoryRepository;
import dangqu.powertrade.automation.modeler.repository.ModelRepository;
import dangqu.powertrade.automation.modeler.serviceapi.ModelService;

@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelServiceImpl.class);

    protected static final String PROCESS_NOT_FOUND_MESSAGE_KEY = "PROCESS.ERROR.NOT-FOUND";

    @Autowired
    protected ModelRepository modelRepository;

    @Autowired
    protected ModelHistoryRepository modelHistoryRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Override
    public Model getModel(String modelId) {
        Model model = modelRepository.get(modelId);
        
        return model;
    }

    @Override
    public ModelRepresentation getModelRepresentation(String modelId) {
        Model model = getModel(modelId);

        if (model == null) {
            NotFoundException modelNotFound = new NotFoundException("No model found with the given id: " + modelId);
            modelNotFound.setMessageKey(PROCESS_NOT_FOUND_MESSAGE_KEY);
            throw modelNotFound;
        }

        return new ModelRepresentation(model);
    }

    @Override
    public ModelKeyRepresentation validateModelKey(Model model, Integer modelType, String key) {
        ModelKeyRepresentation modelKeyResponse = new ModelKeyRepresentation();
        modelKeyResponse.setKey(key);

        List<Model> models = modelRepository.findByKeyAndType(key, modelType);
        for (Model modelInfo : models) {
            if (model == null || !modelInfo.getId().equals(model.getId())) {
                modelKeyResponse.setKeyAlreadyExists(true);
                modelKeyResponse.setId(modelInfo.getId());
                modelKeyResponse.setName(modelInfo.getName());
                break;
            }
        }

        return modelKeyResponse;
    }

    @Override
    public Model saveModel(ModelRepresentation model, String updatedBy) {
        Model modelObject = getModel(model.getId());
        modelObject.setLastUpdated(new Date());
            modelObject.setLastUpdatedBy(updatedBy);
            modelObject.setName(model.getName());
            modelObject.setDescription(model.getDescription());
            modelObject.setModelEditorJson(model.getModelEditorJson());


        return persistModel(modelObject, false);
    }

    @Override
    public void deleteModel(String modelId) {
        Model model = modelRepository.get(modelId);
        if (model == null) {
            NotFoundException modelNotFound = new NotFoundException("No model found with the given id: " + modelId);
            modelNotFound.setMessageKey(PROCESS_NOT_FOUND_MESSAGE_KEY);
            throw modelNotFound;
        }


        // Move model to history and mark removed
        ModelHistory historyModel = createNewModelhistory(model);
        historyModel.setRemovalDate(Calendar.getInstance().getTime());
        persistModelHistory(historyModel);

        modelRepository.delete(model);

    }

    protected ModelHistory createNewModelhistory(Model model) {
        ModelHistory historyModel = new ModelHistory();
        historyModel.setName(model.getName());
        historyModel.setKey(model.getKey());
        historyModel.setDescription(model.getDescription());
        historyModel.setCreated(model.getCreated());
        historyModel.setLastUpdated(model.getLastUpdated());
        historyModel.setCreatedBy(model.getCreatedBy());
        historyModel.setLastUpdatedBy(model.getLastUpdatedBy());
        historyModel.setModelEditorJson(model.getModelEditorJson());
        historyModel.setModelType(model.getModelType());
        historyModel.setVersion(model.getVersion());
        historyModel.setModelId(model.getId());
        historyModel.setComment(model.getComment());
        historyModel.setTenantId(model.getTenantId());

        return historyModel;
    }

    protected void persistModelHistory(ModelHistory modelHistory) {
        modelHistoryRepository.save(modelHistory);
    }

    protected Model persistModel(Model model, Boolean isNew) {

        if (StringUtils.isNotEmpty(model.getModelEditorJson())) {

            // Parse json to java
            try {
                objectMapper.readTree(model.getModelEditorJson());
            } catch (Exception e) {
                LOGGER.error("Could not deserialize json model", e);
                throw new InternalServerErrorException("Could not deserialize json model");
            }

            

            modelRepository.save(model, isNew);

        }

        return model;
    }

    @Override
    public Model createModel(ModelRepresentation model, String createdBy) {
        Model newModel = new Model();
        newModel.setId(model.getId());
        newModel.setVersion(1);
        newModel.setName(model.getName());
        newModel.setKey(model.getKey());
        newModel.setModelType(model.getModelType());
        newModel.setCreated(Calendar.getInstance().getTime());
        newModel.setCreatedBy(createdBy);
        newModel.setDescription(model.getDescription());
        newModel.setModelEditorJson(model.getModelEditorJson());
        newModel.setLastUpdated(Calendar.getInstance().getTime());
        newModel.setLastUpdatedBy(createdBy);
        newModel.setTenantId(model.getTenantId());

        persistModel(newModel, true);
        return newModel;
    }

    

    
}
