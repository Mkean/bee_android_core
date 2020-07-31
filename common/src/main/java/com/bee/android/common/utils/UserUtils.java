package com.bee.android.common.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bee.android.common.CommonApi;
import com.bee.android.common.bean.UserBean;
import com.bee.android.common.config.CommonGlobalConfigKt;
import com.bee.android.common.mmkv.MMKVUtils;
import com.bee.android.common.network.rxjava.BaseTranFormer;
import com.bee.android.common.network.rxjava.ErrorException;
import com.bee.android.common.network.rxjava.ResultCallback;
import com.bee.core.logger.CommonLogger;
import com.bee.core.network.API;
import com.tencent.mmkv.MMKV;

/**
 * @Description: 用户信息数据获取类
 */
public class UserUtils {

    private static final String TAG = "UserUtils";
    private static final String MMKV_ID = "user";

    private UserUtils() {
    }

    public static UserUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static UserUtils INSTANCE = new UserUtils();
    }

    private int isLogin;
    private String token;
    private String head_image;
    private String gender;
    private String nickName;
    private String birthday;
    private String user_id;
    private String hide_phone;
    private String create_time;
    private int bananaCount;
    private String en_name;

    public void storeUserInfo(String head_image, String gender, String nickName, String birthday, String user_id,
                              String hide_phone, String create_time, String en_name) {
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.HEAD_IMAGE, head_image);
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.GENDER, gender);
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.NICKNAME, nickName);
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.BIRTHDAY, birthday);
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.USER_ID, user_id);
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.EN_NAME, en_name);

        CommonLogger.d(TAG, "hide_phone_login:" + hide_phone);
        if (!TextUtils.isEmpty(hide_phone)) {
            if (TextUtils.isEmpty(MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.HIDE_PHONE, ""))) {
                MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.HIDE_PHONE, hide_phone);
            }
        }

        if (!TextUtils.isEmpty(create_time)) {
            if (TextUtils.isEmpty(MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.CREATE_TIME, ""))) {
                MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.CREATE_TIME, create_time);
            }
        }
    }

    public void storeLoginStatus(@NonNull int loginStatus) {
        MMKVUtils.putInt(MMKV_ID, CommonGlobalConfigKt.IS_LOGIN, loginStatus);
    }

    public void storeUserId(@NonNull String user_id) {
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.USER_ID, user_id);
    }

    /**
     * 用户类型
     *
     * @param is_new
     */
    public void storePurchaseStatus(String is_new) {
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.PURCHASE_STATUS, is_new);
    }

    public String getPurchaseStatus() {
        if (!UserUtils.getInstance().isUserLoginValid()) {
            return "-1";
        }
        return MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.PURCHASE_STATUS, "-1");
    }

    public void storeHidePhone(String hide_phone) {
        CommonLogger.d(TAG, "hide_phone_获取个人资料：" + hide_phone);
        if (!TextUtils.isEmpty(hide_phone)) {
            MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.HIDE_PHONE, hide_phone);
        }
    }

    /**
     * 设置香蕉币
     *
     * @param bananaCount
     */
    public void setBananaCount(String bananaCount) {
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.BANANA_COUNT, bananaCount);
    }


    /**
     * 获取香蕉币
     *
     * @return
     */
    public String getBananaCount() {
        return MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.BANANA_COUNT, "0");
    }

    /**
     * 判断用户是否处于已登录状态
     *
     * @return {@code true} 为已登录 {@code false} 为未登录
     */
    public boolean isUserLoginValid() {
        int loginStatus = MMKVUtils.getInt(MMKV_ID, CommonGlobalConfigKt.IS_LOGIN, -1);
        CommonLogger.e(TAG, "用户登录状态 loginStatus :" + loginStatus);
        return loginStatus == 1;
    }

    public void storeToken(String token) {
        MMKVUtils.putString(MMKV_ID, CommonGlobalConfigKt.TOKEN, token);
    }

    public String getToken() {
        token = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.TOKEN, "");

        return token;
    }

    public String getHead_image() {
        head_image = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.HEAD_IMAGE, "");

        return head_image;
    }

    public String getGender() {
        gender = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.GENDER, "");

        return gender;
    }

    public String getNickName() {
        nickName = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.NICKNAME, "");

        return nickName;
    }

    public String getEnglishName() {
        en_name = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.EN_NAME, "");

        return en_name;
    }

    public String getBirthday() {
        birthday = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.BIRTHDAY, "");

        return birthday;
    }

    public String getUser_id() {
        user_id = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.USER_ID, "");

        return user_id;
    }

    public String getHide_phone() {
        hide_phone = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.HIDE_PHONE, "");

        return hide_phone;
    }

    public String getCreate_time() {
        create_time = MMKVUtils.getString(MMKV_ID, CommonGlobalConfigKt.CREATE_TIME, "");

        return create_time;
    }

    public void refreshUserInfo() {
        getUserInfo(null);
    }

    /**
     * @param callback 获取用户资料
     */
    public void getUserInfo(final Callback callback) {
        API.INSTANCE.service(CommonApi.class)
                .getUserInfo()
                .compose(new BaseTranFormer<>())
                .subscribe(new ResultCallback<UserBean>() {
                    @Override
                    public void onSuccess(UserBean bean) {
                        Log.e("is_empty_profile", "getUserInfoOnSuccess>>>>" + bean.toString());

                        if (callback != null) {
                            callback.onSuccess(bean);
                        }

                        if (bean != null) {
                            UserUtils.getInstance().storeUserInfo(
                                    TextUtils.isEmpty(bean.getHead_img()) ? "" : bean.getHead_img(),
                                    bean.getGender() + "",
                                    bean.getNickname(),
                                    bean.getBirthday(),
                                    bean.getUser_id(),
                                    bean.getHide_phone(),
                                    bean.getCreate_time(),
                                    bean.getEn_name());
                            UserUtils.getInstance().storePurchaseStatus(bean.getPurchase_status());
                        }
                    }

                    @Override
                    public void onCommonFailure(ErrorException e) {
                        super.onCommonFailure(e);
                        Log.e("is_empty_profile", "getUserInfoOnFailure>>>" + e.getMessage());
                        Log.e("is_empty_profile", "getUserInfoOnFailure>>>" + e.getCode());
                        Log.e("is_empty_profile", "getUserInfoOnFailure>>>" + e.getLocalizedMessage());

                        if (null != callback) {
                            callback.onFail();
                        }
                    }
                });
    }

    /**
     * 退出登录
     */
    public void logout() {
        API.INSTANCE.service(CommonApi.class)
                .logout()
                .compose(new BaseTranFormer<>())
                .subscribe(new ResultCallback<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }
                });
    }

    public interface Callback {
        void onSuccess(UserBean bean);

        void onFail();
    }

}
