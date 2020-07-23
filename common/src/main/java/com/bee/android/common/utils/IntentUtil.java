package com.bee.android.common.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bee.android.common.base.CommonApplication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @Description:
 */
public class IntentUtil {

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager(); // 获取 packageManager
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0); // 获取所有已安装程序的包信息
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager var2 = context.getPackageManager();

        try {
            return var2.getPackageInfo("com.eg.android.AlipayGphone", 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("CheckResult")
    public static void startWX() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    try {
                        Intent intent = new Intent();
                        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        CommonApplication.app.startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
