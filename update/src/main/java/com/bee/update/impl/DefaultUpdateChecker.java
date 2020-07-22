package com.bee.update.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bee.core.logger.CommonLogger;
import com.bee.update.base.UpdateChecker;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.UpdatePreference;

/**
 * 默认的更新数据检查器
 *
 * <ol> 实现逻辑说明：
 *     <li>当为强制更新时，直接判断当前的{@link Update#getVersionCode()}是否大于当前应用的versionCode。若大于，册代表需要更新 </li>
 *     <li>当为普通更新，判断是否当前更新的版本在忽略版本列表中。如果不是，在进行versionCode对比</li>
 * </ol>
 */
public class DefaultUpdateChecker extends UpdateChecker {

    private static final String TAG = "Update";

    @Override
    public boolean check(Update update) throws Exception {
        int curVersion = getApkVersion(ActivityManager.get().getApplicationContext());
        CommonLogger.i(TAG, String.valueOf(curVersion));
        return update.getVersionCode() > curVersion &&
                (update.isForced() ||
                        !UpdatePreference.getIgnoreVersions().contains(String.valueOf(update.getVersionCode())));
    }

    public int getApkVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }
}
