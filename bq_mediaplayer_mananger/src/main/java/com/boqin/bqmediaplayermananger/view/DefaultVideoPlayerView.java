package com.boqin.bqmediaplayermananger.view;

import com.boqin.bqmediaplayermananger.manager.NEMediaPlayerManager;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

/**
 * 视频播放View
 * 需要设置path
 * Created by Boqin on 2017/3/8.
 * Modified by Boqin
 *
 * @Version
 */
public class DefaultVideoPlayerView extends ScalableTextureView implements TextureView.SurfaceTextureListener{

    private NEMediaPlayerManager mNEMediaPlayerManager;

    private String mPath;

    private boolean mSurfaceAvailable;
    private boolean mSurfaceAvailableCallback;

    private Surface mSurface;

    public DefaultVideoPlayerView(Context context) {
        super(context);
        init();
    }

    public DefaultVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurfaceAvailable = true;
        mSurface = new Surface(surface);
        if (mSurfaceAvailableCallback) {
            activate();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurfaceAvailable = false;
        setNeedsSurfaceAvailableCallback(false);
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public boolean isSurfaceAvailable() {
        return mSurfaceAvailable;
    }

    public void setPath(String path) {
        mPath = path;
        mNEMediaPlayerManager.initKey(path);
        mNEMediaPlayerManager.addSizeGotCallback(mPath, new NEMediaPlayerManager.OnSizeGotCallback() {
            @Override
            public void onVideoSizeGot(int width, int height) {
                NEMediaPlayerManager.VideoSize videoSize = mNEMediaPlayerManager.getVideoSize(mPath);
                if (videoSize == null) {
                    mNEMediaPlayerManager.addVideoSize(mPath, width, height);
                    videoSize = mNEMediaPlayerManager.getVideoSize(mPath);
                }
                setContentWidthHeight(videoSize.width, videoSize.height);
            }
        });
    }

    public Surface getSurface(){
        return mSurface;
    }

//    public void setSurface(Surface surface){
//        mSurface = surface;
//        mNEMediaPlayerManager.switchSurface();
//    }

    private void refreshSurface(){

    }

    private void init() {
        mNEMediaPlayerManager = NEMediaPlayerManager.getInstance();
        setSurfaceTextureListener(this);

    }

    public void activate(){
        if(isSurfaceAvailable()){
            mNEMediaPlayerManager.openVideo(mPath, mPath, mSurface, false, this.getContext());
        }else {
            setNeedsSurfaceAvailableCallback(true);
        }

    }

    public void pause(){
        mNEMediaPlayerManager.pause(mPath);
    }

    public void resume(){
        mNEMediaPlayerManager.resume(mPath);
    }

    public void release(){
        mNEMediaPlayerManager.release(mPath);
    }

    private void setNeedsSurfaceAvailableCallback(boolean isNeed){
        mSurfaceAvailableCallback = isNeed;
    }


}
