package com.boqin.xgmediaplayermanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 滚动方向判定
 *
 * @author hzwangyufei
 * @date 2016/4/13
 */
public class ScrollDirectionDetector {

    public static final int IDLE = -1;
    public static final int UP = 0;
    public static final int DOWN = 1;

    private final OnDetectScrollListener mOnDetectScrollListener;

    private int mOldTop;
    private int mOldFirstVisibleItem;

    private int mOldScrollDirection = IDLE;

    public ScrollDirectionDetector(OnDetectScrollListener onDetectScrollListener) {
        mOnDetectScrollListener = onDetectScrollListener;
    }

    public void onDetectedListScroll(RecyclerView recyclerView, int firstVisibleItem) {
        //移到判断内？
        View view = recyclerView.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();
        //如果第一个可见的Item与上一次一致，就判断其View的Top值，最后确定是上滑还是下滑
        if (firstVisibleItem == mOldFirstVisibleItem) {
            if (top > mOldTop) {
                onScrollDown(); //更新状态，回调OnDetectScrollListener接口的方法
            } else if (top < mOldTop) {
                onScrollUp();
            }
        } else {
            //不同的item直接判断上下滑
            if (firstVisibleItem < mOldFirstVisibleItem) {
                onScrollDown();
            } else {
                onScrollUp();
            }
        }

        mOldTop = top;
        mOldFirstVisibleItem = firstVisibleItem;
    }

    private void onScrollDown() {
        if (mOldScrollDirection != DOWN) {
            mOldScrollDirection = DOWN;
            mOnDetectScrollListener.onScrollDirectionChanged(DOWN);
        }
    }

    private void onScrollUp() {
        if (mOldScrollDirection != UP) {
            mOldScrollDirection = UP;
            mOnDetectScrollListener.onScrollDirectionChanged(UP);
        }
    }

    public interface OnDetectScrollListener {
        void onScrollDirectionChanged(int scrollDirection);
    }
}