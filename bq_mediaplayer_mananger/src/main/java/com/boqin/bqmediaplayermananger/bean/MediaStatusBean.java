package com.boqin.bqmediaplayermananger.bean;

import java.util.ArrayList;
import java.util.List;

import com.boqin.bqmediaplayermananger.manager.NEMediaPlayerManager;

/**
 * 视频状态类
 * Created by Boqin on 2016/10/21.
 * Modified by Boqin
 *
 * @Version
 */
public class MediaStatusBean {

    public static final int PLAYER_S_PAUSED = 1;
    public static final int PLAYER_S_FINISHED = 2;
    public static final int PLAYER_S_PLAYING = 3;
    public static final int PLAYER_S_PERPARE = 4;

    /** 身份标识 */
    private String mKey;

    private String mVideoUrl;

    /**
     * 进度条位置缓存
     * @description: Created by Zhangfeng on 2016/10/17 18:08
     */
    private Long mResumePositions;

    /**
     * 视频尺寸大小
     * @description: Created by Zhangfeng on 2016/10/17 18:08
     */
    private NEMediaPlayerManager.VideoSize mVideoSizeMap;

    /**
     * 视频状态
     * @description: Created by Zhangfeng on 2016/10/17 18:08
     */
    private Integer mPlayerViewResumeStatus;

    /** 是否自动播放 */
    private boolean isAuto;

    /** 回收标志 */
    private boolean isRelease;

    //回调相关
    private List<NEMediaPlayerManager.PlayCompleteCallback> mPlayCompleteCallbacks;
    private List<NEMediaPlayerManager.PrepareDoneCallback> mPrepareDoneCallbacks;
    private List<NEMediaPlayerManager.BufferingCallback> mBufferingCallbacks;
    private List<NEMediaPlayerManager.InfoCallback> mInfoCallbacks;
    /**
     * 错误回调接口，map类型，视频的key为键值，回调接口List为对应值
     * @description: Created by Zhangfeng on 2016/10/11 18:06
     */
    private List<NEMediaPlayerManager.ErrorCallback> mErrorCallbacks;

    private List<NEMediaPlayerManager.OnSizeGotCallback> mSizeGotCallbacks;

    public MediaStatusBean(String key){
        mKey = key;
        initStatus();
    }

    private void initStatus() {
        isAuto = true;
        isRelease = true;
        mPlayCompleteCallbacks = new ArrayList<>();
        mPrepareDoneCallbacks = new ArrayList<>();
        mBufferingCallbacks = new ArrayList<>();
        mInfoCallbacks = new ArrayList<>();
        mErrorCallbacks = new ArrayList<>();
        mSizeGotCallbacks = new ArrayList<>();
    }

    public boolean isFinished(){
        return mPlayerViewResumeStatus != null && mPlayerViewResumeStatus == PLAYER_S_FINISHED;
    }
    public boolean isPlaying(){
        return mPlayerViewResumeStatus != null && mPlayerViewResumeStatus == PLAYER_S_PLAYING;
    }
    public boolean isPaused(){
        return mPlayerViewResumeStatus != null && mPlayerViewResumeStatus == PLAYER_S_PAUSED;
    }


    public Long getResumePositions() {
        return mResumePositions;
    }

    public void setResumePositions(Long resumePositions) {
        mResumePositions = resumePositions;
    }

    public NEMediaPlayerManager.VideoSize getVideoSizeMap() {
        return mVideoSizeMap;
    }

    public void setVideoSizeMap(NEMediaPlayerManager.VideoSize videoSizeMap) {
        mVideoSizeMap = videoSizeMap;
    }

    public Integer getPlayerViewResumeStatus() {
        return mPlayerViewResumeStatus;
    }

    public void setPlayerViewResumeStatus(Integer playerViewResumeStatus) {
        mPlayerViewResumeStatus = playerViewResumeStatus;
    }

    public List<NEMediaPlayerManager.PlayCompleteCallback> getPlayCompleteCallbacks() {
        return mPlayCompleteCallbacks;
    }

    public void addPlayCompleteCallbacks(NEMediaPlayerManager.PlayCompleteCallback playCompleteCallbacks) {
        mPlayCompleteCallbacks.add(playCompleteCallbacks);
    }

    public List<NEMediaPlayerManager.PrepareDoneCallback> getPrepareDoneCallbacks() {
        return mPrepareDoneCallbacks;
    }

    public void addPrepareDoneCallbacks(NEMediaPlayerManager.PrepareDoneCallback prepareDoneCallbacks) {
        mPrepareDoneCallbacks.add(prepareDoneCallbacks);
    }

    public List<NEMediaPlayerManager.BufferingCallback> getBufferingCallbacks() {
        return mBufferingCallbacks;
    }

    public void addBufferingCallbacks(NEMediaPlayerManager.BufferingCallback bufferingCallbacks) {
        mBufferingCallbacks.add(bufferingCallbacks);
    }

    public List<NEMediaPlayerManager.InfoCallback> getInfoCallbacks() {
        return mInfoCallbacks;
    }

    public void addInfoCallbacks(NEMediaPlayerManager.InfoCallback infoCallbacks) {
        mInfoCallbacks.add(infoCallbacks);
    }

    public List<NEMediaPlayerManager.ErrorCallback> getErrorCallbacks() {
        return mErrorCallbacks;
    }

    public void addErrorCallbacks(NEMediaPlayerManager.ErrorCallback errorCallbacks) {
        mErrorCallbacks.add(errorCallbacks);
    }

    public List<NEMediaPlayerManager.OnSizeGotCallback> getSizeGotCallbacks() {
        return mSizeGotCallbacks;
    }

    public void addSizeGotCallbacks(NEMediaPlayerManager.OnSizeGotCallback sizeGotCallbacks) {
        mSizeGotCallbacks.add(sizeGotCallbacks);
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public void setRelease(boolean release) {
        isRelease = release;
    }
}
