package com.bee.android.common.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {
    private static GsonUtil instance;

    private final Gson mGson;

    private static final String TAG = "GsonUtil";

    private GsonUtil() {
        mGson = new Gson();
    }

    public static synchronized GsonUtil getInstance() {
        if (instance == null) {
            instance = new GsonUtil();
        }
        return instance;
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T fromJson(String json, Type type) {
        return mGson.fromJson(json, type);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }

    public String objToJson(Object obj) {
        try {
            return mGson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
