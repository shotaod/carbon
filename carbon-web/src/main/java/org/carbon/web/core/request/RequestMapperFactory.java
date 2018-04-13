package org.carbon.web.core.request;

import java.util.Optional;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;

/**
 * @author Shota Oda 2016/11/29.
 */
@Component
public class RequestMapperFactory {

    // -----------------------------------------------------
    //                              applicable content types
    //                              ------------------------
    private static final String APPLICATION_JSON = "application/json";
    private static final String MULTIPART_FORM = "multipart/form-data";
    private static final String X_WWW_FORM_URL_ENCODE = "application/x-www-form-urlencoded";

    @Assemble
    private JsonRequestMapper jsonRequestMapper;
    @Assemble
    private MultipartFormKeyValueRequestMapper multipartFormKeyValueRequestMapper;
    @Assemble
    private FormUrlEncodeRequestMapper formUrlEncodeRequestMapper;


    public Optional<TypeSafeRequestMapper> factorize(ContentType contentType) {
        String media = contentType.getMediaType();
        if (APPLICATION_JSON.equals(media)) {
            return Optional.of(jsonRequestMapper);
        }

        if (MULTIPART_FORM.equals(media)) {
            return Optional.of(multipartFormKeyValueRequestMapper);
        }

        if (X_WWW_FORM_URL_ENCODE.equals(media) || media == null) {
            return Optional.of(formUrlEncodeRequestMapper);
        }

        return Optional.empty();
    }
}
