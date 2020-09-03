package cn.sxw.android.lib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Random;

import cn.sxw.android.base.dialog.CustomDialogHelper;
import cn.sxw.android.base.utils.dialog.DialogParam;
import cn.sxw.android.lib.camera.ui.ZCameraActivity;
import cn.sxw.android.lib.mvp.ui.activity.EmptyActivity_;
import cn.sxw.android.lib.mvp.ui.activity.CameraDemoActivity;
import cn.sxw.android.lib.ui.PermissionActivity;
import cn.sxw.android.lib.ui.SketchPadActivity;
import cn.sxw.android.lib.ui.base.CustomBaseActivity;

public class MainActivity extends CustomBaseActivity {
    public static final int REQUEST_CODE_CAMERA = 0x001;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.id_iv_photo_result);
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
            Glide.with(this).load(filePath).into(mImageView);
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

    public void openZDialog(View view) {
        String msg = "大法官师大法官师大法官大法官师大法官师大法官个dfg 热图个dfg\n大法官大法官大法官\n\n大法官有金龟换酒过很久过很久光辉结核杆菌过很久\n1\n2\n3\n4\n5\n6\n7\n8\n9\n8\n7\n6\n5\n4\n3\n2\n1\n2\n3\n4\n5\n6\n7\n8\n9";
        CustomDialogHelper.DialogParam dialogParam = new CustomDialogHelper.DialogParam("作业内容", msg);
        dialogParam.setPositiveBtnText("我知道了");
        dialogParam.setCenterContent(false);
        dialogParam.setShowCloseIcon(true);

        Random random = new Random();
        if (random.nextBoolean()) {
            msg = "恭喜你测试成功。";
            dialogParam.setMessage(msg);
            dialogParam.setShowCloseIcon(true);
            dialogParam.setTitle("");
            dialogParam.setCenterContent(true);
        }
        CustomDialogHelper.showCustomMessageDialog(this, dialogParam, new CustomDialogHelper.NativeDialogCallback() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void openConfirmDialog(View view) {
        CustomDialogHelper.DialogParam dialogParam = new CustomDialogHelper.DialogParam("你确定要退出登录吗？");
        dialogParam.setCenterContent(true);
        dialogParam.setPositiveBtnText("退出");
        dialogParam.setNegativeBtnText("点错了");
        CustomDialogHelper.showCustomConfirmDialog(this, dialogParam, new CustomDialogHelper.NativeDialogCallback() {
            @Override
            public void onConfirm() {
                Toast.makeText(MainActivity.this, "你点了 确定", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
