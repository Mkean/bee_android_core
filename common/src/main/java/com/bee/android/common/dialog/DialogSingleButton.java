package com.bee.android.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bee.android.common.R;

/**
 *
 */
public class DialogSingleButton extends BaseDialog implements View.OnClickListener {
    private TextView titleTv;
    private TextView sureTv;

    private DialogTitleSingleButtonListener listener;

    private String msg;
    private String btnTv;


    public DialogSingleButton(Context context) {
        super(context);
        initView();
    }

    public DialogSingleButton(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.common_dialog_content_single_button_layout);
        titleTv = findViewById(R.id.titleTv);
        sureTv = findViewById(R.id.single_sure_tv);
        sureTv.setOnClickListener(this);
    }

    private void setData() {
        titleTv.setText(TextUtils.isEmpty(msg) ? "" : msg);
        sureTv.setText(TextUtils.isEmpty(btnTv) ? "" : btnTv);
    }

    public void setData(String msg, String btnTv, DialogTitleSingleButtonListener listener) {
        this.msg = msg;
        this.btnTv = btnTv;
        this.listener = listener;
        setData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.single_sure_tv) {
            if (listener != null) {
                listener.onSureOnClick(this);
            }
        }
    }

    public interface DialogTitleSingleButtonListener {
        void onSureOnClick(Dialog dialog);
    }
}
