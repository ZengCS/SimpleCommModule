package cn.sxw.android.base.okhttp;

/**
 * @author zcs@sxw.cn
 * @version v1.0
 * @date 2018/08/25 14:42
 */
public interface HttpCallback<T, V> {
    void onStart();

    void onResult(T req, V result);

    void onFail(T req, int code, String msg);

    void onFinish();
}
