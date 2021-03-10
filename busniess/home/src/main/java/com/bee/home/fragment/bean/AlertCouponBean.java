package com.bee.home.fragment.bean;

/**
 * @Description:
 */
public class AlertCouponBean {
    /**
     * is_login : 1
     * img : https://cdnqn.canslife.com/canshi/2020/09/11/1121599594953.png
     * url : http://h5-test.canslife.com/me/coupon/unUse
     * is_new_user : 0
     * is_can_use_coupon : 1
     */

    private int is_login;
    private String img;
    private String url;
    private int is_new_user;
    private int is_can_use_coupon;

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(int is_new_user) {
        this.is_new_user = is_new_user;
    }

    public int getIs_can_use_coupon() {
        return is_can_use_coupon;
    }

    public void setIs_can_use_coupon(int is_can_use_coupon) {
        this.is_can_use_coupon = is_can_use_coupon;
    }
}
