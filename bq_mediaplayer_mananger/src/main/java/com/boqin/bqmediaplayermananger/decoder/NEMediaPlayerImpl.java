package com.boqin.bqmediaplayermananger.decoder;

import java.io.IOException;
import java.util.List;

import com.boqin.bqmediaplayermananger.manager.NEMediaPlayerManager;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.neliveplayer.NEMediaPlayer;

import android.content.Context;
import android.util.Log;
import android.view.Surface;

/**
 * TODO
 * Created by Boqin on 2017/3/8.
 * Modified by Boqin
 *
 * @Version
 */
public class NEMediaPlayerImpl implements IMediaPlayer {

    private Context mContext;
    private NEMediaPlayer mMediaPlayer;

    private IMediaPlayerListener mMediaPlayerListener;

    public NEMediaPlayerImpl(Context context, NEMediaPlayer mediaPlayer){
        mMediaPlayer = mediaPlayer;
        mContext = context.getApplicationContext();
        init();
    }

    private void init() {
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnInfoListener(mVideoInfoListener);
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
    }

    @Override
    public void performPrepare(boolean isMute, Surface surface, String path) {
        mMediaPlayer.setBufferStrategy(NELivePlayer.NELPANTIJITTER);//0为直播低延时，1为点播抗抖动
        mMediaPlayer.setLogLevel(NELivePlayer.NELP_LOG_SILENT);
        mMediaPlayer.setMute(isMute);
        //若 VideoView 继承 TextureView，则通过 setSurface() 接口将 SurfaceTexture 设下去用于显示。
        mMediaPlayer.setSurface(surface);
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performStart() {
        mMediaPlayer.start();
    }

    @Override
    public void performPause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void performResume() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void performSeek(Long pos) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(pos);
        }
    }

    @Override
    public void performRelease() {
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void setSurface(Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void setMute(boolean isMute) {
        mMediaPlayer.setMute(isMute);
    }

    @Override
    public void setListener(IMediaPlayerListener mediaPlayerListener) {
        mMediaPlayerListener = mediaPlayerListener;

    }

    private NELivePlayer.OnPreparedListener mPreparedListener = new NELivePlayer.OnPreparedListener() {
        @Override
        public void onPrepared(NELivePlayer neLivePlayer) {
            if (mMediaPlayerListener!=null) {
                mMediaPlayerListener.onPrepared(NEMediaPlayerImpl.this);
            }
        }
    };

    private NELivePlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new NELivePlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(NELivePlayer mp, int width, int height, int sar_num, int sar_den) {
            if (mMediaPlayerListener!=null) {
                mMediaPlayerListener.onVideoSizeChanged(NEMediaPlayerImpl.this, width, height, sar_num, sar_den);
            }
        }
    };

    private NELivePlayer.OnErrorListener mOnErrorListener = new NELivePlayer.OnErrorListener() {
        @Override
        public boolean onError(NELivePlayer mp, int what, int extra) {
            if (mMediaPlayerListener!=null) {
                return mMediaPlayerListener.onError(NEMediaPlayerImpl.this, what, extra);
            }
            return false;
        }
    };

    private NELivePlayer.OnInfoListener mVideoInfoListener = new NELivePlayer.OnInfoListener() {
        @Override
        public boolean onInfo(NELivePlayer mp, int what, int extra) {
            if (mMediaPlayerListener!=null) {
                return mMediaPlayerListener.onInfo(NEMediaPlayerImpl.this, what, extra);
            }
            return false;
        }
    };

    private NELivePlayer.OnCompletionListener mCompletionListener = new NELivePlayer.OnCompletionListener() {
        @Override
        public void onCompletion(NELivePlayer mp) {
            if (mMediaPlayerListener!=null) {
                mMediaPlayerListener.onCompletion(NEMediaPlayerImpl.this);
            }
        }
    };


    private NELivePlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new NELivePlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(NELivePlayer mp, int percent) {
            if (mMediaPlayerListener!=null) {
                mMediaPlayerListener.onBufferingUpdate(NEMediaPlayerImpl.this, percent);
            }
        }
    };



}
