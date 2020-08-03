package com.bee.android.common.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bee.android.common.R
import com.bee.core.utils.StringUtils
import kotlinx.android.synthetic.main.common_dialog_title_content_wrap_two_button_select_layout.*
import kotlinx.android.synthetic.main.common_dialog_title_single_button_layout.*

/**
 *@Description:
 *
 */
class TitleContentWrapTwoButtonSelectDialog : BaseDialog, View.OnClickListener {

    private var title: String
    private var dec = ""
    private var spanCount: SpannableString? = null
    private var left: String
    private var right: String
    private var listener: ActionListener
    private var selectTv: ImageView? = null
    private var isSelect: Boolean = false


    constructor(context: Context, title: String, content: String, left: String, right: String, listener: ActionListener) : super(context) {
        this.title = title
        this.dec = content
        this.left = left
        this.right = right
        this.listener = listener
    }

    constructor(context: Context, title: String, content: SpannableString, left: String, right: String, listener: ActionListener) : super(context) {
        this.title = title
        this.spanCount = content
        this.left = left
        this.right = right
        this.listener = listener
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_title_content_wrap_two_button_select_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        if (window != null) {
            window!!.setGravity(Gravity.CENTER)
            val param = window!!.attributes
            param.width = WindowManager.LayoutParams.WRAP_CONTENT
            param.height = WindowManager.LayoutParams.WRAP_CONTENT
            window!!.attributes = param
        }

        val selectLL: LinearLayout = findViewById(R.id.selectLL)
        selectTv = findViewById(R.id.selectIV)
        selectLL.setOnClickListener(this)
        val titleTv: TextView = findViewById(R.id.dialog_titleTv)
        val contentTv: TextView = findViewById(R.id.dialog_contentTv)
        contentTv.highlightColor = context.getColor(R.color.common_color_transparent)
        val leftTv: TextView = findViewById(R.id.dialog_leftTv)
        val rightTv: TextView = findViewById(R.id.dialog_rightTv)

        titleTv.text = title
        if (!StringUtils.isNullOrEmpty(dec)) {
            contentTv.text = dec
        } else if (spanCount != null) {
            contentTv.movementMethod = LinkMovementMethod.getInstance()
            contentTv.text = spanCount
        } else {
            contentTv.text = ""
        }

        leftTv.text = left
        rightTv.text = right
        leftTv.setOnClickListener(this)
        rightTv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.dialog_rightTv -> {

                    if (isSelect) {
                        dismiss()
                        listener.clickRight()
                    } else {
                        listener.noSelect()
                    }
                }
                R.id.dialog_leftTv -> {
                    dismiss()
                    listener.clickLeft()
                }
                R.id.selectLL -> {
                    if (selectTv != null) {
                        if (isSelect) {
                            isSelect = false
                            selectTv!!.setImageResource(R.drawable.common_unselect)
                        } else {
                            isSelect = true
                            selectTv!!.setImageResource(R.drawable.common_select)
                        }
                    }
                }
            }
        }
    }

    interface ActionListener {

        fun clickLeft()

        fun clickRight()
        fun noSelect()

    }
}