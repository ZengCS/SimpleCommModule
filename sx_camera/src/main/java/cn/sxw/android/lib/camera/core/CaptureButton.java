package cn.sxw.android.lib.camera.core;

import static cn.sxw.android.lib.camera.core.ZCameraView.BUTTON_STATE_BOTH;
import static cn.sxw.android.lib.camera.core.ZCameraView.BUTTON_STATE_ONLY_CAPTURE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.view.View;

import cn.sxw.android.lib.camera.listener.CaptureListener;
import cn.sxw.android.lib.camera.listener.RecordTimeListener;
import cn.sxw.android.lib.camera.util.CheckPermission;
import cn.sxw.android.lib.camera.util.LogUtil;

public class CaptureButton extends View {
    public static final int ACTION_PHOTO = 0x101;// 拍照
    public static final int ACTION_RECORD = 0x102;// 录视频
    public static final int ACTION_RECORD_FINISH = 0x103;// 停止录视频

    private int state;              //当前按钮状态
    private int mAction = ACTION_PHOTO;
    private int button_state = BUTTON_STATE_BOTH;// 按钮可执行的功能状态（拍照,录制,两者）

    public static final int STATE_IDLE = 0x001;        // 空闲状态
    public static final int STATE_PRESS = 0x002;       // 按下状态
    public static final int STATE_LONG_PRESS = 0x003;  // 长按状态
    public static final int STATE_RECORDER_ING = 0x004;// 录制状态
    public static final int STATE_BAN = 0x005;         // 禁止状态

    private float event_Y;  //Touch_Event_Down时候记录的Y值


    private Paint mPaint;

    private float strokeWidth;          //进度条宽度
    private int outside_add_size;       //长按外圆半径变大的Size
    private int inside_reduce_size;     //长安内圆缩小的Size

    //中心坐标
    private float center_X;
    private float center_Y;

    private float button_radius;            //按钮半径
    private float button_outside_radius;    //外圆半径
    private float button_inside_radius;     //内圆半径
    private int button_size;                //按钮大小

    private int duration;           //录制视频最大时间长度
    private long mRecordedTime;      //记录当前录制的时间

    private LongPressRunnable longPressRunnable;    //长按后处理的逻辑Runnable
    private CaptureListener captureListener;        //按钮回调接口
    private RecordCountDownTimer timer;             //计时器
    private RecordTimeListener recordTimeListener; // 时间回调

    public CaptureButton(Context context) {
        super(context);
    }

    public CaptureButton(Context context, int size) {
        super(context);
        this.button_size = size;
        button_radius = size / 2.0f;

        button_outside_radius = button_radius;
        button_inside_radius = button_radius * 0.75f;

        strokeWidth = size / 15f;
        outside_add_size = size / 5;
        inside_reduce_size = size / 8;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        longPressRunnable = new LongPressRunnable();

        state = STATE_IDLE;                //初始化为空闲状态
        button_state = BUTTON_STATE_BOTH;  //初始化按钮为可录制可拍照
        duration = CameraConfig.MAX_RECORD_DURATION;//默认最长录制时间为1分钟

        center_X = (button_size + outside_add_size * 2) / 2f;
        center_Y = (button_size + outside_add_size * 2) / 2f;

        RectF rectF = new RectF(
                center_X - (button_radius + outside_add_size - strokeWidth / 2),
                center_Y - (button_radius + outside_add_size - strokeWidth / 2),
                center_X + (button_radius + outside_add_size - strokeWidth / 2),
                center_Y + (button_radius + outside_add_size - strokeWidth / 2));

        timer = new RecordCountDownTimer(duration, 1000);    //录制定时器
    }

    public int getAction() {
        return mAction;
    }

