package cn.sxw.android.lib.camera.listener;

/**
 * Created by ZengCS on 2019/5/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public interface CameraResultListener {
    void onDenied();

    void onNeverAsk();

    void requestPermissionAgain();

    void onPhotoResult(String filePath);

    void onRecordResult(String filePath);
}
