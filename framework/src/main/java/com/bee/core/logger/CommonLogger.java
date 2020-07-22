package com.bee.core.logger;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dianping.logan.Logan;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  日志工具类
 */
public class CommonLogger {

    private static final String TAG = "MonkeyLogger";

    /**
     * 日志级别，配置0全部显示，配置大于8全不显示
     * 显示大于等于级别日志，级别参考 android.util.Log的级别VERBOSE = 2;DEBUG = 3;INFO = 4;WARN = 5;ERROR = 6;ASSERT = 7;
     */
    private static int sLevel = 4;
    private static String sLogPath;//日志存储路径
    private static boolean sDebug = false;//是否输出控制台日志
    private static final String DELIM_STR = "{}";//格式化转换符

    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Nullable
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    public static void init(CommonLogConfig config) {
        if (config == null) {
            return;
        }
        sLogPath = config.mPath;
        sDebug = config.mDebug || Log.isLoggable(TAG, Log.DEBUG);
        //sLevel = config.mLevel;
        Logan.init(config.build());
        if (config.mEnableAppCrash) {
            CommonLoggerExceptionHandler.getInstance().init();
        }
    }

    public static String getLogPath() {
        return sLogPath;
    }

    public static void wtf(final String tag, final String msg) {
        wtf(tag, msg, (Object[]) null);
    }

    public static void e(final String tag, final String msg) {
        e(tag, msg, (Object[]) null);
    }

    public static void w(final String tag, final String msg) {
        w(tag, msg, (Object[]) null);
    }

    public static void i(final String tag, final String msg) {
        i(tag, msg, (Object[]) null);
    }

    public static void d(final String tag, final String msg) {
        d(tag, msg, (Object[]) null);
    }

    public static void v(final String tag, final String msg) {
        v(tag, msg, (Object[]) null);
    }

    /**
     * ASSERT级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void wtf(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.wtf(tag, result);
        }

        if (sLevel > Log.ASSERT) {
            return;
        }
        printIntoLogan(tag, result, "WTF");
    }

    /**
     * ERROR级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void e(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.e(tag, result);
        }

        if (sLevel > Log.ERROR) {
            return;
        }
        printIntoLogan(tag, result, "E");
    }

    /**
     * WARN级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void w(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.w(tag, result);
        }

        if (sLevel > Log.WARN) {
            return;
        }

        printIntoLogan(tag, result, "W");
    }

    /**
     * INFO级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void i(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.i(tag, result);
        }

        if (sLevel > Log.INFO) {
            return;
        }

        printIntoLogan(tag, result, "I");
    }

    /**
     * DEBUG级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void d(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.d(tag, result);
        }

        if (sLevel > Log.DEBUG) {
            return;
        }
        printIntoLogan(tag, result, "D");
    }

    /**
     * VERBOSE级别日志
     *
     * @param tag
     * @param format
     * @param obj
     */
    public static void v(String tag, final String format, final Object... obj) {

        String result = formatString(format, obj);

        if (sDebug) {
            Log.v(tag, result);
        }

        if (sLevel > Log.VERBOSE) {
            return;
        }
        printIntoLogan(tag, result, "V");
    }

    /**
     * 输出异常堆栈信息
     *
     * @param tag
     * @param tr
     * @param format
     * @param obj
     */
    public static void printErrStackTrace(String tag, Throwable tr, final String format, final Object... obj) {
        String result = formatString(format, obj);

        /*自2011年5月20日起，Log类发生变化，不会打印UnknownHostException异常堆栈信息。
        这是为了减少应用程序在无错误情况下的日志开销
        网络不可用的情况。
        https://github.com/android/platform_frameworks_base/commit/dba50c7ed24e05ff349a94b8c4a6d9bb9050973b*/
        if (tr instanceof UnknownHostException) {
            result += "  " + tr.toString();
        } else {
            result += "  " + Log.getStackTraceString(tr);
        }

        if (sDebug) {
            Log.e(tag, result);
        }

        printIntoLogan(tag, result, "E");

        Logan.f();
    }

    /**
     * 立即写入日志文件
     */
    public static void f() {
        Logan.f();
    }


