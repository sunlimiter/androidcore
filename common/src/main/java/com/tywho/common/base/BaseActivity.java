package com.tywho.common.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tywho.common.BR;
import com.tywho.common.R;
import com.tywho.common.permissions.PermissionHelp;
import com.tywho.common.utils.FragmentStack;
import com.tywho.common.utils.InputToolUtils;
import com.tywho.common.utils.RxBus;
import com.tywho.common.utils.RxHelper;
import com.tywho.common.utils.show.ToastUtils;
import com.tywho.common.viewmodel.BaseViewModel;
import com.tywho.common.viewmodel.OnViewModelNotifyListener;
import com.umeng.analytics.MobclickAgent;

import rx.functions.Action1;

import static com.tywho.config.CommonConfig.EXITAPP;

/**
 * Created by limit on 2017/3/29.
 */

public abstract class BaseActivity<VB extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements OnViewModelNotifyListener {
    protected VB mBind;
    protected VM mModel;

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    protected ImmersionBar mImmersionBar;

    protected FragmentStack fragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regListener();
        mBind = DataBindingUtil.setContentView(this, getLayoutId());

        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
        mModel = getModel();
        if (mModel != null) {
            mModel.setOnViewModelNotifyListener(this);
            mBind.setVariable(BR.viewModel, mModel);
        }
        if (getContentId() != 0)
            fragmentStack = new FragmentStack(this, getSupportFragmentManager(), getContentId());

        afterCreate(savedInstanceState);
//        mBind.executePendingBindings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        unregListener();
        if (mModel != null) mModel.destroy();
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    protected abstract VM getModel();

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //响应每个菜单项(通过菜单项的ID)
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initToolbarMenu(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_nav_icon_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionHelp.getInstance().handleSpecialPermissionCallback(this, requestCode, resultCode, data);
        if (mModel != null) mModel.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 注册退出事件监听
     **/
    public void regListener() {
        RxBus.getDefault()
                .toObservable(EXITAPP, Boolean.class)
                .compose(new RxHelper<Boolean>().io2main(this))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            finish();
                        }
                    }
                });
    }

    public void unregListener() {
    }


    protected int getContentId() {
        return 0;
    }

    public void replace(Fragment fragment) {
        if (fragmentStack != null && fragment != null) {
            fragmentStack.replace(fragment);
        }
    }

    public void push(Fragment fragment) {
        if (fragmentStack != null && fragment != null) {
            fragmentStack.push(fragment);
        }
    }

    public void add(Fragment fragment) {
        if (fragmentStack != null && fragment != null) {
            fragmentStack.add(fragment);
        }
    }

    /**
     * 处理回退事件
     *
     * @return
     */
    protected boolean onMainBack() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onMainBack() && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - TOUCH_TIME) > WAIT_TIME) {
                ToastUtils.getInstance().showToast_(R.string.press_again_exit);
                TOUCH_TIME = System.currentTimeMillis();
                return true;
            }
            RxBus.getDefault().post(EXITAPP, true);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理ViewModel和activity之间交互
     * @param bundle
     * @param code
     */
    @Override
    public void onViewModelNotify(Bundle bundle, int code) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.getInstance().handlePermissionCallback(requestCode, permissions, grantResults);
    }


    /**
     * 隐藏输入法
     *
     * @param ev
     * @return
     */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (InputToolUtils.isHideInput(view, ev)) {
                InputToolUtils.HideSoftInput(view);

            }

        }
        return super.dispatchTouchEvent(ev);
    }

}
