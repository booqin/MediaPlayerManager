package com.boqin.xgmediaplayermanager;

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
     * 获取系统dp尺寸密度值
     * @param context	应用程序上下文
     * @return
     */
    public static float getDensity(Context context){
        DisplayMetrics dm = getDisplayMetrics(context);

        if(dm != null) {
            return dm.density;
        }
        else{
            return 0;
        }
    }

    /**
     * 获取系统字体sp密度值
     * @param context	应用程序上下文
     * @return
     */
    public static float getScaledDensity(Context context){
        DisplayMetrics dm = getDisplayMetrics(context);

        if(dm != null) {
            return dm.scaledDensity;
        }
        else{
            return 0;
        }
    }

    /**
     * dip转pixel
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        if(context == null){
            return 0;
        }

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * pixel转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        if(context == null){
            return 0;
        }

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * pixel转dip
     * @param pxValue 像素
     * @return dp
     */
    public static int px2dip(float pxValue){
        return (int) (pxValue / 2 + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try{
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e){
            e.printStackTrace();
        }
        return statusHeight;
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

    /**
     * 获取屏幕分辨率
     * @return
     */
    public static String getResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager WM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        WM.getDefaultDisplay().getMetrics(dm);
        StringBuffer buffer = new StringBuffer();
        buffer.append(dm.widthPixels).append('x').append(dm.heightPixels);
        return buffer.toString();
    }
}
