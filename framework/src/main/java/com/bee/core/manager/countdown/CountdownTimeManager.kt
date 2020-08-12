package com.bee.core.manager.countdown

import com.bee.core.logger.CommonLogger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 *@Description: 全局倒计时类，统一管理倒计时，减少有一个倒计时开一个线程导致的资源浪费
 *
 */
class CountdownTimeManager private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: CountdownTimeManager? = null

        val instance: CountdownTimeManager
            get() {
                if (INSTANCE == null) {
                    synchronized(CountdownTimeManager::class.java) {
                        if (null == INSTANCE) {
                            INSTANCE = CountdownTimeManager()
                        }
                    }
                }
                return INSTANCE!!
            }

        var actionDisposable: Disposable? = null
        var countdownTimeBeans: CopyOnWriteArrayList<CountdownTimeBean>? = null
    }

    /**
     * 倒计时管理器是否已经有指定的 回调接口
     *
     * @param listener 指定的回调接口
     */
    @Synchronized
    fun isHasListener(listener: CountdownTimeManagerListener): Boolean {
        return getListenerBean(listener) != null
    }

    /**
     * 获取指定 回调接口的 bean对象
     *
     * @param listener 指定接口
     */
    @Synchronized
    fun getListenerBean(listener: CountdownTimeManagerListener?): CountdownTimeBean? {
        var hasTimeBean: CountdownTimeBean? = null
        if (listener != null && countdownTimeBeans != null && countdownTimeBeans != null && countdownTimeBeans!!.isNotEmpty()) {
            for (timeBean in countdownTimeBeans!!) {
                if (timeBean != null && timeBean.isHasListener(listener)) {
                    hasTimeBean = timeBean
                    break
                }
            }
        }
        return hasTimeBean
    }

    /**
     *注册接口回调
     *
     * @param listener 回调接口
     * @param secondsNum 多少秒回调一次  单位 秒
     */
    @Synchronized
    fun register(listener: CountdownTimeManagerListener?, secondsNum: Int) {
        if (listener != null && secondsNum > 0) {
            if (countdownTimeBeans == null) {
                countdownTimeBeans = CopyOnWriteArrayList(ArrayList())
            }
            if (!isHasListener(listener)) {
                val timeBean = CountdownTimeBean()
                timeBean.countNum = secondsNum
                timeBean.count = 0
                timeBean.listener = listener
                countdownTimeBeans!!.add(timeBean)
            }
            CommonLogger.d(
                "CountdownTimeManagerTestActivity",
                "listener数量——添加==" + countdownTimeBeans!!.size
            )

            if (actionDisposable == null || actionDisposable!!.isDisposed) {
                startTime();
            }
        }
    }

    /**
     * 定时器开始运行
     */
    @Synchronized
    private fun startTime() {
        if (actionDisposable != null && !actionDisposable!!.isDisposed) {
            actionDisposable!!.dispose()
            actionDisposable = null
        }
        CommonLogger.d("CountdownTimeManagerTestActivity", "创建倒计时")

        actionDisposable = Observable
            .interval(0, 1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // 每隔1000毫秒检查所有回调接口

                if (countdownTimeBeans != null && countdownTimeBeans!!.isNotEmpty()) {
                    try {
                        for (timeBean in countdownTimeBeans!!) {
                            if (timeBean?.listener != null) {
                                // 本省计数器 +1
                                timeBean.count++
                                // 如果本身计数器 达到2
                                if (timeBean.count == timeBean.countNum) {
                                    // 则回调监听器
                                    timeBean.listener.accept(it)
                                    timeBean.count = 0
                                }
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }

    /**
     * 取消注册接口
     *
     * @param listener 要取消的注册接口
     */
    @Synchronized
    fun unRegister(listener: CountdownTimeManagerListener?) {
        val timeBean = getListenerBean(listener)
        if (timeBean != null) {
            countdownTimeBeans?.remove(timeBean)
            CommonLogger.d(
                "CountdownManagerTestActivity", "listener数量——移除===" + countdownTimeBeans?.size
            )
        }
        if (countdownTimeBeans != null && countdownTimeBeans!!.size == 0) {
            release()
        }
    }

    /**
     * 清空所有倒计时
     */
    @Synchronized
    fun release() {
        if (actionDisposable != null && !actionDisposable!!.isDisposed) {
            actionDisposable!!.dispose()
        }
        actionDisposable = null
        if (countdownTimeBeans != null) {
            countdownTimeBeans!!.clear()
        }
        countdownTimeBeans = null
    }
}