package org.carbon.util.format;

/**
 * @author Shota Oda 2016/10/22.
 */
public class ChapterAttr {
    private static final String Prefix = "             ";
    private static final String Bottom = "--------------------------------------------------------------------------------";

    public static String get(String title) {
        return getBuilder(title).toString();
    }

    public static StringLineBuilder getBuilder(String title) {
        StringLineBuilder sb = new StringLineBuilder();
        return sb.appendLine().appendLine().append(Prefix).append(title).appendLine().appendLine(Bottom);
    }
}
