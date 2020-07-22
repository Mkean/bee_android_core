package com.bee.update.update;

import com.bee.android.common.base.CommonApplication;
import com.bee.core.manager.CommonFileManager;
import com.bee.update.base.FileCreator;
import com.bee.update.impl.DefaultFileCreator;
import com.bee.update.model.Update;

import java.io.File;

/**
 * @Description: 生成下载apk文件的文件地址
 * <p>
 * 默认使用参考{@link DefaultFileCreator}
 */
public class CustomApkFileCreator extends FileCreator {
    @Override
    protected File create(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
        path.mkdirs();
        return new File(path,"UpdatePlugin_" + update.getVersionName());
    }

    @Override
    protected File createForDaemon(Update update) {
        // 根据传入的versionName创建一个文件。供下载时网络框架使用
        File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
        path.mkdirs();
        return new File(path,"UpdatePlugin_daemon_" + update.getVersionName());
    }
}
