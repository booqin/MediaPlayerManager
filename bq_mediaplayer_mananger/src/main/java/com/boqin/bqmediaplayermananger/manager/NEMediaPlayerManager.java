package com.boqin.bqmediaplayermananger.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayerListener;
import com.boqin.bqmediaplayermananger.decoder.NEMediaPlayerImpl;
import com.boqin.bqmediaplayermananger.message.IPlayerMessage;
import com.boqin.bqmediaplayermananger.message.PauseMessage;
import com.boqin.bqmediaplayermananger.message.PrepareMessage;
import com.boqin.bqmediaplayermananger.message.ReleaseMessage;
import com.boqin.bqmediaplayermananger.message.ResumeMessage;
import com.boqin.bqmediaplayermananger.message.SeekMessage;
import com.boqin.bqmediaplayermananger.message.StartMessage;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.neliveplayer.NEMediaPlayer;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

/**
 * 游购视频播放池，保存播放器，优化多个播放器的切换使用
 * Created by Zhangfeng on 2016/10/13.
 * Modified by Zhangfeng
 *
 * @Version
 */
public class NEMediaPlayerManager implements Handler.Callback {
    public static final int PLAYER_S_PAUSED = 1;
    public static final int PLAYER_S_FINISHED = 2;
    public static final int PLAYER_S_PLAYING = 3;

    /** handler线程，维护视频播放类NEMediaPlayer */
    private static HandlerThread mPlayerThread = new HandlerThread("video_player_thread");
    private Handler mPoolHandler;

    /** 状态表 */
    private Map<String, MediaStatusBean> mMediaStatusMap;

    /** 播放器线程表 */
    private Map<String, IMediaPlayer> mPool;


    static {
        mPlayerThread.start();
    }

    private NEMediaPlayerImpl value;

    /**
     * 构造器
     *
     * @description: Created by Zhangfeng on 2016/10/11 18:00
     */
    private NEMediaPlayerManager() {
        mPoolHandler = new Handler(mPlayerThread.getLooper(), this);
        mPool = new HashMap<>();
        mMediaStatusMap = new HashMap<>();
    }

    /**
     * 初步单例模式获取Manager
     * 需优化
     */
    public static NEMediaPlayerManager getInstance(){

        return NEMediaPlayerManagerHolder.manager;
    }

    @Override
    public boolean handleMessage(Message msg) {
        synchronized (NEMediaPlayerManager.class) {
            IPlayerMessage playerMessage = IPlayerMessage.createByMessage(msg);
            playerMessage.perform(mPool, mMediaStatusMap);
        }
        return true;
    }

    /**
     * 初始化key
     * @description: Created by Zhangfeng on 2016/10/21 11:13
     */
    public void initKey(@NonNull
            String key){
        if (!mMediaStatusMap.containsKey(key)) {
            MediaStatusBean mediaStatusBean = new MediaStatusBean(key);
            mMediaStatusMap.put(key, mediaStatusBean);
        }
    }

