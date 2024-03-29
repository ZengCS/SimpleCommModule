package cn.sxw.android.lib.camera.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.sxw.android.lib.camera.R;
import cn.sxw.android.lib.camera.listener.CaptureListener;
import cn.sxw.android.lib.camera.listener.ClickListener;
import cn.sxw.android.lib.camera.listener.ErrorListener;
import cn.sxw.android.lib.camera.listener.TypeListener;
import cn.sxw.android.lib.camera.listener.ZCameraListener;
import cn.sxw.android.lib.camera.sensor.SensorController;
import cn.sxw.android.lib.camera.state.CameraMachine;
import cn.sxw.android.lib.camera.util.FileUtil;
import cn.sxw.android.lib.camera.util.LogUtil;
import cn.sxw.android.lib.camera.util.ScreenUtils;

public class ZCameraView extends FrameLayout implements CameraInterface.CameraOpenOverCallback, SurfaceHolder.Callback, CameraView {
    private static final String TAG = "ZCameraView";

    // Camera状态机
    private CameraMachine mCameraMachine;
    private boolean hasFlashLight, hasFrontCamera;

    // 闪关灯状态
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;
    private int mTypeFlash = TYPE_FLASH_OFF;

    // 拍照浏览时候的类型
    public static final int TYPE_PICTURE = 0x001;
    public static final int TYPE_VIDEO = 0x002;
    public static final int TYPE_SHORT = 0x003;
    public static final int TYPE_DEFAULT = 0x004;

    // 录制视频比特率
    public static final int MEDIA_QUALITY_HIGH = 20 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 16 * 100000;
    public static final int MEDIA_QUALITY_LOW = 12 * 100000;
    public static final int MEDIA_QUALITY_POOR = 8 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 4 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 2 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 1 * 80000;


    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;      // 只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;     // 只能录像
    public static final int BUTTON_STATE_BOTH = 0x103;              // 两者都可以

    @IntDef({BUTTON_STATE_ONLY_CAPTURE, BUTTON_STATE_ONLY_RECORDER, BUTTON_STATE_BOTH})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ButtonState {
    }

    // 回调监听
    private ZCameraListener zCameraListener;
    private ClickListener leftClickListener;
    private ClickListener rightClickListener;

    private Context mContext;
    private VideoView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private ImageView mFlashLamp;
    private CaptureLayout mCaptureLayout;
    private FocusView mFocusView;
    private MediaPlayer mMediaPlayer;

    private int layout_width;
    private float screenProp = 0f;

    private Bitmap captureBitmap;   //捕获的图片
    private Bitmap firstFrame;      //第一帧图片
    private String videoUrl;        //视频URL


    //切换摄像头按钮的参数
    private int iconSize = 0;       //图标大小
    private int iconMargin = 0;     //右上边距
    private int iconSrc = 0;        //图标资源
    private int iconLeft = 0;       //左图标
    private int iconRight = 0;      //右图标
    private int duration = 0;       //录制时间

    //缩放梯度
    private int zoomGradient = 0;

    private boolean firstTouch = true;
    private float firstTouchLength = 0;

    public boolean isShowingConfirm = false; //当前是否处于拍照-拍完状态
    public boolean isRecording = false;

    private Handler handler = new Handler();

    public ZCameraView(Context context) {
        this(context, null);
    }

    public ZCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //get AttributeSet
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZCameraView, defStyleAttr, 0);
        iconSize = a.getDimensionPixelSize(R.styleable.ZCameraView_iconSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()));
        iconMargin = a.getDimensionPixelSize(R.styleable.ZCameraView_iconMargin, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        iconSrc = a.getResourceId(R.styleable.ZCameraView_iconSrc, R.drawable.ic_camera);
        iconLeft = a.getResourceId(R.styleable.ZCameraView_iconLeft, 0);
        iconRight = a.getResourceId(R.styleable.ZCameraView_iconRight, 0);
        duration = a.getInteger(R.styleable.ZCameraView_duration_max, CameraConfig.MAX_RECORD_DURATION);       //没设置默认为60s
        a.recycle();
        initData();

        hasFlashLight = mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        hasFrontCamera = mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        initView();
    }

    /**
     * @param dms 单位:ms
     */
    public void setDuration(long dms) {
        if (dms <= 0 || dms > CameraConfig.MAX_RECORD_DURATION) {
            dms = CameraConfig.MAX_RECORD_DURATION;
        }
        this.duration = (int) dms;
        mCaptureLayout.setDuration(this.duration);
    }

    /**
     * 获取相机焦点
     */
    private void requestCameraFocus() {
        SensorController.getInstance().lockFocus();
        int x = ScreenUtils.getScreenWidth(mContext) / 2;
        int y = ScreenUtils.getScreenHeight(mContext) / 2;
        LogUtil.d(TAG, "onFocus called");
        CameraInterface.getInstance().handleFocus(mCameraMachine.getContext(), x, y, () -> {
            // 自动对焦成功
            LogUtil.d(TAG, "focusSuccess called");
            SensorController.getInstance().unlockFocus();
        });
    }

    private void initData() {
        layout_width = ScreenUtils.getScreenWidth(mContext);
        // 缩放梯度
        zoomGradient = (int) (layout_width / 16f);
        LogUtil.i("zoom = " + zoomGradient);
        mCameraMachine = new CameraMachine(getContext(), this, this);
    }

    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.camera_view, this);
        mVideoView = view.findViewById(R.id.video_preview);
        mPhoto = view.findViewById(R.id.image_photo);
        mSwitchCamera = view.findViewById(R.id.image_switch);
        mSwitchCamera.setImageResource(iconSrc);
        mFlashLamp = view.findViewById(R.id.image_flash);
