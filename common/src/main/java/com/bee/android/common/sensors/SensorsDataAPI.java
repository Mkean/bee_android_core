package com.bee.android.common.sensors;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Map;

public class SensorsDataAPI {

    public final String TAG = this.getClass().getSimpleName();

    public static final String SDK_VERSION = "1.0.0";
    private static SensorsDataAPI INSTANCE;
    private static final Object mLock = new Object();
    private static Map<String, Object> mDeviceInfo;
    private String mDeviceId;

    public static SensorsDataAPI init(Application application) {
        synchronized (mLock) {
            if (null == INSTANCE) {
                INSTANCE = new SensorsDataAPI(application);
            }
            return INSTANCE;
        }
    }

    public static SensorsDataAPI getInstance() {
        return INSTANCE;
    }

    private SensorsDataAPI(Application application) {
        mDeviceId = SensorsDataPrivate.getAndroidID(application.getApplicationContext());
        mDeviceInfo = SensorsDataPrivate.getDeviceInfo(application.getApplicationContext());
        SensorsDataPrivate.registerActivityLifecycleCallbacks(application);
    }

    /**
     * track 事件
     *
     * @param eventName  事件名称
     * @param properties 事件自定义属性
     */
    public void track(@NonNull String eventName, @Nullable JSONObject properties) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", eventName);
            jsonObject.put("device_id", mDeviceId);

            JSONObject sendProperties = new JSONObject(mDeviceInfo);

            if (properties != null) {
                SensorsDataPrivate.mergeJsonObject(properties, sendProperties);
            }

            jsonObject.put("properties", sendProperties);
            jsonObject.put("time", System.currentTimeMillis());
            Log.i(TAG, SensorsDataPrivate.formatJson(jsonObject.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
