package com.tywho.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tywho.common.R;

/**
 * Created by limit on 2017/3/31.
 */

public class ProgressHUD extends Dialog {
    private AnimationDrawable mAnimation;

    public ProgressHUD(Context context) {
        super(context);
        defaultDialog(context,null);
    }

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    private void defaultDialog(Context context,String message){
        setContentView(R.layout.progress_hud);
        if (message == null || message.length() == 0) {
            findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        getWindow().setAttributes(lp);
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static ProgressHUD show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                                   OnCancelListener cancelListener) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}
