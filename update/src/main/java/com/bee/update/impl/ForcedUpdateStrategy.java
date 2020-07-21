package com.bee.update.impl;

import com.bee.update.base.UpdateStrategy;
import com.bee.update.model.Update;

/**
 * 当为强制更新时，将会强制使用此更新策略
 * <p>此更新策略表现为：<br>
 * 当下载完成后，强制显示下载完成后的界面通知，其他的通知策略默认不变
 */
public class ForcedUpdateStrategy extends UpdateStrategy {

    private UpdateStrategy delegate;

    public ForcedUpdateStrategy(UpdateStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isShowUpdateDialog(Update update) {
        return delegate.isShowUpdateDialog(update);
    }

    @Override
    public boolean isAutoInstall() {
        return false;
    }

    @Override
    public boolean isShowDownloadDialog() {
        return delegate.isShowDownloadDialog();
    }
}
