package com.tywho.common.utils.refresh;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tywho.common.R;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.common.viewmodel.BaseViewModel;

/**
 * Created by limit on 2017/4/1.
 */

public class BindingRefreshConfig {
    /**
     * attribute:支持双向绑定的属性（string格式）。
     * event:可以省略，用来通知DataBinding系统attribute已经改变，默认为attribute + "AttrChanged"。（UI通知数据）
     *
     * @param view
     * @return
     */
    @InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
    public static boolean isRefreshing(SwipeRefreshLayout view) {
        return view.isRefreshing();
    }


    @BindingAdapter("refreshing")
    public static void setRefreshing(SwipeRefreshLayout view, boolean refreshing) {
        if (refreshing != view.isRefreshing()) {
            view.setRefreshing(refreshing);
        }
    }

    /**
     * requireAll意思是是否需要设置你在value中声明的全部属性，默认为true。如果设定为false，那么没赋值的自定义属性会传默认值。
     *
     * @param view
     * @param listener
     * @param refreshingAttrChanged
     */
    @BindingAdapter(value = {"onRefreshListener", "refreshingAttrChanged"}, requireAll = false)
    public static void setOnRefreshListener(final SwipeRefreshLayout view,
                                            final SwipeRefreshLayout.OnRefreshListener listener,
                                            final InverseBindingListener refreshingAttrChanged) {

        SwipeRefreshLayout.OnRefreshListener newValue = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listener != null) {
                    if (refreshingAttrChanged != null) {
                        refreshingAttrChanged.onChange();
                    }
                    listener.onRefresh();
                }
            }
        };

        SwipeRefreshLayout.OnRefreshListener oldValue = ListenerUtil.trackListener(view, newValue, R.id.onRefreshListener);
        if (oldValue != null) {
            view.setOnRefreshListener(null);
        }
        view.setOnRefreshListener(newValue);
    }

    @BindingAdapter({"addOnItemClick"})
    public static void addOnItemClick(RecyclerView view, RecyclerViewItemClickSupport.OnItemClickListener listener) {
        RecyclerViewItemClickSupport.addTo(view).setOnItemClickListener(listener);
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"addOnScrollListener"})
    public static void addOnScrollListener(RecyclerView view, RecyclerView.OnScrollListener listener) {
        if (listener != null)
            view.addOnScrollListener(listener);
    }

    @BindingAdapter({"addItemDecoration"})
    public static void addItemDecoration(RecyclerView view, RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null)
            view.addItemDecoration(itemDecoration);
    }

    @BindingAdapter({"setLoadMoreReload"})
    public static void setReload(View view, final BaseViewModel viewModel) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loadMoreReload();
            }
        });
    }

    @BindingAdapter("simpleItemAnimator")
    public static void setSupportsChangeAnimations(RecyclerView view, boolean visible) {
        LogUtils.d("setSupportsChangeAnimations", "visible=" + visible);
//        ((SimpleItemAnimator) view.getItemAnimator()).setSupportsChangeAnimations(visible);
        if (!visible) {
            view.setItemAnimator(null);
        }
    }
}
