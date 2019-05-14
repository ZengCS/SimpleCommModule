package cn.sxw.android.lib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.sxw.android.lib.mvp.ui.activity.EmptyActivity_;
import cn.sxw.android.lib.mvp.ui.activity.ZCameraActivity;
import cn.sxw.android.lib.ui.PermissionActivity;
import cn.sxw.android.lib.ui.SketchPadActivity;
import cn.sxw.android.lib.ui.base.CustomBaseActivity;

public class MainActivity extends CustomBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openZCamera(View view){
        Intent intent = new Intent(this, ZCameraActivity.class);
        startActivity(intent);
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
