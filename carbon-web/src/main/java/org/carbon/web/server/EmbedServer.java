package org.carbon.web.server;

/**
 * @author Shota Oda 2016/10/17.
 */
public interface EmbedServer {
    void run(Class base) throws Exception;
    void await() throws Exception;
}
