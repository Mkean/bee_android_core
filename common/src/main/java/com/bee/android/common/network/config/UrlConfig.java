package com.bee.android.common.network.config;

public class UrlConfig {

    public static String H5_URL;
    public static String FYJ_URL;
    public static String GCX_URL;


    public static void initUrl(boolean mode) {
        if (mode) {
            H5_URL = Debug.H5_URL;
            FYJ_URL = Debug.FYJ_URL;
            GCX_URL = Debug.GCX_URL;
        }else{
            H5_URL = Release.H5_URL;
            FYJ_URL = Release.FYJ_URL;
            GCX_URL = Release.GCX_URL;
        }
    }


    private static class Debug { // 开发环境
        static final String H5_URL = "http://h5-test.canslife.com/";
        static final String FYJ_URL = "http://zmnjz.bidanet.com/api/v2/";
        static final String GCX_URL = "http://118.178.95.214:10999/api/v2/";
    }

    private static class Release { // 线上环境
        static final String H5_URL = "https://h5.canslife.com/";
        static final String FYJ_URL = "http://front.canslife.com/api/v2/";
        static final String GCX_URL = "http://life.canslife.com/api/v2/";
    }
}
