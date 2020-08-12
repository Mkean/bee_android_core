package com.bee.core.manager.countdown;

/**
 * @Description: 倒计时需要用到的bean对象
 */
public class CountdownTimeBean {
    // 计数器 要执行的次数
    public int countNum;

    //计数器
    public int count;

    // 回调接口
    public CountdownTimeManagerListener listener;

    public boolean isHasListener(CountdownTimeManagerListener listener) {
        return listener != null && listener.equals(this.listener);
    }
}
