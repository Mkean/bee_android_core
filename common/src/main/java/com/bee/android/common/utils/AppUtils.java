package com.bee.android.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.security.MessageDigest;

/**
 *  APP版本相关信息
 */
public class AppUtils {
    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    /**
     * 获取包名信息
     */
    public static String getPackageName(Context content) {
        try {
            String pkName = content.getPackageName();
            return pkName;
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 获取版本信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
            return "1.0.0";
        }
    }


    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (Exception e) {
//            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, 64);
            return hexDigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException var3) {
            throw new RuntimeException("the " + pkgName + "'s application not found");
        }
    }

    private static String hexDigest(byte[] paramArrayOfByte) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;

            for (int j = 0; i < 16; ++j) {
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[15 & k >>> 4];
                ++j;
                arrayOfChar[j] = hexDigits[k & 15];
                ++i;
            }

            return new String(arrayOfChar);
        } catch (Exception var8) {
            return "";
        }
    }
}

