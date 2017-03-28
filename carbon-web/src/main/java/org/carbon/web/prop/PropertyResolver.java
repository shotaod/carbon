package org.carbon.web.prop;

import java.util.List;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.mapper.PropertyMapper;
import org.carbon.web.annotation.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/03/06.
 */
@Configuration
public class PropertyResolver {

    private static Logger logger = LoggerFactory.getLogger(PropertyResolver.class);

    @Inject
    private PropertyMapper propertyMapper;

    @Assemble({Property.class})
    private List<Object> props;

    @AfterInject
    public void resolveProperty() {
        if (logger.isInfoEnabled()) {
            props.forEach(prop -> logger.info(prop.getClass().getCanonicalName()));
        }
        props.forEach(prop -> {
            Property propertyAnnotation = prop.getClass().getDeclaredAnnotation(Property.class);
            String key = propertyAnnotation.key();
            propertyMapper.findAndMapping(key, prop);
        });
    }
}
