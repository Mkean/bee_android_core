package com.bee.android.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.bee.android.common.R
import kotlinx.android.synthetic.main.common_dialog_title_two_button_layout.*

class TitleTwoButtonDialog(context: Context, private val title: String, private val left: String,
                           private val right: String, private val listener: ActionListener) : BaseDialog(context), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_two_button_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }
        titleTv.text = title
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