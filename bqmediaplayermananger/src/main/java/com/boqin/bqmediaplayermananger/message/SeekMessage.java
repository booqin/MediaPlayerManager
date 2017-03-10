package com.boqin.bqmediaplayermananger.message;

import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 进度条操作Message
 * Created by Boqin on 2016/10/27.
 * Modified by Boqin
 *
 * @Version
 */
public class SeekMessage extends IPlayerMessage {
    private String mKey;
    private Long mPos;

    /**
     * @param handler 对应处理的handler
     * @param key     map键值
     * @description: Created by Boqin on 2016/11/7 9:43
     */
    public SeekMessage(Handler handler, String key, Long pos) {
        mMessage = handler.obtainMessage();
        mMessage.what = IPlayerMessage.MSG_SEEK;
        Bundle data = new Bundle();
        data.putString("key", key);
        data.putLong("pos", pos);
        mMessage.setData(data);
    }

    /**
     * handleMessage中生成
     *
     * @description: Created by Boqin on 2016/11/7 9:44
     */
    protected SeekMessage(Message message) {
        mKey = message.getData().getString("key");
        mPos = message.getData().getLong("pos");
    }

    @Override
    public void performAction(Map<String, IMediaPlayer> pool) {
        if (mKey == null || mPos == null) {
            return;
        }

        IMediaPlayer player = pool.get(mKey);
        player.performSeek(mPos);
    }

    @Override
    public void statusBefore(Map<String, MediaStatusBean> statusMap) {

    }

    @Override
    public void statusAfter(Map<String, MediaStatusBean> statusMap) {

    }
}
