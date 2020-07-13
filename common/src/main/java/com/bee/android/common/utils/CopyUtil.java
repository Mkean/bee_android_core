package com.bee.android.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 复制类容至粘贴板
 */
public class CopyUtil {

    /**
     * 复制粘贴板
     *
     * @param context 上下文
     * @param content 复制内容
     */
    public static void copyText(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("CopyUtil", content);
        cm.setPrimaryClip(mClipData);
    }
}
