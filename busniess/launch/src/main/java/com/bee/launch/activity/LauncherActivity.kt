package com.bee.launch.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.bee.android.common.base.CommonApplication
import com.bee.core.logger.CommonLogger
import com.bee.core.utils.ScreenUtil
import com.bee.launch.constant.LAUNCH_LAUNCHER
import com.bee.launch.constant.LAUNCH_MAIN
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.autosize.internal.CancelAdapt
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 *@Description:
 *
 * TODO:有误
 */
@Route(path = LAUNCH_LAUNCHER)
class LauncherActivity : Activity(), CancelAdapt {

    companion object {
        const val TAG = "LauncherActivity"
        const val SKIP_TAG = "Skip-$TAG"
        const val MMKV_ID = "user"
    }


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {

        // App正常的启动，设置App的启动状态为正常启动
        CommonApplication.APP_STATUS = CommonApplication.APP_STATUS_NORMAL

        super.onCreate(savedInstanceState)

        /*if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }*/
        val screenWidth = ScreenUtil.getScreenWidth(this)
        val screenHeight = ScreenUtil.getScreenHeight(this)
        val realWidth = ScreenUtil.getRealWidth(this)
        val realHeight = ScreenUtil.getRealHeight(this)

        CommonLogger.i(TAG, "屏幕宽度：$screenWidth, 屏幕高度：$screenHeight," +
                " 屏幕真实宽度：$realWidth, 屏幕真实高度：$realHeight")

        CommonLogger.i(TAG, "LauncherActivity onCreate isTaskRoot = $isTaskRoot")
        CommonLogger.i(SKIP_TAG, "onCreate isTaskRoot = $isTaskRoot")

        if (!isTaskRoot) {
            if (intent != null) {
                val action = intent!!.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                    return
                }
            }
        }

        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ARouter.getInstance().build(LAUNCH_MAIN)
                            .navigation(this, object : NavCallback() {
                                override fun onArrival(postcard: Postcard?) {
                                    finish()
                                }
                            })
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        /*if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }*/
    }
}