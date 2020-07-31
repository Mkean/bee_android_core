package com.bee.android.common.bean;

/**
 * @Description: 用户资料
 */
public class UserBean {

    /**
     * birthday ： 2014-06-01
     * en_name : Anna
     * gender : 1
     * head_img : https://test-img.100tal.com/user/20200529/TalzwZVRnK1M09Sohhv4hb3E7g1145.jpg
     * hide_phone : 183****4270
     * nickname : 刚刚回来
     * tal_id : TalzwZVRnK1M09Sohhv4hb3E7g
     * user_id : 23
     * user_type : 1
     * credits : 0
     * app_code : 1
     * purchase_list_status : 012
     * create_time : 2019-10-28 17:24:33
     */

    private String birthday;
    private String en_name;
    private String gender;
    private String head_img;
    private String hide_phone;
    private String nickname;
    private String tal_id;
    private String user_id;
    private int user_type;
    private String credits;
    private int app_code;
    private String purchase_status;
    private String create_time;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getHide_phone() {
        return hide_phone;
    }

    public void setHide_phone(String hide_phone) {
        this.hide_phone = hide_phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTal_id() {
        return tal_id;
    }

    public void setTal_id(String tal_id) {
        this.tal_id = tal_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public int getApp_code() {
        return app_code;
    }

    public void setApp_code(int app_code) {
        this.app_code = app_code;
    }

    public String getPurchase_status() {
        return purchase_status;
    }

    public void setPurchase_status(String purchase_status) {
        this.purchase_status = purchase_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "birthday='" + birthday + '\'' +
                ", en_name='" + en_name + '\'' +
                ", gender='" + gender + '\'' +
                ", head_img='" + head_img + '\'' +
                ", hide_phone='" + hide_phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", tal_id='" + tal_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_type=" + user_type +
                ", credits='" + credits + '\'' +
                ", app_code=" + app_code +
                ", purchase_status='" + purchase_status + '\'' +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
