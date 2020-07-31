package com.bee.android.common.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.bee.android.common.R

/**
 *@Description: 标题，内容，两个按钮
 *
 */
class TitleMsgTwoButtonDialog : BaseDialog, View.OnClickListener {


    private lateinit var titleTv: TextView
    private lateinit var msgTv: TextView
    private lateinit var leftTv: TextView
    private lateinit var rightTv: TextView

    private var listener: ActionListener? = null

    var title = ""
    var msg = ""
    var left = ""
    var right = ""

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        initView()
    }

    constructor(context: Context, title: String, msg: String, left: String, right: String, listener: ActionListener) : super(context) {
        this.title = title
        this.msg = msg
        this.left = left
        this.right = right
        this.listener = listener
        initView()
    }

    private fun initView() {
        setContentView(R.layout.common_dialog_address)
        titleTv = findViewById(R.id.titleTv)
        msgTv = findViewById(R.id.msgTv)
        leftTv = findViewById(R.id.leftTv)
        rightTv = findViewById(R.id.rightTv)

        leftTv.setOnClickListener(this)
        rightTv.setOnClickListener(this)

        if (!TextUtils.isEmpty(title)) {
            titleTv.text = title
        }
        if (!TextUtils.isEmpty(msg)) {
            msgTv.text = msg
        }
        if (!TextUtils.isEmpty(left)) {
            leftTv.text = left
        }
        if (!TextUtils.isEmpty(right)) {
            rightTv.text = right
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.leftTv -> {
                    dismiss()
                    if (listener != null) {
                        listener!!.clickLeft()
                    }

                }

                R.id.rightTv -> {
                    dismiss()
                    if (listener != null) {
                        listener!!.clickRight()
                    }
                }
            }
        }
    }
}