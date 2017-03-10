package com.boqin.xgmediaplayermanager;


import com.boqin.bqmediaplayermananger.manager.NEMediaPlayerManager;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * Miss项目的Application，可以做一些控件的初始化，以及需要单例模式的对象
 * Created by yefeng on 2016/11/21.
 * Modified by yefeng
 *
 * @Version
 */
public class MissApplication extends Application {

    /** 产品版本 */
    public static String APP_VERSION;
    /** 设备ID */
    public static String DEVICE_ID;
    /** 通道 */
    public static String CHANNEL;

    /** 应用上下文 */
    private static Context AppContext;
    private static MissApplication MissApp;

    private NEMediaPlayerManager mPlayerManager;

    private Bitmap[] mPtrPullBitmaps = null;
    private Bitmap[] mPtrAnimBitmaps = null;

    /**
     * 获取应用Context
     *
     * @description: Created by lenghuo on 2016/11/3 20:23
     */
    public static Context getAppContext() {
        return AppContext;
    }

    /**
     * 获取App实例
     *
     * @description: Created by lenghuo on 2016/11/5 10:49
     */
    public static MissApplication getAppInstance() {
        return MissApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
        MissApp = this;

    }

    /**
     * 安卓中实现单例模式的一种方法
     * 获取播放器管理类
     *
     * @description: Created by lenghuo on 2016/11/5 10:46
     */
    public NEMediaPlayerManager getPlayerManager() {
        if (mPlayerManager == null) {
            mPlayerManager = NEMediaPlayerManager.getInstance();
        }
        return mPlayerManager;
    }



    public void finishApp() {
        releaseVideoPools();
    }

    public void releaseVideoPools() {

        if (mPlayerManager != null) {
            mPlayerManager.releasePool();
        }
        mPlayerManager = null;
    }
}
