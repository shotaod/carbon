package org.carbon.authentication.translator;

import org.carbon.web.translate.dto.Translatable;

/**
 * @author Shota.Oda 2018/03/02.
 */
public class SignedTranslatable<T extends Translatable> {
    private Integer code;
    private T translatable;

    public SignedTranslatable(Integer code, T translatable) {
        this.code = code;
        this.translatable = translatable;
    }

    public Integer getCode() {
        return code;
    }

    public T getTranslatable() {
        return translatable;
    }
}
