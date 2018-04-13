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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentType)) return false;

        ContentType that = (ContentType) o;

        if (!mediaType.equals(that.mediaType)) return false;
        //noinspection SimplifiableIfStatement
        if (charset != null ? !charset.equals(that.charset) : that.charset != null) return false;
        return boundary != null ? boundary.equals(that.boundary) : that.boundary == null;
    }

    @Override
    public int hashCode() {
        int result = mediaType.hashCode();
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + (boundary != null ? boundary.hashCode() : 0);
        return result;
    }
}
