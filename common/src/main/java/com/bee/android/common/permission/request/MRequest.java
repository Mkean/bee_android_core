package com.bee.android.common.permission.request;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.bee.android.common.permission.checker.StrictChecker;
import com.bee.android.common.permission.config.SpecialModel;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 6.0+ 权限处理
 */
public class MRequest extends BaseRequest {

    public MRequest(DealPermissionListener dealListener) {
        super(dealListener);
    }

    @Override
    public void requestPermission(FragmentActivity activity, String... permissions) {
        List<String> needRequest = new ArrayList<>();
        RxPermissions rxPermissions = new RxPermissions(activity);
        //过滤已经申请过的权限
        for (String permission : permissions) {
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }
        if (!needRequest.isEmpty()) {//没有申请过,则开始申请
            rxPermissions.requestEach(needRequest.toArray(new String[needRequest.size()]))
                    .buffer(permissions.length)
                    .subscribe(new Observer<List<Permission>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<Permission> permissions) {
                            dealNextPermission(permissions);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {//全部权限都已经申请过
            dealAllGrant(activity, permissions);
        }
    }

    private void dealNextPermission(List<Permission> permissions) {
        List<String> deny = new ArrayList<>();
        List<String> denyWithNoAsk = new ArrayList<>();
        for (Permission p : permissions) {
            if (!p.granted) {
                if (p.shouldShowRequestPermissionRationale) {
                    deny.add(p.name);
                } else {
                    denyWithNoAsk.add(p.name);
                }
            }
        }
        Log.e("dealNextPermission", deny.toString());
        Log.e("dealNextPermission", denyWithNoAsk.toString());
        if (mDealListener != null) {
            mDealListener.dealFinish(deny, denyWithNoAsk);
        }
    }

    private void dealAllGrant(FragmentActivity activity, String[] permissions) {
        List<String> deny = new ArrayList<>();
        List<String> denyWithNoAsk = new ArrayList<>();

        if (SpecialModel.specialModel.contains(android.os.Build.MODEL)) {//特殊机型处理
            for (String per : permissions) {
                if (StrictChecker.hasPermission(activity, per)) {
                    denyWithNoAsk.add(per);
                }
            }
        }
        if (mDealListener != null) {
            mDealListener.dealFinish(deny, denyWithNoAsk);
        }
    }
}
