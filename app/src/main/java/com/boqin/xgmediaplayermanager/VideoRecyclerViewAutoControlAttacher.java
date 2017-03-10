package com.boqin.xgmediaplayermanager;

import java.util.Timer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 提供 RecyclerView 视频自动播放自动暂停支持
 *
 * @author hzwangyufei
 * @date 2016/4/14
 */
public final class VideoRecyclerViewAutoControlAttacher{
    private static final long FIRST_CHECK_DELAY = 500L;

    private int mScrollDirection = ScrollDirectionDetector.IDLE;

    private RecyclerView mRecyclerView;

    private int mHeaderHeight;
    private int mVideoHeight;

    private Rect mRect4UpDeactivate;
    private Rect mRect4UpActivate;
    private Rect mRect4DownDeactivate;
    private Rect mRect4DownActivate;

    private int mScrollState = RecyclerView.SCROLL_STATE_IDLE;

    private LinearLayoutManager mLayoutManager;

    private Timer mTimer;

//    private ViewHandler mViewHandler;

    private boolean mIsAutoPlayEnabled = false;

    private int mPositionToActivate = -1;

    private int mReleaseDownLimit, mReleaseUpLimit;

    private int mRecyclerViewHeaderCount = 0;

    private String mCurrentActiveKey = null;

    public VideoRecyclerViewAutoControlAttacher() {
        mRect4UpDeactivate = new Rect();
        mRect4UpActivate = new Rect();
        mRect4DownDeactivate = new Rect();
        mRect4DownActivate = new Rect();

//        mViewHandler = new ViewHandler(this);
        //设置不自动播放标志位
        setAutoPlayEnabled(false);
    }

    /**
     * 设置是否开启视频自动播放
     */
    private void setAutoPlayEnabled(boolean isAutoPlayEnabled) {
        mIsAutoPlayEnabled = isAutoPlayEnabled;
    }

