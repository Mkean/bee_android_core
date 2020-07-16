package com.bee.update.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.ContextThemeWrapper;

/**
 * 用于安全的进行Dialog显示隐藏的工具类
 */
public class SafeDialogHandle {

    public static void safeShowDialog(Dialog dialog) {
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        Activity bindAt = getActivity(dialog);
        if (!Utils.isValid(bindAt)) {
            L.d("Dialog shown failed:%s", "The Dialog bind's Activity was recycled or finished!");
            return;
        }
        dialog.show();
    }

    private static Activity getActivity(Dialog dialog) {
        Activity bindAt = null;
        Context context = dialog.getContext();
        do {
            if (context instanceof Activity) {
                bindAt = (Activity) context;
                break;
            } else if (context instanceof ContextThemeWrapper) {
                context = ((ContextThemeWrapper) context).getBaseContext();
            } else {
                break;
            }
        } while (true);

        return bindAt;
    }

    public static void safeDismissDialog(Dialog dialog) {
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        Activity bindAt = getActivity(dialog);
        if (bindAt != null && !bindAt.isFinishing()) {
            dialog.dismiss();
        }
    }
}
