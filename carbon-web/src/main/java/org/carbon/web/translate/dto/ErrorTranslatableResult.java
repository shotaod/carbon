package org.carbon.web.translate.dto;

/**
 * @author Shota.Oda 2018/02/19.
 */
public class ErrorTranslatableResult<TRANSLATE extends Translatable> {
    private int code;
    private TRANSLATE translatable;

    public ErrorTranslatableResult(int code, TRANSLATE translatable) {
        this.code = code;
        this.translatable = translatable;
    }

    public int getCode() {
        return code;
    }

    public TRANSLATE getTranslatable() {
        return translatable;
    }
}
