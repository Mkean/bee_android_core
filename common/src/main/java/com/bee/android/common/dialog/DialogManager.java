package com.bee.android.common.dialog;

import android.content.Context;

/**
 * Dialog 管理器
 */
public class DialogManager {
    private static DialogManager INSTANCE;

    private DialogManager() {
    }

    public static DialogManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DialogManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new DialogManager();
                    DestroyDialogUtils.setCallback(DialogManager::destroyInstance);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

    private BaseDialog baseDialog;

    public void showTitleContentTwoButtonDialog(Context context, String title, String content,
                                                String left, String right, BaseDialog.ActionListener listener) {
        baseDismiss();
        baseDialog = new TitleContentTwoButtonDialog(context, title, content, left, right, listener);
        baseDialog.show();
    }

    private void baseDismiss() {
        if (baseDialog != null) {
            baseDialog.dismiss();
            baseDialog = null;
        }
    }


}
