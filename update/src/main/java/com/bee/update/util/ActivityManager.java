package com.bee.update.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

/**
 * 一个简单的用于自动管理Activity模拟栈，主要用于在进行更新通知时，提供最顶层的Activity实例作为有效的context进行使用
 */
public class ActivityManager implements Application.ActivityLifecycleCallbacks {
    private Context applicationContext;
    private static ActivityManager manager = new ActivityManager();

    public static ActivityManager get() {
        return manager;
    }

    private LinkedList<Activity> stack = new LinkedList<>();

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (!stack.contains(activity)) {
            stack.add(activity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (stack.contains(activity)) {
            stack.remove(activity);
        }
    }

    public Activity topActivity() {
        Activity activity = null;
        if (!stack.isEmpty()) {
            activity = stack.getLast();
        }
        return activity;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    void registerSelf(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(ActivityManager.get());
        this.applicationContext = context.getApplicationContext();
    }

    public void exit() {
        Activity activity;
        while (!stack.isEmpty()) {
            activity = stack.pop();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
        Process.killProcess(Process.myPid());
    }
}
