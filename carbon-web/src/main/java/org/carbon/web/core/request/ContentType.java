package org.carbon.web.core.request;

/**
 * @author ubuntu 2017/03/28.
 */
public class ContentType {
    private final static String Delimiter = ";";
    private final static String Charset = "charset";
    private final static String Boundary = "boundary";

    private String mediaType;
    private String charset;
    private String boundary;

    public ContentType(String contentType) {
        if (!contentType.contains(Delimiter)) {
            mediaType = contentType;
            return;
        }

        String[] params = contentType.split(Delimiter);
        mediaType = params[0];
        for (int i = 1; i < params.length; i++) {
            String param = params[i];
            if (param.startsWith(Charset)) {
                charset = param;
            } else if (param.startsWith(Boundary)) {
                boundary = param;
            }
        }
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getCharset() {
        return charset;
    }

    public String getBoundary() {
        return boundary;
    }
}
