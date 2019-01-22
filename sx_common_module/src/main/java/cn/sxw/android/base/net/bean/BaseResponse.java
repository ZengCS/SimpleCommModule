package cn.sxw.android.base.net.bean;

import android.text.TextUtils;

public class BaseResponse {
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_SUCCESS_0 = 0;// 升级平台成功的返回值是0

    /**
     * 判断请求是否成功
     */
    public boolean isRequestSuccess() {
        return (success && (code == CODE_SUCCESS || code == CODE_SUCCESS_0))
                || (code == CODE_SUCCESS_0 && !TextUtils.isEmpty(data));
    }

    public boolean isFormatError() {
        return TextUtils.isEmpty(errorData) &&
                TextUtils.isEmpty(md5) &&
                TextUtils.isEmpty(alg) &&
                code == 0 &&
                TextUtils.isEmpty(message) &&
                !success &&
                TextUtils.isEmpty(data);
    }

    private String errorData;
    private String md5;
    private String alg;
    private int code;
    private String message;
    private boolean success;
    private String data;

    public int getCode() {
        return code;
    }

    public String getAlg() {
        return alg;
    }

    public String getData() {
        return data;
    }

    public String getErrorData() {
        return errorData;
    }

    public String getMd5() {
        return md5;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
