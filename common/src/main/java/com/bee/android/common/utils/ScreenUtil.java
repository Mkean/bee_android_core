package com.bee.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScreenUtil {
    private static double RATIO = 0.85;

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return px
     */
    public static int dip2px(Context context, float dipValue) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        return (int) (dipValue * density + 0.5f);
    }
    public static int dip2px( float dipValue) {

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        float density = dm.density;
        return (int) (dipValue * density + 0.5f);
    }
    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return dip
     */
    public static int px2dip(Context context, float pxValue) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return px
     */
    public static int sp2px(Context context, float spValue) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float scaleDensity = dm.scaledDensity;
        return (int) (spValue * scaleDensity + 0.5f);
    }

    /**
     * 获取默认的dialog宽度
     *
     * @param context
     * @return 默认dialog宽度
     */
    public static int getDialogWidth(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int screenMin = (screenWidth > screenHeight) ? screenHeight : screenWidth;
        return (int) (screenMin * RATIO);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight == 0) {
            statusBarHeight = ScreenUtil.dip2px(context, 25);
        }
        return statusBarHeight;
    }

    /**
     * 获取navigationBar高度
     *
     * @param context
     * @return navigationBar高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取ActionBar高度
     *
     * @param context
     * @return ActionBar高度
     */
    public static int getActionBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context 上下文对象
     * @return 返回屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        return metric.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context 上下文对象
     * @return 返回屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        return metric.heightPixels;
    }

    /**
     * 获取宽高中，小的一边
     *
     * @param context
     * @return 小的一边的长度
     */
    public static int getScreenMin(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int screenWidth = metric.widthPixels;
        int screenHeight = metric.heightPixels;
        return (screenWidth > screenHeight) ? screenHeight : screenWidth;
    }


    /**
     * 获取DisplayMetrics
     *
     * @param context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (null == context) {
            return null;
        }
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        return metric;
    }

    /**
     * Get Screen Real Height
     *
     * @param context Context
     * @return Real Height
     */
    public static int getRealHeight(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * Get Screen Real Width
     *
     * @param context Context
     * @return Real Width
     */
    public static int getRealWidth(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            wm = activity.getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            return wm.getDefaultDisplay();
        }
        return null;
    }
    /**
     * Android P 刘海屏判断
     *
     * @param activity
     * @return
     */
    public static DisplayCutout isAndroidP(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null && Build.VERSION.SDK_INT >= 28) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null) {
                return windowInsets.getDisplayCutout();
            }
        }
        return null;
    }

    /**
     * 判断是否是刘海屏
     *
     * @return
     */
    public static boolean hasNotchScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return isAndroidP(activity) != null;
        }

        return getInt("ro.miui.notch", activity) == 1
                || hasNotchAtHuawei(activity) ||
                hasNotchAtOPPO(activity) || hasNotchAtVivo(activity);

    }

    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 小米刘海屏判断.
     *
     * @return 0 if it is not notch ; return 1 means notch
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key, Activity activity) {
        int result = 0;
        if (isXiaomi()) {
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = key;
                params[1] = new Integer(0);
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }


}
