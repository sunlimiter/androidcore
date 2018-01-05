package com.tywho.common.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.util.SparseArray;
import android.view.View;

import com.tywho.common.BR;
import com.tywho.common.R;
import com.tywho.common.base.api.BaseApi;
import com.tywho.common.base.bean.HeaderFooterMapping;
import com.tywho.common.exception.ApiException;
import com.tywho.common.http.HttpManager;
import com.tywho.common.http.listener.CallBackListener;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.config.CommonConfig;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_CONTENT;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_EMPTY;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_ERROR;
import static com.tywho.common.widget.ProgressFrameLayout.VIEW_STATE_LOADING;

/**
 * Created by limit on 2017/4/1.
 */

public abstract class BaseListViewModel<T> extends BaseViewModel {
    private int firstPage = CommonConfig.FIRST_PAGE;
    //分页页码
    private int page = firstPage;
    //分页每页Item数量
    private int pageSize = CommonConfig.PAGE_SIZE;
    private final ObservableBoolean hasMore = new ObservableBoolean(false);
    private final ObservableBoolean loadingMore = new ObservableBoolean(false);
    protected final ObservableList<Object> items = new ObservableArrayList<>();
    //特殊的item集合，传入对应的 position位置和 layoutId,把原本默认的item layout 转为 指定的 layout，item数据不是插进 items里面额外的数据，而是原本存在的数据。
    private final SparseArray<Integer> specialViews = new SparseArray<>();
    //header集合，layout 和 Object，Object作为额外的 item 插进 items
    private final ArrayList<HeaderFooterMapping> headers = new ArrayList<>();
    //footer集合，layout 和 Object，Object作为额外的 item 插进 items
    private final ArrayList<HeaderFooterMapping> footers = new ArrayList<>();
    //正常的item对应的layout
    private int itemLayout;
    private boolean once = false;//控制loading状态只有一次,对于列表的loading概念，就是首次加载数据，其余加载分别是刷新和更多
    private boolean firstStart = false;
    private BindingRecyclerViewAdapter adapter;

    private BaseApi api;//访问接口api

