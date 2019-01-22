package cn.sxw.android.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.sxw.android.HelloWorld;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String helloWorld = HelloWorld.getHelloWorld();
        Log.d(TAG, "onCreate: helloWorld = " + helloWorld);
    }
}
