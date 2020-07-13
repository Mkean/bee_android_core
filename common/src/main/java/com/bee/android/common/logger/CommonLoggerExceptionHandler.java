package com.bee.android.common.logger;

/**
 *  全局异常捕获
 */
public class CommonLoggerExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MonkeyExceptionHandler";

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;


    private static class Holder {
        static final CommonLoggerExceptionHandler INSTANCE = new CommonLoggerExceptionHandler();
    }

    public static CommonLoggerExceptionHandler getInstance() {
        return Holder.INSTANCE;
    }

    private CommonLoggerExceptionHandler() {
    }

    public void init() {
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        try {
            CommonLogger.printErrStackTrace(TAG, e, "程序异常");
        } catch (Exception exception) {
            CommonLogger.printErrStackTrace(TAG, e, "处理全局异常信息错误");
        }

        if (mDefaultExceptionHandler != null) {
            mDefaultExceptionHandler.uncaughtException(t, e);
        } else {
            killProcessAndExit();
        }
    }

    private void killProcessAndExit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
