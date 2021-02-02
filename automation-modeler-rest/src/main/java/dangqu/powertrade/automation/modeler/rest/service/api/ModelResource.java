package dangqu.powertrade.automation.modeler.rest.service.api;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dangqu.powertrade.automation.modeler.domain.Model;
import dangqu.powertrade.automation.modeler.model.ModelRepresentation;
import dangqu.powertrade.automation.modeler.rest.service.api.dto.SaveModelRequestDto;
import dangqu.powertrade.automation.modeler.serviceapi.ModelService;
import dangqu.powertrade.security.conf.constants.RequestHeaderConstants;
import dangqu.powertrade.security.conf.security.SecurityUtils;
import dangqu.powertrade.security.conf.tenant.TenantProvider;

@RestController
public class ModelResource implements RequestHeaderConstants {

    // private static final Logger LOGGER = LoggerFactory.getLogger(ModelResource.class);

    // private static final String RESOLVE_ACTION_OVERWRITE = "overwrite";
    // private static final String RESOLVE_ACTION_SAVE_AS = "saveAs";
    // private static final String RESOLVE_ACTION_NEW_VERSION = "newVersion";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelService modelService;

    // @Autowired
    // private WebClient webClient;

    @Autowired
    private TenantProvider tenantProvider;

    @GetMapping(value = "/rest/models/{modelId}")
    public ModelRepresentation getModel(@PathVariable String modelId) {

        // Object block = webClient
        // .get()
        // .uri("https://test.powertrade.lumicable.cn/tenantmanagement/api/app/customOption")
        // .headers(headers -> headers.set(TEANT_REQUEST_HEADER,
        // tenantProvider.getTenantId()))
        // .attributes(clientRegistrationId("permission_manage"))
        // .retrieve()
        // .bodyToMono(Object.class)
        // .block();

        return modelService.getModelRepresentation(modelId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/rest/models/{modelId}")
    public void deleteModel(@PathVariable String modelId) {

        modelService.deleteModel(modelId);
    }

    // protected void checkForDuplicateKey(ModelRepresentation modelRepresentation) {
    //     ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, modelRepresentation.getModelType(),
    //             modelRepresentation.getKey());
    //     if (modelKeyInfo.isKeyAlreadyExists()) {
    //         throw new ConflictingRequestException("Provided model key already exists: " + modelRepresentation.getKey());
    //     }
    // }

    @PostMapping(value = "/rest/models")
    public ModelRepresentation saveModel(@RequestBody SaveModelRequestDto dto, HttpServletRequest request) {

        Model model = modelService.getModel(dto.getId());
        if (model == null) {
            ModelRepresentation modelRepresentation = new ModelRepresentation();
            modelRepresentation.setId(dto.getId());
            modelRepresentation.setName(dto.getName());
            modelRepresentation.setKey(dto.getId());
            modelRepresentation.setDescription(dto.getDescription());
            modelRepresentation.setModelType(dto.getModelType());

            modelRepresentation.setModelEditorJson(dto.getModelEditorJson());
            modelRepresentation.setTenantId(tenantProvider.getTenantId());

            Model newModel = modelService.createModel(modelRepresentation, SecurityUtils.getCurrentUserId());
            return new ModelRepresentation(newModel);

        } else {
            ModelRepresentation modelRepresentation = new ModelRepresentation();
            modelRepresentation.setId(dto.getId());
            modelRepresentation.setName(dto.getName());
            modelRepresentation.setDescription(dto.getDescription());
            modelRepresentation.setModelEditorJson(dto.getModelEditorJson().toString());

            Model saveModel = modelService.saveModel(modelRepresentation, SecurityUtils.getCurrentUserId());

            return new ModelRepresentation(saveModel);
        }

    }

}
