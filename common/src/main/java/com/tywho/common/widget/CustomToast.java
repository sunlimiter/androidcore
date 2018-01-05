package com.tywho.common.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tywho.common.R;
import com.tywho.config.CommonConfig;

/**
 * ********************** Copyright (c) ***********************\
 * *
 * *              Copyright Reserved 2013-2016
 * *
 * *                 By(个人项目)
 * *                    www.tywho.com
 * *
 * *                       _oo0oo_
 * *                      o8888888o
 * *                      88" . "88
 * *                      (| -_- |)
 * *                      0\  =  /0
 * *                    ___/`---'\___
 * *                  .' \\|     |// '.
 * *                 / \\|||  :  |||// \
 * *                / _||||| -:- |||||- \
 * *               |   | \\\  -  /// |   |
 * *               | \_|  ''\---/''  |_/ |
 * *               \  .-\__  '-'  ___/-. /
 * *             ___'. .'  /--.--\  `. .'___
 * *          ."" '<  `.___\_<|>_/___.' >' "".
 * *         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * *         \  \ `_.   \_ __\ /__ _/   .-` /  /
 * *     =====`-.____`.___ \_____/___.-`___.-'=====
 * *                       `=---='
 * *
 * *
 * *     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * *
 * *               佛祖保佑         永无BUG
 * *
 * *
 * *                   南无本师释迦牟尼佛
 * *
 * ******************** Copyright (c) ***********************\
 * Created with IntelliJ IDEA.
 * User: limiter0417@foxmail.com
 * Date: 2017/9/14/0014
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class CustomToast {
    private final Context mContext;
    private int mDuration;
    private View mNextView;
    private Toast mToast;
    private int gravity;

    public CustomToast(Context context) {
        mContext = context;
        mToast = new Toast(context);
    }

    public static CustomToast makeText(Context context) {
        CustomToast result = new CustomToast(context);


        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.tywho_toast_view, null);

        result.setView(view);

        return result;
    }


    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void show() {
        if (mToast != null) {
            mToast.setGravity(gravity, 0, 0);
            mToast.setView(mNextView);
            mToast.setDuration(mDuration);
            mToast.show();
        }
    }

    public void setView(View view) {
        if (view == null) return;
        mNextView = view;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setText(@StringRes int resId) {
        setText(mContext.getText(resId));
    }


    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(R.id.toast_message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    public void setIv(Integer iv) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(R.id.toast_message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        if (iv == null) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(CommonConfig.APPLICATION.getResources().getDrawable(iv), null, null, null);
        }
    }
}
