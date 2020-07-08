package com.bee.android.common.permission.request;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

/**
 * 基础 Request
 */
public abstract class BaseRequest {

    DealPermissionListener mDealListener;

    public BaseRequest(DealPermissionListener dealPermission) {
        this.mDealListener = dealPermission;
    }

    public abstract void requestPermission(FragmentActivity context, String[] permission);

    public interface DealPermissionListener {
        void dealFinish(List<String> deny, List<String> denyWithNoAsk);
    }
}
