package com.bee.android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;

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

    /**
     * 标题 内容 （包裹内容） 两个按钮
     *
     * @param context
     * @param title
     * @param content
     * @param left
     * @param right
     * @param listener
     */
    public void showTitleContentTwoButtonDialog(Context context, String title, String content,
                                                String left, String right, BaseDialog.ActionListener listener) {
        baseDismiss();
        baseDialog = new TitleContentTwoButtonDialog(context, title, content, left, right, listener);
        baseDialog.show();
    }

    /**
     * 标题 内容（包裹内容） 两个按钮 --SpannableString 类型
     *
     * @param context
     * @param title
     * @param content
     * @param left
     * @param right
     * @param listener
     */
    public void showTitleContentTwoButtonDialog(Context context, String title, SpannableString content,
                                                String left, String right, BaseDialog.ActionListener listener) {
        baseDismiss();
        baseDialog = new TitleContentWrapTwoButtonDialog(context, title, content, left, right, listener);
        baseDialog.show();
    }

    /**
     * 标题 内容（包裹内容） 两个按钮--String 类型
     *
     * @param context
     * @param title
     * @param content
     * @param left
     * @param right
     * @param listener
     */
    public void showTitleContentStrTwoButtonDialog(Context context, String title, String content,
                                                   String left, String right, BaseDialog.ActionListener listener) {
        baseDismiss();
        baseDialog = new TitleContentWrapTwoButtonDialog(context, title, content, left, right, listener);
        baseDialog.show();
    }

    /**
     * 标题 两个按钮
     *
     * @param context
     * @param title
     * @param left
     * @param right
     * @param listener
     */
    public void showTitleTwoButtonDialog(Context context, String title, String left, String right, BaseDialog.ActionListener listener) {
        baseDismiss();
        baseDialog = new TitleTwoButtonDialog(context, title, left, right, listener);
        baseDialog.show();
    }

    /**
     * 标题  一个按钮
     *
     * @param context
     * @param title
     * @param msg
     * @param btnStr
     * @param listener
     */
    public void showSingleButtonDialog(Context context, String title, String msg, String btnStr,
                                       DialogTitleSingleButton.DialogTitleSingleButtonListener listener) {
        baseDismiss();
        baseDialog = new DialogTitleSingleButton(context, title, msg, btnStr, listener);
        baseDialog.setCanceledOnTouchOutsideAndKeyBack(false, false);
        baseDialog.show();
    }

    /**
     * 内容 一个按钮
     *
     * @param context
     * @param msg
     * @param btnStr
     * @param listener
     */
    public void showSingleButtonDialog(Context context, String msg, String btnStr, DialogSingleButton.DialogTitleSingleButtonListener listener) {
        baseDismiss();
        baseDialog = new DialogSingleButton(context);
        ((DialogSingleButton) baseDialog).setData(msg, btnStr, listener);
        baseDialog.setCanceledOnTouchOutsideAndKeyBack(false, false);
        baseDialog.show();
    }

    /**
     * 加载沙漏弹窗
     *
     * @param context
     * @param msg
     */
    public BaseDialog showShaLouDialog(Context context, String msg) {
        baseDismiss();
        baseDialog = new FunnelDialog(context, msg);
        baseDialog.show();
        return baseDialog;
    }


    private void baseDismiss() {
        if (baseDialog != null) {
            baseDialog.dismiss();
            baseDialog = null;
        }
    }

    // TODO：后面需要在继续添加

    /**
     * @param dialog
     */
    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.getOwnerActivity() != null && !dialog.getOwnerActivity().isFinishing()) {
            dialog.dismiss();
        }
    }

}
