package com.boqin.bqmediaplayermananger.message;

import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;
import com.netease.neliveplayer.NEMediaPlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 暂停操作Message
 * Created by Boqin on 2016/10/27.
 * Modified by Boqin
 *
 * @Version
 */
public class PauseMessage extends IPlayerMessage {
    /** 对应的map中的key */
    private String mKey;
    /** 进度条位置 */
    private Long mPosition;

    /**
     * @param handler 对应处理的handler
     * @param key     map键值
     * @description: Created by Boqin on 2016/11/7 9:43
     */
    public PauseMessage(Handler handler, String key) {
        //        addPlayerViewResumeStatus(key, PLAYER_S_PAUSED);
        mMessage = handler.obtainMessage();
        mMessage.what = IPlayerMessage.MSG_PAUSE;
        Bundle data = new Bundle();
        data.putString("key", key);
        mMessage.setData(data);
    }

    /**
     * handleMessage中生成
     *
     * @description: Created by Boqin on 2016/11/7 9:44
     */
    protected PauseMessage(Message message) {
        mKey = message.getData().getString("key");
    }

    @Override
    public void performAction(Map<String, IMediaPlayer> pool) {
        if (mKey == null) {
            return;
        }
//        NEMediaPlayer mediaPlayer = pool.get(mKey);
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mPosition = mediaPlayer.getCurrentPosition();
//            mediaPlayer.pause();
//        }

        IMediaPlayer mediaPlayer = pool.get(mKey);
        if (mediaPlayer != null) {
            mPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.performPause();
        }
    }

    @Override
    public void statusBefore(Map<String, MediaStatusBean> statusMap) {
        if (mKey == null) {
            return;
        }
        MediaStatusBean mediaBean = statusMap.get(mKey);
        if (mediaBean != null) {
            //当介绍状态下不需要更新标志位到状态表中
            if (mediaBean.getPlayerViewResumeStatus() == null || mediaBean.getPlayerViewResumeStatus() != MediaStatusBean.PLAYER_S_FINISHED) {
                mediaBean.setPlayerViewResumeStatus(MediaStatusBean.PLAYER_S_PAUSED);
            }
        }
    }

    @Override
    public void statusAfter(Map<String, MediaStatusBean> statusMap) {
        if (mKey == null) {
            return;
        }
        MediaStatusBean mediaBean = statusMap.get(mKey);
        if (mediaBean != null && mPosition != null) {
            mediaBean.setResumePositions(mPosition);
        }
    }
}
