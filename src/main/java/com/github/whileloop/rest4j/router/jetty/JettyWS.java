package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.WebsocketConn;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import static com.github.whileloop.rest4j.JsonUtils.is2String;

@WebSocket
public class JettyWS implements WebsocketConn {

    private ServletUpgradeRequest req;
    private Session sesh;

    public JettyWS(ServletUpgradeRequest servletUpgradeRequest) {
        req = servletUpgradeRequest;
    }

    @Override
    public InetSocketAddress localAddr() {
        return req.getLocalSocketAddress();
    }

    @Override
    public InetSocketAddress remoteAddr() {
        return req.getRemoteSocketAddress();
    }

    @Override
    public String read() throws IOException {
        return null;
    }

    @Override
    public void write(String s) throws IOException {
        sesh.getRemote().sendString(s);
    }

    @Override
    public void close() throws IOException {
        sesh.close();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sesh = session;
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println(statusCode);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.out.println(throwable);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String text) {
        System.out.println("testtt"+text);
    }

    @OnWebSocketMessage
    public void onBinaryMessage(Session session, InputStream stream) {
        System.out.println(is2String(stream));
    }

}
