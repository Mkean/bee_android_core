package com.bee.update.impl;

import android.app.Activity;
import android.app.Dialog;

import com.bee.update.base.CheckNotifier;

public class DefaultUpdateNotifier extends CheckNotifier {
    @Override
    public Dialog create(Activity activity) {
        return null;
    }
}
