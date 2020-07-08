package com.bee.android.common.permission;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

/**
 * 权限申请工具类
 */
public class PermissionUtils {
    private static long mCurTime;

    public static void requestPermission(FragmentActivity activity, RequestPermission requestPermission, String... permissions) {
        if (activity == null || requestPermission == null || permissions.length == 0) {
            return;
        }
        if (System.currentTimeMillis() - mCurTime >= 1000) {
            mCurTime = System.currentTimeMillis();
            new PermissionHelp(activity, requestPermission).requestPermission(permissions);
        }
    }

    public interface RequestPermission {
        /**
         * 权限申请成功
         */
        void onRequestPermissionSuccess();

        /**
         * 用户拒绝了权限申请，权限申请失败
         *
         * @param permissionDeny
         * @param permissionNoAsk
         */
        void onRequestPermissionFailure(List<String> permissionDeny, List<String> permissionNoAsk);
    }
}
