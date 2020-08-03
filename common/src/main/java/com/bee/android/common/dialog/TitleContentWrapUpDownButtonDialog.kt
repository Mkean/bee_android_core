package com.bee.android.common.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.bee.android.common.R
import com.bee.core.utils.StringUtils

/**
 *@Description: 标题+内容（包裹内容）+上下两个按钮--SpannableString类型
 *
 */
class TitleContentWrapUpDownButtonDialog : BaseDialog, View.OnClickListener {

    private var title: String
    private var dec: String = ""
    private var spanContent: SpannableString? = null
    private var up: String
    private var down: String
    private var listener: ActionListener

    constructor(context: Context, title: String, content: String, up: String, down: String, listener: ActionListener) : super(context) {
        this.title = title
        this.dec = content
        this.up = up
        this.down = down
        this.listener = listener
    }

    constructor(context: Context, title: String, content: SpannableString, up: String, down: String, listener: ActionListener) : super(context) {
        this.title = title
        this.spanContent = content
        this.up = up
        this.down = down
        this.listener = listener
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_content_wrap_up_down_layout)
        setCancelable(false) // 点击外部不可dismiss
        setCanceledOnTouchOutside(false) // 控制返回键是否dismiss
        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }

        val titleTv: TextView = findViewById(R.id.dialog_titleTv)
        val contentTv: TextView = findViewById(R.id.dialog_contentTv)
        contentTv.highlightColor = context.getColor(R.color.common_color_transparent)
        val upTv: TextView = findViewById(R.id.dialog_upTv)
        val downTv: TextView = findViewById(R.id.dialog_downUp)

        if (!TextUtils.isEmpty(title)) {
            titleTv.text = title
        }
        if (!StringUtils.isNullOrEmpty(dec)) {
            contentTv.text = dec
        } else if (spanContent != null) {
            contentTv.movementMethod = LinkMovementMethod.getInstance()
            contentTv.text = spanContent
        } else {
            contentTv.text = ""
        }

        if (!StringUtils.isNullOrEmpty(up)) {
            upTv.text = up
        }
        if (!StringUtils.isNullOrEmpty(down)) {
            downTv.text = down
        }

        upTv.setOnClickListener(this)
        downTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.dialog_upTv -> {
                    dismiss()
                    listener.clickUp()
                }
                R.id.dialog_downUp -> {
                    dismiss()
                    listener.clickDown()
                }
            }
        }

    }

    interface ActionListener {
        fun clickUp()

        fun clickDown()
    }
}