    private static void deeplyAppendParameter(StringBuilder sbuf, Object o) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            appendObject(sbuf, o);
        } else {
            //检查基础数组类型，它们不能被强制转换为Object[]
            if (o instanceof boolean[]) {
                appendBooleanArray(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                appendByteArray(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                appendCharArray(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                appendShortArray(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                appendIntArray(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                appendLongArray(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                appendFloatArray(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                appendDoubleArray(sbuf, (double[]) o);
            } else {
                appendObjectArray(sbuf, (Object[]) o);
            }
        }
    }

    private static void appendObject(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Exception e) {
            sbuf.append("[FAILED toString()]");
        }

    }

    private static void appendObjectArray(StringBuilder sbuf, Object[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            deeplyAppendParameter(sbuf, a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendBooleanArray(StringBuilder sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendByteArray(StringBuilder sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendCharArray(StringBuilder sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendShortArray(StringBuilder sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendIntArray(StringBuilder sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendLongArray(StringBuilder sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendFloatArray(StringBuilder sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    private static void appendDoubleArray(StringBuilder sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }

    /**
     * 根据占位符{}格式化日志
     *
     * @param format
     * @param obj
     * @return
     */
    private static String formatString(final String format, final Object... obj) {
        String result;
        if (TextUtils.isEmpty(format)) {
            result = "";
        } else if (obj != null && obj.length > 0 && format.indexOf(DELIM_STR) >= 0) {
            StringBuilder sbuf = new StringBuilder();
            int i = 0;//记录开始查找{}符号的起始位置
            for (Object object : obj) {
                int j = format.indexOf(DELIM_STR, i);//记录找到{}符号的位置
                if (j == -1) {
                    break;
                } else {
                    sbuf.append(format.substring(i, j));
                    deeplyAppendParameter(sbuf, object);
                    i = j + 2;
                }
            }

            sbuf.append(format.substring(i));
            result = sbuf.toString();
        } else {
            result = format;
        }

        return result;
    }

    /**
     * 写入日志文件
     *
     * @param tag     日志标签
     * @param result  日志内容
     * @param typeStr 日志级别标识
     */
    private static void printIntoLogan(String tag, String result, String typeStr) {
        if (TextUtils.isEmpty(tag)) {
            tag = "<<>>";
        }

        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e;
        if (stacktrace[3].getClassName().indexOf("MonkeyLogger") != -1) {
            e = stacktrace[4];
        } else {
            e = stacktrace[3];
        }

        String strWhere = null;

        if (e != null) {
            int line = e.getLineNumber();
            String filename = e.getFileName();
            strWhere = filename + "(" + line + ")";
        }


        if (TextUtils.isEmpty(strWhere)) {
            strWhere = "";
        }

        Logan.w(df.get().format(System.currentTimeMillis()) + " | " + Build.BRAND + " ｜ " + Build.MODEL + " ｜ " + "Android " + Build.VERSION.RELEASE + " | " + typeStr + " | " + tag + " | " + strWhere + " | " + result, 1);
    }

    public static void sendLog(SendLogBaseRunnable runnable) {
        if (runnable == null) {
            return;
        }
        sendLog(runnable, null);
    }
    
    public static void sendLog(SendLogBaseRunnable runnable, SendLogListener sendLogListener) {
        if (runnable == null) {
            return;
        }
        Map<String, Long> map = Logan.getAllFilesInfo();

        List<String> list = new ArrayList();
        long totalSize = 0;
        if (map != null) {
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                if (entry != null && !TextUtils.isEmpty(entry.getKey())) {
                    list.add(entry.getKey());
                    totalSize += entry.getValue();
                    i(TAG, "上传日志>>>data={},size={}", entry.getKey(), entry.getValue());
                }
            }
        }

        if (list != null && list.size() > 0) {
            if (sendLogListener != null) {
                runnable.setSendLogListener(sendLogListener);
            }
            runnable.setTotalSize(totalSize);
            runnable.setFileCount(list.size());
            String[] dates = new String[list.size()];
            list.toArray(dates);
            Logan.s(dates, runnable);
        } else {
            if (sendLogListener != null) {
                sendLogListener.onComplete(true);
            }
        }
    }
}
