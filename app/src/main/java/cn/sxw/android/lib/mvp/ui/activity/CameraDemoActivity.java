package cn.sxw.android.lib.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import cn.sxw.android.base.dialog.CustomDialogHelper;
import cn.sxw.android.lib.R;
import cn.sxw.android.lib.camera.core.ZCameraView;
import cn.sxw.android.lib.camera.ui.ZCameraActivity;

public class CameraDemoActivity extends ZCameraActivity {
    @Override
    protected int btnFeatureType() {
        // return ZCameraView.BUTTON_STATE_BOTH;
        // return ZCameraView.BUTTON_STATE_ONLY_CAPTURE;
        return ZCameraView.BUTTON_STATE_ONLY_RECORDER;
    }

    @Override
    protected long recordMaxTimeMillis() {
        return 30_000;// 30 秒
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDenied() {
        requestPermissionAgain();
    }

    @Override
    public void onNeverAsk() {
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

    @Override
    public void onPhotoResult(String filePath) {
        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.putExtra("fileType", ZCameraActivity.TYPE_PICTURE);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRecordResult(String filePath) {
        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.putExtra("fileType", ZCameraActivity.TYPE_VIDEO);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected boolean isOpenAppSettings = false;

    /**
     * 打开应用程序设置界面
     */
    protected void openAppDetailSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }
}
