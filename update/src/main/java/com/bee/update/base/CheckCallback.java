package com.bee.update.base;

import com.bee.update.model.Update;

/**
 * 检查更新的回调监听
 */
public interface CheckCallback {

    /**
     *
     */
    void onCheckStart();

    void hasUpdate(Update update);

    void noUpdate();

    void onCheckError(Throwable e);

    /**
     *
     */
    void onUserCancel();


    /**
     * 当用户点击忽略此版本更新时会触发到此回调中，
     * @param update
     */
    void onCheckIgnore(Update update);
}
