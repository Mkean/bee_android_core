package com.bee.core.logger;

import android.text.TextUtils;

import com.dianping.logan.LoganConfig;

/**
 *  日志配置
 */
public class CommonLogConfig {
    private static final long M = 1024 * 1024; //M
    private static final long DEFAULT_FILE_SIZE = 10;//最大文件大小,默认10M
    private static final long DEFAULT_DAY = 7;//默认删除天数
    private static final long DEFAULT_MIN_SDCARD_SIZE = 50 * M; //最小的SD卡小于这个大小不写入
    private static final boolean DEFAULT_ENABLE_APP_CRASH = false;
    private static final boolean DEFAULT_ENABLE_DEBUG = false;
    //private static final int DEFAULT_LEVEL = 4;//默认日志级别，debug级及debug以下级别不输出到日志文件

    String mCachePath; //mmap缓存路径
    String mPath; //file文件路径
    long mMaxFile = DEFAULT_FILE_SIZE; //最大文件大小
    long mDay = DEFAULT_DAY; //删除天数
    byte[] mEncryptKey16; //128位ase加密Key
    byte[] mEncryptIv16; //128位aes加密IV
    long mMinSDCard = DEFAULT_MIN_SDCARD_SIZE;//SD卡小于这个大小不写入
    boolean mEnableAppCrash=DEFAULT_ENABLE_APP_CRASH;//是否开启全局crash日志收集
    boolean mDebug=DEFAULT_ENABLE_DEBUG;//是否开启控制台日志输出
    /**
     * 日志级别，配置0全部显示，配置大于8全不显示
     * 显示大于等于级别日志，级别参考 android.util.Log的级别VERBOSE = 2;DEBUG = 3;INFO = 4;WARN = 5;ERROR = 6;ASSERT = 7;
     */
    //int mLevel=DEFAULT_LEVEL;


    public CommonLogConfig setCachePath(String cachePath) {
        mCachePath = cachePath;
        return this;
    }

    public CommonLogConfig setPath(String path) {
        mPath = path;
        return this;
    }

    public CommonLogConfig setMaxFile(long maxFile) {
        mMaxFile = maxFile;
        return this;
    }

    public CommonLogConfig setDay(long day) {
        mDay = day;
        return this;
    }

    public CommonLogConfig setEncryptKey16(byte[] encryptKey16) {
        mEncryptKey16 = encryptKey16;
        return this;
    }

    public CommonLogConfig setEncryptIV16(byte[] encryptIv16) {
        mEncryptIv16 = encryptIv16;
        return this;
    }

    public CommonLogConfig enableAppCrash(boolean enableAppCrash) {
        this.mEnableAppCrash = enableAppCrash;
        return this;
    }

    public CommonLogConfig enableDebug(boolean enableDebug) {
        this.mDebug = enableDebug;
        return this;
    }

//    public MonkeyLogConfig setLevel(int level) {
//        this.mLevel = level;
//        return this;
//    }

    public CommonLogConfig setMinSDCard(long minSDCard) {
        this.mMinSDCard = minSDCard;
        return this;
    }

    public LoganConfig build() {

        LoganConfig.Builder builder = new LoganConfig.Builder();
        if (!TextUtils.isEmpty(mCachePath)){
            builder.setCachePath(mCachePath);
        }
        if (!TextUtils.isEmpty(mPath)){
            builder.setPath(mPath);
        }
        if (mEncryptKey16!=null){
            builder.setEncryptKey16(mEncryptKey16);
        }
        if (mEncryptIv16!=null){
            builder.setEncryptIV16(mEncryptIv16);
        }
        builder.setDay(mDay);
        builder.setMaxFile(mMaxFile);
        builder.setMinSDCard(mMinSDCard);
        return builder.build();
    }
}
