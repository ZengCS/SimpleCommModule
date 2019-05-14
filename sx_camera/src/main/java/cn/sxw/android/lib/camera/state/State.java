package cn.sxw.android.lib.camera.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import cn.sxw.android.lib.camera.core.CameraInterface;

public interface State {

    void start(SurfaceHolder holder, float screenProp);

    void stop();

    void focus(float x, float y, CameraInterface.FocusCallback callback);

    void switchCamera(SurfaceHolder holder, float screenProp);

    void restart();

    void capture();

    void record(Surface surface, float screenProp);

    void stopRecord(boolean isShort, long time);

    void cancel(SurfaceHolder holder, float screenProp);

    void confirm();

    void zoom(float zoom, int type);

    void flash(String mode);
}
