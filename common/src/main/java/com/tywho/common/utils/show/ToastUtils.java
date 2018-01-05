package com.tywho.common.utils.show;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tywho.common.R;
import com.tywho.common.widget.CustomToast;
import com.tywho.config.CommonConfig;

/**
 * Created by limit on 2017/3/29.
 */

public class ToastUtils {
    private static volatile ToastUtils sToastUtil = null;

    private CustomToast mToast = null;
    private Toast toast = null;

    /**
     * 获取实例
     *
     * @return
     */
    public static ToastUtils getInstance() {
        if (sToastUtil == null) {
            synchronized (ToastUtils.class) {
                if (sToastUtil == null) {
                    sToastUtil = new ToastUtils();
                }
            }
        }
        return sToastUtil;
    }

    protected Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 显示Toast，多次调用此函数时，Toast显示的时间不会累计，并且显示内容为最后一次调用时传入的内容
     * 持续时间默认为short
     *
     * @param tips 要显示的内容
     *             {@link Toast#LENGTH_LONG}
     */
    public void showToast(String tips) {
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast(int tips) {
        showToast(tips, Toast.LENGTH_SHORT);
    }

    public void showToast_(int tips) {
        showToast_(tips, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast，多次调用此函数时，Toast显示的时间不会累计，并且显示内容为最后一次调用时传入的内容
     *
     * @param tips     要显示的内容
     * @param duration 持续时间，参见{@link Toast#LENGTH_SHORT}和
     *                 {@link Toast#LENGTH_LONG}
     */
    public void showToast(String tips, int duration) {
        showToast(tips, R.mipmap.ic_toast_logo, duration, Gravity.CENTER);
    }

    public void showToast(int tips, int duration) {
        showToast(tips, R.mipmap.ic_toast_logo, duration, Gravity.CENTER);
    }

    public void showToast(String tips, Integer iv, int duration) {
        showToast(tips, iv, duration, Gravity.CENTER);
    }

    public void showToast(int tips, Integer iv, int duration) {
        showToast(tips, iv, duration, Gravity.CENTER);
    }


    public void showToast_(final int tips, final int duration) {
        if (tips <= 0) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(CommonConfig.APPLICATION, tips, duration);
                }
                toast.setText(tips);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    public void showToast(final String tips, final Integer iv, final int duration, final int gravity) {
        if (TextUtils.isEmpty(tips)) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = CustomToast.makeText(CommonConfig.APPLICATION);
                }
                mToast.setText(tips);
                mToast.setIv(iv);
                mToast.setGravity(gravity);
                mToast.setDuration(duration);
                mToast.show();
            }
        });
    }

    public void showToast(final int tips, final Integer iv, final int duration, final int gravity) {
        if (tips <= 0) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = CustomToast.makeText(CommonConfig.APPLICATION);
                }
                mToast.setText(tips);
                mToast.setIv(iv);
                mToast.setGravity(gravity);
                mToast.setDuration(duration);
                mToast.show();
            }
        });
    }

    public void showToast(final View view, final int tips, final Integer iv, final int duration, final int gravity) {
        if (tips <= 0) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = CustomToast.makeText(CommonConfig.APPLICATION);
                }
                mToast.setView(view);
                mToast.setText(tips);
                mToast.setIv(iv);
                mToast.setGravity(gravity);
                mToast.setDuration(duration);
                mToast.show();
            }
        });
    }

    public Toast show(String tips) {
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(CommonConfig.APPLICATION).inflate(R.layout.tywho_special_toast_view, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_message);
        textView.setText(tips);
        Toast toast = new Toast(CommonConfig.APPLICATION);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        return toast;
    }
}