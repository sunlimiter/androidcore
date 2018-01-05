package com.tywho.common.base;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle.components.support.RxDialogFragment;
import com.tywho.common.BR;
import com.tywho.common.R;
import com.tywho.common.permissions.PermissionHelp;
import com.tywho.common.viewmodel.BaseViewModel;
import com.tywho.common.viewmodel.OnViewModelNotifyListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by limit on 2017/5/24/0024.
 */

public abstract class BaseDialogFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxDialogFragment implements OnViewModelNotifyListener {

    protected VB mBind;
    protected VM mModel;
    protected View mRootView;

    protected Window mWindow;

    protected int mWidth;  //屏幕宽度
    protected int mHeight;  //屏幕高度

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
        mBind.executePendingBindings();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterCreate(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);  //点击外部消失
        mWindow = dialog.getWindow();
        //测量宽高
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            mWidth = dm.widthPixels;
            mHeight = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;
        }
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
    }

    protected void initToolbarMenu(Toolbar toolbar, boolean nav) {
        if (!nav) {
            toolbar.setNavigationIcon(null);
        } else {
            toolbar.setNavigationIcon(R.mipmap.ic_nav_icon_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dismiss();
                }
            });
        }
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(View view, @Nullable Bundle savedInstanceState);

    protected abstract VM getModel();

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
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
