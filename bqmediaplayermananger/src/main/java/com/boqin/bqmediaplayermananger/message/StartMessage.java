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
 * 开始操作Message
 * Created by Boqin on 2016/10/27.
 * Modified by Boqin
 *
 * @Version
 */
public class StartMessage extends IPlayerMessage {
    private String mKey;

    /**
     * @param handler 对应处理的handler
     * @param key     map键值
     * @description: Created by Boqin on 2016/11/7 9:43
     */
    public StartMessage(Handler handler, String key) {
        mMessage = handler.obtainMessage();
        mMessage.what = IPlayerMessage.MSG_START;
        Bundle data = new Bundle();
        data.putString("key", key);
        mMessage.setData(data);
    }

    /**
     * handleMessage中生成
     *
     * @description: Created by Boqin on 2016/11/7 9:44
     */
    protected StartMessage(Message message) {
        mKey = message.getData().getString("key");
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
        mediaPlayer.performStart();
    }

    @Override
    public void statusBefore(
            @NonNull
                    Map<String, MediaStatusBean> statusMap) {
        MediaStatusBean mediaStatusBean = statusMap.get(mKey);
        if (mediaStatusBean == null) {
            return;
        }
        mediaStatusBean.setPlayerViewResumeStatus(MediaStatusBean.PLAYER_S_PLAYING);
    }

    @Override
    public void statusAfter(
            @NonNull
                    Map<String, MediaStatusBean> statusMap) {

    }
}
