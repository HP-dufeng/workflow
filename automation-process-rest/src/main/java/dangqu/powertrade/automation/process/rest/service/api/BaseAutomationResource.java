package dangqu.powertrade.automation.process.rest.service.api;

import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import dangqu.powertrade.automation.editor.language.json.service.EditorJsonService;
import dangqu.powertrade.spring.boot.modelmapper.MapperUtils;

@RestController
public class BaseAutomationResource {

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected FormService formService;

    @Autowired
    protected EditorJsonService editorJsonService;



    @Autowired
    protected ModelMapper modelMapper ;

    @Autowired
    protected MapperUtils mapper;
}
