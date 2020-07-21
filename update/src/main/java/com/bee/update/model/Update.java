package com.bee.update.model;

import com.bee.update.impl.DefaultInstallNotifier;
import com.bee.update.impl.DefaultUpdateChecker;
import com.bee.update.impl.DefaultUpdateNotifier;
import com.bee.update.impl.ForcedUpdateStrategy;

/**
 * 此实体类用于存储框架所需的更新数据
 */
public class Update {

    private boolean forced;
    private boolean ignore;
    private String updateContent;
    private String updateUrl;
    private int versionCode;
    private String versionName;
    private String md5;

    /**
     * 指定是否展示忽略此版本更新按钮：
     * <p>
     * 此属性为非必须属性。即框架核心操作层并未依赖此属性，此属性只用于提供的默认弹窗通知中：{@link DefaultInstallNotifier} 和 {@link DefaultUpdateNotifier}
     *
     * @param ignore true 代表在弹出通知中进行展示 <b>忽略此版本更新按钮</b>
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    /**
     * 指定是否要求进行强制更新，当设置为强制更新时，将会导致设置的更新策略无效，而直接使用框架内部所提供的{@link ForcedUpdateStrategy}进行更新策略管理
     *
     * @param forced {@code true}代表此版本需要进行强制更新
     */
    public void setForced(boolean forced) {
        this.forced = forced;
    }

    /**
     * 设置此次版本更新内容，将在更新弹窗通知中使用
     *
     * @param updateContent
     */
    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    /**
     * 置此次版本的远程更新apk包
     *
     * @param updateUrl
     */
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    /**
     * 版本apk的版本号。此版本号将被用于与本地apk进行版本号比对。判断该版本是否应该被更新. 默认版本号检查器为：{@link DefaultUpdateChecker}.
     *
     * @param versionCode
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 新版本的apk的版本名。
     *
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 指定下载文件的md5值。用于对下载文件进行检查时使用。
     *
     * @param md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isForced() {
        return forced;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getMd5() {
        return md5;
    }

    @Override
    public String toString() {
        return "Update{" +
                "forced=" + forced +
                ", ignore=" + ignore +
                ", updateContent='" + updateContent + '\'' +
                ", updateUrl='" + updateUrl + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
