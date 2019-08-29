package cn.sxw.android.lib.camera.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import cn.sxw.android.lib.camera.R;
import cn.sxw.android.lib.camera.core.ZCameraView;
import cn.sxw.android.lib.camera.listener.CameraResultListener;
import cn.sxw.android.lib.camera.listener.ErrorListener;
import cn.sxw.android.lib.camera.listener.ZCameraListener;
import cn.sxw.android.lib.camera.util.FileUtil;
import cn.sxw.android.lib.camera.util.LogUtil;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public abstract class ZCameraActivity extends AppCompatActivity implements CameraResultListener {
    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;

    private ZCameraView mCameraView;
    private boolean isGranted = false;

    private @ZCameraView.ButtonState int mButtonState = ZCameraView.BUTTON_STATE_BOTH;

    // ************************************ RuntimePermissions ************************************
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void openCamera() {
        setContentView(R.layout.activity_z_camera);
        requestFullScreen();

        mCameraView = findViewById(R.id.ic_zcv_camera);

        if(getIntent() != null){
            mButtonState = getIntent().getIntExtra("button_state",ZCameraView.BUTTON_STATE_BOTH);
        }

        //设置视频保存路径
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "ZCamera");
        mCameraView.setFeatures(mButtonState);
        mCameraView.setTip("轻触拍照");
        mCameraView.setMediaQuality(ZCameraView.MEDIA_QUALITY_MIDDLE);
        // 设置错误监听
        mCameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                // 错误监听
                Toast.makeText(ZCameraActivity.this, "相机无法打开", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onAudioPermissionError() {
                Toast.makeText(ZCameraActivity.this, "请授予录音权限！", Toast.LENGTH_SHORT).show();
            }
        });
        //ZCameraView监听
        mCameraView.setZCameraListener(new ZCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("ZCameraView", "bitmap = " + bitmap.getWidth());
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isGranted && mCameraView != null) {
            mCameraView.onResume();
        }
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
            mCameraView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraView != null)
            mCameraView.onStop();
    }
}
