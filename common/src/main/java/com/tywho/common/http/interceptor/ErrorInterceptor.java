package com.tywho.common.http.interceptor;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: limiter0417@foxmail.com
 * Date: 2017/7/17/0017
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 * Description: 网络访问错误拦截器
 */
public interface ErrorInterceptor {
    public boolean onError(Context context, Exception exception);
}
