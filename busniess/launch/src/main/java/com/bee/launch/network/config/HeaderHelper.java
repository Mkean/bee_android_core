package com.bee.launch.network.config;

import com.bee.android.common.utils.AppUtils;
import com.bee.launch.base.BaseApplication;

public class HeaderHelper {
    public static final String API_KEY = "from_client";
    public static final String API_SECRET = "com.yingegou.android";
    public static final String VERSION_CODE = "version";
    public static final long VERSION_NAME = AppUtils.getAppVersionCode(BaseApplication.app);
}
