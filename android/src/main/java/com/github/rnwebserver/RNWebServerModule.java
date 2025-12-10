package com.github.rnwebserver;

import androidx.annotation.NonNull;

import com.activatortube.rnwebserver.NativeWebServerSpec;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RNWebServerModule extends NativeWebServerSpec {
    private WebServer webServer;

    public RNWebServerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NativeWebServerSpec.NAME;
    }

    @Override
    public void start(ReadableArray handlers, Promise promise) {
        if (webServer != null) {
            if (webServer.wasStarted()) {
                webServer.stop();
            }
            webServer = null;
        }

        List<HandlerOption> options = new ArrayList<>();
        for (int i = 0; i < handlers.size(); i++) {
            ReadableMap map = handlers.getMap(i);
            HandlerOption handlerOption = new HandlerOption(map.getString("prefix"), map.getString("path"));
            options.add(handlerOption);
        }

        webServer = new WebServer(options);
        try {
            webServer.start();
        } catch (IOException e) {
            promise.reject(e);
        }

        promise.resolve(webServer.getListeningPort());
    }

    @Override
    public void stop() {
        if (webServer == null || !webServer.wasStarted()) {
            return;
        }
        webServer.stop();
        webServer = null;
    }
}
