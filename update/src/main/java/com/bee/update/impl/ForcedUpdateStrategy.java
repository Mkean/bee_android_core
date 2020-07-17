package com.bee.update.impl;

import com.bee.update.base.UpdateStrategy;
import com.bee.update.model.Update;

public class ForcedUpdateStrategy extends UpdateStrategy {

private UpdateStrategy delegate;

    public ForcedUpdateStrategy(UpdateStrategy delegate) {
        this.delegate = delegate;
    }


    @Override
    public boolean isShowUpdateDialog(Update update) {
        return false;
    }

    @Override
    public boolean isShowDownloadDialog() {
        return false;
    }

    @Override
    public boolean isAutoInstall() {
        return false;
    }
}
