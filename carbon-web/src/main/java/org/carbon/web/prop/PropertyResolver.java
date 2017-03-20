package org.carbon.web.prop;

import java.util.List;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.mapper.PropertyMapper;
import org.carbon.web.annotation.Property;

/**
 * @author ubuntu 2017/03/06.
 */
@Configuration
public class PropertyResolver {

    @Inject
    private PropertyMapper propertyMapper;

    @Assemble({Property.class})
    private List<Object> props;

    @AfterInject
    public void hoge() {

    }
}
