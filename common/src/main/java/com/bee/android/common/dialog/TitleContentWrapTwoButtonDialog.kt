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
import com.bee.android.common.R
import kotlinx.android.synthetic.main.common_dialog_title_content_wrap_two_button_layout.*

class TitleContentWrapTwoButtonDialog : BaseDialog, View.OnClickListener {

    private lateinit var span_content: SpannableString

    private var title: String
    private lateinit var content: String
    private var left: String
    private var right: String
    private var listener: ActionListener


    constructor(context: Context, title: String, content: String,
                left: String, right: String, listener: ActionListener) : super(context) {
        this.title = title
        this.content = content
        this.left = left
        this.right = right
        this.listener = listener
    }

    constructor(context: Context, title: String, content: SpannableString,
                left: String, right: String, listener: ActionListener) : super(context) {
        this.title = title
        this.span_content = content
        this.left = left
        this.right = right
        this.listener = listener
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_content_wrap_two_button_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }

        contentTv.highlightColor = context.getColor(R.color.common_color_transparent)
        titleTv.text = title
        if (!TextUtils.isEmpty(content)) {
            contentTv.text = content
        } else if (!TextUtils.isEmpty(span_content)) {
            contentTv.movementMethod = LinkMovementMethod.getInstance()
            contentTv.text = span_content
        } else {
            contentTv.text = ""
        }
        leftTv.text = left
        rightTv.text = right
        leftTv.setOnClickListener(this)
        rightTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rightTv -> {
                dismiss()
                listener.clickRight()
            }
            R.id.leftTv -> {
                dismiss()
                listener.clickLeft()
            }
        }
    }
}