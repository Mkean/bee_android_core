package com.bee.update.impl;

import com.bee.update.base.UpdateStrategy;
import com.bee.update.model.Update;

public class WifiFirstStrategy extends UpdateStrategy {
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