    public BaseListViewModel(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    private OnItemBind<Object> itemViews = new OnItemBind<Object>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, Object item) {
            onItemViewSelector(itemBinding, position, item);
        }
    };

    public void onItemViewSelector(ItemBinding itemView, int position, Object item) {
        int layout = itemLayout;
        if (position < headers.size()) {//如果符合header条件则使用header中object对应的layout
            layout = headers.get(position).getLayout();
        } else if (position >= items.size() - footers.size()) {//如果符合footer条件则使用footer中object对应的layout
            int footerPosition = footers.size() - (items.size() - position);
            layout = footers.get(footerPosition).getLayout();
        }
        Integer layoutRes = specialViews.get(position);
        //如果specialViews中能匹配位置key则使用对应的value，否则使用正常的itemLayout
        //注意 无论是 itemLayout 还是 specialViews 中对应的 value Layout，都必须有 viewModel 属性
        itemView.set(BR.viewModel, (layoutRes == null) ? layout : layoutRes);
    }

    @Override
    public void loadLazy() {
        if (!firstStart) {
            firstStart = true;
        } else {
            return;
        }
        onLoad();
    }

    /**
     * 刷新数据
     */
    public void onListRefresh() {
        setRefreshing(true);
        //把分页配置还原成加载第一页状态
        page = firstPage;
        hasMore.set(false);
        loadingMore.set(false);
        if (!once)
            setViewState(VIEW_STATE_LOADING);
        loadPage();
    }

    /**
     * 加载数据
     */
    public void onListLoadMore() {
        //判断是否已经在进行加载更多 或 没有更多了，是则直接返回等待加载完成。
        if (loadingMore.get() || !hasMore.get()) return;
        //刷新中也直接返回不加载更多
        if (getRefreshing().get()) return;
        if (!getLoadMoreStatusError().get()) {
            //分页增加
            page++;
        }
        loadingMore.set(true);

        loadPage();
    }

    @Override
    public void loadMoreReload() {
        super.loadMoreReload();
        onListLoadMore();
    }

    protected void loadPage() {
        LogUtils.d("page===" + page);
        api = getApi();
        if (api == null) return;
        HttpManager httpManager = HttpManager.getInstance();
        api.setPage(page);
        httpManager.request(api, new CallBackListener<List<T>>() {
            @Override
            public void onNext(List<T> t) {
                setViewState(VIEW_STATE_CONTENT);
                setLoadMoreStatusError(false);
                once = true;
                if (isFirstPage()) {
                    items.clear();
                    //把header和footer的数据先加回来
                    reloadHeaderAndFooter();
                }
                if (t != null) {
                    items.addAll(items.size() - footers.size(), t);
                    //如果获取的数据数量比申请的数量少 则为没有更多了
                    hasMore.set(t.size() < pageSize ? false : true);
                }
                if (isFirstPage() && t.isEmpty()) {
                    setViewState(VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onError(ApiException apiException) {
                super.onError(apiException);
                setErrorMsg(apiException.getMessage());
                LogUtils.d("onError");
                if (isFirstPage()) {//因为在刷新之前已经把page设为了firstPage，所以可以判断isFirstPage()来判断当前是否刷新
                    setRefreshing(false);
                    setViewState(VIEW_STATE_ERROR);
                    setLoadMoreStatusError(false);
                } else {
                    loadingMore.set(false);
                    setLoadMoreStatusError(true);
                }
                onLoadListError();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                if (isFirstPage()) {//因为在刷新之前已经把page设为了firstPage，所以可以判断isFirstPage()来判断当前是否刷新
                    setRefreshing(false);
                } else {
                    loadingMore.set(false);
                }
                onLoadListComplete();
            }
        });
    }

    /**
     * 可选重写，通常用于加载完成后判定是否有更多而进行加入或减去footer
     */
    public void onLoadListComplete() {
        if (isFirstPage() && getHasMore().get() && getFooterSize() == 0) {
            addFooter(R.layout.ssrl_footer_loadmore_layout, this);
        } else if (!getHasMore().get() && getFooterSize() != 0) {
            removeFooters();
        }
    }

    public void onLoadListError() {
    }

    /**
     * 重新加载header和footer中的对象到items中
     */
    private void reloadHeaderAndFooter() {
        for (int i = 0; i < headers.size(); i++) {
            items.add(headers.get(i).getObject());
        }
        for (int i = 0; i < footers.size(); i++) {
            items.add(footers.get(i).getObject());
        }
    }

    /**
     * 网络请求
     *
     * @return
     */
    public abstract BaseApi getApi();

    public abstract void onItemClick(View v, int position, View itemView, T item);


    public View.OnClickListener onTryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onLoad();
        }
    };


    /**
     * 设置特殊的样式
     *
     * @param position item的位置
     * @param layout   改为对应的layout
     */
    public void setSpecialView(int position, int layout) {
        specialViews.append(position, layout);
    }

    /**
     * 清除所有特殊样式
     */
    public void clearSpecialViews() {
        specialViews.clear();
    }

    /**
     * 清除指定位置的样式
     *
     * @param position 位置
     */
    public void removeSpecialView(int position) {
        specialViews.remove(position);
    }

    public OnItemBind<Object> getItemViews() {
        return itemViews;
    }

    /**
     * 加入Header
     *
     * @param layout layout的Id
     * @param o      对应的Model 或 ViewModel
     */
    public void addHeader(int layout, Object o) {
        headers.add(new HeaderFooterMapping(layout, o));
        items.add(headers.size() - 1, o);
    }

    /**
     * 加入Footer
     *
     * @param layout layout的Id
     * @param o      对应的Model 或 ViewModel
     */
    public void addFooter(int layout, Object o) {
        footers.add(new HeaderFooterMapping(layout, o));
        items.add(o);
    }

    /**
     * 移除 layout 对应的 Header
     *
     * @param layout layout的Id
     * @param o      对应的Model 或 ViewModel
     */
    public void removeHeader(int layout, Object o) {
        headers.remove(layout);
        items.remove(o);
    }

    /**
     * 移除Footer
     *
     * @param layout layout的下标
     * @param o      对应的Model 或 ViewModel
     */
    public void removeFooter(int layout, Object o) {
        footers.remove(layout);
        items.remove(o);
    }

    /**
     * 移除所有 Header
     */
    public void removeHeaders() {
        for (HeaderFooterMapping headerFooterMapping : headers)
            items.remove(headerFooterMapping.getObject());
        headers.clear();
    }

    /**
     * 移除所有 Footer
     */
    public void removeFooters() {
        for (HeaderFooterMapping headerFooterMapping : footers)
            items.remove(headerFooterMapping.getObject());
        footers.clear();
    }

    public int getHeaderSize() {
        return headers.size();
    }

    public int getFooterSize() {
        return footers.size();
    }

    @Override
    public void onLoad() {
        onListRefresh();
    }

    public boolean isFirstPage() {
        return page == firstPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ObservableBoolean getHasMore() {
        return hasMore;
    }

    public ObservableBoolean getLoadingMore() {
        return loadingMore;
    }

    public ObservableList<Object> getItems() {
        return items;
    }

    public int getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public BindingRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BindingRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

}