    public void switchAction() {
        if (mAction == ACTION_PHOTO)
            mAction = ACTION_RECORD;
        else
            mAction = ACTION_PHOTO;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button_size + outside_add_size * 2, button_size + outside_add_size * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(CameraConfig.OUTSIDE_COLOR); //外圆（半透明灰色）
        canvas.drawCircle(center_X, center_Y, button_outside_radius, mPaint);

        mPaint.setColor(mAction == ACTION_PHOTO ? CameraConfig.INSIDE_COLOR : CameraConfig.INSIDE_RECORD_COLOR);  //内圆（拍照:白色,视频:红色）
        if (mAction == ACTION_RECORD_FINISH) {
            float rectSize = button_size * 0.25f;
            @SuppressLint("DrawAllocation")
            RectF rectF = new RectF(center_X - rectSize, center_Y - rectSize,
                    center_X + rectSize, center_X + rectSize);
            canvas.drawRoundRect(rectF, rectSize * 0.2f, rectSize * 0.2f, mPaint);
        } else {
            canvas.drawCircle(center_X, center_Y, button_inside_radius, mPaint);
        }

        //如果状态为录制状态，则绘制录制进度条
//        if (state == STATE_RECORDER_ING) {
//            mPaint.setColor(CameraConfig.PROGRESS_COLOR);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(strokeWidth);
//            canvas.drawArc(rectF, -90, progress, false, mPaint);
//        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                LogUtil.i("state = " + state);
//                if (event.getPointerCount() > 1 || state != STATE_IDLE)
//                    break;
//                event_Y = event.getY();     //记录Y值
//                state = STATE_PRESS;        //修改当前状态为点击按下
//
//                //判断按钮状态是否为可录制状态
//                if ((button_state == BUTTON_STATE_ONLY_RECORDER || button_state == BUTTON_STATE_BOTH))
//                    postDelayed(longPressRunnable, 500);    //同时延长500启动长按后处理的逻辑Runnable
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (captureListener != null
//                        && state == STATE_RECORDER_ING
//                        && (button_state == BUTTON_STATE_ONLY_RECORDER || button_state == BUTTON_STATE_BOTH)) {
//                    //记录当前Y值与按下时候Y值的差值，调用缩放回调接口
//                    captureListener.recordZoom(event_Y - event.getY());
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                // 根据当前按钮的状态进行相应的处理
//                handlerUnpressByState();
//                break;
//        }
//        return true;
//    }

    public void stopRecord() {
        timer.cancel(); //停止计时器
        recordEnd();    //录制结束
    }

    //当手指松开按钮时候处理的逻辑
    private void handlerUnpressByState() {
        removeCallbacks(longPressRunnable); //移除长按逻辑的Runnable
        //根据当前状态处理
        switch (state) {
            //当前是点击按下
            case STATE_PRESS:
                if (captureListener != null && (button_state == BUTTON_STATE_ONLY_CAPTURE || button_state ==
                        BUTTON_STATE_BOTH)) {
                    startCaptureAnimation();
                } else {
                    state = STATE_IDLE;
                }
                break;
            // 当前是长按状态
            case STATE_RECORDER_ING:
                timer.cancel(); //停止计时器
                recordEnd();    //录制结束
                break;
        }
    }

    //录制结束
    private void recordEnd() {
        if (captureListener != null) {
            if (mRecordedTime < CameraConfig.MIN_RECORD_DURATION)
                captureListener.recordShort(mRecordedTime);//回调录制时间过短
            else
                captureListener.recordEnd(mRecordedTime);  //回调录制结束
        }
        resetRecordAnim();  //重制按钮状态
    }

    //重制状态
    private void resetRecordAnim() {
        state = STATE_BAN;

        invalidate();
        // 还原按钮初始状态动画
        startRecordAnimation(
                button_outside_radius,
                button_radius,
                button_inside_radius,
                button_radius * 0.75f
        );
    }

    // 内圆动画
    public void startCaptureAnimation() {
        float inside_start = button_inside_radius;
        ValueAnimator inside_anim = ValueAnimator.ofFloat(inside_start, inside_start * 0.75f, inside_start);
        inside_anim.addUpdateListener(animation -> {
            button_inside_radius = (float) animation.getAnimatedValue();
            invalidate();
        });
        inside_anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //回调拍照接口
                captureListener.takePictures();
                state = STATE_BAN;
            }
        });
        inside_anim.setDuration(100);
        inside_anim.start();
    }

    //内外圆动画
    private void startRecordAnimation(float outside_start, float outside_end, float inside_start, float inside_end) {
        ValueAnimator outside_anim = ValueAnimator.ofFloat(outside_start, outside_end);
        ValueAnimator inside_anim = ValueAnimator.ofFloat(inside_start, inside_end);
        // 外圆动画监听
        outside_anim.addUpdateListener(animation -> {
            button_outside_radius = (float) animation.getAnimatedValue();
            invalidate();
        });
        // 内圆动画监听
        inside_anim.addUpdateListener(animation -> {
            button_inside_radius = (float) animation.getAnimatedValue();
            invalidate();
        });
        AnimatorSet set = new AnimatorSet();
        //当动画结束后启动录像Runnable并且回调录像开始接口
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //设置为录制状态
                if (state == STATE_LONG_PRESS) {
                    if (captureListener != null)
                        captureListener.recordStart();
                    state = STATE_RECORDER_ING;
                    timer.start();
                }
            }
        });
        set.playTogether(outside_anim, inside_anim);
        set.setDuration(100);
        set.start();
    }


    //更新进度条
    private void updateProgress(long millisUntilFinished) {
        mRecordedTime = duration - millisUntilFinished;
        if (recordTimeListener != null)
            recordTimeListener.onTimeChanged(mRecordedTime);
        invalidate();
    }

    //录制视频计时器
    private class RecordCountDownTimer extends CountDownTimer {
        RecordCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            LogUtil.d("onTick: millisUntilFinished = " + millisUntilFinished);
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            updateProgress(0);
            recordEnd();
        }
    }

    //长按线程
    private class LongPressRunnable implements Runnable {
        @Override
        public void run() {
            state = STATE_LONG_PRESS;   //如果按下后经过500毫秒则会修改当前状态为长按状态
            //没有录制权限
            if (CheckPermission.getRecordState() != CheckPermission.STATE_SUCCESS) {
                state = STATE_IDLE;
                if (captureListener != null) {
                    captureListener.recordError();
                    return;
                }
            }
            //启动按钮动画，外圆变大，内圆缩小
            startRecord();
        }
    }

    public void startRecord() {
        mAction = ACTION_RECORD_FINISH;
        invalidate();

        if (captureListener != null)
            captureListener.recordStart();
        state = STATE_RECORDER_ING;
        timer.start();
    }

    /**************************************************
     * 对外提供的API                     *
     **************************************************/

    //设置最长录制时间
    public void setDuration(int d) {
        this.duration = (int) (d * 1.007f);
        LogUtil.d("onTick: millisUntilFinished, this.duration = " + this.duration);
        timer = new RecordCountDownTimer(this.duration, 1000);    //录制定时器
    }

    //设置回调接口
    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }

    public void addRecordTimeListener(RecordTimeListener recordTimeListener) {
        this.recordTimeListener = recordTimeListener;
    }

    //设置按钮功能（拍照和录像）
    public void setButtonFeatures(int state) {
        this.button_state = state;
    }

    //是否空闲状态
    public boolean isIdle() {
        return state == STATE_IDLE ? true : false;
    }

    //设置状态
    public void resetState() {
        state = STATE_IDLE;
        if (mAction == ACTION_RECORD_FINISH) {
            mAction = ACTION_RECORD;
            //需要刷新按钮状态
            invalidate();
        }
    }
}
