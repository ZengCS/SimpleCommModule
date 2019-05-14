package cn.sxw.android.lib.camera.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Calendar;

import cn.sxw.android.lib.camera.core.CameraApp;
import cn.sxw.android.lib.camera.listener.IActivityLifeCycle;

/**
 * 加速度控制器  用来控制对焦
 */
public class SensorController implements IActivityLifeCycle, SensorEventListener {
    private static final String TAG = "SensorController";
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int mX, mY, mZ;
    private long lastStaticStamp = 0;

    private boolean isFocusing = false;
    private boolean canFocusIn = false;  //内部是否能够对焦控制机制
    private boolean canFocus = false;

    private static final int DELAY_DURATION = 500;

    private static final int STATUS_NONE = 0;
    private static final int STATUS_STATIC = 1;
    private static final int STATUS_MOVE = 2;
    private int STATUE = STATUS_NONE;

    private CameraFocusListener mCameraFocusListener;

    private static SensorController mInstance;

    private int focusing = 1;  //1 表示没有被锁定 0表示被锁定

    private SensorController() {
        Context context = CameraApp.getApp();
        if (context == null)
            throw new NullPointerException("Have you init the CameraApp");
        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        if (mSensorManager != null)
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
    }

    public static SensorController getInstance() {
        if (mInstance == null) {
            mInstance = new SensorController();
        }
        return mInstance;
    }

    public SensorController setCameraFocusListener(CameraFocusListener mCameraFocusListener) {
        this.mCameraFocusListener = mCameraFocusListener;
        return this;
    }

    @Override
    public void onStart() {
        restParams();
        canFocus = true;
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (mCameraFocusListener != null)// 第一次自动对焦
            mCameraFocusListener.onFocus();
    }

    @Override
    public void onStop() {
        mSensorManager.unregisterListener(this, mSensor);
        canFocus = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (isFocusing) {
            restParams();
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            Calendar mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis();// 1393844912

            if (STATUE != STATUS_NONE) {
                int px = Math.abs(mX - x);
                int py = Math.abs(mY - y);
                int pz = Math.abs(mZ - z);
                // Log.d(TAG, "pX:" + px + "  pY:" + py + "  pZ:" + pz + "    stamp:" + stamp + "  second:" + second);
                double value = Math.sqrt(px * px + py * py + pz * pz);
                if (value > 1.4) {
                    // 检测手机在移动..
                    Log.i(TAG, "mobile moving");
                    STATUE = STATUS_MOVE;
                } else {
                    // 检测手机静止..
                    // Log.i(TAG, "mobile static");
                    //上一次状态是move，记录静态时间点
                    if (STATUE == STATUS_MOVE) {
                        lastStaticStamp = stamp;
                        canFocusIn = true;
                    }

                    if (canFocusIn) {
                        if (stamp - lastStaticStamp > DELAY_DURATION) {
                            //移动后静止一段时间，可以发生对焦行为
                            if (!isFocusing) {
                                canFocusIn = false;
                                if (mCameraFocusListener != null) {
                                    mCameraFocusListener.onFocus();
                                }
                                Log.i(TAG, "mobile focusing");
                            }
                        }
                    }

                    STATUE = STATUS_STATIC;
                }
            } else {
                lastStaticStamp = stamp;
                STATUE = STATUS_STATIC;
            }

            mX = x;
            mY = y;
            mZ = z;
        }
    }

    private void restParams() {
        STATUE = STATUS_NONE;
        canFocusIn = false;
        mX = 0;
        mY = 0;
        mZ = 0;
    }

    /**
     * 对焦是否被锁定
     *
     * @return
     */
    public boolean isFocusLocked() {
        if (canFocus) {
            return focusing <= 0;
        }
        return false;
    }

    /**
     * 锁定对焦
     */
    public void lockFocus() {
        isFocusing = true;
        focusing--;
        Log.i(TAG, "lockFocus");
    }

    /**
     * 解锁对焦
     */
    public void unlockFocus() {
        isFocusing = false;
        focusing++;
        Log.i(TAG, "unlockFocus");
    }

    public void restFoucs() {
        focusing = 1;
    }

    public interface CameraFocusListener {
        void onFocus();
    }
}