package cn.sxw.android.lib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.sxw.android.lib.camera.ui.ZCameraActivity;
import cn.sxw.android.lib.mvp.ui.activity.EmptyActivity_;
import cn.sxw.android.lib.mvp.ui.activity.CameraDemoActivity;
import cn.sxw.android.lib.ui.PermissionActivity;
import cn.sxw.android.lib.ui.SketchPadActivity;
import cn.sxw.android.lib.ui.base.CustomBaseActivity;

public class MainActivity extends CustomBaseActivity {
    public static final int REQUEST_CODE_CAMERA = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            String filePath = data.getStringExtra("filePath");
            int type = data.getIntExtra("fileType", -1);
            String typeName = "未知";
            if (type == ZCameraActivity.TYPE_PICTURE) {
                typeName = "图片";
            } else if (type == ZCameraActivity.TYPE_VIDEO) {
                typeName = "视频";
            }
            Toast.makeText(this, "filePath = [" + typeName + "]" + filePath, Toast.LENGTH_SHORT).show();
        }
    }

    public void openZCamera(View view) {
        Intent intent = new Intent(this, CameraDemoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
        // startActivity(intent);
    }

    public void openPermissionActivity(View view) {
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
    }

    public void openEmptyActivity(View view) {
        Intent intent = new Intent(this, EmptyActivity_.class);
        startActivity(intent);
    }

    public void openSketchActivity(View view) {
        Intent intent = new Intent(this, SketchPadActivity.class);
        startActivity(intent);
    }
}
