package com.tywho.common.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.tywho.common.base.api.BaseApi;
import com.tywho.common.exception.ApiException;
import com.tywho.common.http.HttpManager;
import com.tywho.common.http.listener.CallBackListener;
import com.tywho.common.utils.JsonUtils;
import com.tywho.common.utils.StringUtils;
import com.tywho.common.utils.show.LogUtils;

import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_CONTENT;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_CONTENT_LOADING;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_EMPTY;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_ERROR;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_LOADING;

/**
 * Created by limit on 2017/5/27/0027.
 */

public abstract class BaseDetailViewModel<T> extends BaseViewModel {
    private ObservableField<T> detail = new ObservableField<>();
    private boolean once = false;//控制loading状态只有一次,对于列表的loading概念，就是首次加载数据，其余加载是刷新

    private boolean firstStart = false;

    /**
     * 刷新数据
     */
    public void onLoadDatail() {
        setRefreshing(true);
        if (!once)
            setViewState(VIEW_STATE_LOADING);
        else
            setViewState(VIEW_STATE_CONTENT_LOADING);
        loadInfo();
    }

    /**
     * 网络请求
     *
     * @return
     */
    public abstract BaseApi getApi();

    private void loadInfo() {
        if (getApi() == null) {
            setViewState(VIEW_STATE_CONTENT);
            setRefreshing(false);
            return;
        }
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.request(getApi(), new CallBackListener<T>() {
            @Override
            public void onNext(T resultData) {
                once = true;
                if (StringUtils.isEmpty(resultData)){
                    setViewState(VIEW_STATE_EMPTY);
                }else{
                    detail.set(resultData);
                    onLoadDatailNext();
                    setViewState(VIEW_STATE_CONTENT);
                }
            }

            @Override
            public void onError(ApiException apiException) {
                super.onError(apiException);
                setErrorMsg(apiException.getMessage());
                if (!once) {
                    setViewState(VIEW_STATE_ERROR);
                }
                setRefreshing(false);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                setRefreshing(false);

                onLoadDatailComplete();
            }
        });
    }

    public void onLoadDatailNext() {
    }

    public void onLoadDatailComplete() {
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    @Override
    public void onLoad() {
        onLoadDatail();
    }

    @Override
    public void loadLazy() {
        if (!firstStart) {
            firstStart = true;
        } else {
            return;
        }
        onLoadDatail();
    }

    public ObservableField<T> getDetail() {
        return detail;
    }


    public View.OnClickListener onTryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLoad();
        }
    };
}
