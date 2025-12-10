package com.github.rnwebserver;

public class HandlerOption {
    private String prefix;
    private String path;

    public HandlerOption(String prefix, String path) {
        this.prefix = prefix;
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPath() {
        return path;
    }
}
