package cn.sxw.android.base.utils.dialog;

import android.text.TextUtils;

/**
 * Created by ZengCS on 2017/7/20.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class DialogParam {
    private String title;
    private String msg;
    private String confirmBtnText;
    private String cancelBtnText;
    private DialogCallback callback;

    public DialogParam(String msg) {
        this.msg = msg;
    }

    public DialogParam setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogParam setConfirmBtnText(String confirmBtnText) {
        this.confirmBtnText = confirmBtnText;
        return this;
    }

    public DialogParam setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
        return this;
    }

    public DialogParam setCallback(DialogCallback callback) {
        this.callback = callback;
        return this;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title)) {
            title = "提示信息";
        }
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public String getConfirmBtnText() {
        return confirmBtnText;
    }

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public DialogCallback getCallback() {
        return callback;
    }
}
