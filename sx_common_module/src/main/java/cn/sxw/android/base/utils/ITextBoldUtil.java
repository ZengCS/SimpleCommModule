package cn.sxw.android.base.utils;

import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

/**
 * @Description 中文字体加粗
 * @Author kk20
 * @Date 2017/5/24
 * @Version V1.0.0
 */
public class ITextBoldUtil {

    public static void bold(View view) {
        TextPaint textPaint = null;
        if (view instanceof TextView) {
            textPaint = ((TextView) view).getPaint();
        }
        if (textPaint != null) {
            textPaint.setFakeBoldText(true);
        }
    }

    public static void unBold(View view) {
        TextPaint textPaint = null;
        if (view instanceof TextView) {
            textPaint = ((TextView) view).getPaint();
        }
        if (textPaint != null) {
            textPaint.setFakeBoldText(false);
        }
    }
}
