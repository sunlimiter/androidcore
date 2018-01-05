package com.tywho.common.utils.refresh;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by limit on 2017/4/1.
 */

public class DividerHorizontalDecoration extends RecyclerView.ItemDecoration {
    private int margin = 2;

    public DividerHorizontalDecoration(int margin) {
        this.margin = margin;
    }

    public DividerHorizontalDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.set(0, 0, 0, margin);
        int pos = parent.getChildAdapterPosition(view);
        if (pos != 0) {
            outRect.bottom = margin;
        }
    }
}