package org.carbon.web.server.jetty;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.exception.ServerStartupException;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/07/22.
 */
@Component
public class StaticHandler extends ServletContextHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticHandler.class);
    // -----------------------------------------------------
    //                                               favicon
    //                                               -------
    private byte[] favicon;
    private final long faviconModified = System.currentTimeMillis();

    @Assemble
    private WebProperty config;
    private boolean enabled = false;
    private String resourcePath;

    public StaticHandler() throws IOException {
        URL favUrl = getClass().getClassLoader().getResource("org/carbon/web/favicon.ico");
        if (favUrl != null) {
            favicon = IO.readBytes(Resource.newResource(favUrl).getInputStream());
        }
    }

    @AfterAssemble
    public void afterInject() {
        WebProperty.Resource resource = config.getResource();
        if (resource == null || resource.getDirectory() == null || resource.getOutPath() == null) {
            logger.info("Not found resource setting, so skip Resource handler mapping");
            return;
        }

        this.enabled = true;

        ServletHolder resourceServletHolder = new ServletHolder(DefaultServlet.class);
        resourceServletHolder.setInitParameter("acceptRanges", "f");
        resourceServletHolder.setInitParameter("dirAllowed", "f");
        resourceServletHolder.setInitParameter("redirectWelcome", "f");
        resourceServletHolder.setInitParameter("welcomeServlets", "f");
        resourceServletHolder.setInitParameter("gzip", "t");
        resourceServletHolder.setInitParameter("etags", "t");
        resourceServletHolder.setInitParameter("cacheControl", "t");

        String resourceDirectory = resource.getDirectory();
        String resourceBase = Optional.ofNullable(ClassLoader.getSystemResource(resourceDirectory))
                .map(URL::toString)
                .orElseThrow(() -> new ServerStartupException(resourceSetupFailMessage(resourceDirectory)));
        resourceServletHolder.setInitParameter("resourceBase", resourceBase);

        this.resourcePath = config.getResource().getOutPath();
        String context = Paths.get("/", this.resourcePath).toString();
        this.setContextPath(context);
        this.addServlet(resourceServletHolder, "/");
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean handledFavicon = handleFavicon(request, response);
        baseRequest.setHandled(handledFavicon);

        if (enabled && !handledFavicon) {
            super.doHandle(target, baseRequest, request, response);
        }
    }

    public boolean canHandle(HttpServletRequest request) {
        return enabled && (checkFavicon(request) || checkResource(request));
    }

    private boolean checkFavicon(HttpServletRequest request) {
        return request.getMethod().equals("GET") && request.getRequestURI().equals("/favicon.ico");
    }

    private boolean checkResource(HttpServletRequest request) {
        return request.getRequestURI().startsWith(resourcePath);
    }

    private boolean handleFavicon(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (checkFavicon(request)) {
            if (request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE.asString()) == faviconModified)
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("image/x-icon");
                response.setContentLength(favicon.length);
                response.setDateHeader(HttpHeader.LAST_MODIFIED.asString(), faviconModified);
                response.setHeader(HttpHeader.CACHE_CONTROL.asString(), "max-age=360000,public");
                response.getOutputStream().write(favicon);
            }
            return true;
        }
        return false;
    }

    private String resourceSetupFailMessage(String resourceDirectory) {
        return String.format(
                "Fail to load Resource directory: %s\n--------------------------------------------------------------------------------\nTry either\n・ Delete resource setting property\nor\n・ Create dir: resource/%s and stuff some resource file\n--------------------------------------------------------------------------------",
                resourceDirectory,
                resourceDirectory
        );
    }
}
