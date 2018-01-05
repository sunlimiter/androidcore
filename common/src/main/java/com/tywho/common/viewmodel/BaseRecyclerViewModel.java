package com.tywho.common.viewmodel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tywho.common.utils.refresh.RecyclerViewItemClickSupport;

import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by limit on 2017/4/1.
 */

public abstract class BaseRecyclerViewModel extends BaseListViewModel {
    private LayoutManagers.LayoutManagerFactory layoutManager = LayoutManagers.linear();
    private RecyclerView.ItemDecoration itemDecoration;

    public BaseRecyclerViewModel(int itemLayout) {
        super(itemLayout);
    }

    public BaseRecyclerViewModel(int itemLayout, boolean loadMore) {
        super(itemLayout);
        if (!loadMore) {
            onScrollListener = null;
        }
    }

    public RecyclerViewItemClickSupport.OnItemClickListener onItemClickListener = new RecyclerViewItemClickSupport.OnItemClickListener() {
        @SuppressWarnings("unchecked")
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            try {
                //refresh之后会clean items的数据，这个瞬间有可能造成点击 超出数组范围异常
                onItemClick(recyclerView, position, v, items.get(position));
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载
            // dy>0 表示向下滑动
            if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                onListLoadMore();
            }
        }
    };

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return itemDecoration;
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.itemDecoration = itemDecoration;
    }

    public LayoutManagers.LayoutManagerFactory getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(LayoutManagers.LayoutManagerFactory layoutManager) {
        this.layoutManager = layoutManager;
    }
}
