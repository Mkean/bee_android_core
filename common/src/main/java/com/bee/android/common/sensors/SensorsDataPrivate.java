package com.bee.android.common.sensors;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SensorsDataPrivate {

    private static List<Integer> mIgnoredActivities;

    static {

        mIgnoredActivities = new ArrayList<>();
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-" +
            "MM-dd HH:mm:ss");

    public static void ignoreAutoTrackActivity(Class<?> activity) {
        if (activity == null) {
            return;
        }
        mIgnoredActivities.add(activity.hashCode());
    }

    public static void removeIgnoredActivity(Class<?> activity) {
        if (activity == null) {
            return;
        }
        if (mIgnoredActivities.contains(activity.hashCode())) {
            mIgnoredActivities.remove(activity.hashCode());
        }
    }

    public static void mergeJsonObject(final JSONObject source, JSONObject dest)
            throws JSONException {

        Iterator<String> superPropertiesIterator = source.keys();

        while (superPropertiesIterator.hasNext()) {
            String key = superPropertiesIterator.next();
            Object values = source.get(key);
            if (values instanceof Date) {
                synchronized (mDateFormat) {
                    dest.put(key, mDateFormat.format(((Date) values)));
                }
            } else {
                dest.put(key, values);
            }
        }
    }

    public static String getToolbarTitle(Activity activity) {
        try {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if (!TextUtils.isEmpty(actionBar.getTitle())) {
                    return actionBar.getTitle().toString();
                }
            } else {
                if (activity instanceof AppCompatActivity) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                    androidx.appcompat.app.ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
                    if (supportActionBar != null) {
                        if (!TextUtils.isEmpty(supportActionBar.getTitle())) {
                            return supportActionBar.getTitle().toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 Activity 的 title
     *
     * @param activity activity
     * @return String 返回页面 title
     */
    public static String getActivityTitle(Activity activity) {
        String activityTitle = null;

        if (activity == null) {
            return null;
        }

        try {
            activityTitle = activity.getTitle().toString();

            if (Build.VERSION.SDK_INT >= 11) {

                String toolbarTitle = getToolbarTitle(activity);
                if (!TextUtils.isEmpty(toolbarTitle)) {
                    activityTitle = toolbarTitle;
                }
            }

            if (TextUtils.isEmpty(activityTitle)) {
                PackageManager packageManager = activity.getPackageManager();
                if (packageManager != null) {
                    ActivityInfo activityInfo = packageManager.getActivityInfo(activity.getComponentName(), 0);

                    if (activityInfo != null) {
                        activityTitle = activityInfo.loadLabel(packageManager).toString();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityTitle;
    }

    /**
     * 页面浏览事件
     *
     * @param activity
     */
    @Keep
    private static void trackAppViewScreen(Activity activity) {
        try {
            if (activity == null) return;

            if (mIgnoredActivities.contains(activity.getClass().hashCode())) return;

            JSONObject properties = new JSONObject();
            properties.put("$activity", activity.getClass().getCanonicalName());
            properties.put("title", getActivityTitle(activity));

            SensorsDataAPI.getInstance().track("$AppViewScreen", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                trackAppViewScreen(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public static Map<String, Object> getDeviceInfo(Context context) {
        final Map<String, Object> deviceInfo = new HashMap<>();
        {
            deviceInfo.put("$lib", "Android");
            deviceInfo.put("$lin_version", SensorsDataAPI.SDK_VERSION);
            deviceInfo.put("$os", "Android");
            deviceInfo.put("$os_version",
                    Build.VERSION.RELEASE == null ? "UNKNOWN" : Build.VERSION.RELEASE);

            deviceInfo.put("$manufacturer", Build.MANUFACTURER == null ? "UNKNOWN" :
                    Build.MANUFACTURER);
            if (TextUtils.isEmpty(Build.MODEL)) {
                deviceInfo.put("$model", "UNKNOWN");
            } else {
                deviceInfo.put("$model", Build.MODEL.trim());
            }

            try {
                final PackageManager manager = context.getPackageManager();
                final PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
                deviceInfo.put("$app_version", packageInfo.versionName);

                int labelRes = packageInfo.applicationInfo.labelRes;
                deviceInfo.put("$app_name", context.getResources().getString(labelRes));

            } catch (final Exception e) {
                e.printStackTrace();
            }

            final DisplayMetrics displayMetrics = context.getResources()
                    .getDisplayMetrics();
            deviceInfo.put("$screen_height", displayMetrics.heightPixels);
            deviceInfo.put("$screen_width", displayMetrics.widthPixels);

            return Collections.unmodifiableMap(deviceInfo);
        }
    }

    /**
     * 获取 Android ID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        String androidID = "";
        try {
            androidID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidID;
    }

    private static void addIndentBlack(StringBuilder sb, int indent) {
        try {
            for (int i = 0; i < indent; i++) {
                sb.append('\t');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatJson(String jsonStr) {
        try {
            if (null == jsonStr || "".equals(jsonStr)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            char last;
            char current = '\0';
            int indent = 0;
            boolean isInQuotationMarks = false;
            for (int i = 0; i < jsonStr.length(); i++) {
                last = current;
                current = jsonStr.charAt(i);
                switch (current) {
                    case '"':
                        if (last != '\\') {
                            isInQuotationMarks = !isInQuotationMarks;
                        }
                        sb.append(current);
                        break;
                    case '{':
                    case '[':
                        sb.append(current);
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent++;
                            addIndentBlack(sb, indent);
                        }
                        break;
                    case ']':
                    case '}':
                        if (!isInQuotationMarks) {
                            sb.append('\n');
                            indent--;
                            addIndentBlack(sb, indent);
                        }
                        sb.append(current);
                        break;
                    case ',':
                        sb.append(current);
                        if (last != '\\' && !isInQuotationMarks) {
                            sb.append('\n');
                            addIndentBlack(sb, indent);
                        }
                        break;
                    default:
                        sb.append(current);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}