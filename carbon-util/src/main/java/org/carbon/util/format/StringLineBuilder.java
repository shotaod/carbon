package org.carbon.util.format;

/**
 * @author Shota Oda 2016/10/08.
 */
public class StringLineBuilder {
    private StringBuilder sb;

    public StringLineBuilder() {
        this.sb = new StringBuilder();
    }

    public StringLineBuilder append(Object o) {
        sb.append(o);
        return this;
    }


    public StringLineBuilder appendLine() {
        sb.append("\n");
        return this;
    }
    public StringLineBuilder appendLine(Object o) {
        sb.append(o);
        sb.append("\n");
        return this;
    }
    public StringLineBuilder appendLine(String s, Object... arg) {
        sb.append(String.format(s, arg));
        sb.append("\n");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
