package com.boqin.bqmediaplayermananger.decoder;

import com.netease.neliveplayer.NELivePlayer;

/**
 * TODO
 * Created by Boqin on 2017/3/9.
 * Modified by Boqin
 *
 * @Version
 */
public interface IMediaPlayerListener {

    void onBufferingUpdate(IMediaPlayer var1, int var2);

    void onCompletion(IMediaPlayer var1);

    boolean onError(IMediaPlayer var1, int var2, int var3);

    boolean onInfo(IMediaPlayer var1, int var2, int var3);

    void onPrepared(IMediaPlayer var1);

    void onSeekComplete(IMediaPlayer var1);

    void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3, int var4, int var5);

}
