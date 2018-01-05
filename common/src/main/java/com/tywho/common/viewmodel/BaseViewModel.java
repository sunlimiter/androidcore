package com.tywho.common.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;

/**
 * Created by limit on 2017/3/29.
 */

public abstract class BaseViewModel {
    //刷新状态
    private final ObservableBoolean refreshing = new ObservableBoolean(false);
    //load more错误状态
    private final ObservableBoolean loadMoreStatusError = new ObservableBoolean(false);
    //view状态
    private final ObservableInt viewState = new ObservableInt(0);
    //错误提示
    private final ObservableField<String> errorMsg = new ObservableField();
    //通知View进行交互的监听器
    private OnViewModelNotifyListener onViewModelNotifyListener;

    /**
     * get set
     * @return
     */
    public ObservableField<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String msg) {
        errorMsg.set(msg);
    }

    public ObservableInt getViewState() {
        return viewState;
    }

    public void setViewState(int state) {
        viewState.set(state);
    }

    public void setRefreshing(Boolean isRefreshing) {
        refreshing.set(isRefreshing);
    }

    public ObservableBoolean getRefreshing() {
        return refreshing;
    }

    public void setOnViewModelNotifyListener(OnViewModelNotifyListener onViewModelNotifyListener) {
        this.onViewModelNotifyListener = onViewModelNotifyListener;
    }

    public void setLoadMoreStatusError(Boolean isError) {
        loadMoreStatusError.set(isError);
    }

    public ObservableBoolean getLoadMoreStatusError() {
        return loadMoreStatusError;
    }

    /**
     * 通知View进行交互
     *
     * @param bundle 装载数据
     * @param code   判别View要做什么操作
     */
    public void onViewModelNotify(Bundle bundle, int code) {
        if (onViewModelNotifyListener != null)
            onViewModelNotifyListener.onViewModelNotify(bundle, code);
    }

    /**
     * 加载数据
     */
    public void onLoad() {
    }

    /**
     * 懒加载
     */
    public void loadLazy() {
    }

    /**
     * 加载更多重试
     */
    public void loadMoreReload() {
    }

    public void destroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Override me!
    }
}
