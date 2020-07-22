package com.bee.android.common.permission;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.bee.android.common.base.CommonApplication;
import com.bee.android.common.dialog.BaseDialog;
import com.bee.android.common.dialog.DialogManager;
import com.bee.core.R;
import com.bee.core.permission.PermissionDeal;
import com.bee.core.permission.config.PermissionStr;
import com.bee.core.permission.request.BaseRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限帮助类
 */
public class PermissionHelp {

    private FragmentActivity activity;
    private PermissionDeal mPermissionDeal;
    private PermissionUtils.RequestPermission mRequestPermission;
    private BaseRequest.DealPermissionListener mDealPermissionListener = this::dealRequest;

    public PermissionHelp(FragmentActivity activity, PermissionUtils.RequestPermission requestPermission) {
        this.activity = activity;
        this.mRequestPermission = requestPermission;
        this.mPermissionDeal = new PermissionDeal(mDealPermissionListener);
    }

    public void requestPermission(String... permission) {
        if (activity != null && mPermissionDeal != null) {
            mPermissionDeal.dealPermissions(activity, permission);
        }
    }

    private void dealRequest(List<String> deny, List<String> denyWithNoAsk) {
        if (deny != null && deny.size() > 0) {
            if (mRequestPermission != null) {
                mRequestPermission.onRequestPermissionFailure(deny, denyWithNoAsk);
            }
        }

        if (denyWithNoAsk != null && denyWithNoAsk.size() > 0) {
            String tips = getTips(deny, denyWithNoAsk);
            showTipsDialog(tips, deny, denyWithNoAsk);
        }

        if (deny != null && deny.size() == 0 && denyWithNoAsk != null && denyWithNoAsk.size() == 0 && mRequestPermission != null) {
            mRequestPermission.onRequestPermissionSuccess();
            dealFinished();
        }
    }

    private void showTipsDialog(String tips, List<String> deny, List<String> denyWithNoAsk) {
        if (activity == null) {
            return;
        }
        DialogManager.getInstance().showTitleContentTwoButtonDialog(activity, "权限被禁用", tips, "取消", "去设置",
                new BaseDialog.ActionListener() {
                    @Override
                    public void clickLeft() {
                        if (mRequestPermission != null) {
                            mRequestPermission.onRequestPermissionFailure(deny, denyWithNoAsk);
                        }
                        dealFinished();
                    }

                    @Override
                    public void clickRight() {
                        goSettingPage();
                        dealFinished();
                    }
                });
    }

    private void goSettingPage() {
        if (activity != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        }
    }

    private String getTips(List<String> deny, List<String> denyWithNoAsk) {
        List<String> values = new ArrayList<>();
        for (String s : deny) {
            String str = PermissionStr.query(s);
            if (!values.contains(str)) {
                values.add(str);
            }
        }
        for (String s : denyWithNoAsk) {
            String str = PermissionStr.query(s);
            if (!values.contains(str)) {
                values.add(str);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : values) {
            stringBuilder.append(PermissionStr.query(s)).append("、");
            Log.e("GetTips", s);
            Log.e("GetTips", PermissionStr.query(s));

        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        String appName = CommonApplication.app.getResources().getString(R.string.app_name);
        return "请到\"设置-应用-" + appName + "\"中打开" + stringBuilder.toString() + "权限";
    }

    private void dealFinished() {
        activity = null;
        mPermissionDeal = null;
        mDealPermissionListener = null;
    }
}
