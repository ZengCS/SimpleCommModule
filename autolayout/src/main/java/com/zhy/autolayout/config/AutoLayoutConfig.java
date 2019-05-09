package com.zhy.autolayout.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.zhy.autolayout.utils.L;
import com.zhy.autolayout.utils.ScreenUtils;

/**
 * Created by zhy on 15/11/18.
 */
public class AutoLayoutConfig {

    private static AutoLayoutConfig sInstance = new AutoLayoutConfig();


    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static final String KEY_DESIGN_HEIGHT = "design_height";
    private static final String KEY_DESIGN_WIDTH_FOR_PHONE = "design_width_phone";
    private static final String KEY_DESIGN_HEIGHT_FOR_PHONE = "design_height_phone";

    private int mScreenWidth;
    private int mScreenHeight;

    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize = false;
    private boolean useLandscape = false;
    private boolean forPhoneUsage = false;// 给手机用的


    private AutoLayoutConfig() {
    }

    public void checkParams() {
        if (mDesignHeight <= 0 || mDesignWidth <= 0) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.");
        }
    }

    public AutoLayoutConfig useDeviceSize() {
        L.e("useDeviceSize");
        useDeviceSize = true;
        return this;
    }

    public AutoLayoutConfig useLandscape() {
        L.e("useLandscape");
        useLandscape = true;
        return this;
    }

    public AutoLayoutConfig usePortrait() {
        L.e("usePortrait");
        useLandscape = false;
        return this;
    }

    public AutoLayoutConfig forPhoneUsage() {
        L.e("forPhoneUsage");
        forPhoneUsage = true;
        return this;
    }

    public static AutoLayoutConfig getInstance() {
        return sInstance;
    }


    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getDesignWidth() {
        return mDesignWidth;
    }

    public int getDesignHeight() {
        return mDesignHeight;
    }


    public void init(Context context) {
        if (forPhoneUsage) {
            getPhoneMetaData(context);
        } else {
            getMetaData(context);
        }

        int[] screenSize = ScreenUtils.getScreenSize(context, useDeviceSize);
        int size1 = screenSize[0];
        int size2 = screenSize[1];
        if (useLandscape) {// 使用横屏模式
            L.e("使用横屏模式");
            mScreenWidth = Math.max(size1, size2);
            mScreenHeight = Math.min(size1, size2);
        } else {// 竖屏
            L.e("使用竖屏模式");
            mScreenWidth = Math.min(size1, size2);
            mScreenHeight = Math.max(size1, size2);
        }
        L.e(" screenWidth =" + mScreenWidth + " ,screenHeight = " + mScreenHeight);
    }

    private void getPhoneMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH_FOR_PHONE);
                mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT_FOR_PHONE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH_FOR_PHONE + " and " + KEY_DESIGN_HEIGHT_FOR_PHONE + "  in your manifest file.", e);
        }

        L.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }

    private void getMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                int size1 = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                int size2 = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
                if (useLandscape) {// 使用横屏模式
                    L.e("使用横屏模式");
                    mDesignWidth = Math.max(size1, size2);
                    mDesignHeight = Math.min(size1, size2);
                } else {// 竖屏
                    L.e("使用竖屏模式");
                    mDesignWidth = Math.min(size1, size2);
                    mDesignHeight = Math.max(size1, size2);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
        }

        L.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }


}
