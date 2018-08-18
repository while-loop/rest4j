package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.WebsocketHandler;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JettyWSHandler implements Handler {

    private WebsocketHandler handler;
    private WebSocketServerFactory factory;

    public JettyWSHandler(WebsocketHandler handler) {
        this.handler = handler;
        factory = new WebSocketServerFactory();
        factory.setCreator((servletUpgradeRequest, servletUpgradeResponse) -> {
            JettyWS ws = new JettyWS(servletUpgradeRequest);
            try {
                handler.handle(ws);
            } catch (Exception e) {
                return null;
            }
            return ws;
        });
    }


    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {

        HttpServletRequest request = ((JettyRequest) req).getReq();
        HttpServletResponse response = ((JettyResponse) resp).getResp();

        if (factory.isUpgradeRequest(request, response)) {
            if (factory.acceptWebSocket(request, response)) {
                return;
            }

            if (response.isCommitted()) {
                return;
            }
        }
    }
}
