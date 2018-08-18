package com.github.whileloop.rest4j;

/**
 * Created by aalves on 12/18/17
 */
public interface WebsocketHandler {
    void handle(WebsocketConn conn) throws Exception;
}
