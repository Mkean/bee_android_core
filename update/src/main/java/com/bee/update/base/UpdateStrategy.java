package com.bee.update.base;

import com.bee.update.model.Update;

/**
 * 此接口用于定制更新时各节点通知的显示逻辑
 */
public abstract class UpdateStrategy {

    /**
     * 当通过 {@link UpdateChecker}检测到需要更新时。是否显示界面更新通知
     *
     * @param update 更新数据实体类
     * @return {@code true} 代表需要显示更新，{@code false} 代表不显示更新通知，直接调起后续流程（启动apk文件下载任务）
     */
    public abstract boolean isShowUpdateDialog(Update update);

    /**
     * 在使用{@link DownloadWorker} 执行文件下载任务时，是否显示界面进度条通知
     *
     * @return {@code true} 显示 ，{@code false} 不显示
     */
    public abstract boolean isShowDownloadDialog();

    /**
     * 下载完成后，跳过下载完成的界面通知，直接自动启动安装任务
     *
     * @return
     */
    public abstract boolean isAutoInstall();
}
