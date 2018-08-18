package com.github.whileloop.rest4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

public interface WebsocketConn extends Closeable {
    InetSocketAddress localAddr();

    InetSocketAddress remoteAddr();

    String read() throws IOException;

    void write(String s) throws IOException;
}
