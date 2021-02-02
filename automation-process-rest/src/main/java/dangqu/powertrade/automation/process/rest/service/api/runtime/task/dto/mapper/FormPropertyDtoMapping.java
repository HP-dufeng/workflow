package dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto.mapper;

import org.flowable.engine.impl.form.FormPropertyImpl;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto.FormPropertyDto;
import dangqu.powertrade.spring.boot.modelmapper.PropertyMapConfigurerSupport;

@Component
public class FormPropertyDtoMapping extends PropertyMapConfigurerSupport<FormPropertyImpl, FormPropertyDto> {

    @Override
    public PropertyMap<FormPropertyImpl, FormPropertyDto> mapping() {
        return new PropertyMap<FormPropertyImpl,FormPropertyDto>(){
            @Override
            protected void configure() {
                map().setType(source.getType().getName());
            }
        };
    }
    
}
