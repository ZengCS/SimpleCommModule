package cn.sxw.android.lib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zcs.android.lib.sketch.pen.IPenConfig;
import com.zcs.android.lib.sketch.pen.view.NewDrawPenView;

import cn.sxw.android.lib.R;

/**
 * Created by ZengCS on 2018/9/30.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class PenActivity extends AppCompatActivity implements View.OnClickListener {
    private NewDrawPenView mDrawPenView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_demo_layout);
        findViews();
        doSomeThing();
    }

    private void doSomeThing() {
        findViewById(R.id.btn_chalk_pen).setOnClickListener(this);
        findViewById(R.id.btn_pencil).setOnClickListener(this);
        findViewById(R.id.btn_stroke_pen).setOnClickListener(this);
        findViewById(R.id.btn_brush_pen).setOnClickListener(this);
        findViewById(R.id.btn_clear_canvas).setOnClickListener(this);
        findViewById(R.id.btn_clear_undo).setOnClickListener(this);
        findViewById(R.id.btn_clear_redo).setOnClickListener(this);
    }

    private void findViews() {
        mDrawPenView = findViewById(R.id.draw_pen_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pencil:// 铅笔
                mDrawPenView.setCanvasCode(IPenConfig.STROKE_TYPE_PENCIL);
                break;
            case R.id.btn_stroke_pen:// 钢笔
                mDrawPenView.setCanvasCode(IPenConfig.STROKE_TYPE_PEN);
                break;
            case R.id.btn_clear_canvas:// 清空
                mDrawPenView.setCanvasCode(IPenConfig.STROKE_TYPE_ERASER);
                break;
            case R.id.btn_brush_pen:// 毛笔
                mDrawPenView.setCanvasCode(IPenConfig.STROKE_TYPE_BRUSH);
                break;
            case R.id.btn_chalk_pen:// 粉笔
                mDrawPenView.setCanvasCode(IPenConfig.STROKE_TYPE_CHALK);
                break;
            case R.id.btn_clear_undo:
                Toast.makeText(this, "撤销", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_clear_redo:
                mDrawPenView.redo();
                break;
        }
    }

}
