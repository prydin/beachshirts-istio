package com.wfsample.common;

import java.util.HashSet;
import java.util.Set;

public class B3Headers {
    static Set<String> headers = new HashSet<>();

    static {
        headers.add("x-request-id");
        headers.add("x-b3-traceid");
        headers.add("x-b3-spanid");
        headers.add("x-b3-parentspanid");
        headers.add("x-b3-sampled");
        headers.add("x-b3-flags");
        headers.add("x-ot-span-context");
    }

    static public boolean isB3Header(String s) {
        return headers.contains(s);
    }
}
