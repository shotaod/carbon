package org.carbon.web.translate.dto;

/**
 * @author Shota.Oda 2018/02/19.
 */
public final class Forward implements Transfer {
    private String forwardTo;

    public Forward(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    @Override
    public String to() {
        return this.forwardTo;
    }
}
