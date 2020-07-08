package com.bee.android.common.dialog;

/**
 * Dialog 销毁工具类
 */
public class DestroyDialogUtils {
    private static DestroyDialogInterface mDestroyDialogInterface;

    public static void setCallback(DestroyDialogInterface destroyDialogInterface) {
        mDestroyDialogInterface = destroyDialogInterface;
    }

    public static void destroyDialog() {
        if (null != mDestroyDialogInterface) {
            mDestroyDialogInterface.destroyDialogManager();
        }
    }
}
