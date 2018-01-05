package com.tywho.common.widget;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.tywho.common.utils.SysUtils;

/**
 * Created by limit on 2017/5/4/0004.
 */

public class CustomerSwipeRefreshLayout extends SwipeRefreshLayout {
    private View contentView;

    public CustomerSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public CustomerSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        setProgressViewOffset(true, -100, 200);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);

    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    @BindingAdapter("addContentView")
    public static void setContentView(CustomerSwipeRefreshLayout view, int resId) {
        view.setContentView(SysUtils.getActivityFromView(view).findViewById(resId));
    }

    @Override
    public boolean canChildScrollUp() {
        if (contentView == null) {
            return super.canChildScrollUp();
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (contentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) contentView;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(contentView, -1) || contentView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(contentView, -1);
        }
    }
}
