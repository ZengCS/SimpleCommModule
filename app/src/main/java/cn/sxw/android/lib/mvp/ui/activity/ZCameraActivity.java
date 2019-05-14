package cn.sxw.android.lib.mvp.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import cn.sxw.android.lib.R;
import cn.sxw.android.lib.camera.core.ZCameraView;
import cn.sxw.android.lib.camera.listener.ErrorListener;
import cn.sxw.android.lib.camera.listener.ZCameraListener;
import cn.sxw.android.lib.camera.util.FileUtil;
import cn.sxw.android.lib.camera.util.LogUtil;

public class ZCameraActivity extends AppCompatActivity {
    private final int GET_PERMISSION_REQUEST = 100; // 权限申请自定义码
    private ZCameraView mCameraView;
    private boolean granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_camera);

        mCameraView = findViewById(R.id.ic_zcv_camera);

        //设置视频保存路径
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        mCameraView.setFeatures(ZCameraView.BUTTON_STATE_BOTH);
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
        mCameraView.setJCameraListener(new ZCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("ZCameraView", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                LogUtil.d("图片保存成功:" + path);
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                LogUtil.d("视频保存成功:" + url);
            }
        });
        mCameraView.setLeftClickListener(ZCameraActivity.this::finish);
        //6.0动态权限获取
        getPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted && mCameraView != null) {
            mCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null)
            mCameraView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraView != null)
            mCameraView.onStop();
    }

    /**
     * 获取权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                granted = true;
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(ZCameraActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    mCameraView.onResume();
                } else {
                    Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
