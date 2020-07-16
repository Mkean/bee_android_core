package com.bee.update.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * 框架内部所提供使用的一些缓存数据存储：如下载进度、忽略版本
 */
public class UpdatePreference {

    private static final String PREF_NAME = "update_preference";

    public static Set<String> getIgnoreVersions() {
        return getUpdatePref().getStringSet("ignoreVersions", new HashSet<String>());
    }

    public static void saveIgnoreVersion(int versionCode) {
        Set<String> ignoreVersions = getIgnoreVersions();
        if (!ignoreVersions.contains(String.valueOf(versionCode))) {
            ignoreVersions.add(String.valueOf(versionCode));
            getUpdatePref().edit().putStringSet("ignoreVersions", ignoreVersions).apply();
        }
    }

    private static SharedPreferences getUpdatePref() {
        return ActivityManager.get().getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
