package com.boqin.bqmediaplayermananger.message;

import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;

/**
 * 缓冲操作Message
 * Created by Boqin on 2016/10/28.
 * Modified by Boqin
 *
 * @Version
 */
public class PrepareMessage extends IPlayerMessage {
    /** 视频Id */
    private String mKey;
    /** 播放的Surface */
    private Surface mSurface;
    /** 路径 */
    private String mPath;
    /** 静音 */
    private boolean isMute;

    /**
     * @param handler 对应处理的handler
     * @param key     map键值
     * @description: Created by Boqin on 2016/11/7 9:43
     */
    public PrepareMessage(Handler handler, String key, String path, Surface surface, boolean isMute) {
        mMessage = handler.obtainMessage();
        mMessage.what = IPlayerMessage.MSG_PERPARE;
        Bundle data = new Bundle();
        data.putString("key", key);
        data.putString("path", path);
        data.putParcelable("surface", surface);
        data.putBoolean("isMute", isMute);
        mMessage.setData(data);
    }

    /**
     * handleMessage中生成
     *
     * @description: Created by Boqin on 2016/11/7 9:44
     */
    protected PrepareMessage(Message message) {
        mKey = message.getData().getString("key");
        isMute = message.getData().getBoolean("isMute");
        mSurface = message.getData().getParcelable("surface");
        mPath = message.getData().getString("path");
    }

    @Override
    public void performAction(Map<String, IMediaPlayer> pool) {
        if (mKey == null) {
            return;
        }
        IMediaPlayer mediaPlayer = pool.get(mKey);
        if (mediaPlayer == null) {
            return;
        }

        if (mPath == null) {
            return;
        }

        mediaPlayer.performPrepare(isMute, mSurface, mPath);
    }

    @Override
    public void statusBefore(Map<String, MediaStatusBean> statusMap) {
        MediaStatusBean mediaStatusBean = statusMap.get(mKey);
        if (mediaStatusBean == null) {
            return;
        }
        mediaStatusBean.setPlayerViewResumeStatus(MediaStatusBean.PLAYER_S_PERPARE);
        mediaStatusBean.setRelease(false);
    }

    @Override
    public void statusAfter(Map<String, MediaStatusBean> statusMap) {

    }
}
