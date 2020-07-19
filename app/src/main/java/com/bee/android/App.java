package com.bee.android;

import android.app.Application;

import com.bee.android.common.sensors.SensorsDataAPI;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SensorsDataAPI.init(this);

    }
}
