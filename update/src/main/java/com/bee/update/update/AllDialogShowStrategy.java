package com.bee.update.update;

import com.bee.update.base.UpdateStrategy;
import com.bee.update.impl.WifiFirstStrategy;
import com.bee.update.model.Update;

/**
 * @Description: 自定义强制显示所有Dialog策略
 * <p>
 * 默认使用参考 {@link WifiFirstStrategy}
 */
public class AllDialogShowStrategy extends UpdateStrategy {
    /**
     * 指定是否在判断出有需要更新的版本时，弹窗更新提醒按钮
     *
     * @param update 更新数据实体类
     * @return {@code true} 显示弹窗
     */
    @Override
    public boolean isShowUpdateDialog(Update update) {
        return true;
    }

    /**
     * 指定是否在下载完成后自动进行安装不显示弹窗
     *
     * @return {@code true} 直接安装，不显示弹窗
     */
    @Override
    public boolean isAutoInstall() {
        return false;
    }

    /**
     * 指定是否在下载的时候显示下载进度弹窗
     *
     * @return {@code true} 显示弹窗
     */
    @Override
    public boolean isShowDownloadDialog() {
        return true;
    }
}
