package org.dabuntu.web.server;

/**
 * @author ubuntu 2016/10/17.
 */
public interface EmbedServer {
	void run() throws Exception;
	void await() throws Exception;
}
