package com.bee.android.common.network.config;

import com.bee.android.common.base.CommonApplication;
import com.bee.core.utils.AppUtils;

public class HeaderHelper {
    public static final String API_KEY = "from_client";
    public static final String API_SECRET = "com.yingegou.android";
    public static final String VERSION_CODE = "version";
    public static final long VERSION_NAME = AppUtils.getAppVersionCode(CommonApplication.app);
}
