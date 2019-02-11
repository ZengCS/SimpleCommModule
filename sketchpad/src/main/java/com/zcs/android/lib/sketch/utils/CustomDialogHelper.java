package com.zcs.android.lib.sketch.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zcs.android.lib.sketch.R;

/**
 * Created by ZengCS on 2017/7/31.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class CustomDialogHelper {
    /**
     * 创建一个自定义内容的Dialog
     *
     * @param context
     * @param customView
     * @return
     */
    public static AlertDialog showCustomDialog(Context context, View customView, boolean cancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        builder.setView(customView).setCancelable(cancelAble);
        return builder.show();
    }

}
