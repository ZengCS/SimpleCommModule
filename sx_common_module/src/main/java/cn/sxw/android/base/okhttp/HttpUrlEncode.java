package cn.sxw.android.base.okhttp;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.HttpUrl;

/**
 * Created by ZengCS on 2019/1/10.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class HttpUrlEncode {
    /**
     * @param scheme   must be http or https
     * @param host     sxw.cn
     * @param api      a/b/c/d/e
     * @param paramMap 请求参数
     * @return
     */
    public static String encode(String scheme, String host, String api, ConcurrentHashMap<String, String> paramMap) {
        if (!TextUtils.isEmpty(api) && api.startsWith("/"))
            api = api.substring(1, api.length());

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .addPathSegments(api);
        if (paramMap != null && paramMap.size() > 0) {// 组装url完整地址
            for (String key : paramMap.keySet()) {
                urlBuilder.addQueryParameter(key, paramMap.get(key));
            }
        }
        return urlBuilder.build().toString();
    }

    /**
     * @param url      完整的url路径，http://sxw.cn/api2/123/test/login
     * @param paramMap 请求参数
     * @return
     */
    public static String encode(String url, ConcurrentHashMap<String, String> paramMap) {
        try {
            if (TextUtils.isEmpty(url))
                return "";
            String scheme, host, api;
            if (url.startsWith("http:")) {
                scheme = "http";
                url = url.substring("http://".length(), url.length());
                int index = url.indexOf("/");
                if (index < 0)
                    index = url.length();
                host = url.substring(0, index);
                api = url.substring(index, url.length());
                return encode(scheme, host, api, paramMap);
            } else if (url.startsWith("https:")) {
                scheme = "https";
                url = url.substring("https://".length(), url.length());
                int index = url.indexOf("/");
                if (index < 0)
                    index = url.length();
                host = url.substring(0, index);
                api = url.substring(index, url.length());
                return encode(scheme, host, api, paramMap);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
