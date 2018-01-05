package com.tywho.common.viewmodel;

import android.databinding.ObservableField;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by limit on 2017/4/1.
 */

public abstract class BaseRefreshDetailViewModel<T> extends BaseDetailViewModel {
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onLoad();
        }
    };

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ObservableField<T> getDetail() {
        return super.getDetail();
    }
}
