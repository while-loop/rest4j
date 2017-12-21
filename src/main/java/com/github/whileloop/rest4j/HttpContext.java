package com.github.whileloop.rest4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface HttpContext {

    /**
     * Get Path or Query parameter
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T getParam(String key);

    <T> void setParam(String key, T object);

    List<String> getHeader(String field);

    void setHeader(String field, String... values);

    default void addToHeader(String field, String... values) {
        List<String> vals = getHeader(field);
        if (vals == null || vals.size() <= 0) {
            vals = new ArrayList<>();
        }

        vals.addAll(Arrays.asList(values));
        setHeader(field, vals.toArray(new String[vals.size()]));
    }

    default String getFirstHeader(String key) {
        List<String> values = getHeader(key);
        return ((values == null || values.size() <= 0)) ? null : values.get(0);
    }
}
