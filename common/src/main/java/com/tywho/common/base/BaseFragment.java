package com.tywho.common.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.tywho.common.BR;
import com.tywho.common.R;
import com.tywho.common.permissions.PermissionHelp;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.common.viewmodel.BaseViewModel;
import com.tywho.common.viewmodel.OnViewModelNotifyListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by limit on 2017/3/31.
 */

public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment implements OnViewModelNotifyListener {

    protected VB mBind;
    protected VM mModel;
    protected View mRootView;

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    //标志当前页面是否可见
    private boolean isVisible;

    protected ImmersionBar mImmersionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mBind.getRoot();
        mModel = getModel();
        if (mModel != null) {
            mModel.setOnViewModelNotifyListener(this);
            mBind.setVariable(BR.viewModel, mModel);
        }
//        mBind.executePendingBindings();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        if (isImmersionBarEnabled())
            initImmersionBar();
        afterCreate(view, savedInstanceState);
        getData();

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mModel != null) mModel.destroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(View view, @Nullable Bundle savedInstanceState);

    protected abstract VM getModel();

    protected void onVisible() {
        getData();
    }

    protected void getData() {
        if (!isVisible || !isPrepared) {
            return;
        }
        lazyLoad();
    }

    protected void lazyLoad() {
    }

    protected void onInvisible() {
    }

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

    protected void initToolbarMenu(Toolbar toolbar) {
        initToolbarMenu(toolbar, false);
    }

    protected void initToolbarMenu(Toolbar toolbar, boolean nav) {
        if (!nav) {
            toolbar.setNavigationIcon(null);
        } else {
            toolbar.setNavigationIcon(R.mipmap.ic_nav_icon_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(getActivity());
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelp.getInstance().handleSpecialPermissionCallback(getContext(), requestCode, resultCode, data);
        if (mModel != null) mModel.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 处理ViewModel和activity之间交互
     * @param bundle
     * @param code
     */
    @Override
    public void onViewModelNotify(Bundle bundle, int code) {

    }
}
