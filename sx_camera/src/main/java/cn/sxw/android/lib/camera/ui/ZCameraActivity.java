package cn.sxw.android.lib.camera.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import cn.sxw.android.lib.camera.R;
import cn.sxw.android.lib.camera.core.CameraConfig;
import cn.sxw.android.lib.camera.core.CameraInterface;
import cn.sxw.android.lib.camera.core.ZCameraView;
import cn.sxw.android.lib.camera.listener.CameraResultListener;
import cn.sxw.android.lib.camera.listener.ErrorListener;
import cn.sxw.android.lib.camera.listener.ZCameraListener;
import cn.sxw.android.lib.camera.util.FileUtil;
import cn.sxw.android.lib.camera.util.LogUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;

//@RuntimePermissions
public abstract class ZCameraActivity extends AppCompatActivity implements CameraResultListener {
    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;

    public ZCameraView mCameraView;
    private boolean isGranted = false;

    private OrientationEventListener orientationEventListener;

    private int mRotation = 0;

    private void rotationUIListener() {
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                // 0,90,180,270
                int angle = rotation / 45;
                switch (angle) {
                    case 0:
                    case 7:
                    case 8:
                        mRotation = 0;
                        break;
                    case 1:
                    case 2:
                        mRotation = 90;
                        break;
                    case 3:
                    case 4:
                        mRotation = 180;
                        break;
                    case 5:
                    case 6:
                        mRotation = 270;
                        break;
                }
                CameraInterface.getInstance().setPhoneRotation(mRotation);
                // JLogUtil.d("onOrientationChanged called with rotation = " + rotation + " --> " + mRotation);
            }
        };
        orientationEventListener.enable();
    }

    /**
     * 设置按钮组合，默认拍照+视频
     */
    protected int configFeatureType() {
        return ZCameraView.BUTTON_STATE_BOTH;
    }

    /**
     * 是否仅启动拍照功能
     *
     * @return
     */
    protected boolean isOnlyCameraCapture() {
        return configFeatureType() == ZCameraView.BUTTON_STATE_ONLY_CAPTURE;
    }

    protected long configMaxRecordTimeMillis() {
        return CameraConfig.MAX_RECORD_DURATION;
    }

    // ************************************ RuntimePermissions ************************************
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void openCamera() {
        setContentView(R.layout.activity_z_camera);
        requestFullScreen();

        mCameraView = findViewById(R.id.ic_zcv_camera);

        //设置视频保存路径
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "ZCamera");
        mCameraView.setFeatures(configFeatureType());
        mCameraView.setDuration(configMaxRecordTimeMillis());
        mCameraView.setMediaQuality(ZCameraView.MEDIA_QUALITY_MIDDLE);
        // 设置错误监听
        mCameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                runOnUiThread(()->{
                    // 错误监听
                    Toast.makeText(ZCameraActivity.this, "相机无法打开", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onAudioPermissionError() {
                runOnUiThread(()->{
                    Toast.makeText(ZCameraActivity.this, "请授予录音权限！", Toast.LENGTH_SHORT).show();
                });
            }
        });
        //ZCameraView监听
        mCameraView.setZCameraListener(new ZCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                LogUtil.i("ZCameraView", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("ZCamera", bitmap);
                LogUtil.d("图片保存成功:" + path);
                onPhotoResult(path);
            }

            @Override
            public void recordSuccess(String path, Bitmap firstFrame) {
                LogUtil.d("视频保存成功:" + path);
                onRecordResult(path);
            }
        });
        mCameraView.setLeftClickListener(() -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        isGranted = true;
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void deniedForCamera() {
        onDenied();
        // Toast.makeText(this, "为保证相机的正常运行，请授予指定权限。", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void neverAskForCamera() {
        onNeverAsk();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZCameraActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void requestPermissionAgain() {
        ZCameraActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
    }

    // ************************************ RuntimePermissions ************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 申请权限
        ZCameraActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
        rotationUIListener();
    }

    private void requestFullScreen() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        // 设置常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isGranted && mCameraView != null) {
            mCameraView.onResume(isFirst);
        }
        isFirst = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestFullScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isGranted && mCameraView != null)
            mCameraView.onPause(isFinishing());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraView != null){
            mCameraView.onStop(isFinishing());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraView != null){
            mCameraView.onDestroy();
        }
        try {
            if (orientationEventListener != null) {
                orientationEventListener.disable();
                orientationEventListener = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
