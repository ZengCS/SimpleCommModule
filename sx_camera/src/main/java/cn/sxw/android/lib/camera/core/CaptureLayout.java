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
    private TypeButton btn_confirm;         // 确认按钮
    private TypeButton btn_cancel;          // 取消按钮
    private ReturnButton btn_return;        // 返回按钮
    private ImageView iv_custom_left;       // 左边自定义按钮
    private ImageView iv_custom_right;      // 右边自定义按钮
    private TextView txt_tip;               // 提示文本
    private TextView txt_time;              // 提示文本

    private int layout_width;
    private int layout_height;
    private int button_size;
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
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }
        button_size = (int) (layout_width / 4.5f);
        layout_height = button_size + (button_size / 5) * 2 + 100;

        initView();
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(layout_width, layout_height);
    }

    public void initEvent() {
        //默认Typebutton为隐藏
        iv_custom_right.setVisibility(GONE);
        btn_cancel.setVisibility(GONE);
        btn_confirm.setVisibility(GONE);
    }

    public void startTypeBtnAnimator() {
        // 隐藏录制时间
        showTime(false, "");
        //拍照录制结果后的动画
        if (this.iconLeft != 0)
            iv_custom_left.setVisibility(GONE);
        else
            btn_return.setVisibility(GONE);
        if (this.iconRight != 0)
            iv_custom_right.setVisibility(GONE);
        mCaptureBtn.setVisibility(GONE);
        btn_cancel.setVisibility(VISIBLE);
        btn_confirm.setVisibility(VISIBLE);
        btn_cancel.setClickable(false);
        btn_confirm.setClickable(false);
        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(btn_cancel, "translationX", layout_width / 4f, 0);
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(btn_confirm, "translationX", -layout_width / 4f, 0);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator_cancel, animator_confirm);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                btn_cancel.setClickable(true);
                btn_confirm.setClickable(true);
            }
        });
        set.setDuration(200);
        set.start();
    }


    private void initView() {
        setWillNotDraw(false);

        // 拍照按钮
        mCaptureBtn = new CaptureButton(getContext(), button_size);
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
                    iv_custom_right.setVisibility(GONE);
                    btn_return.setVisibility(GONE);
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
        btn_cancel = new TypeButton(getContext(), TypeButton.TYPE_CANCEL, button_size);
        final LayoutParams btn_cancel_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_cancel_param.gravity = Gravity.CENTER_VERTICAL;
        btn_cancel_param.setMargins((layout_width / 4) - button_size / 2, 0, 0, 0);
        btn_cancel.setLayoutParams(btn_cancel_param);
        btn_cancel.setOnClickListener(view -> {
            if (typeListener != null) {
                typeListener.cancel();
            }
            startAlphaAnimation();
//                resetCaptureLayout();
        });

        //确认按钮
        btn_confirm = new TypeButton(getContext(), TypeButton.TYPE_CONFIRM, button_size);
        LayoutParams btn_confirm_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        btn_confirm_param.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        btn_confirm_param.setMargins(0, 0, (layout_width / 4) - button_size / 2, 0);
        btn_confirm.setLayoutParams(btn_confirm_param);
        btn_confirm.setOnClickListener(view -> {
            if (typeListener != null) {
                typeListener.confirm();
            }
            startAlphaAnimation();
//                resetCaptureLayout();
        });

        //返回按钮
        btn_return = new ReturnButton(getContext(), (int) (button_size / 2.5f));
        LayoutParams btn_return_param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn_return_param.gravity = Gravity.CENTER_VERTICAL;
        btn_return_param.setMargins(layout_width / 6, 0, 0, 0);
        btn_return.setLayoutParams(btn_return_param);
        btn_return.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });
        //左边自定义按钮
        iv_custom_left = new ImageView(getContext());
        LayoutParams iv_custom_param_left = new LayoutParams((int) (button_size / 2.5f), (int) (button_size / 2.5f));
        iv_custom_param_left.gravity = Gravity.CENTER_VERTICAL;
        iv_custom_param_left.setMargins(layout_width / 6, 0, 0, 0);
        iv_custom_left.setLayoutParams(iv_custom_param_left);
        iv_custom_left.setOnClickListener(v -> {
            if (leftClickListener != null) {
                leftClickListener.onClick();
            }
        });

        //右边自定义按钮
        iv_custom_right = new ImageView(getContext());
        LayoutParams iv_custom_param_right = new LayoutParams((int) (button_size / 2.5f), (int) (button_size / 2.5f));
        iv_custom_param_right.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        iv_custom_param_right.setMargins(0, 0, layout_width / 6, 0);
        iv_custom_right.setLayoutParams(iv_custom_param_right);
        iv_custom_right.setOnClickListener(v -> {
            mCaptureBtn.switchAction();
            int action = mCaptureBtn.getAction();
            if (action == CaptureButton.ACTION_PHOTO) {// 拍照
                setTip("轻触拍照");
                iv_custom_right.setImageResource(R.drawable.ic_record);
            } else {// 视频
                setTip("轻触录视频");
                iv_custom_right.setImageResource(R.drawable.ic_camera_normal);
            }

            // 开始录视频
            /*mCaptureBtn.startRecord();

            if (rightClickListener != null) {
                rightClickListener.onClick();
            }*/
        });
        // 提示信息
        txt_tip = new TextView(getContext());
        LayoutParams txt_param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txt_param.gravity = Gravity.CENTER_HORIZONTAL;
        txt_param.setMargins(0, 0, 0, 0);
        txt_tip.setText("轻触拍照，长按摄像");
        txt_tip.setTextColor(0xFFFFFFFF);
        txt_tip.setGravity(Gravity.CENTER);
        txt_tip.setLayoutParams(txt_param);

        // 录像时间
        txt_time = new TextView(getContext());
        LayoutParams timeLp = new LayoutParams(LayoutParams.WRAP_CONTENT, (layout_height - button_size) / 2);
        timeLp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        txt_time.setText("00:00");
        txt_time.setTextColor(0xFFFFFFFF);
        txt_time.setGravity(Gravity.CENTER);
        txt_time.setLayoutParams(timeLp);
        txt_time.setVisibility(GONE);// 默认不显示时间

        this.addView(mCaptureBtn);
        this.addView(btn_cancel);
        this.addView(btn_confirm);
        this.addView(btn_return);
        this.addView(iv_custom_left);
        this.addView(iv_custom_right);
        this.addView(txt_tip);
        this.addView(txt_time);
    }

    /**************************************************
     * 对外提供的API                      *
     **************************************************/
    public void resetCaptureLayout() {
        mCaptureBtn.resetState();
        btn_cancel.setVisibility(GONE);
        btn_confirm.setVisibility(GONE);
        mCaptureBtn.setVisibility(VISIBLE);
        if (this.iconLeft != 0)
            iv_custom_left.setVisibility(VISIBLE);
        else
            btn_return.setVisibility(VISIBLE);
        if (this.iconRight != 0)
            iv_custom_right.setVisibility(VISIBLE);
    }


    public void startAlphaAnimation() {
        if (isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 1f, 0f);
            animator_txt_tip.setDuration(500);
            animator_txt_tip.start();
            isFirst = false;
        }
    }

    public void setTextWithAnimation(String tip) {
        txt_tip.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(txt_tip, "alpha", 0f, 1f, 1f, 0f);
        animator_txt_tip.setDuration(2500);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        mCaptureBtn.setDuration(duration);
    }

    public void setButtonFeatures(int state) {
        mCaptureBtn.setButtonFeatures(state);
        if (state == ZCameraView.BUTTON_STATE_BOTH) {
            iv_custom_right.setVisibility(VISIBLE);
        } else {
            iv_custom_right.setVisibility(INVISIBLE);
        }
    }

    public void setTip(String tip) {
        showTip();
        txt_tip.setText(tip);
    }

    public void showTime(boolean show, String time) {
        txt_time.setVisibility(show ? VISIBLE : GONE);
        if (!TextUtils.isEmpty(time))
            txt_time.setText(time);
    }

    public void showTip() {
        txt_tip.setVisibility(VISIBLE);
        txt_tip.setAlpha(1);
        isFirst = true;
    }

    public void setIconSrc(int iconLeft, int iconRight) {
        this.iconLeft = iconLeft;
        this.iconRight = iconRight;
        if (this.iconLeft != 0) {
            iv_custom_left.setImageResource(iconLeft);
            iv_custom_left.setVisibility(VISIBLE);
            btn_return.setVisibility(GONE);
        } else {
            iv_custom_left.setVisibility(GONE);
            btn_return.setVisibility(VISIBLE);
        }
        if (this.iconRight != 0) {
            iv_custom_right.setImageResource(iconRight);
            iv_custom_right.setVisibility(VISIBLE);
        } else {
            iv_custom_right.setVisibility(GONE);
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

}
