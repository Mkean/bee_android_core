package com.bee.android.common.web.config;

import com.bee.android.common.network.config.UrlConfig;

/**
 * @Description:
 */
public class H5WebConfig {

    // H5页面Router协议
    public static final String H5_ROUTER_WEB_ACTIVITY = "/common/H5WebActivity";

    /**
     * 页面接受参数
     */
    // 打开页面的URL（必传）
    public static final String H5_PARAM_URL = "h5_url";

    // H5页面标识，对应下面的H5_FLAG_xxx(建议传)
    public static final String H5_PARAM_FLAG = "h5_flag";

    // H5页面描述（用于埋点）（建议传）
    public static final String H5_PARAM_DESC = "h5_desc";

    // 页面来源（用于H5标识）（非必传）
    public static final String H5_PARAM_PREVIOUS_SOURCE = "previous_source";


    /**
     * URL：H5页面的URL，（必须项，URL需要拼接的业务相关的参数，各业务自行拼接，H5WebActivity负责拼接公共参数）
     * FLAG：H5页面对应的一个标识，可用于针对某个业务做某些操作（非必传项，建议传）（例如对系统课分享埋点）
     */

    // 系统课
    public static final String H5_FLAG_SYSTEM_COURSE = "1";
    public static final String H5_URL_SYSTEM_COURSE = UrlConfig.H5_URL + "static/monkey-chinese/systemClass";

    // 体验课
    public static final String H5_FLAG_EXPERIENCE_COURSE = "2";
    public static final String H5_URL_EXPERIENCE_COURSE = UrlConfig.H5_URL + "static/monkey-chinese/experienceClass";

    // 打卡
    public static final String H5_FLAG_SING_IN = "3";
    public static final String H5_URL_SING_IN = UrlConfig.H5_URL + "static/monkey-chinese/clockIn";

    // 日报
    public static final String H5_FLAG_SHARE_DAILY = "4";
    public static final String H5_URL_SHARE_DAILY = UrlConfig.H5_URL + "static/monkey-chinese/report/day";

    // 香蕉币明细
    public static final String H5_FLAG_DETAIL_RECORD = "5";
    public static final String H5_URL_DETAIL_RECORD = UrlConfig.H5_URL + "static/monkey-chinese/bonusDetail";

    // 香蕉币规则
    public static final String H5_FLAG_BANANA_RULE = "6";
    public static final String H5_URL_BANANA_RULE = UrlConfig.H5_URL + "static/monkey-chinese/bonusCoinDesc";

    // 积分商城详情页面
    public static final String H5_FLAG_INTEGRAL_STORE = "7";
    public static final String H5_URL_INTEGRAL_STORE = UrlConfig.H5_URL + "static/monkey-chinese/bonusMall";

    // 个人中心优惠券列表
    public static final String H5_FLAG_COUPON_LIST = "8";
    public static final String H5_URL_COUPON_LIST = UrlConfig.H5_URL + "wukong-app/coupon/";

    // 订单物流
    public static final String H5_FLAG_ORDER_LIST = "9";
    public static final String H5_URL_ORDER_LIST = UrlConfig.H5_URL + "wukong-app/order/order-list";

    // 物流详情页面
    public static final String H5_FLAG_LOGISTICS_DETAILS = "10";
    public static final String H5_URL_LOGISTICS_DETAILS = UrlConfig.H5_URL + "wukong-app/order/express-detail";

    // 运单地址编辑页面
    public static final String H5_FLAG_ADDRESS_EDIT = "11";
    public static final String H5_URL_ADDRESS_EDIT = UrlConfig.H5_URL + "wukong-app/order/edit-address";

    // 课程管理
    public static final String H5_FLAG_COURSE_MANAGE = "12";
    public static final String H5_URL_COURSE_MANAGE = UrlConfig.H5_URL + "wukong-app/courseManage/";

    // 训练场游戏战绩
    public static final String H5_FLAG_GAME_SHARE = "13";
    public static final String H5_URL_GAME_SHARE = UrlConfig.H5_URL + "static/monkey-chinese/shareGame";

    // 隐私协议
    public static final String H5_FLAG_PRIVATE = "14";
    // 和 H5沟通，隐私协议和注册协议不区分环境
    public static final String H5_URL_PRIVATE = "https://h5.xueersi.com/5eeb4d52ba7cae35408af4a0.html";

    // 注册协议
    public static final String H5_URL_REGISTER = "https://h5.xueersi.com/5eeb4d23631b292c76e66a37.html";
    // 和 H5沟通，隐私协议和注册协议不区分环境
    public static final String H5_FLAG_REGISTER = "15";

    // 关于web页
    public static final String H5_FLAG_ABOUT_US = "16";
    public static final String H5_URL_ABOUT_US = UrlConfig.H5_URL + "static/monkey-chinese/personal/agreement/about";

    // 课程兑换
    public static final String H5_FLAG_EXCHANGE_PAGE = "17";
    public static final String H5_URL_EXCHANGE_PAGE = UrlConfig.H5_URL + "wukong-app/exchangePage/";


    /**
     * h5 页面唯一标识
     */
    // 系统课
    public static final String PAGE_ID_SYSTEM_CLASS = "systemClass";
    // 体验课
    public static final String PAGE_ID_EXPERIENCE_CLASS = "experienceClass";
    // 打卡
    public static final String PAGE_ID_CLOCKIN = "clockIn";
    // 日报
    public static final String PAGE_ID_DAY_REPORT = "dayReport";
    // 周报
    public static final String PAGE_WEEK_REPORT = "weekReport";
    // 商品详情页
    public static final String PAGE_ID_MALL_DETAIL = "mallDetail";


    /**
     * h5 打开原生页面协议 -- imk 开头
     */

    public static final String IMK_TOP = "wangxiaohmk://";
    public static final String IMK_LOGIN = IMK_TOP + "native/login"; // 跳转登录
    public static final String IMK_COURSE_DETAILS = IMK_TOP + "native/goclass"; // 跳转课程详情（上课页）
    public static final String IMK_TAB_SHOP = IMK_TOP + "native/integral_shop"; // 跳转积分商城tab

}
