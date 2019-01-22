package cn.sxw.android.base.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.util.TypedValue;

/**
 * Created by ZengCS on 2016/7/21.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class AndroidFrameworkUtil {
    public static int getSelectableItemBackground(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        return outValue.resourceId;
    }

    public static ColorStateList getColorByXml(Context context, int xmlId) {
        try {
            // TODO 初始化颜色
            XmlResourceParser xrp1 = context.getResources().getXml(xmlId);
            return ColorStateList.createFromXml(context.getResources(), xrp1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
