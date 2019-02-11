package cn.sxw.android.lib.ui;

import android.app.Activity;
import android.os.Bundle;

import com.zcs.android.lib.sketch.view.SimpleSketchView;

public class BezierPenActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new MySurfaceView(this));
        setContentView(new SimpleSketchView(this));
        //setContentView(new DrawingWithoutBezier(this));      
    }
}