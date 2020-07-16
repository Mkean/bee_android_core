package com.bee.update.impl;

import com.bee.update.base.UpdateChecker;
import com.bee.update.model.Update;

public class DefaultUpdateChecker extends UpdateChecker {
    @Override
    public boolean check(Update update) throws Exception {
        return false;
    }
}
