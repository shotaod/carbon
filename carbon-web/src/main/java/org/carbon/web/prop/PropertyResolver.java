package org.carbon.web.prop;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.web.annotation.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2017/03/06.
 */
@Configuration
public class PropertyResolver {

    private static Logger logger = LoggerFactory.getLogger(PropertyResolver.class);

    @Inject
    private EnvironmentMapper envMapper;

    @Assemble({Property.class})
    private List<Object> props;

    @AfterInject
    public void resolveProperty() {
        if (logger.isInfoEnabled()) {
            String classes = props.stream().map(prop -> "-" + prop.getClass().getName()).collect(Collectors.joining("\n"));
            logger.info("Resolve Property Classes By environment values Below\n{}", classes);
        }
        props.forEach(prop -> {
            Property propertyAnnotation = prop.getClass().getDeclaredAnnotation(Property.class);
            String key = propertyAnnotation.key();
            envMapper.apply(key, prop);
        });
    }
}
