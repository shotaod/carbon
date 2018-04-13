package org.carbon.web.translate.dto;

/**
 * @author Shota.Oda 2018/02/19.
 */
public class Text implements Translatable {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
