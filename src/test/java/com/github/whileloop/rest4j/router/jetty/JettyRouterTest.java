package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.integration.RouterTest;

/**
 * Created by aalves on 8/17/18
 */
public class JettyRouterTest extends RouterTest {

    @Override
    public String before(Router apiRouter, FileHandler fileHandler) throws Exception {

        return null;
    }

    @Override
    public void after() {

    }
}