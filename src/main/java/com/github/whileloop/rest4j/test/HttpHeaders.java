package com.github.whileloop.rest4j.test;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by aalves on 12/18/17
 */
class HttpHeaders extends TreeMap<String, List<String>> {
    public void set(String key, String... values) {
        this.set(key, Arrays.asList(values));
    }

    public void set(String key, List<String> values) {
        super.put(key, values);
    }
}
