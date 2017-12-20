package com.github.whileloop.rest4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by aalves on 12/18/17
 */
public class HttpHeaders extends TreeMap<String, List<String>> {

    public String getFirst(String key) {
        List<String> values = get(key);
        if (values == null || values.size() <= 0) {
            return null;
        }

        return values.get(0);
    }

    public void set(String key, String... values) {
        this.set(key, Arrays.asList(values));
    }

    public void set(String key, List<String> values) {
        super.put(key, values);
    }

    public void add(String key, String... values) {
        List<String> vals = get(key);
        if (vals == null || vals.size() <= 0) {
            vals = new ArrayList<>();
        }

        vals.addAll(Arrays.asList(values));
        super.put(key, vals);
    }
}
