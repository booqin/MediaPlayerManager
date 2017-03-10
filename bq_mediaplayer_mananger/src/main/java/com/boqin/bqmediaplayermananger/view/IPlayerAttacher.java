package com.boqin.bqmediaplayermananger.view;

/**
 * 播放操作接口，用于与VideoRecyclerViewAutoControlAttacher类进行解耦
 * Created by Boqin on 2016/11/8.
 * Modified by Boqin
 *
 * @Version
 */
public interface IPlayerAttacher {

    /**
     * 开始播放
     * @description: Created by Boqin on 2016/11/8 14:13
     */
    void activate();

    /**
     * 取消播放
     * @description: Created by Boqin on 2016/11/8 14:13
     */
    void deactivate();

    /**
     * 当进入Resume状态时视频的相关操作
     * @description: Created by Boqin on 2016/11/8 17:56
     * @param holderKey
     * @param isOnPauseWhenPlaying
     */
    void onResume(String holderKey, boolean isOnPauseWhenPlaying);

    /**
     * 当进入暂停状态时视频的相关操作
     * @description: Created by Boqin on 2016/11/8 17:56
     * @param holderKey
     * @return ture:对应key的视频在播放中进入该状态
     */
    boolean onPause(String holderKey);

    /**
     * 是否马上播放
     * @return true:立马播放
     * @description: Created by Boqin on 2016/11/8 14:13
     */
    boolean isNeedPlayNow();

    /**
     * 释放资源
     * @description: Created by Boqin on 2016/11/8 14:14
     */
    void release();

    /**
     * 获取播放器key值
     * @return 播放器ket
     * @description: Created by Boqin on 2016/11/8 14:14
     */
    String getPlayerKey();
}
