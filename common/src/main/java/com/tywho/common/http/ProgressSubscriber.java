package com.tywho.common.http;

import android.content.Context;
import android.content.DialogInterface;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tywho.common.base.api.BaseApi;
import com.tywho.common.base.bean.BaseBean;
import com.tywho.common.exception.ApiException;
import com.tywho.common.http.listener.CallBackListener;
import com.tywho.common.utils.JsonUtils;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.common.widget.ProgressHUD;
import com.tywho.config.CommonConfig;
import com.tywho.dblibrary.bean.CookieResulte;
import com.tywho.dblibrary.dao.CookieResulteDao;
import com.tywho.dblibrary.helper.CookieResulteHelper;

import org.greenrobot.greendao.query.Query;

import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by limit on 2017/3/31.
 */

public class ProgressSubscriber<T> extends Subscriber<T> {
    /*是否弹框*/
    private boolean showPorgress = true;
    //回调接口
    private CallBackListener callBackListener;
    //软引用防止内存泄露
    private SoftReference<RxAppCompatActivity> mActivity;
    //加载框可自己定义
    private ProgressHUD progressDialog;

    private BaseApi baseApi;

    public ProgressSubscriber(BaseApi api, CallBackListener callBackListener) {
        this.baseApi = api;
        this.callBackListener = callBackListener;

        if (baseApi.getRxAppCompatActivity() != null) {
            this.mActivity = api.getRxAppCompatActivity();
        } else {
            baseApi.setShowProgress(false);
        }
        setShowPorgress(baseApi.isShowProgress());
        if (baseApi.isShowProgress()) {
            initProgressDialog(baseApi.isCancel());
        }
    }

    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        Context context = mActivity.get();
        if (progressDialog == null && context != null) {
            progressDialog = new ProgressHUD(context);
            progressDialog.setCancelable(cancel);
            progressDialog.setMessage(baseApi.getLoadMessage());
            if (cancel) {
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onCancelProgress();
                    }
                });
            }
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowPorgress()) return;
        Context context = mActivity.get();
        if (progressDialog == null || context == null) return;
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowPorgress()) return;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
        if (callBackListener == null) return;
        callBackListener.onComplete();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (baseApi.isCache()) {
            getCache();
        } else {
            errorDo(e);
        }
        dismissProgressDialog();
    }

    /**
     * 获取cache数据
     */
    private void getCache() {
        Observable.just(baseApi.getUrl()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                if (callBackListener == null) return;
                callBackListener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                errorDo(e);
            }

            @Override
            public void onNext(String s) {
                           /*获取缓存数据*/
                Query<CookieResulte> query = CookieResulteHelper.getInstance().queryBuilder()
                        .where(CookieResulteDao.Properties.Url.eq(baseApi.getUrl()))
                        .build();
                CookieResulte cookieResulte = query.unique();
                if (cookieResulte != null) {
                    long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                    if (time < baseApi.getCookieNoNetWorkTime()) {
                        if (callBackListener != null && baseApi.getClassType() != null) {
                            callBackListener.onNext(JsonUtils.fromJson(cookieResulte.getResulte(), baseApi.getClassType()));
                        }
                    } else {
                        CookieResulteHelper.getInstance().delete(cookieResulte);
                        onError(new ApiException(new BaseBean(ApiException.CHACHE_TIMEOUT_ERROR)));
                    }
                } else {
                    onError(new ApiException(new BaseBean(ApiException.NO_CHACHE_ERROR)));
                }
            }
        });
    }

    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        LogUtils.e("errorDo", e.getMessage(), e);
        if (callBackListener == null) return;

        int code = ApiException.UNKNOWN_ERROR;
        if (e instanceof ApiException) {
            callBackListener.onError((ApiException) e);
        } else {
            String message;
            if (e instanceof TimeoutException) {
                code = ApiException.NETWORD_ERROR;
            } else if (e instanceof SocketTimeoutException) {
                code = ApiException.NETWORD_ERROR;
            } else if (e instanceof ConnectException) {
                code = ApiException.NETWORD_ERROR;
            } else if (e instanceof FileNotFoundException) {
                code = ApiException.NETWORD_ERROR;
            } else if (e instanceof NumberFormatException) {
                code = ApiException.JSON_ERROR;
            } else if (e instanceof UnknownHostException) {
                code = ApiException.HTTP_ERROR;
            }else if (e instanceof HttpException) {
                code = ApiException.HTTP_ERROR;
            }
            message = ApiException.getApiExceptionMessage(code);
            if (CommonConfig.isDebug)
                message += " " + e.getClass().getSimpleName();
            callBackListener.onError(new ApiException(new BaseBean(code, message)));
        }
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onNext(T t) {
         /*缓存处理*/
        if (baseApi.isCache()) {
            Query<CookieResulte> query = CookieResulteHelper.getInstance().queryBuilder()
                    .where(CookieResulteDao.Properties.Url.eq(baseApi.getUrl()))
                    .build();
            CookieResulte resulte = query.unique();
            long time = System.currentTimeMillis();

            /*保存和更新本地数据*/
            if (resulte == null) {
                resulte = new CookieResulte(baseApi.getUrl(), JsonUtils.toJson(t), time);
                CookieResulteHelper.getInstance().save(resulte);
            } else {
                resulte.setResulte(JsonUtils.toJson(t));
                resulte.setTime(time);
                CookieResulteHelper.getInstance().update(resulte);
            }
        }
        if (callBackListener != null) callBackListener.onNext(t);
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    public boolean isShowPorgress() {
        return showPorgress;
    }

    /**
     * 是否需要弹框设置
     *
     * @param showPorgress
     */
    public void setShowPorgress(boolean showPorgress) {
        this.showPorgress = showPorgress;
    }
}