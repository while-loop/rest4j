package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.WebsocketHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JettyWSHandlerTest {


    @Test
    public void testWS() throws Exception {

        CountDownLatch cdl = new CountDownLatch(2);
        WebsocketHandler h = conn -> {
            System.out.println(conn.remoteAddr());
            cdl.countDown();
            System.out.println(conn.read());
        };

        Router r = new Router();
        r.handle("/ws", new JettyWSHandler(h));

        Server server = new Server(new InetSocketAddress(0));
        ContextHandler context = new ContextHandler("/");
        context.setHandler(new JettyRouter(r));

        server.setHandler(context);
        server.start();


        WebSocketClient client = new WebSocketClient();
        SimpleEchoSocket socket = new SimpleEchoSocket();
        client.start();
        Session s = client.connect(socket, new URI("ws://localhost:" + server.getURI().getPort() + "/ws")).get();

        cdl.await(2, TimeUnit.SECONDS);
    }

    @WebSocket(maxTextMessageSize = 64 * 1024)
    public class SimpleEchoSocket {
        private final CountDownLatch closeLatch;
        @SuppressWarnings("unused")
        private Session session;

        public SimpleEchoSocket() {
            this.closeLatch = new CountDownLatch(1);
        }

        public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
            return this.closeLatch.await(duration, unit);
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
            this.session = null;
            this.closeLatch.countDown(); // trigger latch
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            this.session = session;
            try {
                session.getRemote().sendString("Hello");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @OnWebSocketMessage
        public void onMessage(String msg) {
            System.out.printf("Got msg: %s%n", msg);
        }
    }
}