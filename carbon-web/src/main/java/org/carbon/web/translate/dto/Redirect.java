package org.carbon.web.translate.dto;

/**
 * @author Shota.Oda 2018/02/19.
 */
public final class Redirect implements Transfer {
    private String redirectTo;

    public Redirect(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    @Override
    public String to() {
        return this.redirectTo;
    }
}
