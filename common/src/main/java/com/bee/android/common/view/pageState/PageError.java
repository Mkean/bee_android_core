package com.bee.android.common.view.pageState;

/**
 * 页面切换错误封装类
 */
public class PageError {

    public static final int ERROR_CODE_NETWORK = 1; // 网络错误
    public static final int ERROR_CODE_SERVER = 2; // 服务器错误
    public static final int ERROR_CODE_CODE = 3; // 代码错误

    public PageError() {
    }

    public PageError(int errCode) {
        this.errCode = errCode;
        if (errCode == ERROR_CODE_SERVER) {
            msg = "服务器错误";
        } else if (errCode == ERROR_CODE_NETWORK) {
            msg = "网络错误";
        }else if(errCode ==  ERROR_CODE_CODE){
            msg = "代码错误";
        }
    }

    private int errCode;
    private String msg;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
