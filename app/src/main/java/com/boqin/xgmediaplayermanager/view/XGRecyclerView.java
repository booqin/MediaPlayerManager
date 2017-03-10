package com.boqin.xgmediaplayermanager.view;

import com.boqin.bqmediaplayermananger.view.IPlayerAttacher;
import com.boqin.bqmediaplayermananger.utils.VideoRecyclerViewAutoControlAttacher;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * TODO
 * Created by Boqin on 2017/3/9.
 * Modified by Boqin
 *
 * @Version
 */
public class XGRecyclerView extends RecyclerView{


    private VideoRecyclerViewAutoControlAttacher mAutoControlAttacher;
    private boolean mIsFromPause = false;

    private String mHolderKeyToRecover = null;

    private float mFlingFactor = 0.7f;

    private LinearLayoutManager mLayoutManager;

    private boolean mIsOnPauseWhenPlaying = false;


    public XGRecyclerView(Context context) {
        super(context);
        init();
    }

    public XGRecyclerView(Context context,
            @Nullable
                    AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XGRecyclerView(Context context,
            @Nullable
                    AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        throw new IllegalStateException("do not use 'setAdapter(adapter)', use 'setLayoutManagerAndAdapter' instead.");
    }

    @Deprecated
    @Override
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        throw new IllegalStateException("do not use 'setLayoutManager', use 'setLayoutManagerAndAdapter' instead.");
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY = (int) (mFlingFactor * velocityY);
        return super.fling(velocityX, velocityY);
    }

    @Override
    protected void onDetachedFromWindow() {
        //        VsIMMLeaksFixer.fixLeakForView(getContext());
        super.onDetachedFromWindow();
    }

    public void actionsForPull() {
        //        EventBus.getDefault().post(new EventNetworkChanged(EventNetworkChanged.JUST_STOP_VIDEO));
    }

    /**
     * 设置 Adapter、LayoutManager 以及其他辅助信息
     * 请勿直接使用 setAdapter 以及 setLayoutManager
     *
     * @param isLoadMoreNeed 是否需要加载更多
     */
    public void setLayoutManagerAndAdapter(RecyclerView.Adapter adapter, LinearLayoutManager layoutManager, boolean isLoadMoreNeed) {
        mLayoutManager = layoutManager;
        super.setLayoutManager(layoutManager);
        //        super.setAdapter(adapter, isLoadMoreNeed);
        super.setAdapter(adapter);
        mAutoControlAttacher = new VideoRecyclerViewAutoControlAttacher();//ViewRecycler与视频播放的连接器，管理着Recycler的滑动以及对应情况下ViewVideoPlayNEContent相关操作。
        mAutoControlAttacher.attach(this);//连接Recycler

    }

    /**
     * 释放资源，主要是视频相关的资源占用
     * 务必在 onDestroy 中调用，否则可能出现内存泄漏
     */
    public void release() {
        mAutoControlAttacher.releaseResources();
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            int i = linearLayoutManager.findFirstVisibleItemPosition();
            int j = linearLayoutManager.findLastVisibleItemPosition();
            for (; i <= j; i++) {
                RecyclerView.ViewHolder holder = findViewHolderForAdapterPosition(i);
                if (holder != null && holder instanceof IPlayerAttacher) {
                    ((IPlayerAttacher) holder).release();
                }
            }
        }
    }

    /**
     * 务必在 onPause 中调用此方法
     * 主要处理了视频播放相关的现场保护
     */
    public void actionsForOnPause() {
        mIsFromPause = true;
        if (mAutoControlAttacher != null) {
            if (mAutoControlAttacher.getCurrentActiveKey() != null) {
                //当前在播放的key
                mHolderKeyToRecover = mAutoControlAttacher.getCurrentActiveKey();
                //循环获取可见item的ViewHolder
                for (int i = mLayoutManager.findFirstVisibleItemPosition(), j = mLayoutManager.findLastVisibleItemPosition(); i <= j; i++) {
                    RecyclerView.ViewHolder holder = findViewHolderForAdapterPosition(i);
                    if (holder != null && holder instanceof IPlayerAttacher) {
                        IPlayerAttacher player = (IPlayerAttacher) holder;
                        mIsOnPauseWhenPlaying = player.onPause(mHolderKeyToRecover);
                    }
                }
            }
        }
    }

    /**
     * 务必在 onResume 中调用此方法
     * 主要处理了视频播放相关的现场恢复
     * 在MissV1.0中，不需要自动续播，所以不做处理
     */
    public void actionsForOnResume() {

        if (mIsFromPause) {
            mIsFromPause = false;
            if (mHolderKeyToRecover != null) {
                for (int i = mLayoutManager.findFirstVisibleItemPosition(), j = mLayoutManager.findLastVisibleItemPosition(); i <= j; i++) {
                    RecyclerView.ViewHolder holder = findViewHolderForAdapterPosition(i);
                    if (holder != null && holder instanceof IPlayerAttacher) {
                        IPlayerAttacher player = (IPlayerAttacher) holder;
                        player.onResume(mHolderKeyToRecover, mIsOnPauseWhenPlaying);
                    }
                    mIsOnPauseWhenPlaying = false;
                }
            }
            mHolderKeyToRecover = null;
        }
    }

    /**
     * 设置快速滑动阻力
     *
     * @param flingFactor 0表示无法快速滑动，1表示标准快速滑动，比1大表示更小阻力更快速度
     */
    @SuppressWarnings("unused")
    public void setFlingFactor(float flingFactor) {
        mFlingFactor = flingFactor;
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(Color.WHITE);
    }

}
