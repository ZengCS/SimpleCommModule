package cn.sxw.android.lib.camera.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sxw.android.lib.camera.R;
import cn.sxw.android.lib.camera.listener.CaptureListener;
import cn.sxw.android.lib.camera.listener.ClickListener;
import cn.sxw.android.lib.camera.listener.TypeListener;
import cn.sxw.android.lib.camera.util.CommonUtils;

public class CaptureLayout extends FrameLayout {

    private CaptureListener captureListener;    //拍照按钮监听
    private TypeListener typeListener;          //拍照或录制后接结果按钮监听
    private ClickListener leftClickListener;    //左边按钮监听

    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }

    private CaptureButton mCaptureBtn;      // 拍照按钮
    private TypeButton mConfirmBtn;         // 确认按钮
    private TypeButton mCancelBtn;          // 取消按钮
    private ReturnButton mReturnBtn;        // 返回按钮
    private ImageView mCustomBtnLeft;       // 左边自定义按钮
    private ImageView mCustomBtnRight;      // 右边自定义按钮
    private TextView mTipsTv;               // 提示文本
    private TextView mRecordTimeTv;              // 提示文本

    private int mLayoutWidth;
    private int mLayoutHeight;
    private int mBtnSize;
    private int iconLeft = 0;
    private int iconRight = 0;

    private boolean isFirst = true;

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutWidth = outMetrics.widthPixels;
        } else {
            mLayoutWidth = outMetrics.widthPixels / 2;
        }
        mBtnSize = (int) (mLayoutWidth / 4.5f);
        mLayoutHeight = mBtnSize + (mBtnSize / 5) * 2 + 100;

        initView();
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mLayoutWidth, mLayoutHeight);
    }

    public void initEvent() {
        //默认Typebutton为隐藏
        mCustomBtnRight.setVisibility(GONE);
        mCancelBtn.setVisibility(GONE);
        mConfirmBtn.setVisibility(GONE);
    }

    public void startTypeBtnAnimator() {
        // 隐藏录制时间
        showTime(false, "");
        //拍照录制结果后的动画
        if (this.iconLeft != 0)
            mCustomBtnLeft.setVisibility(GONE);
        else
            mReturnBtn.setVisibility(GONE);
        if (this.iconRight != 0)
            mCustomBtnRight.setVisibility(GONE);
        mCaptureBtn.setVisibility(GONE);
        mCancelBtn.setVisibility(VISIBLE);
        mConfirmBtn.setVisibility(VISIBLE);
        mCancelBtn.setClickable(false);
        mConfirmBtn.setClickable(false);
        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(mCancelBtn, "translationX", mLayoutWidth / 4f, 0);
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(mConfirmBtn, "translationX", -mLayoutWidth / 4f, 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator_cancel, animator_confirm);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCancelBtn.setClickable(true);
                mConfirmBtn.setClickable(true);
            }
        });
        set.setDuration(200);
        set.start();
    }


    private void initView() {
        setWillNotDraw(false);

        // 拍照按钮
        mCaptureBtn = new CaptureButton(getContext(), mBtnSize);
        LayoutParams btnCaptureParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btnCaptureParam.gravity = Gravity.CENTER;
        mCaptureBtn.setLayoutParams(btnCaptureParam);
        mCaptureBtn.setOnClickListener(v -> {
            switch (mCaptureBtn.getAction()) {
                case CaptureButton.ACTION_PHOTO:// 拍照
                    mCaptureBtn.startCaptureAnimation();
                    break;
                case CaptureButton.ACTION_RECORD:// 录视频
                    mCaptureBtn.startRecord();
                    // 录制过程中，不允许切换和退出
                    mCustomBtnRight.setVisibility(GONE);
                    mReturnBtn.setVisibility(GONE);
                    break;
                case CaptureButton.ACTION_RECORD_FINISH:// 停止录视频
                    mCaptureBtn.stopRecord();
                    break;
            }
        });

        mCaptureBtn.addRecordTimeListener(time -> showTime(true, CommonUtils.timeToString4Timer(time)));

        mCaptureBtn.setCaptureListener(new CaptureListener() {
            @Override
            public void takePictures() {
                if (captureListener != null) {
                    captureListener.takePictures();
                }
            }

            @Override
            public void recordShort(long time) {
                if (captureListener != null) {
                    captureListener.recordShort(time);
                }
                startAlphaAnimation();
            }

            @Override
            public void recordStart() {
                if (captureListener != null) {
                    captureListener.recordStart();
                }
                startAlphaAnimation();
            }

            @Override
            public void recordEnd(long time) {
                if (captureListener != null) {
                    captureListener.recordEnd(time);
                }
                startAlphaAnimation();
                startTypeBtnAnimator();
            }

            @Override
            public void recordZoom(float zoom) {
                if (captureListener != null) {
                    captureListener.recordZoom(zoom);
                }
            }

            @Override
            public void recordError() {
                if (captureListener != null) {
                    captureListener.recordError();
                }
            }
        });

        // 取消按钮
        mCancelBtn = new TypeButton(getContext(), TypeButton.TYPE_CANCEL, mBtnSize);
        final LayoutParams btn_cancel_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_cancel_param.gravity = Gravity.CENTER_VERTICAL;
        btn_cancel_param.setMargins((mLayoutWidth / 4) - mBtnSize / 2, 0, 0, 0);
        mCancelBtn.setLayoutParams(btn_cancel_param);
        mCancelBtn.setOnClickListener(view -> {
            if (typeListener != null) {
                typeListener.cancel();
            }
            startAlphaAnimation();
//                resetCaptureLayout();
        });

        //确认按钮
        mConfirmBtn = new TypeButton(getContext(), TypeButton.TYPE_CONFIRM, mBtnSize);
        LayoutParams btn_confirm_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_confirm_param.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        btn_confirm_param.setMargins(0, 0, (mLayoutWidth / 4) - mBtnSize / 2, 0);
        mConfirmBtn.setLayoutParams(btn_confirm_param);
        mConfirmBtn.setOnClickListener(view -> {
            if (typeListener != null) {
                typeListener.confirm();
            }
            startAlphaAnimation();
//                resetCaptureLayout();
        });

        //返回按钮
        mReturnBtn = new ReturnButton(getContext(), (int) (mBtnSize / 2.5f));
        LayoutParams btn_return_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn_return_param.gravity = Gravity.CENTER_VERTICAL;
        btn_return_param.setMargins(mLayoutWidth / 6, 0, 0, 0);
        mReturnBtn.setLayoutParams(btn_return_param);
        mReturnBtn.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });
        //左边自定义按钮
        mCustomBtnLeft = new ImageView(getContext());
        LayoutParams iv_custom_param_left = new LayoutParams((int) (mBtnSize / 2.5f), (int) (mBtnSize / 2.5f));
        iv_custom_param_left.gravity = Gravity.CENTER_VERTICAL;
        iv_custom_param_left.setMargins(mLayoutWidth / 6, 0, 0, 0);
        mCustomBtnLeft.setLayoutParams(iv_custom_param_left);
        mCustomBtnLeft.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });

        //右边自定义按钮
        mCustomBtnRight = new ImageView(getContext());
        LayoutParams iv_custom_param_right = new LayoutParams((int) (mBtnSize / 2.5f), (int) (mBtnSize / 2.5f));
        iv_custom_param_right.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        iv_custom_param_right.setMargins(0, 0, mLayoutWidth / 6, 0);
        mCustomBtnRight.setLayoutParams(iv_custom_param_right);
        mCustomBtnRight.setOnClickListener(v -> {
            mCaptureBtn.switchAction();
            int action = mCaptureBtn.getAction();
            if (action == CaptureButton.ACTION_PHOTO) {// 拍照
                setTip("轻触拍照");
                mCustomBtnRight.setImageResource(R.drawable.ic_record);
            } else {// 视频
                setTip("轻触录视频");
                mCustomBtnRight.setImageResource(R.drawable.ic_camera_normal);
            }

            // 开始录视频
            /*mCaptureBtn.startRecord();

            if (rightClickListener != null) {
                rightClickListener.onClick();
            }*/
        });
        // 提示信息
        mTipsTv = new TextView(getContext());
        LayoutParams txt_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txt_param.gravity = Gravity.CENTER_HORIZONTAL;
        txt_param.setMargins(0, 0, 0, 0);
        mTipsTv.setText("轻触拍照，长按摄像");
        mTipsTv.setTextColor(0xFFFFFFFF);
        mTipsTv.setGravity(Gravity.CENTER);
        mTipsTv.setLayoutParams(txt_param);

        // 录像时间
        mRecordTimeTv = new TextView(getContext());
        LayoutParams timeLp = new LayoutParams(LayoutParams.WRAP_CONTENT, (mLayoutHeight - mBtnSize) / 2);
        timeLp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mRecordTimeTv.setText("00:00");
        mRecordTimeTv.setTextColor(0xFFFFFFFF);
        mRecordTimeTv.setGravity(Gravity.CENTER);
        mRecordTimeTv.setLayoutParams(timeLp);
        mRecordTimeTv.setVisibility(GONE);// 默认不显示时间

        this.addView(mCaptureBtn);
        this.addView(mCancelBtn);
        this.addView(mConfirmBtn);
        this.addView(mReturnBtn);
        this.addView(mCustomBtnLeft);
        this.addView(mCustomBtnRight);
        this.addView(mTipsTv);
        this.addView(mRecordTimeTv);
    }

    /**
     * 当前是录制视频状态
     * @return
     */
    public boolean isStateOfVideo(){
        return mCaptureBtn.getAction() != CaptureButton.ACTION_PHOTO;
    }

    /**************************************************
     * 对外提供的API                      *
     **************************************************/
    public void resetCaptureLayout() {
        mCaptureBtn.resetState();
        //不显示时间
        showTime(false,"");
        mCancelBtn.setVisibility(GONE);
        mConfirmBtn.setVisibility(GONE);
        mCaptureBtn.setVisibility(VISIBLE);
        if (this.iconLeft != 0)
            mCustomBtnLeft.setVisibility(VISIBLE);
        else
            mReturnBtn.setVisibility(VISIBLE);
        if (this.iconRight != 0) {
            if (mFeatureState == ZCameraView.BUTTON_STATE_BOTH) {
                mCustomBtnRight.setVisibility(VISIBLE);
            } else {
                mCustomBtnRight.setVisibility(INVISIBLE);
            }
        }
    }


    public void startAlphaAnimation() {
        if (isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(mTipsTv, "alpha", 1f, 0f);
            animator_txt_tip.setDuration(500);
            animator_txt_tip.start();
            isFirst = false;
        }
    }

    public void setTextWithAnimation(String tip) {
        mTipsTv.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(mTipsTv, "alpha", 0f, 1f, 1f, 0f);
        animator_txt_tip.setDuration(2500);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        mCaptureBtn.setDuration(duration);
    }

    private int mFeatureState;

    public void setButtonFeatures(int state) {
        mFeatureState = state;
        mCaptureBtn.setButtonFeatures(state);
        setTip("轻触拍照");
        if (state == ZCameraView.BUTTON_STATE_BOTH) {
            mCustomBtnRight.setVisibility(VISIBLE);
        } else {
            mCustomBtnRight.setVisibility(INVISIBLE);
            if (state == ZCameraView.BUTTON_STATE_ONLY_RECORDER) {
                setTip("轻触录视频");
                mCaptureBtn.switchAction();
            }
        }
    }

    public void setTip(String tip) {
        showTip();
        mTipsTv.setText(tip);
    }

    public void showTime(boolean show, String time) {
        mRecordTimeTv.setVisibility(show ? VISIBLE : GONE);
        if (!TextUtils.isEmpty(time))
            mRecordTimeTv.setText(time);
    }

    public void showTip() {
        mTipsTv.setVisibility(VISIBLE);
        mTipsTv.setAlpha(1);
        isFirst = true;
    }

    public void setIconSrc(int iconLeft, int iconRight) {
        this.iconLeft = iconLeft;
        this.iconRight = iconRight;
        if (this.iconLeft != 0) {
            mCustomBtnLeft.setImageResource(iconLeft);
            mCustomBtnLeft.setVisibility(VISIBLE);
            mReturnBtn.setVisibility(GONE);
        } else {
            mCustomBtnLeft.setVisibility(GONE);
            mReturnBtn.setVisibility(VISIBLE);
        }
        if (this.iconRight != 0) {
            mCustomBtnRight.setImageResource(iconRight);
            mCustomBtnRight.setVisibility(VISIBLE);
        } else {
            mCustomBtnRight.setVisibility(GONE);
        }
    }

    public void setLeftClickListener(ClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public static String timeToString(long time) {
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;
        int d = h / 24;
        if (d > 0) {
            return d + "d" + h % 24 + "h" + m % 60 + "'" + s % 60 + "\"";
        } else if (h > 0) {
            return h % 24 + "h" + m % 60 + "'" + s % 60 + "\"";
        } else if (m > 0) {
            return m % 60 + "'" + s % 60 + "\"";
        } else {
            return "0'" + s % 60 + "\"";
        }
    }

    public void stopRecord() {
        if (mCaptureBtn != null)
            mCaptureBtn.stopRecord();
    }
}
