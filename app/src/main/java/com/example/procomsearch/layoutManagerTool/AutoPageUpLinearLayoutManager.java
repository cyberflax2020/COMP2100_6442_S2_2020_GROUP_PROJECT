package com.example.procomsearch.layoutManagerTool;
/**
 * @Authout Chaofan Li
 * @reference https://blog.csdn.net/ljq869115191/article/details/90666688
 */

import android.content.Context;
import android.os.Handler;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AutoPageUpLinearLayoutManager extends LinearLayoutManager {

    private RecyclerView recyclerView;
    private Handler mHandler;
    private long time = 2000; //time rolling
    private int itemSize; //total item size
    private int height; //height of single height
    private int slideHeight; // slide height
    private int countHeight;//total height
    private int slideCountHeight;//total slide height

    public AutoPageUpLinearLayoutManager(Context context, int orientation, boolean reverseLayout, RecyclerView recyclerView) {
        super(context, orientation, reverseLayout);
        this.recyclerView = recyclerView;
        init();
    }

    private void init() {
        mHandler = new Handler();
        mHandler.postDelayed(runnable, time);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getItemCount() != 0) {
                if (getChildCount() != 0 && height == 0) {
                    View child = getChildAt(0);
                    height = child.getHeight();
                }
                itemSize = findLastVisibleItemPosition() - findFirstVisibleItemPosition();
                slideCountHeight += slideHeight = height * itemSize;
                countHeight = height * getItemCount();
                //if reach the bottom, reset the slideCountHeight
                if ((countHeight - slideCountHeight) <= 0) {
                    slideHeight = 0 - countHeight;
                    slideCountHeight = 0;
                }
                recyclerView.smoothScrollBy(0, slideHeight);
            }
            mHandler.postDelayed(runnable, time);
        }
    };
}

