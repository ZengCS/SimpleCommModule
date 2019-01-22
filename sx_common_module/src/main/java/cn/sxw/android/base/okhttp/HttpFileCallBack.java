package cn.sxw.android.base.okhttp;

import java.io.File;

/**
 * @author zcs@sxw.cn
 * @version v1.0
 * @date 2018/08/25 14:42
 */
public interface HttpFileCallBack extends HttpCallback<String, File> {
    void onProgress(long down, long length);
}
