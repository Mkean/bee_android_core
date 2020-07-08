package com.bee.android.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.bee.android.common.R
import kotlinx.android.synthetic.main.common_dialog_title_content_two_button_layout.*

class TitleContentTwoButtonDialog(context: Context, private val title: String, private val content: String,
                                  private val left: String, private val right: String,
                                  private val listener: ActionListener) : BaseDialog(context), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_content_two_button_layout)
        setCancelable(false) //点击外部不可dismiss
        setCanceledOnTouchOutside(false) //控制返回键是否dismiss
        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }
        titleTv.text = title
        contentTv.text = content
        leftTv.text = left
        rightTv.text = right
        leftTv.setOnClickListener(this)
        rightTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftTv -> {
                dismiss()
                listener.clickLeft()
            }
            R.id.rightTv -> {
                dismiss()
                listener.clickRight()
            }
        }
    }
}