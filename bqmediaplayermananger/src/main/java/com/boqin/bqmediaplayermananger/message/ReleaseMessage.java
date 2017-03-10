package com.boqin.bqmediaplayermananger.message;

import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;
import com.netease.neliveplayer.NEMediaPlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

/**
 * 释放操作Message
 * Created by Boqin on 2016/10/27.
 * Modified by Boqin
 *
 * @Version
 */
public class ReleaseMessage extends IPlayerMessage {
    private String mKey;
    private Long mPosition;

    /**
     * @param handler 对应处理的handler
     * @param key     map键值
     * @description: Created by Boqin on 2016/11/7 9:43
     */
    public ReleaseMessage(Handler handler, String key) {
        mMessage = handler.obtainMessage();
        mMessage.what = IPlayerMessage.MSG_RELEASE;
        Bundle data = new Bundle();
        data.putString("key", key);
        mMessage.setData(data);
    }

    /**
     * handleMessage中生成
     *
     * @description: Created by Boqin on 2016/11/7 9:44
     */
    protected ReleaseMessage(Message message) {
        mKey = message.getData().getString("key");
    }

    @Override
    public void performAction(Map<String, IMediaPlayer> pool) {
        if (mKey == null) {
            return;
        }
        IMediaPlayer player = pool.get(mKey);
        if (player == null) {
            return;
        }
        mPosition = player.getCurrentPosition();
        player.performRelease();
        pool.remove(mKey);
    }

    @Override
    public void statusBefore(
            @NonNull
                    Map<String, MediaStatusBean> statusMap) {
        MediaStatusBean mediaStatusBean = statusMap.get(mKey);
        if (mediaStatusBean == null) {
            return;
        }
        mediaStatusBean.setAuto(true);
        mediaStatusBean.setRelease(true);
    }

    @Override
    public void statusAfter(
            @NonNull
                    Map<String, MediaStatusBean> statusMap) {
        MediaStatusBean mediaStatusBean = statusMap.get(mKey);
        if (mediaStatusBean == null) {
            return;
        }
        if (mPosition != null) {
            //进度条和播放进度存在偏差
            if (mediaStatusBean.getPlayerViewResumeStatus() == MediaStatusBean.PLAYER_S_FINISHED) {
                mediaStatusBean.setResumePositions(0L);
            } else {
                mediaStatusBean.setResumePositions(mPosition);
            }
        }
        mediaStatusBean.setPlayerViewResumeStatus(null);
    }
}
