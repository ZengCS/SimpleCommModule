package cn.sxw.android.base.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScalableButton extends AppCompatButton implements View.OnTouchListener {
    private static final float SCALE = 0.95f;

    public ScalableButton(Context context) {
        this(context, null, 0);
    }

    public ScalableButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isClickable() && isFocusable()) {
            if (isClickable() && isFocusable()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下时,图片缩放
                    setScaleX(SCALE);
                    setScaleY(SCALE);
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                    setScaleX(1f);
                    setScaleY(1f);
                }
            }
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}