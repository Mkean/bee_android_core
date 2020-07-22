package com.bee.core.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bee.core.utils.FileUtil;

import java.io.File;

/**
 * 文件管理器：统一父级目录、可清理目录等等
 */
public class CommonFileManager {

    public static final String APP_CLEAR_ABLE_CONTENT = "clearAbleContent";

    /**
     * 获取外部私有存储父路径
     *
     * @param context
     * @param dir
     * @return /storage/emulate/0/Android/data/包名/files/dir
     */
    public static String getExternalFilePath(Context context, String dir) {
        return FileUtil.getExternalFilePath(context, dir);
    }

    /**
     * 获取外部可以清理父路径：可清除的文件统一放置在目录下，方便统一清除
     *
     * @param context
     * @return
     */
    public static String getExternalClearAbleFilePath(Context context) {
        return FileUtil.getExternalFilePath(context, APP_CLEAR_ABLE_CONTENT);
    }

    /**
     * 同上
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getExternalClearAbleFilePath(Context context, String dir) {
        return FileUtil.getExternalFilePath(context, APP_CLEAR_ABLE_CONTENT +
                (TextUtils.isEmpty(dir) ? "" : File.separator + dir));
    }

    /**
     * 获取内部私有存储父路径
     *
     * @param dir 子目录名称
     * @return /data/user/0/包名/files/dir
     */
    public static String getInnerFilePath(Context context, String dir) {
        return FileUtil.getInnerFilePath(context, dir);
    }

    /**
     * 获取内部私有存储可清除父路径：可清除的文件统一放置在目录下，方便统一清除
     *
     * @return /data/user/0/包名/files/clearAbleContent
     */
    public static String getInnerClearAbleFilePath(Context context) {
        return FileUtil.getInnerFilePath(context, APP_CLEAR_ABLE_CONTENT);
    }

    /**
     * 同上
     *
     * @return /data/user/0/包名/files/clearAbleContent/dir
     */
    public static String getInnerClearAbleFilePath(Context context, String dir) {
        return FileUtil.getInnerFilePath(context, APP_CLEAR_ABLE_CONTENT +
                (TextUtils.isEmpty(dir) ? "" : File.separator + dir));
    }
}
