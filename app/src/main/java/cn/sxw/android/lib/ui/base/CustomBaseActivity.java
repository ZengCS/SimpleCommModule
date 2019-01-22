package cn.sxw.android.lib.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ZengCS on 2019/1/3.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public abstract class CustomBaseActivity extends AppCompatActivity {
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
