package com.zcs.android.lib.sketch.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 有关文本的一些工具类
 *
 * @author ZengCS 2014年5月19日17:16:04
 */
public class MyTextUtils {
    public static final int STRING = 0;
    public static final int E_MAIL = 1;
    public static final int WEBSITE = 2;
    public static final int TELEPHONE = 3;

    /**
     * 对指定位置的字符进行高亮显示
     *
     * @param src   源字符串
     * @param start 高亮起点
     * @param end   高亮终点
     * @param color 颜色
     * @return
     */
    public static SpannableString highlight(CharSequence src, int start, int end, int color) {
        SpannableString spannable = new SpannableString(src);
        try {
            CharacterStyle span = new ForegroundColorSpan(color);
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }

        return spannable;
    }

    public static SpannableString highlight(CharSequence src, String[] strs, int starts[], int ends[], int types[], int color) {
        SpannableString spannable = new SpannableString(src);

        for (int i = 0; i < starts.length; i++) {
            CharacterStyle span = null;
            switch (types[i]) {
                case E_MAIL:
                    span = new URLSpan("mailto:" + strs[i]);
                    break;
                case WEBSITE:
                    span = new URLSpan(strs[i]);
                    break;
                case TELEPHONE:
                    span = new URLSpan("tel:" + strs[i]);
                    break;
                default:
                    span = new ForegroundColorSpan(color);
                    break;
            }

            try {
                spannable.setSpan(span, starts[i], ends[i], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
            }
        }

        return spannable;
    }

    /**
     * 高亮指定字符串
     *
     * @param src    源字符串
     * @param target 需要高亮的字符串
     * @param color  高亮颜色
     * @return
     */
    public static SpannableString highlight(CharSequence src, String target, int color) {
        int idx = src.toString().indexOf(target);
        int start = 0;
        int end = 0;
        if (idx > -1) {
            start = idx;
            end = idx + target.length();
        }
        return highlight(src, start, end, color);
    }

    /**
     * @param src
     * @param target
     * @param color
     * @return
     */
    public static SpannableString highlight(CharSequence src, String[] target, int color) {
        int[] starts = new int[target.length];
        int[] ends = new int[target.length];
        int[] types = new int[target.length];
        String[] strs = new String[target.length];
        int c = 0;

        // added 2017年2月20日18:03:45
        // 解决字符串出现相同换色目标,导致只有第一个目标被换色
        String s = new String(src.toString());

        for (String temp : target) {
            strs[c] = temp;
            int idx = s.indexOf(temp);
            if (idx > -1) {
                if (c > 0) {
                    starts[c] = idx + ends[c - 1];
                } else {
                    starts[c] = idx;
                }
                ends[c] = starts[c] + temp.length();
                if (isEmail(temp)) {
                    types[c] = E_MAIL;
                } else if (isWebsite(temp)) {
                    types[c] = WEBSITE;
                } else if (isPhoneNumber(temp)) {
                    types[c] = TELEPHONE;
                } else {
                    types[c] = STRING;
                }
            }
            s = src.toString().substring(ends[c]);
            c++;
        }
        return highlight(src, strs, starts, ends, types, color);
    }

    /**
     * @param email
     * @return isEmail
     */
    public static boolean isEmail(String email) {
        String regex = "[a-zA-Z_0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,3}){1,3}";
        return email.matches(regex);
    }

    /**
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String strRegex = "[1][34578]\\d{9}";
        boolean result = str.matches(strRegex);
        return result;
    }

    /**
     * 判断是否是被允许的小组名称
     * 规则:名称只允许包含中文、英文大小写、数字、下划线和减号。长度2-16
     */
    public static boolean isAllowedGroupName(String name) {
        String validateStr = "^[\\w\\-_[0-9]\u4e00-\u9fa5]+$";
        return matcher(validateStr, name);
    }

    public static boolean isCharacterOnly(String str) {
        String validateStr = "^[a-zA-Z]+$";
        return matcher(validateStr, str);
    }

    /**
     * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号
     *
     * @param userName
     * @return
     * @author www.sangedabuliu.com
     */
    public static boolean validateUserName(String userName) {
        String validateStr = "^[\\w\\-－＿[0-9]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
        boolean rs = false;
        rs = matcher(validateStr, userName);
        return rs;
    }

    private static boolean matcher(String reg, String string) {
        boolean tem;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }

    /**
     * @param url
     * @return
     */
    public static boolean isWebsite(String url) {
        if (isEmpty(url)) {
            return false;
        } else if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www.")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param str
     * @return isEmpty
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().equals("") || str.trim().length() == 0 || str.trim().equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 替换回车
     */
    public static String replaceEnter(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        } else {
            return s.trim().replaceAll("\\n", "");
        }
    }

    /**
     * 获取名
     */
    public static String getLastName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "无";
        }
        int len = name.length();
        return name.substring(len - 1, len);
    }

    public static void hideSoftInput(View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftInput(View view) {
        try {
            if (view instanceof EditText) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                view.requestFocusFromTouch();
            }

            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
