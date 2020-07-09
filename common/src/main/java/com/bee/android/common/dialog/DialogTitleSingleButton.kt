package com.bee.android.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.bee.android.common.R
import kotlinx.android.synthetic.main.common_dialog_title_single_button_layout.*

class DialogTitleSingleButton(context: Context, private val title: String, private val content: String,
                              private val btnStr: String, private val listener: DialogTitleSingleButtonListener) : BaseDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_single_button_layout)
        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = params
        }
        titleTv.text = title
        contentTv.text = content
        single_sure_tv.text = btnStr
        single_sure_tv.setOnClickListener {
            if (it.id == R.id.single_sure_tv) {
                listener.onSureOnClick(this)
            }
        }
    }


    interface DialogTitleSingleButtonListener {
        fun onSureOnClick(dialog: Dialog)
    }
}