package com.github.whileloop.rest4j;

import com.sun.net.httpserver.HttpExchange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.whileloop.rest4j.HttpMethod.GET;

public class Route {
    String path = "";
    transient Handler handler;
    List<HttpMethod> methods = Arrays.asList(GET);
    Map<String, String> vars = new HashMap<>();
    boolean strictSlash = false;

    public Route(String path, Handler handler, HttpMethod... methods) {
        this.path = path;
        this.handler = handler;
        if (methods != null && methods.length > 0) {
            this.methods = Arrays.asList(methods);
        }
    }

    public Route(Route route) {
        this.path = route.path;
        this.methods = route.methods;
        this.handler = route.handler;
        this.strictSlash = route.strictSlash;
        this.vars = new HashMap<>(route.vars);
    }


    public Route setStrictSlash(boolean strictSlash) {
        this.strictSlash = strictSlash;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Route setMethods(HttpMethod... methods) {
        this.methods = Arrays.asList(methods);
        return this;
    }

    public boolean matches(HttpExchange ex) {
        HttpMethod method = HttpMethod.valueOf(ex.getRequestMethod());
        String path = ex.getRequestURI().getRawPath();
        return this.methods.contains(method) && buildVars(path);
    }

    boolean buildVars(String matchedPath) {
        String rPath = path;
        String mPath = matchedPath;

        int i = 0;
        while (rPath.length() > 0 && i < rPath.length()) {
            if (rPath.charAt(i) != ':') {
                i++;
                continue;
            }

            rPath = rPath.substring(i + 1); //+1 for ':'
            mPath = mPath.substring(i);

            // found var.. look for end of string or first /
            i = findEnd(rPath);
            String key = rPath.substring(0, i);
            rPath = rPath.substring(Math.min(i + 1, rPath.length()));

            i = findEnd(mPath);
            String val = mPath.substring(0, i);
            mPath = mPath.substring(Math.min(i + 1, mPath.length()));

            vars.put(key, val);
            i = 0;
        }

        return rPath.equals(mPath);
    }
//
//    @Override
//    public String toString() {
//        return GSON.toJson(this);
//    }

    static int findEnd(String path) {
        int end = path.indexOf('/');
        if (end == -1) {
            end = path.length();
        }
        return end;
    }
}
