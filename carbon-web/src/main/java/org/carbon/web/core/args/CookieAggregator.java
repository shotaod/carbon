package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.Cookie;
import org.carbon.web.container.ArgumentMeta;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class CookieAggregator implements ArgumentAggregator<Cookie> {
    @Assemble
    private KeyValueMapper keyValueMapper;

    @Override
    public Class<Cookie> target() {
        return Cookie.class;
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        Map<String, Object> cookies = Arrays.stream(request.getCookies())
                .collect(Collectors.toMap(
                        javax.servlet.http.Cookie::getName,
                        javax.servlet.http.Cookie::getValue
                ));
        Object value = keyValueMapper.mapAndConstruct(cookies, parameter.getType());
        return new ArgumentMeta(parameter, value);
    }
}
