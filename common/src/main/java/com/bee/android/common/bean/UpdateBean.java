package com.bee.android.common.bean;

/**
 * @Description:
 */
public class UpdateBean {
    public static final int UPDATE_TYPE_ONE = 1; //1 强制升级
    public static final int UPDATE_TYPE_TWO = 2; //2建议升级
    public static final int UPDATE_TYPE_THREE = 3; //3不弹窗

    private String version;
    private String desc;
    private String file_url;
    private int update_type;  //1 强制升级  2建议升级 3不弹窗
    private String file_size;//apk包大小

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(int update_type) {
        this.update_type = update_type;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }
}