    public void releaseResources() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        mTimer = null;
    }

    public void attach(final RecyclerView loadMoreRecyclerView) {
        if (loadMoreRecyclerView != null && loadMoreRecyclerView.getLayoutManager() != null && loadMoreRecyclerView.getAdapter() != null) {
            Context context = loadMoreRecyclerView.getContext().getApplicationContext();
            Resources resources = context.getResources();

            mRecyclerView = loadMoreRecyclerView;
            mLayoutManager = (LinearLayoutManager) loadMoreRecyclerView.getLayoutManager();
//            mRecyclerViewHeaderCount = loadMoreRecyclerView.getHeaderCount();
            mRecyclerViewHeaderCount = 0;

            mVideoHeight = ScreenUtil.getScreenWidth(context);
            //ItemView中不只是视频，视频上的宽度即为headerHeight,此处为52dp
            mHeaderHeight = resources.getDimensionPixelSize(R.dimen.post_holder_header_height);
            //上滑回收的最大值，当显示的部分小于该大小时回收,此处为43dp
            mReleaseUpLimit = resources.getDimensionPixelSize(R.dimen.post_holder_action_container_height);
            //下滑回收的最大值此处为
            mReleaseDownLimit = (int) (resources.getDimensionPixelSize(R.dimen.post_holder_header_height) / 3f * 2);

            //该类用与滑动方向的判定，在Sroll事件中被调用
            final ScrollDirectionDetector scrollDirectionDetector = new ScrollDirectionDetector(new ScrollDirectionDetector.OnDetectScrollListener() {
                @Override
                public void onScrollDirectionChanged(int scrollDirection) {
                    mScrollDirection = scrollDirection;
                }
            });

            //RecyclerView添加滑动事件监听
            loadMoreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    // 滑动停止时根据当前位置激活视频播放
//                    if (newState != mScrollState && newState == RecyclerView.SCROLL_STATE_IDLE && mPositionToActivate >= 0) {
                    //获取要播放的ViewHolder
//                        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(mPositionToActivate);
//                        if (holder != null && holder instanceof IPlayerAttacher) {
//                            if (mIsAutoPlayEnabled) {
//                                IPlayerAttacher video = (IPlayerAttacher) holder;
//                                if (video.isNeedPlayNow()) {
//                                    //如果需要自动播放，开始播放
//                                    video.activate();
//                                    //更新key
//                                    mCurrentActiveKey = video.getPlayerKey();
//                                }
//                            }
//                            //重置位置
//                            mPositionToActivate = -1;
//                        }
//                    }
//                    //保存状态
//                    mScrollState = newState;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    // 滑动过程中一旦触发停止播放的条件则立即执行
//                    mRecyclerViewHeaderCount = loadMoreRecyclerView.getHeaderCount();
                    ////头部
                    mRecyclerViewHeaderCount = 0;

                    //获取第一个和最后一个可见Item的位置值，不包含头部
                    int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition() - mRecyclerViewHeaderCount;
                    int lastVisiblePos = mLayoutManager.findLastVisibleItemPosition() - mRecyclerViewHeaderCount;
                    //检测滑动方向,更新mScrollDirection
                    scrollDirectionDetector.onDetectedListScroll(recyclerView, firstVisiblePos);
                    if (mScrollDirection == ScrollDirectionDetector.UP) {
                        //根据位置获取要停止的ViewHolder
                        RecyclerView.ViewHolder videoToStop = findDeactivateHolderWhenUp(firstVisiblePos);
                        if (videoToStop != null) {
                            IPlayerAttacher playerAttacherToStop = (IPlayerAttacher) videoToStop;
                            //停止
                            playerAttacherToStop.deactivate();
//                            playerAttacherToStop.release();
                            MissApplication.getAppInstance().getPlayerManager().removeCompleteNoAutoPlay(playerAttacherToStop.getPlayerKey());
                            //如果ViewHolder位置满足回收的大小,直接释放资源
                            if (videoToStop.itemView.getHeight() - mRect4UpDeactivate.top < mReleaseUpLimit) {
                                //释放资源
                                playerAttacherToStop.release();
                            }
                        }
                        //获取需要播放的ViewHolder位置
//                        int posToActivate = findActivateHolderWhenUp(lastVisiblePos);
//                        if (posToActivate != -1) {
//                            //更新成员变量，在停止滑动时根据该值播放视频
//                            mPositionToActivate = posToActivate;
//                        }
                    }
                    else if (mScrollDirection == ScrollDirectionDetector.DOWN) {
                        RecyclerView.ViewHolder videoToStop = findDeactivateHolderWhenDown(lastVisiblePos);
                        if (videoToStop != null) {
                            IPlayerAttacher playerAttacherToStop = (IPlayerAttacher) videoToStop;
                            playerAttacherToStop.deactivate();
                            MissApplication.getAppInstance().getPlayerManager().removeCompleteNoAutoPlay(playerAttacherToStop.getPlayerKey());
                            if (mRect4DownDeactivate.bottom < mReleaseDownLimit) {
                                playerAttacherToStop.release();
                            }
                        }
//                        int posToActivate = findActivateHolderWhenDown(firstVisiblePos);
//                        if (posToActivate != -1) {
//                            mPositionToActivate = posToActivate;
//                        }
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException("必须先设置 Adapter 与 LayoutManager 后才能调用 attach()");
    }

    public String getCurrentActiveKey() {
        return mCurrentActiveKey;
    }

    /**
     * 列表向上移动时 - 尝试找出需要停止播放的 ViewHolder
     *
     * @param firstVisiblePos 列表首个可见的 item 的位置
     * @return 需要停止播放的 ViewHolder，如果没有找到返回 null
     */
    private RecyclerView.ViewHolder findDeactivateHolderWhenUp(int firstVisiblePos) {
        //获取当前ViewHolder的位置
//        int currentCalcPos = firstVisiblePos + mRecyclerViewHeaderCount;
        int currentCalcPos = firstVisiblePos;
        if (currentCalcPos > mRecyclerViewHeaderCount - 1) {
            //在RecyclerView中获取ViewHolder
//            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(currentCalcPos);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForLayoutPosition(currentCalcPos);
            if (viewHolder instanceof IPlayerAttacher) {
                //获取显示View的Rect，即该View显示相对于自己完整的Rect的位置区域
                viewHolder.itemView.getLocalVisibleRect(mRect4UpDeactivate);
                //TextureView区域是否大于基准值
                if (mRect4UpDeactivate.top - mHeaderHeight > mVideoHeight / 3f) {
                    return viewHolder;
                }
            }
        }
        return null;
    }

    private int findActivateHolderWhenUp(int lastVisiblePos) {
//        int currentCalcPos = lastVisiblePos + mRecyclerViewHeaderCount;
//        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(currentCalcPos);
        int currentCalcPos = lastVisiblePos;
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(currentCalcPos);
        if (holder != null && holder instanceof IPlayerAttacher) {
            holder.itemView.getLocalVisibleRect(mRect4UpActivate);
            if (mRect4UpActivate.bottom - mHeaderHeight > mVideoHeight / 3f) {
                return currentCalcPos;
            }
        }
        return -1;
    }

    private int findActivateHolderWhenDown(int firstVisiblePos) {
        int currentCalcPos = firstVisiblePos + mRecyclerViewHeaderCount;
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(currentCalcPos);
        if (holder != null && holder instanceof IPlayerAttacher) {
            holder.itemView.getLocalVisibleRect(mRect4DownActivate);
            int noVideoBottomHeight = holder.itemView.getHeight() - mVideoHeight - mHeaderHeight;
            if ((mRect4DownActivate.bottom - mRect4DownActivate.top) - noVideoBottomHeight > mVideoHeight / 3f) {
                return currentCalcPos;
            }
        }
        return -1;
    }

    /**
     * 下移获取需要停止的ViewHolder
     *
     * @description: Created by Boqin on 2016/12/5 14:42
     */
    private RecyclerView.ViewHolder findDeactivateHolderWhenDown(int lastVisiblePos) {
//        int currentCalcPos = lastVisiblePos + mRecyclerViewHeaderCount;
//        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(currentCalcPos);

        int currentCalcPos = lastVisiblePos;
        RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForLayoutPosition(currentCalcPos);

        if (holder != null && holder instanceof IPlayerAttacher) {
            holder.itemView.getLocalVisibleRect(mRect4DownDeactivate);
            if (mRect4DownDeactivate.bottom - mHeaderHeight < mVideoHeight / 3f) {
                return holder;
            }
        }
        return null;
    }

}
