package com.bee.update.util;

import com.bee.android.common.logger.CommonLogger;

/**
 * 统一日志打印
 */
public class L {
    public static final String TAG = "UpdateLog";
    public static boolean ENABLE = true;

    public static void d(String message, Object... args) {
        if (ENABLE) {
            CommonLogger.d(TAG, String.format(message, args));
        }
    }

    public static void e(Throwable t, String message, Object... args) {
        if (ENABLE) {
            CommonLogger.e(TAG, String.format(message, args), t);
        }
    }
}
