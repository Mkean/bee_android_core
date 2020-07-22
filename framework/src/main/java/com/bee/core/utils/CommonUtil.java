package com.bee.core.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 解决在Android P上的提醒弹窗 （Detected problems with API compatibility(visit g.co/dev/appcompat for more info)
     * android 9 禁用 api 反射之后，会弹出提示窗口, 此方法禁用此窗口
     */
    public static void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT <= 27) {

            return;

        }
        try {

            Class aClass = Class.forName("android.content.pm.PackageParser$Package");

            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);

            declaredConstructor.setAccessible(true);

        } catch (Exception e) {

            e.printStackTrace();

        }

        try {

            Class cls = Class.forName("android.app.ActivityThread");

            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");

            declaredMethod.setAccessible(true);

            Object activityThread = declaredMethod.invoke(null);

            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");

            mHiddenApiWarningShown.setAccessible(true);

            mHiddenApiWarningShown.setBoolean(activityThread, true);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    private static final int MIN_CLICK_DELAY_TIME = 400;//两次点击时间不低于500
    private static long lastClickTime;

    public static boolean isFastClick() {//防止重复点击
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
    /**
     * 隐藏软键盘
     *
     * @param activity
     * @return
     */
    public static boolean hideSoftKeyboard(Activity activity) {
        Context context=activity.getApplicationContext();
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (mInputMethodManager.isActive()) {
            try {
                mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * 判断一个字符串是否含有数字
     */
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
    /**
     * 版本号比较
     * <p>
     * 1.上面的值为-1 代表：前者小于后者
     * 2.上面的值为0  代表：两者相等
     * 3.上面的值为1  代表：前者大于后者
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("version", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }


    public static <E>boolean isListEqual(List<E> list1, List<E> list2) {
        // 两个list引用相同（包括两者都为空指针的情况）
        if (list1 == list2) {
            return true;
        }

        // 两个list都为空（包括空指针、元素个数为0）
        if ((list1 == null && list2 != null && list2.size() == 0)
                || (list2 == null && list1 != null && list1.size() == 0)) {
            return true;
        }

        // 两个list元素个数不相同
        if (list1.size() != list2.size()) {
            return false;
        }

        // 两个list元素个数已经相同，再比较两者内容
        // 采用这种可以忽略list中的元素的顺序
        // 涉及到对象的比较是否相同时，确保实现了equals()方法
//        if (!list1.containsAll(list2)) {
//            return false;
//        }

        for (int i = 0; i < list1.size(); i++) {
            if(list1.get(i) == null && list2.get(i) != null){
                return false;
            }
            if(list1.get(i) != null && !list1.get(i).equals(list2.get(i))){
                return false;
            }
        }

        return true;
    }

}
