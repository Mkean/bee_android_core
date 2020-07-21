package com.bee.android.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bee.android.common.R
import com.bee.android.common.view.shadow.ShadowClipRelativeLayout
import kotlinx.android.synthetic.main.common_dialog_update_layout.*

/**
 *@Description:应用更新弹窗
 */
class UpdateApkDialog(context: Context) : BaseDialog(context) {
    var dialogTitleTv: TextView? = null
    var tvVersionName: TextView? = null
    var tvApkSize: TextView? = null

    var dialogContentTv: TextView? = null

    var llBtnTips: LinearLayout? = null
    var llNext: LinearLayout? = null
    var llNow: LinearLayout? = null
    var tvUpdateDownloadError: TextView? = null
    var updateCornerRl: ShadowClipRelativeLayout? = null

    var progressBar: ProgressBar? = null
    var tvProgress: TextView? = null

    var tvStatusNet: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_update_layout)
        setCancelable(false) //点击外部不可dismiss

        setCanceledOnTouchOutside(false) //控制返回键是否dismiss

        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }
        this.dialogTitleTv = dialog_titleTv
        this.tvVersionName = tv_version_name
        this.tvApkSize = tv_apk_size
        this.dialogContentTv = dialog_contentTv
        this.llBtnTips = ll_btn_tips
        this.llNext = ll_next
        this.llNow = ll_now
        this.tvUpdateDownloadError = tv_update_download_error
        this.updateCornerRl = update_corner_rl
        this.progressBar = progress_bar
        this.tvProgress = tv_progress
        this.tvStatusNet = tv_status_net


    }
}