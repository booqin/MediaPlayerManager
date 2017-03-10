package com.boqin.bqmediaplayermananger.decoder;

import android.view.Surface;
import android.view.ViewOutlineProvider;

/**
 * 播放器接口
 * Created by Boqin on 2017/3/8.
 * Modified by Boqin
 *
 * @Version
 */
public interface IMediaPlayer {


    /**
     * 视频播放准备操作
     * @param isMute
     * @param surface
     * @param path
     */
    void performPrepare(boolean isMute, Surface surface, String path);

    /**
     * 开始播放
     */
    void performStart();

    /**
     * 暂停播放
     */
    void performPause();

    /**
     * 继续播放
     */
    void performResume();

    /**
     * 进度条相关操作
     * @param pos
     */
    void performSeek(Long pos);

    /**
     * 释放资源操作
     */
    void performRelease();

    long getCurrentPosition();

    void setSurface(Surface surface);

    boolean isPlaying();

    long getDuration();

    void setMute(boolean isMute);

    void setListener(IMediaPlayerListener mediaPlayerListener);


}
