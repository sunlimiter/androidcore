package com.tywho.common.viewmodel;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by limit on 2017/4/1.
 */

public abstract class BaseRefreshRecyclerViewModel extends BaseRecyclerViewModel {
    public BaseRefreshRecyclerViewModel(int itemLayout) {
        super(itemLayout);
    }

    public BaseRefreshRecyclerViewModel(int itemLayout, boolean loadMore) {
        super(itemLayout, loadMore);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onListRefresh();
        }
    };

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }
}
