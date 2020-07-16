package com.bee.update.flow;

import com.bee.update.UpdateBuilder;
import com.bee.update.model.Update;

/**
 * 此类用于调起更新流程中的两处网络任务进行执行。
 *
 * <p> 1. 检查更新api网络任务{@link #launchCheck(UpdateBuilder)}
 *
 * <p> 2.发起apk文件下载任务:{@link #launchDownload(Update, UpdateBuilder)}
 */
public class Launcher {
    public static final String TAG = "Update";

    private static Launcher launcher;

    private Launcher() {
    }

    public static Launcher getInstance() {
        if (launcher == null) {
            launcher = new Launcher();
        }
        return launcher;
    }

    /**
     * 调起检查api更新任务
     *
     * @param builder
     */
    public void launchCheck(UpdateBuilder builder) {

    }

    public void launchDownload(Update update, UpdateBuilder builder) {

    }
}
