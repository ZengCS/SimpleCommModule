package cn.sxw.android.lib.ui;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import cn.sxw.android.base.dialog.CustomDialogHelper;
import cn.sxw.android.lib.R;
import cn.sxw.android.lib.ui.base.CustomBaseActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by ZengCS on 2019/1/3.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 * <p>
 * 权限申请测试界面
 */
@RuntimePermissions
public class PermissionActivity extends CustomBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    public void openCamera(View view) {
        PermissionActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void openCamera() {
        Toast.makeText(this, "已具备访问相机的权限，可以在这里编写具体代码。", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void deniedForCamera() {
        Toast.makeText(this, "申请相机权限时被拒绝了。", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void neverAskForCamera() {
        String mAppName = getString(R.string.app_name);
        // 申请权限时被拒绝了，并且不再询问。
        CustomDialogHelper.DialogParam dialogParam = new CustomDialogHelper.DialogParam();
        dialogParam.setTitle("授权被拒绝");
        dialogParam.setMessage(getString(R.string.permission_tips_never_ask, mAppName, mAppName, "[存储/手机信息]"));
        dialogParam.setPositiveBtnText("应用设置");
        dialogParam.setNegativeBtnText("退出");
        CustomDialogHelper.showCustomConfirmDialog(this, dialogParam, new CustomDialogHelper.NativeDialogCallback() {
            @Override
            public void onConfirm() {
                openAppDetailSettings();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }
}
