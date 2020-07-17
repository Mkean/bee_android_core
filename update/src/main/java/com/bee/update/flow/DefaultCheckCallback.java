package com.bee.update.flow;

import com.bee.update.base.CheckCallback;
import com.bee.update.base.CheckWorker;
import com.bee.update.model.Update;

/**
 * @Description: 核心操作类
 * <p>
 * 此类为默认的检查更新api网络任务的通知回调，用于接收从{@link CheckWorker} 中所传递过来的各种状态，并进行后续流程触发
 */
public class DefaultCheckCallback implements CheckCallback {
    @Override
    public void onCheckStart() {

    }

    @Override
    public void hasUpdate(Update update) {

    }

    @Override
    public void noUpdate() {

    }

    @Override
    public void onCheckError(Throwable e) {

    }

    @Override
    public void onUserCancel() {

    }

    @Override
    public void onCheckIgnore(Update update) {

    }
}
