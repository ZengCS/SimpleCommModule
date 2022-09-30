package cn.sxw.android.lib.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zcs.android.lib.sketch.config.SketchConfig;
import com.zcs.android.lib.sketch.mvp.ui.SketchPadFragment;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.sxw.android.lib.BuildConfig;
import cn.sxw.android.lib.R;

public class SketchPadActivity extends AutoLayoutActivity implements View.OnClickListener {
    private SketchPadFragment mSketchPadFragment;

    private Button mBgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_pad);

        mSketchPadFragment = (SketchPadFragment) getSupportFragmentManager().findFragmentById(R.id.id_fragment_sketch_pad);
        mSketchPadFragment.setNeedDrawingCache(false);

        mBgBtn = findViewById(R.id.id_btn_set_bg);

        findViewById(R.id.id_btn_undo).setOnClickListener(this);
        findViewById(R.id.id_btn_redo).setOnClickListener(this);
        findViewById(R.id.id_btn_clear).setOnClickListener(this);
        findViewById(R.id.id_btn_save).setOnClickListener(this);
        findViewById(R.id.id_btn_set_bg).setOnClickListener(this);
        findViewById(R.id.id_btn_test_pen).setOnClickListener(this);
        findViewById(R.id.id_btn_bezier_pen).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_bezier_pen:// 贝塞尔画笔
                startActivity(new Intent(this, BezierPenActivity.class));
                break;
            case R.id.id_btn_test_pen:// 笔迹测试
                startActivity(new Intent(this, PenActivity.class));
                break;
            case R.id.id_btn_undo:
                mSketchPadFragment.sketchUndo();
                break;
            case R.id.id_btn_redo:
                mSketchPadFragment.sketchRedo();
                break;
            case R.id.id_btn_clear:
                mSketchPadFragment.sketchClear();
                break;
            case R.id.id_btn_save:
                checkPermission();
                break;
            case R.id.id_btn_set_bg:
                if (mBgBtn.getText().equals("设置背景")) {
                    // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sample_pic).copy(Bitmap.Config.ARGB_8888, true);
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sample_bg_special).copy(Bitmap.Config.ARGB_8888, true);
                    if (BuildConfig.DEBUG) {
                        mSketchPadFragment.addPicByBitmap(bitmap);
                        return;
                    }
                    mSketchPadFragment.sketchSetBg(bitmap);
                    mBgBtn.setText("清除背景");
                } else {
                    mSketchPadFragment.sketchSetBg(null);
                    mBgBtn.setText("设置背景");
                }
                break;
        }
    }

    /**
     * 保存图片
     */
    private void savePic() {
        String filePath = SketchConfig.SKETCH_SAVE_DIR + "sketch_" + System.currentTimeMillis() + ".jpg";
        mSketchPadFragment.sketchSaveJpg(filePath);
    }

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x001;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                savePic();
            }
        } else {
            savePic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePic();
            } else {
                // Permission Denied
                Toast.makeText(this, "授权失败，无法保存图片~", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
