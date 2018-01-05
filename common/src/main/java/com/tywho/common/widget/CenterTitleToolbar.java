package com.tywho.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tywho.common.R;

/**
 * Created by limit on 2017/4/12/0012.
 */

public class CenterTitleToolbar extends Toolbar {
    private int mTitleTextAppearance;
    private TextView mTitleTextView, mRightTextView;
    private int mTitleTextColor;
    private CharSequence mTitleText;

    private OnClickListener click;

    public CenterTitleToolbar(Context context) {
        this(context, null);
    }

    public CenterTitleToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public CenterTitleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CenterTitleToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Toolbar,
                defStyleAttr, defStyleRes);

        mTitleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        a.recycle();
    }

    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            ensureTitleView();
            if (mTitleTextView.getParent() == null) {
                LayoutParams lp = new LayoutParams(Gravity.CENTER);
                mTitleTextView.setLayoutParams(lp);
                mTitleTextView.setGravity(Gravity.CENTER);
                addView(mTitleTextView, lp);
            }
        } else if (mTitleTextView != null && mTitleTextView.getParent() != null) {
            removeView(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    @Override
    public void setTitleTextColor(int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    public void setTitleTextAppearance(Context context, int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    private void ensureTitleView() {
        if (mTitleTextView == null) {
            final Context context = getContext();
            mTitleTextView = new TextView(context);
            mTitleTextView.setSingleLine();
            mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (mTitleTextAppearance != 0) {
                mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
            }
            if (mTitleTextColor != 0) {
                mTitleTextView.setTextColor(mTitleTextColor);
            }
        }
    }

    public void setRightText(Context context, String mTitleText) {
        if (!TextUtils.isEmpty(mTitleText)) {
            mRightTextView = new TextView(context);
            LayoutParams lp = new LayoutParams(Gravity.RIGHT);
            lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.mRight);
            mRightTextView.setLayoutParams(lp);
            mRightTextView.setGravity(Gravity.CENTER);
            addView(mRightTextView, lp);
        } else if (mRightTextView != null && mRightTextView.getParent() != null) {
            removeView(mRightTextView);
        }
        if (mRightTextView != null) {
            mRightTextView.setText(mTitleText);
            if (click != null) {
                mRightTextView.setOnClickListener(click);
            }
        }
    }

    public void setRightClick(OnClickListener clickListener) {
        this.click = clickListener;
        if (mRightTextView != null) {
            mRightTextView.setOnClickListener(clickListener);
        }
    }


    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    @BindingAdapter({"mTitleText"})
    public static void setTitleTextView(CenterTitleToolbar view, String mTitleText) {
        view.setTitle(mTitleText);
    }

    @BindingAdapter("mTitleTextColor")
    public static void setTitleTextColor(CenterTitleToolbar view, int color) {
        view.setTitleTextColor(color);
    }

    @BindingAdapter({"mRightText"})
    public static void setRightTextView(CenterTitleToolbar view, String mRightText) {
        view.setRightText(view.getContext(), mRightText);
    }

    @BindingAdapter({"mRightTextColor"})
    public static void setRightTextColor(CenterTitleToolbar view, int color) {
        view.setTitleTextColor(color);
    }

    @BindingAdapter({"mRightTextClick"})
    public static void setRightClick(CenterTitleToolbar view, OnClickListener listener) {
        view.setRightClick(listener);
    }

    @BindingAdapter({"addNavigationIcon"})
    public static void setNavigationIcon(CenterTitleToolbar view, int icon) {
        if (icon == 0) {
            view.setNavigationIcon(null);
        } else {
            view.setNavigationIcon(icon);
        }

    }

    @BindingAdapter({"addNavigationClick"})
    public static void setNavigationClick(CenterTitleToolbar view, OnClickListener listener) {
        view.setNavigationOnClickListener(listener);
    }

    @BindingAdapter({"inflateMenu", "onMenuItemClick"})
    public static void setOnMenuItemClickListener(CenterTitleToolbar view, int menuRes, OnMenuItemClickListener menuItemClickListener) {
        view.inflateMenu(menuRes);
        view.setOnMenuItemClickListener(menuItemClickListener);
    }
}
