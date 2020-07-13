package com.bee.android.common.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bee.android.common.R;
import com.bee.android.common.app.BaseApplication;

import java.lang.reflect.Field;

/**
 * @author qjw
 * @date 2019/9/24
 */
public class ToastUtils {
    private static final String TAG = "ToastUtils";

    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static boolean isReflectedHandler;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "static error" + e.getMessage());
        }
    }

    /**
     * 展示文本 Toast
     *
     * @param msg 内容
     */
    public static void show(String msg) {
        synchronized (ToastUtils.class) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            View view = View.inflate(BaseApplication.app, R.layout.toast_layout, null);
            TextView textView = view.findViewById(R.id.toastTv);
            textView.setText(msg);
            show(view);
        }
    }

    /**
     * 展示带警告图片 Toast
     *
     * @param msg 内容
     */
    public static void showWarring(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        View view = View.inflate(BaseApplication.app, R.layout.toast_layout, null);
        TextView textView = view.findViewById(R.id.toastTv);
        textView.setText(msg);
        ImageView imageView = view.findViewById(R.id.toastImageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.icon_toast_warning);
        show(view);
    }

    /**
     * 展示带对勾图片Toast
     *
     * @param msg 内容
     */
    public static void showSuccess(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        View view = View.inflate(BaseApplication.app, R.layout.toast_layout, null);
        TextView textView = view.findViewById(R.id.toastTv);
        textView.setText(msg);
        ImageView imageView = view.findViewById(R.id.toastImageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.icon_toast_success);
        show(view);
    }


    private static void show(View view) {
        Toast mToast = new Toast(BaseApplication.app);
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        hook(mToast);
        mToast.show();
    }

    private static void hook(Toast toast) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.N && sdkInt < Build.VERSION_CODES.O && !isReflectedHandler) {
            try {
                Object tn = sField_TN.get(toast);
                Handler preHandler = (Handler) sField_TN_Handler.get(tn);
                sField_TN_Handler.set(tn, new SafeHandler(preHandler));
            } catch (Exception e) {
                Log.e(TAG, "hook error" + e.getMessage());
            }
            //这里为了避免多次反射，使用一个标识来限制
            isReflectedHandler = true;
        }
    }

    private static class SafeHandler extends Handler {
        private Handler impl;

        public SafeHandler(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
                Log.e(TAG, "dispatchMessage:" + e.getMessage());
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
}
