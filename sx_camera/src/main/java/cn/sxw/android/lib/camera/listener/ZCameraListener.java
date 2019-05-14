package cn.sxw.android.lib.camera.listener;

import android.graphics.Bitmap;

public interface ZCameraListener {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);

}
