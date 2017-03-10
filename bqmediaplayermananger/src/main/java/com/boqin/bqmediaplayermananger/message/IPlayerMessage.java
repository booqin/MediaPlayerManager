package com.boqin.bqmediaplayermananger.message;

import java.util.Map;

import com.boqin.bqmediaplayermananger.bean.MediaStatusBean;
import com.boqin.bqmediaplayermananger.decoder.IMediaPlayer;
import com.netease.neliveplayer.NEMediaPlayer;

import android.os.Message;
import android.support.annotation.NonNull;

/**
 * 视频播放用Message接口类
 * Created by Boqin on 2016/10/27.
 * Modified by Boqin
 *
 * @Version
 */
public abstract class IPlayerMessage {
    public static final int MSG_START = 532;
    public static final int MSG_PERPARE = 533;
    public static final int MSG_RELEASE = 709;
    public static final int MSG_SEEK = 829;
    public static final int MSG_RESUME = 534;
    public static final int MSG_PAUSE = 707;

    protected Message mMessage;

    /**
     * 根据message的类型获取对应的操作Message
     *
     * @param message handleMessage中传入的Message
     * @description: Created by Boqin on 2016/11/7 9:36
     */
    public static IPlayerMessage createByMessage(Message message) {
        IPlayerMessage playerMessage = null;
        switch (message.what) {
            case MSG_START:
                playerMessage = new StartMessage(message);
                break;
            case MSG_PERPARE:
                playerMessage = new PrepareMessage(message);
                break;
            case MSG_RELEASE:
                playerMessage = new ReleaseMessage(message);
                break;
            case MSG_SEEK:
                playerMessage = new SeekMessage(message);
                break;
            case MSG_PAUSE:
                playerMessage = new PauseMessage(message);
                break;
            case MSG_RESUME:
                playerMessage = new ResumeMessage(message);
                break;
        }
        return playerMessage;
    }

    /**
     * 执行操作
     *
     * @param pool 视频类Map表
     * @description: Created by Boqin on 2016/11/7 9:37
     */
    public abstract void performAction(Map<String, IMediaPlayer> pool);

    /**
     * 执行之前的操作，比如更新状态
     *
     * @param statusMap 视频状态Map
     * @description: Created by Boqin on 2016/11/7 9:38
     */
    public abstract void statusBefore(Map<String, MediaStatusBean> statusMap);

    /**
     * 执行之后的操作，比如更新状态
     *
     * @param statusMap 视频状态Map
     * @description: Created by Boqin on 2016/11/7 9:38
     */
    public abstract void statusAfter(Map<String, MediaStatusBean> statusMap);

    /**
     * 发生到Handler处理
     *
     * @description: Created by Boqin on 2016/11/7 9:40
     */
    public void sendToTarget() {
        if (mMessage != null) {
            mMessage.sendToTarget();
        }
    }

    /**
     * 执行模板方法
     *
     * @param pool      视频类Map表
     * @param statusMap 视频状态Map
     * @description: Created by Boqin on 2016/11/7 9:40
     */
    final public void perform(
            @NonNull
                    Map<String, IMediaPlayer> pool,
            @NonNull
                    Map<String, MediaStatusBean> statusMap) {
        statusBefore(statusMap);
        performAction(pool);
        statusAfter(statusMap);
    }
}
