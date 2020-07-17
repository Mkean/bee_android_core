package com.bee.update.base;

import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;

import java.io.File;

/**
 * 更新下载回调
 * <p> 设置方式{@link UpdateConfig#setDownloadCallback(DownloadCallback)}或者{@link UpdateBuilder#setDownloadCallback(DownloadCallback)}
 */
public interface DownloadCallback {

    /**
     * 启动下载任务
     * 启动线程 UI
     */
    void onDownloadStart();

    /**
     * 下载完成
     * 启动线程：UI
     *
     * @param file 下载的文件
     */
    void onDownloadComplete(File file);

    /**
     * 下载的进度
     * 回调线程：UI
     *
     * @param current 当前已下载长度
     * @param total   整个文件传长度
     */
    void onDownloadProgress(long current, long total);

    /**
     * 当前下载出现异常通知到此回调
     * 回调线程：UI
     *
     * @param t
     */
    void onDownloadError(Throwable t);
}
