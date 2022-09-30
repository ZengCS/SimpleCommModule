package com.zhy.autolayout;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhy on 15/11/19.
 */
public class AutoLayoutActivity extends AppCompatActivity {
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT = "android.support.constraint.ConstraintLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT_X = "androidx.constraintlayout.widget.ConstraintLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (name.equals(LAYOUT_CONSTRAINTLAYOUT) || name.equals(LAYOUT_CONSTRAINTLAYOUT_X)) {
            // 增加AndroidX ConstraintLayout适配
            view = new AutoConstraintLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }
}
