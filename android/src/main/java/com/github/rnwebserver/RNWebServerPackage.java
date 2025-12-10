package com.github.rnwebserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.BaseReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;

import java.util.HashMap;
import java.util.Map;

public class RNWebServerPackage extends BaseReactPackage {
    @Nullable
    @Override
    public NativeModule getModule(@NonNull String name, @NonNull ReactApplicationContext reactContext) {
        if (RNWebServerModule.NAME.equals(name)) {
            return new RNWebServerModule(reactContext);
        }

        return null;
    }

    @NonNull
    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return new ReactModuleInfoProvider() {
            @NonNull
            @Override
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                Map<String, ReactModuleInfo> map = new HashMap<>();
                map.put(RNWebServerModule.NAME, new ReactModuleInfo(
                        RNWebServerModule.NAME, // name
                        RNWebServerModule.NAME, // className
                        false,                           // canOverrideExistingModule
                        false,                           // needsEagerInit
                        false,                           // isCxxModule
                        true                             // isTurboModule
                ));
                return map;
            }
        };
    }
}
