package cn.sxw.android.base.okhttp;

/**
 * Created by ZengCS on 2019/1/9.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface OkApiHelper {
    int METHOD_UNKNOWN = -1;
    int METHOD_GET = 0;
    int METHOD_POST = 1;
    int METHOD_PUT = 2;
    int METHOD_DELETE = 3;

    void sendPost(BaseRequest request);

    void sendGet(BaseRequest request);

    void sendPut(BaseRequest request);

    void sendDelete(BaseRequest request);
}
