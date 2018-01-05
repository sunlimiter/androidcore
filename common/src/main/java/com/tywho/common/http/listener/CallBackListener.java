package com.tywho.common.http.listener;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.tywho.common.exception.ApiException;
import com.tywho.common.http.interceptor.ErrorInterceptor;
import com.tywho.common.utils.Foreground;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.common.utils.show.ToastUtils;
import com.tywho.config.CommonConfig;

/**
 * Created by limit on 2017/3/31.
 */

public abstract class CallBackListener<T> {
    /**
     * 成功后回调方法
     *
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param apiException
     */
    public void onError(ApiException apiException) {

        ToastUtils.getInstance().showToast(apiException.getMessage());
        ApplicationInfo appInfo = null;
        try {
            appInfo = CommonConfig.APPLICATION.getPackageManager().getApplicationInfo(
                    CommonConfig.APPLICATION.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null && appInfo.metaData != null) {
            String interceptor = appInfo.metaData.getString("http_error_interceptor");
            if (!TextUtils.isEmpty(interceptor)) {
                LogUtils.i("ApiException","Foreground.get().getActivity()"+Foreground.get().getActivity().getClass().getSimpleName());
                try {
                    ErrorInterceptor errorInterceptor = (ErrorInterceptor) Class.forName(interceptor).newInstance();
                    if (errorInterceptor.onError(Foreground.get().getActivity(), apiException))
                        return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 结束
     */
    public void onComplete() {
    }
}