//        if (hasFlashLight) {
//            setFlashRes();
//            mFlashLamp.setOnClickListener(v -> {
//                mTypeFlash++;
//                if (mTypeFlash > 0x023)
//                    mTypeFlash = TYPE_FLASH_AUTO;
//                setFlashRes();
//            });
//        } else {// 你的设备没有闪光灯
//            mFlashLamp.setVisibility(INVISIBLE);
//        }

        mCaptureLayout = view.findViewById(R.id.capture_layout);
        mCaptureLayout.setDuration(duration);
        mCaptureLayout.setIconSrc(iconLeft, iconRight);
        mFocusView = view.findViewById(R.id.id_focus_view);
        //移到onResume中注册
        mVideoView.getHolder().addCallback(this);
        if (hasFrontCamera) {
            //切换摄像头
            mSwitchCamera.setOnClickListener(v -> {
                mCameraMachine.switchCamera(mVideoView.getHolder(), screenProp);
                // 每次切换摄像头后，刷新对焦
                autoFocusDelay();
            });
        } else {
            mSwitchCamera.setVisibility(INVISIBLE);
        }

        //拍照 录像
        mCaptureLayout.setCaptureListener(new CaptureListener() {
            @Override
            public void takePictures() {
                isShowingConfirm = true;
                mSwitchCamera.setVisibility(INVISIBLE);
                // mFlashLamp.setVisibility(INVISIBLE);
                mCameraMachine.capture();
            }

            @Override
            public void recordStart() {
                mSwitchCamera.setVisibility(INVISIBLE);
                // mFlashLamp.setVisibility(INVISIBLE);
                mCameraMachine.record(mVideoView.getHolder().getSurface(), screenProp);
                isRecording = true;
            }

            @Override
            public void recordShort(final long time) {
                isRecording = false;
                mCaptureLayout.setTextWithAnimation("录制时间过短");
                mSwitchCamera.setVisibility(hasFrontCamera ? VISIBLE : INVISIBLE);
                // mFlashLamp.setVisibility(hasFlashLight ? VISIBLE : INVISIBLE);
                postDelayed(() -> mCameraMachine.stopRecord(true, time), 1500 - time);
            }

            @Override
            public void recordEnd(long time) {
                isShowingConfirm = true;
                isRecording = false;
                mCameraMachine.stopRecord(false, time);
            }

            @Override
            public void recordZoom(float zoom) {
                LogUtil.i("recordZoom");
                mCameraMachine.zoom(zoom, CameraInterface.TYPE_RECORDER);
            }

            @Override
            public void recordError() {
                if (errorListener != null) {
                    errorListener.onAudioPermissionError();
                }
            }
        });
        //确认 取消
        mCaptureLayout.setTypeListener(new TypeListener() {
            @Override
            public void cancel() {
                isShowingConfirm = false;
                mCameraMachine.cancel(mVideoView.getHolder(), screenProp);
            }

            @Override
            public void confirm() {
                isShowingConfirm = false;
                mCameraMachine.confirm();
            }
        });
        mCaptureLayout.setLeftClickListener(() -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = mVideoView.getMeasuredWidth();
        float heightSize = mVideoView.getMeasuredHeight();
        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(mVideoView.getHolder(), screenProp);
    }

    //生命周期onResume
    public void onResume(boolean isFirst) {
        LogUtil.i("ZCameraView onResume");
        isActivityPause = false;
        //已经拍了照或录了视频时，需要保留原来状态，这里不执行任何操作
        if (isShowingConfirm){
            //移到onResume中注册
            mVideoView.getHolder().addCallback(this);
            return;
        }
        resetState(TYPE_DEFAULT); //重置状态
        // CameraInterface.getInstance().registerSensorManager(mContext);
        CameraInterface.getInstance().setSwitchView(mSwitchCamera, mFlashLamp);
        if (!isFirst) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CameraInterface.PRETEND_SWITCH_CAMERA = true;
                    mCameraMachine.switchCamera(mVideoView.getHolder(), screenProp);
                    // 每次切换摄像头后，刷新对焦
                    autoFocusDelay();
                }
            }, 500);
        }else {
            mCameraMachine.start(mVideoView.getHolder(), screenProp);
        }
    }

    private void autoFocusDelay() {
        // 延时500毫秒进行自动对焦操作
        handler.postDelayed(() -> {
            // Toast.makeText(mContext, "开始自动对焦...", Toast.LENGTH_SHORT).show();
            // 添加自动对焦监听
            SensorController.getInstance().setCameraFocusListener(this::requestCameraFocus).onStart();
        }, 500);
    }

    //Activity暂停
    private boolean isActivityPause = false;
    //生命周期onPause
    public void onPause(boolean finishing) {
        LogUtil.i("ZCameraView onPause");
        this.isActivityPause = true;
        if (finishing){
            //停止视频播放，放这里无效，因为录制还未结束，播放还未开始
            stopVideo();
            //正在录制视频时，才调用，否则在拍照状态下退出到后台再回来提示“录视时间过短"
            if (isRecording){
                mCaptureLayout.stopRecord();
            }
            resetState(TYPE_PICTURE);
            CameraInterface.getInstance().isPreview(false);
            // CameraInterface.getInstance().unregisterSensorManager(mContext);
        }else {
            onStopRecordOnly();
        }
    }

    public void onStop(boolean finishing) {
        LogUtil.i("ZCameraView onStop");
        if (finishing){
            SensorController.getInstance().onStop();
            CameraInterface.getInstance().doDestroyCamera();
            // 移除监听，防止泄漏
            mVideoView.getHolder().removeCallback(this);
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 视频模式下，停止录制和停止播放
     */
    public void onStopRecordOnly(){
        if (mCaptureLayout.isStateOfVideo()){
            //正在录制，新停止录制
            if (isRecording){
                isRecording = false;
                mCaptureLayout.stopRecord();
            }

            //停止播放
            if (isShowingConfirm){
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
            }
        }

    }

    /**
     * 回收Bitmap
     */
    public void onDestroy(){
        if (firstFrame != null && !firstFrame.isRecycled()){
            firstFrame.recycle();
            firstFrame = null;
        }

    }

    //SurfaceView生命周期
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.i("ZCameraView SurfaceCreated");
        //如果是正在显示已拍的照片或预览已录制的视频时，不去打开相机
        if (isShowingConfirm){
            //是录制视频状态时，需要去播放视频
            if (mCaptureLayout.isStateOfVideo()){
                if (firstFrame != null && !TextUtils.isEmpty(videoUrl)){
                    playVideo(firstFrame,videoUrl);
                }
            }
        }
        new Thread() {
            @Override
            public void run() {
                //预览时仅检测打开相机
                if (isShowingConfirm){
                    CameraInterface.getInstance().doOpenCameraOnly();
                }else {
                    CameraInterface.getInstance().doOpenCamera(ZCameraView.this);
                }
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.i("ZCameraView SurfaceDestroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    //显示对焦指示器
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }
                if (event.getPointerCount() == 2) {
                    LogUtil.i("ZCamera", "ACTION_DOWN = " + 2);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    firstTouch = true;
                }
                if (event.getPointerCount() == 2) {
                    //第一个点
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    //第二个点
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (firstTouch) {
                        firstTouchLength = result;
                        firstTouch = false;
                    }
                    if ((int) (result - firstTouchLength) / zoomGradient != 0) {
                        firstTouch = true;
                        mCameraMachine.zoom(result - firstTouchLength, CameraInterface.TYPE_CAPTURE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
        }
        return true;
    }

    //对焦框指示器动画
    private void setFocusViewWidthAnimation(float x, float y) {
        mCameraMachine.focus(x, y, () -> {
            mFocusView.setVisibility(INVISIBLE);
            SensorController.getInstance().unlockFocus();
        });
    }

    private void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWidth());
            videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            videoViewParam.gravity = Gravity.CENTER;
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    /**************************************************
     * 对外提供的API                     *
     **************************************************/

    public void setSaveVideoPath(String path) {
        CameraInterface.getInstance().setSaveVideoPath(path);
    }

    public void setZCameraListener(ZCameraListener cameraListener) {
        this.zCameraListener = cameraListener;
    }

    private ErrorListener errorListener;

    //启动Camera错误回调
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
        CameraInterface.getInstance().setErrorListener(errorListener);
    }

    //设置CaptureButton功能（拍照和录像）
    public void setFeatures(int state) {
        handler.postDelayed(() -> ZCameraView.this.mCaptureLayout.setButtonFeatures(state), 200);
    }

    //设置录制质量
    public void setMediaQuality(int quality) {
        CameraInterface.getInstance().setMediaQuality(quality);
    }

    @Override
    public void resetState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();    //停止播放
                //初始化VideoView
                FileUtil.deleteFile(videoUrl);
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mCameraMachine.start(mVideoView.getHolder(), screenProp);
                break;
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                break;
        }
        mSwitchCamera.setVisibility(hasFrontCamera ? VISIBLE : INVISIBLE);
        // mFlashLamp.setVisibility(hasFlashLight ? VISIBLE : INVISIBLE);
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void confirmState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();    //停止播放
                mVideoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mCameraMachine.start(mVideoView.getHolder(), screenProp);
                if (zCameraListener != null) {
                    zCameraListener.recordSuccess(videoUrl, firstFrame);
                }
                break;
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                if (zCameraListener != null) {
                    zCameraListener.captureSuccess(captureBitmap);
                }
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                break;
        }
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void showPicture(Bitmap bitmap, boolean isVertical) {
        if (isVertical) {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        captureBitmap = bitmap;
        mPhoto.setImageBitmap(bitmap);
        mPhoto.setVisibility(VISIBLE);
        mCaptureLayout.startAlphaAnimation();
        mCaptureLayout.startTypeBtnAnimator();
    }

    @Override
    public void playVideo(Bitmap firstFrame, final String url) {
        LogUtil.i("ZCameraView playVideo --"+mVideoView.getHolder().getSurface());
        videoUrl = url;
        ZCameraView.this.firstFrame = firstFrame;
        if (isActivityPause){
            LogUtil.i("ZCameraView playVideo -- 未开始播放");
            return;
        }
        new Thread(() -> {
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                } else {
                    mMediaPlayer.reset();
                }
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
                mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnVideoSizeChangedListener((mp, width, height) -> {
                    // 更新updateVideoViewSize
                    updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                });
                mMediaPlayer.setOnPreparedListener(mp -> mMediaPlayer.start());
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stopVideo() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void setTip(String tip) {
        mCaptureLayout.setTip(tip);
    }

    @Override
    public void startPreviewCallback() {
        LogUtil.i("startPreviewCallback");
        handlerFocus(mFocusView.getWidth() / 2f, mFocusView.getHeight() / 2f);
    }

    @Override
    public boolean handlerFocus(float x, float y) {
        LogUtil.w(TAG, "handlerFocus() called with: x = [" + x + "], y = [" + y + "]");
        if (y > mCaptureLayout.getTop()) {
            return false;
        }
        mFocusView.setVisibility(VISIBLE);
        if (x < mFocusView.getWidth() / 2) {
            x = mFocusView.getWidth() / 2f;
        }
        if (x > layout_width - mFocusView.getWidth() / 2) {
            x = layout_width - mFocusView.getWidth() / 2f;
        }
        if (y < mFocusView.getWidth() / 2) {
            y = mFocusView.getWidth() / 2f;
        }
        if (y > mCaptureLayout.getTop() - mFocusView.getWidth() / 2) {
            y = mCaptureLayout.getTop() - mFocusView.getWidth() / 2f;
        }
        mFocusView.setX(x - mFocusView.getWidth() / 2f);
        mFocusView.setY(y - mFocusView.getHeight() / 2f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFocusView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFocusView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFocusView, "alpha", 1f, 0.4f, 1f, 0.4f, 1f, 0.4f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFocusView.setVisibility(INVISIBLE);
            }
        });
        return true;
    }

    public void setLeftClickListener(ClickListener clickListener) {
        this.leftClickListener = clickListener;
    }

//    private void setFlashRes() {
//        switch (mTypeFlash) {
//            case TYPE_FLASH_AUTO:
//                mFlashLamp.setImageResource(R.drawable.ic_flash_auto);
//                mCameraMachine.flash(Camera.Parameters.FLASH_MODE_AUTO);
//                break;
//            case TYPE_FLASH_ON:
//                mFlashLamp.setImageResource(R.drawable.ic_flash_on);
//                mCameraMachine.flash(Camera.Parameters.FLASH_MODE_ON);
//                break;
//            case TYPE_FLASH_OFF:
//                mFlashLamp.setImageResource(R.drawable.ic_flash_off);
//                mCameraMachine.flash(Camera.Parameters.FLASH_MODE_OFF);
//                break;
//        }
//    }
}