    /**
     * 根据key判断是否未释放，即是否在播放列表中
     *
     * @description: Created by Zhangfeng on 2016/10/11 18:04
     */
    public boolean isPlayingStarted(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return !mMediaStatusMap.get(key).isRelease();
        }
        return false;
    }

    /**
     * 添加播放出错的回调接口
     *
     * @description: Created by Zhangfeng on 2016/10/11 18:05
     */
    public void addErrorCallback(String key, NEMediaPlayerManager.ErrorCallback errorCallback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addErrorCallbacks(errorCallback);
        }
    }

    /**
     * 添加视频的显示大小
     *
     * @description: Created by Zhangfeng on 2016/10/11 18:07
     */
    public void addVideoSize(String key, int width, int height) {
        NEMediaPlayerManager.VideoSize videoSize = new NEMediaPlayerManager.VideoSize();
        videoSize.width = width;
        videoSize.height = height;
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).setVideoSizeMap(videoSize);
        }
    }

    /**
     * 获取视频大小
     *
     * @description: Created by Zhangfeng on 2016/10/11 19:48
     */
    public NEMediaPlayerManager.VideoSize getVideoSize(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return mMediaStatusMap.get(key).getVideoSizeMap();
        }
        return null;
    }

    public void addNoAutoPlayKey(String key) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).setAuto(false);
        }
    }

    @SuppressWarnings("unused")
    public void removeAllNoAutoPlayKey(String key) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).setAuto(true);
        }
    }

    public boolean isNoAutoPlay(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return !mMediaStatusMap.get(key).isAuto();
        }
        return false;
    }

    /**
     * 播放完整标志
     *
     * @description: Created by Zhangfeng on 2016/10/11 19:56
     */
    public boolean isComplete(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return mMediaStatusMap.get(key).isFinished();
        }
        return false;
    }

    /**
     * 获取视频状态
     * 播放，暂停，结束，准备
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:00
     */
    public int getPlayerViewResumeStatus(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return mMediaStatusMap.get(key).getPlayerViewResumeStatus();
        }
        return PLAYER_S_PLAYING;
    }

    /**
     * 释放线程池资源
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:05
     */
    public void releasePool() {
        if (mPool != null) {
            for (String key : mPool.keySet()) {
                release(key);
            }
            mPool.clear();
        }
//        mPoolHandler.removeCallbacksAndMessages(null);
        mMediaStatusMap.clear();
    }

    /**
     * 释放特定key的资源
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:09
     */
    public void release(String key) {
        ReleaseMessage releaseMessage = new ReleaseMessage(mPoolHandler,key);
        releaseMessage.sendToTarget();
    }

    public void removeCompleteNoAutoPlay(String key) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).setAuto(true);
        }
    }

    /**
     * handler线程处理视频相关操作事件
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:17
     */
    public void resume(String key) {
        ResumeMessage resumeMessage = new ResumeMessage(mPoolHandler, key);
        resumeMessage.sendToTarget();
    }

    public void pause(String key) {
        PauseMessage pauseMessage = new PauseMessage(mPoolHandler, key);
        pauseMessage.sendToTarget();
    }

    public void openVideo(String key, String path, Surface surface, boolean isMute, Context context) {
        if (!mPool.containsKey(key)) {
            NEMediaPlayer mediaPlayer = new NEMediaPlayer();
            value = new NEMediaPlayerImpl(context, mediaPlayer);
            value.setListener(mMediaPlayerListener);
            mPool.put(key, value);
            prepare(key, path, surface, isMute);
        }else {
            //已经存在
            mPool.get(key).setSurface(surface);
            start(key);
        }


    }

    public void start(String key){
        StartMessage startMessage = new StartMessage(mPoolHandler, key);
        startMessage.sendToTarget();
    }

    public void seek(String key, long pos) {
        SeekMessage seekMessage = new SeekMessage(mPoolHandler, key, pos);
        seekMessage.sendToTarget();
    }

    /**
     * 重播
     * @description: Created by Boqin on 2016/11/11 9:24
     */
    public void reStart(String key){
        seek(key, 0);
        start(key);
    }

    /**
     * 切换视频播放区域
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:21
     */
    public void switchSurface(String key, Surface surface) {
        if (mPool != null) {
            IMediaPlayer player = mPool.get(key);
            if (player != null) {
                player.setSurface(surface);
            }
        }
    }

    /**
     * 是否在播放
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:22
     */
    public boolean isPlaying(String key) {
        if (mPool != null) {
            IMediaPlayer player = mPool.get(key);
            return player != null && player.isPlaying();
        }
        return false;
    }

    /**
     * 获取播放延时时间
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:23
     */
    public long getDuration(String key) {
        IMediaPlayer player = mPool.get(key);
        if (player != null) {
            try {
                return player.getDuration();
            }
            catch (Exception e) {
                return 0L;
            }
        }
        return 0L;
    }

    /**
     * 播放器是否存在
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:29
     */
    public boolean isMediaPlayerExist(String key) {
        return mPool.containsKey(key);
    }

    /**
     * 获取当前进度
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:35
     */
    public long getCurrentProgress(String key) {
        IMediaPlayer player = mPool.get(key);
        if (player != null) {
            try {
                return player.getCurrentPosition();
            }
            catch (Exception e) {
                return 0L;
            }
        }
        return 0L;
    }

    /**
     * 播放完成回调
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:37
     */
    public void removePlayCompleteCallback(String key, NEMediaPlayerManager.PlayCompleteCallback completeCallback) {
//        List<VsMediaPlayerPool.PlayCompleteCallback> currentList = mPlayCompleteCallbacks.get(key);
//        if (currentList != null) {
//            currentList.remove(completeCallback);
//            if (currentList.size() == 0) {
//                mPlayCompleteCallbacks.remove(key);
//            }
//        }
        if (mMediaStatusMap.get(key) == null) {
            return;
        }
        List<PlayCompleteCallback> currentList = mMediaStatusMap.get(key).getPlayCompleteCallbacks();
        if (currentList != null) {
            currentList.remove(completeCallback);
        }
    }

    /**
     * 播放完成回调
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:37
     */
    public void addPlayCompleteCallback(String key, NEMediaPlayerManager.PlayCompleteCallback completeCallback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addPlayCompleteCallbacks(completeCallback);
        }
    }

    public void addSizeGotCallback(String key, NEMediaPlayerManager.OnSizeGotCallback sizeGotCallback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addSizeGotCallbacks(sizeGotCallback);
        }
    }

    public void removeSizeGotCallback(String key, NEMediaPlayerManager.OnSizeGotCallback sizeGotCallback) {
//        List<VsMediaPlayerPool.OnSizeGotCallback> currentList = mSizeGotCallbacks.get(key);
//        if (currentList != null) {
//            currentList.remove(sizeGotCallback);
//            if (currentList.size() == 0) {
//                mSizeGotCallbacks.remove(key);
//            }
//        }
        List<OnSizeGotCallback> currentList = mMediaStatusMap.get(key).getSizeGotCallbacks();
        if (currentList != null) {
            currentList.remove(sizeGotCallback);
        }
    }

    public void addPrepareDoneCallback(String key, NEMediaPlayerManager.PrepareDoneCallback doneCallback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addPrepareDoneCallbacks(doneCallback);
        }
    }

    public void removePrepareDoneCallback(String key, NEMediaPlayerManager.PrepareDoneCallback doneCallback) {
//        List<VsMediaPlayerPool.PrepareDoneCallback> currentList = mPrepareDoneCallbacks.get(key);
//        if (currentList != null) {
//            currentList.remove(doneCallback);
//            if (currentList.size() == 0) {
//                mPrepareDoneCallbacks.remove(key);
//            }
//        }
        List<PrepareDoneCallback> currentList = mMediaStatusMap.get(key).getPrepareDoneCallbacks();
        if (currentList != null) {
            currentList.remove(doneCallback);
        }
    }

    public void addInfoCallback(String key, NEMediaPlayerManager.InfoCallback infoCallback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addInfoCallbacks(infoCallback);
        }
    }

    public void removeInfoCallback(String key, NEMediaPlayerManager.InfoCallback infoCallback) {
//        List<VsMediaPlayerPool.InfoCallback> currentList = mInfoCallbacks.get(key);
//        if (currentList != null) {
//            currentList.remove(infoCallback);
//            if (currentList.size() == 0) {
//                mInfoCallbacks.remove(key);
//            }
//        }
        List<InfoCallback> currentList = mMediaStatusMap.get(key).getInfoCallbacks();
        if (currentList != null) {
            currentList.remove(infoCallback);
        }
    }

    @SuppressWarnings("unused")
    public void addBufferingCallback(String key, NEMediaPlayerManager.BufferingCallback callback) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).addBufferingCallbacks(callback);
        }
    }

    public void setPlayerMute(String key, boolean isMute, Context context) {
        if (!isMute) {
            AudioManager am = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        if (mPool != null) {
            IMediaPlayer player = mPool.get(key);
            if (player != null) {
                player.setMute(isMute);
            }
        }
    }

    public int getCompleteCallbacksCount(String key) {
        if (mMediaStatusMap.get(key) != null) {
            return mMediaStatusMap.get(key).getPlayCompleteCallbacks().size() == 0 ? 0 : 1;
        }
        return 0;
    }

    public void removeErrorCallback(String key, NEMediaPlayerManager.ErrorCallback callback) {
//        List<VsMediaPlayerPool.ErrorCallback> list = mErrorCallbacks.get(key);
//        if (list != null) {
//            list.remove(callback);
//            if (list.size() == 0) {
//                mErrorCallbacks.remove(key);
//            }
//        }
        List<ErrorCallback> currentList = mMediaStatusMap.get(key).getErrorCallbacks();
        if (currentList != null) {
            currentList.remove(callback);
        }
    }

    /**
     * 添加视频状态
     *
     * @description: Created by Zhangfeng on 2016/10/11 20:05
     */
    private void addPlayerViewResumeStatus(String key, int status) {
        if (mMediaStatusMap.get(key) != null) {
            mMediaStatusMap.get(key).setPlayerViewResumeStatus(status);
        }
    }

    private void prepare(String key, String path, Surface surface, boolean isMute){
        PrepareMessage prepareMessage = new PrepareMessage(mPoolHandler, key, path, surface, isMute);
        prepareMessage.sendToTarget();
    }

    /**
     * 暂停其他播放器
     * @description: Created by Zhangfeng on 2016/10/28 14:32
     */
    private void threadCheckIfOthersStopped(String key) {
        if (mPool != null) {
            for (String current : mPool.keySet()) {
                if (!current.equals(key)) {
                    pause(current);
                }
            }
        }
    }

    private String getKeyFromPool(IMediaPlayer neLivePlayer) {
        if (mPool != null) {
            for (String key : mPool.keySet()) {
                if (neLivePlayer == mPool.get(key)) {
                    return key;
                }
            }
        }
        return null;
    }


    //接口

    private IMediaPlayerListener mMediaPlayerListener = new IMediaPlayerListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer var1, int var2) {
            String key = getKeyFromPool(var1);
            if (!mMediaStatusMap.containsKey(key)) {
                return;
            }
            List<BufferingCallback> relatedBufferCallbacks = mMediaStatusMap.get(key).getBufferingCallbacks();
            for (BufferingCallback relatedBufferCallback : relatedBufferCallbacks) {
                relatedBufferCallback.onBuffering(var2);
            }
        }

        @Override
        public void onCompletion(IMediaPlayer var1) {
            String key = getKeyFromPool(var1);
            addPlayerViewResumeStatus(key, PLAYER_S_FINISHED);
            if (mMediaStatusMap.containsKey(key)) {
                mMediaStatusMap.get(key).setResumePositions(0L);
                //                mMediaStatusMap.get(key).setPlayerViewResumeStatus(MediaStatusBean.PLAYER_S_PLAYING);
                mMediaStatusMap.get(key).setAuto(false);
            }

            List<PlayCompleteCallback> playCompleteCallbacks = mMediaStatusMap.get(key).getPlayCompleteCallbacks();
            for (PlayCompleteCallback playCompleteCallback : playCompleteCallbacks) {
                playCompleteCallback.onPlayerComplete(key);
            }
        }

        @Override
        public boolean onError(IMediaPlayer var1, int var2, int var3) {
            String key = getKeyFromPool(var1);
            if (!mMediaStatusMap.containsKey(key)) {
                return false;
            }
            List<ErrorCallback> callbacks = mMediaStatusMap.get(key).getErrorCallbacks();
            //            if (callbacks != null) {
            //                callbacks.onNetworkError();
            //            }
            for (ErrorCallback callback : callbacks) {
                callback.onError();
            }
            return false;
        }

        @Override
        public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
            String key = getKeyFromPool(var1);
            if (!mMediaStatusMap.containsKey(key)) {
                return false;
            }
            List<InfoCallback> infoCallbacks = mMediaStatusMap.get(key).getInfoCallbacks();
            for (InfoCallback infoCallback : infoCallbacks) {
                if (infoCallback != null) {
                    switch (var2) {
                        case NELivePlayer.NELP_BUFFERING_START:
                            infoCallback.onBufferingStart();
                            break;
                        case NELivePlayer.NELP_BUFFERING_END:
                            infoCallback.onBufferingEnd();
                            break;
                    }
                }
            }
            return false;
        }

        @Override
        public void onPrepared(IMediaPlayer var1) {
            Log.d("ThreadName", "" + Thread.currentThread());
            String key = getKeyFromPool(var1);
            if (!mMediaStatusMap.containsKey(key)) {
                return;
            }
            List<PrepareDoneCallback> prepareDoneCallbacks = mMediaStatusMap.get(key).getPrepareDoneCallbacks();
            Long resumePos = mMediaStatusMap.get(key).getResumePositions();
            if (resumePos != null) {
                long resumePosValue = resumePos;
                if (resumePosValue != 0L) {
                    //有播放记录，设置位置
                    seek(key, resumePos);
                }
            }
            //开始播放
            start(key);
            for (PrepareDoneCallback prepareDoneCallback : prepareDoneCallbacks) {
                prepareDoneCallback.onPrepareDone(key);
            }
            threadCheckIfOthersStopped(getKeyFromPool(var1));

        }

        @Override
        public void onSeekComplete(IMediaPlayer var1) {

        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3, int var4, int var5) {
            if (var2 != 0 && var3 != 0) {
                String key = getKeyFromPool(var1);
                if (!mMediaStatusMap.containsKey(key)) {
                    return;
                }
                List<OnSizeGotCallback> onSizeGotCallbacks = mMediaStatusMap.get(key).getSizeGotCallbacks();
                //                if (onSizeGotCallback != null) {
                //                    onSizeGotCallback.onVideoSizeGot(width, height);
                //                }
                for (OnSizeGotCallback onSizeGotCallback : onSizeGotCallbacks) {
                    onSizeGotCallback.onVideoSizeGot(var2, var3);
                }
            }
        }
    };

    public interface PlayCompleteCallback {
        void onPlayerComplete(String playerKey);
    }

    public interface PrepareDoneCallback {
        void onPrepareDone(String playerKey);
    }

    public interface BufferingCallback {
        void onBuffering(int percent);
    }

    public interface InfoCallback {
        void onBufferingStart();

        void onBufferingEnd();
    }

    public interface ErrorCallback {
        void onError();
    }

    public interface OnSizeGotCallback {
        void onVideoSizeGot(int width, int height);
    }

    public class VideoSize {
        public int width;
        public int height;
    }

    private static class NEMediaPlayerManagerHolder{
        public static NEMediaPlayerManager manager = new NEMediaPlayerManager();
    }
}
