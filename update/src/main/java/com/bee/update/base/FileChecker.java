package com.bee.update.base;

import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.impl.DefaultFileChecker;
import com.bee.update.model.Update;

import java.io.File;

/**
 * 用于提供在更新中对apk进行有效性、安全性检查的接口
 * <p>
 * 配置方式：通过{@link UpdateConfig#setFileChecker(FileChecker)}或者{@link UpdateBuilder#setFileChecker(FileChecker)}进行配置
 * <p>
 * 默认实现：{@link DefaultFileChecker}
 */
public abstract class FileChecker {

    protected Update update;
    protected File file;

    public final void attach(Update update, File file) {
        this.update = update;
        this.file = file;
    }

    public final boolean checkBeforeDownload() {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            return onCheckBeforeDownload();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在启动下载任务前，对{@link FileCreator} 创建的缓存文件进行验证，判断是否此文件已经被成功下载完成。
     * <p>
     * 当验证成功时，则代表此文件在之前就已经被下载好了，则跳过下载任务
     *
     * <p>若下载失败
     *
     * @return
     */
    protected abstract boolean onCheckBeforeDownload() throws Exception;

    /**
     * 当下载完成后，触发到此，进行文件安全校验检查。当检查成功，即可启动安装任务，安装更新apk
     * <p>
     * 若检查失败，则可主动抛出一个异常。用于提供给框架捕获并通知给用户。
     *
     * @throws Exception
     */
    protected abstract void onCheckBeforeInstall() throws Exception;
}
