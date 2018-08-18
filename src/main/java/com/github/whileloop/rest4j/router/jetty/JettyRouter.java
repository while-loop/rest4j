package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Router;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by aalves on 8/17/18
 */
public class JettyRouter extends AbstractHandler {
    private Logger logger = LoggerFactory.getLogger(JettyRouter.class);

    private final Router base;

    public JettyRouter(Router r) {
        if (r == null) {
            throw new NullPointerException("null sun router");
        }

        base = r;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            JettyRequest req = new JettyRequest(httpServletRequest);
            JettyResponse resp = new JettyResponse(httpServletResponse);
            base.handle(req, resp);
            if (!resp.sentHeaders) {
                httpServletResponse.setStatus(resp.getStatus().code());
            }
        } catch (Exception e) {
            logger.error("failed to execute handler: " + e.toString());
            httpServletResponse.setStatus(500);
        } finally {
            try {
                httpServletResponse.getOutputStream().close();
            } catch (IOException e) {
                logger.error("failed to get os", e);
            }
        }
    }
}
