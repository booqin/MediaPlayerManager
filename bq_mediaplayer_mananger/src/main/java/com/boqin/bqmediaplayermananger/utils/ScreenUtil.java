package com.boqin.bqmediaplayermananger.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕工具类，涉及到屏幕宽度、高度、密度比、(像素、dp、sp)之间的转换等
 * @author hzguojujun on 2015-12-18
 */
public class ScreenUtil {
    /**
     * 获取屏幕宽度，单位为px
     * @param context	应用程序上下文
     * @return 屏幕宽度，单位px
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = getDisplayMetrics(context);

        if(dm != null) {
            return dm.widthPixels;
        }
        else{
            return 0;
        }
    }

    /**
     * 获固定比例的高度
     * @param width 宽度值
     * @return 屏幕高度，单位px
     */
    public static int getMissScaleHeight(int width){
        return width*235/345;
    }

    /**
     * 获取屏幕高度，单位为px
     * @param context	应用程序上下文
     * @return 屏幕高度，单位px
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics dm = getDisplayMetrics(context);

        if(dm != null) {
            return dm.heightPixels;
        }
        else{
            return 0;
        }
    }

    /**
     * 获取DisplayMetrics对象
     * @param context	应用程序上下文
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        if(context == null){
            return null;
        }

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

}
