package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Router;

/**
 * Created by aalves on 8/17/18
 */
public class JettyRouter {

    private final Router base;

    public JettyRouter(Router r) {
        if (r == null) {
            throw new NullPointerException("null sun router");
        }

        base = r;
    }
}
