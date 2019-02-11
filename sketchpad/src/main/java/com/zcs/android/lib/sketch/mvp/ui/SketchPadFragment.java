package com.zcs.android.lib.sketch.mvp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.PhotoRecord;
import com.zcs.android.lib.sketch.bean.SketchHistory;
import com.zcs.android.lib.sketch.bean.ToolOptionBean;
import com.zcs.android.lib.sketch.config.SketchConfig;
import com.zcs.android.lib.sketch.event.ChangeToolModeEvent;
import com.zcs.android.lib.sketch.mvp.base.BaseFragment;
import com.zcs.android.lib.sketch.mvp.present.SketchPresenter;
import com.zcs.android.lib.sketch.mvp.present.ToolKitPresenter;
import com.zcs.android.lib.sketch.mvp.view.ISketchView;
import com.zcs.android.lib.sketch.utils.MyTextUtils;
import com.zcs.android.lib.sketch.utils.SPKeyMap;
import com.zcs.android.lib.sketch.utils.SPUtils;
import com.zcs.android.lib.sketch.utils.SketchMode;
import com.zcs.android.lib.sketch.view.CustomSketchViewAdv;
import com.zcs.android.lib.sketch.view.NoScrollViewPager;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class SketchPadFragment extends BaseFragment implements ISketchView {
    private static final String TAG = "SketchPadFragment";
    private boolean needDrawingCache = true;

    /**
     * 学生端编辑图片等，会需要连同最底层的视图一起存储
     * 默认 true
     */
    public void setNeedDrawingCache(boolean needDrawingCache) {
        this.needDrawingCache = needDrawingCache;
    }

    // Views
    private View mSketchRootView;
    private ImageView mIvImage;
    private CustomSketchViewAdv mSketchView;
    private View mCursor, mToolPan, mToolInner;
    private NoScrollViewPager mToolViewPager;
    private RecyclerView mOptionsRecyclerView;
    private View mEditPicTipsView;

    // Input
    private View mInputRoot;
    private View mInputContainer;
    private View mDragContainer;
    private EditText mInputEditText;
    private TextView mTvDraw;
    private int mCurrTextSize = 36;

    // Presenter
    private SketchPresenter mPresenter;
    private ToolKitPresenter mToolKitPresenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeToolModeEvent(ChangeToolModeEvent event) {
        mToolKitPresenter.onToolClickEvent(3);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sketch_pad, container, false);
        return mRootView;
    }

    @Override
    public void init() {
        // 注册EventBus
        EventBus.getDefault().register(this);

        // 第一步：初始化Presenter
        mPresenter = new SketchPresenter(mContext, this);
        mToolKitPresenter = new ToolKitPresenter(mContext, this);

        // 第二步：初始化Views
        initViews();

        // 第三步：初始化工具条 - Update at 2018年2月7日
        mToolKitPresenter.initToolOptions(mOptionsRecyclerView);
        mToolKitPresenter.initSketchToolKit(mToolViewPager);
    }

    @Override
    public void initViews() {
        mSketchRootView = mRootView.findViewById(R.id.id_sketch_root_view);
        mIvImage = mRootView.findViewById(R.id.id_iv_img);
        mSketchView = mRootView.findViewById(R.id.id_custom_sketch);
        mCursor = mRootView.findViewById(R.id.id_container_cursor);

        mToolPan = mRootView.findViewById(R.id.id_container_tool_pan);
        mToolInner = mRootView.findViewById(R.id.id_container_tool_inner);
        mEditPicTipsView = mRootView.findViewById(R.id.id_view_edit_pic_tips);
        mRootView.findViewById(R.id.id_btn_no_tips_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setParam(mContext, SPKeyMap.KEY_NO_TIPS_EDIT_PIC, true);
                mEditPicTipsView.setVisibility(View.GONE);
            }
        });

        // 输入板
        mInputRoot = mRootView.findViewById(R.id.id_container_input_root);
        mInputRoot.setVisibility(View.GONE);
        mInputContainer = mRootView.findViewById(R.id.id_container_draw_input);
        mInputContainer.setVisibility(View.INVISIBLE);
        mDragContainer = mRootView.findViewById(R.id.id_container_input_drag);

        // 菜单容器
        mOptionsRecyclerView = mRootView.findViewById(R.id.id_rv_tool_options);
        mToolViewPager = mRootView.findViewById(R.id.id_nvp_tool);

        mInputEditText = mRootView.findViewById(R.id.id_et_input);
        mTvDraw = mRootView.findViewById(R.id.id_tv_draw_temp);
        mRootView.findViewById(R.id.id_btn_dialog_confirm).setOnClickListener(mInputClickListener);
        mRootView.findViewById(R.id.id_btn_dialog_cancel).setOnClickListener(mInputClickListener);
        mRootView.findViewById(R.id.id_btn_text_confirm).setOnClickListener(mInputClickListener);
        mRootView.findViewById(R.id.id_btn_text_delete).setOnClickListener(mInputClickListener);

        // 一级菜单
//        mRootView.findViewById(R.id.id_container_input).setOnClickListener(mMenuClickListener);
//        mRootView.findViewById(R.id.id_container_line).setOnClickListener(mMenuClickListener);
//        mRootView.findViewById(R.id.id_container_geo).setOnClickListener(mMenuClickListener);
//        mRootView.findViewById(R.id.id_container_pic).setOnClickListener(mMenuClickListener);
//        mRootView.findViewById(R.id.id_container_tools).setOnClickListener(mMenuClickListener);
//        mRootView.findViewById(R.id.id_container_more).setOnClickListener(mMenuClickListener);

        // 二级菜单
//        mRootView.findViewById(R.id.id_btn_eraser_small).setOnClickListener(this);
//        mRootView.findViewById(R.id.id_btn_eraser_large).setOnClickListener(this);
    }

    private void showInputDialog() {
        if (mInputContainer.getVisibility() != View.VISIBLE)
            mInputContainer.setVisibility(View.VISIBLE);
        int height = mInputContainer.getHeight();
        Log.d(TAG, "showInputDialog: height =" + height);
        ObjectAnimator anim = ObjectAnimator.ofFloat(mInputContainer, "translationY", -height, 0);
        anim.setDuration(300);
        anim.start();

        // MyTextUtils.showSoftInput(mInputEditText);
        // release
        releaseDragView();
    }

    @Override
    public void onOptionChanged(int option, int position) {
        if (option == ToolOptionBean.TOOL_OPTION_HIDE) {
            mPresenter.displayToolPan(mToolPan, mToolInner);
        } else {
            boolean needMove = mPresenter.moveCursor(mCursor, position);
            if (needMove) {
                mToolViewPager.setCurrentItem(option, false);
                mToolKitPresenter.updateToolKit(option);
                // 切换工具的时候，必须显示二级菜单
                if (!mPresenter.isToolPanShowing()) {
                    mPresenter.displayToolPan(mToolPan, mToolInner);
                }
            }
        }
    }

    /**
     * 一级菜单点击事件监听
     */
//    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int vId = v.getId();
//            if (vId == R.id.id_container_more) {
//                mPresenter.displayToolPan(mToolPan, mToolInner);
//            } else {
//                int position = 0;
//                if (vId == R.id.id_container_input) {
//                    position = 0;
//                } else if (vId == R.id.id_container_line) {
//                    position = 1;
//                } else if (vId == R.id.id_container_geo) {
//                    position = 2;
//                } else if (vId == R.id.id_container_pic) {
//                    position = 3;
//                } else if (vId == R.id.id_container_tools) {
//                    position = 4;
//                }
//                boolean needMove = mPresenter.moveCursor(mCursor, position);
//                if (needMove) {
//                    mToolViewPager.setCurrentItem(position, false);
//                    mToolKitPresenter.updateToolKit(position);
//                    // 切换工具的时候，必须显示二级菜单
//                    if (!mPresenter.isToolPanShowing()) {
//                        mPresenter.displayToolPan(mToolPan, mToolInner);
//                    }
//                }
//            }
//        }
//    };
    @Override
    public void showToast(String msg) {
        super.showToastOnUi(msg);
    }

    @Override
    public void sketchUndo() {
        showToast(mSketchView.undo());
    }

    @Override
    public void sketchRedo() {
        showToast(mSketchView.redo());
    }

    @Override
    public boolean isSketchEmpty() {
        return mSketchView.isEmpty();
    }

    @Override
    public void sketchClear() {
        mSketchView.clear();
        mToolKitPresenter.cleanSpotlightState();
    }

    @Override
    public void sketchClearAutoResetUserPhoto() {
        // 第一步：获取用户图片记录
        PhotoRecord userPhotoRecord = mSketchView.getUserPhotoRecord();
        // 第二步：清空画布
        sketchClear();
        // 第三步：恢复用户图片
        mSketchView.resetUserPhoto(userPhotoRecord);
    }

    @Override
    public PhotoRecord getUserPhotoRecord() {
        return mSketchView.getUserPhotoRecord();
    }

    @Override
    public void resetUserPhoto(PhotoRecord userPhotoRecord) {
        mSketchView.resetUserPhoto(userPhotoRecord);
    }

    @Override
    public boolean sketchSaveJpg(String filePath) {
        /**
         * TODO 学生端编辑图片等，会需要连同最底层的视图一起存储
         * Modify by kk20 on 2018/4/23
         */
        if (needDrawingCache) {
            mSketchRootView.buildDrawingCache();
            Bitmap bitmap = mSketchRootView.getDrawingCache();
            if (bitmap == null) {
                return false;
            }
            boolean saveResult = mPresenter.saveBitmap(filePath, bitmap);
            mSketchRootView.destroyDrawingCache();
            return saveResult;
        } else {
            return mPresenter.saveBitmap(filePath, mSketchView.getSketchBitmap());
        }
    }

    @Override
    public void sketchSaveThumb(String filePath) {
        /**
         * TODO 学生端编辑图片等，会需要连同最底层的视图一起存储
         * Modify by kk20 on 2018/4/23
         */
        if (needDrawingCache) {
            mSketchRootView.buildDrawingCache();
            Bitmap bitmap = mSketchRootView.getDrawingCache();
            if (bitmap != null) {
                mPresenter.saveThumb(filePath, bitmap);
            }
            mSketchRootView.destroyDrawingCache();
        } else {
            mPresenter.saveThumb(filePath, mSketchView.getSketchBitmap());
        }
    }

    @Override
    public void drawByHistory(List<SketchHistory> historyList) {
        mSketchView.drawByHistoryJson(historyList);
    }

    @Override
    public List<SketchHistory> getHistoryList() {
        return mSketchView.getHistoryList();
    }

    @Override
    public boolean updateFontSize(int mode) {
        if (TextUtils.isEmpty(mTvDraw.getText()))
            return true;
        switch (mode) {
            case SketchMode.Text.NORMAL:
                mCurrTextSize = 36;// 1f
                break;
            case SketchMode.Text.BIG:
                mCurrTextSize = 48;// 4/3
                break;
            case SketchMode.Text.LARGE:
                mCurrTextSize = 60;// 5/3
                break;
        }
        final float x1 = mDragContainer.getX();
        final float y1 = mDragContainer.getY();
        Log.d(TAG, "updateFontSize: ----------------------------");
        Log.d(TAG, "updateFontSize-before: " + x1 + "," + y1);
        mTvDraw.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCurrTextSize);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                float x2 = mDragContainer.getX();
                float y2 = mDragContainer.getY();
                Log.d(TAG, "updateFontSize-after: " + x2 + "," + y2);
            }
        }, 200);
        return false;
    }

    @Override
    public void onColorChanged() {
        try {
            mTvDraw.setTextColor(SketchConfig.currColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 2018年2月25日10:18:37 颜色改变时,重绘临时图形
            mSketchView.invalidateTempGeometric();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sketchSetBg(Bitmap bitmap) {
        mSketchView.setBackgroundBitmap(bitmap);
    }

    @Override
    public void sketchSetBgByPath(String filePath) {
        mSketchView.setBackgroundByPath(filePath);
    }

    /**
     * 展示图片编辑模式提示信息
     */
    private void displayEditPicTips(boolean show) {
        final Boolean ignoreTips = SPUtils.getBoolean(mContext, SPKeyMap.KEY_NO_TIPS_EDIT_PIC, false);
        if (ignoreTips) return;
        final int percentHeightSize = AutoUtils.getPercentHeightSize(200);
        if (show) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mEditPicTipsView, "translationY", -percentHeightSize, 0);
            animator.setDuration(300);
            animator.start();
            mEditPicTipsView.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mEditPicTipsView, "translationY", 0, -percentHeightSize);
                    animator.setDuration(300);
                    animator.start();
                }
            }, 3000);
        }
    }

    @Override
    public void addPicByUser(final String url, final Bitmap bitmap) {

        // 延时设置，保证图片居中
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSketchView.addPicByUser(url, bitmap);
                displayEditPicTips(true);
            }
        }, 500);

        displayEditPicTips(false);
    }

    /**
     * 添加图片，可编辑
     */
    @Override
    public void addPicByBitmap(final Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled())
            return;

        // 延时设置，保证图片居中
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSketchView.addPicBitmap(bitmap);
                displayEditPicTips(true);
                // 添加图片后，默认不可编辑
//                setSketchMode(new SketchMode(SketchMode.Mode.EDIT_PIC, SketchMode.MODE_DFT));
            }
        }, 500);
        displayEditPicTips(false);
    }

    //不设置默认画笔(主要用于板书中，有图片移动情况)
    @Override
    public void NotSetPenDefault() {
        mToolKitPresenter.noPenDefault();
    }

    @Override
    public void invalidateToolMenus(boolean showAll) {
        mToolKitPresenter.invalidateToolOptions(showAll);
    }

    /**
     * 添加图片，可编辑
     */
    @Override
    public void addPicByPath(final String filePath) {
        // 延时设置，保证图片居中
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSketchView.addPicByPath(filePath);
                setSketchMode(new SketchMode(SketchMode.Mode.EDIT_PIC, SketchMode.MODE_DFT));
            }
        }, 500);
    }

    @Override
    public void cleanSpotlight() {
        mSketchView.cleanSpotlight();
    }

    @Override
    public void setSketchMode(SketchMode sketchMode) {
        if (sketchMode.getModeType() == SketchMode.Mode.TEXT) {
            if (TextUtils.isEmpty(mTvDraw.getText())) {
                showInputDialog();
                mInputRoot.setVisibility(View.VISIBLE);
                switch (sketchMode.getDrawType()) {
                    case SketchMode.Text.NORMAL:
                        mCurrTextSize = 36;
                        break;
                    case SketchMode.Text.BIG:
                        mCurrTextSize = 48;
                        break;
                    case SketchMode.Text.LARGE:
                        mCurrTextSize = 60;
                        break;
                }
                mTvDraw.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCurrTextSize);
                mTvDraw.setTextColor(SketchConfig.currColor);
            }
        } else {
            dismissDialog();
            mInputRoot.setVisibility(View.GONE);
        }
        mSketchView.setSketchMode(sketchMode);
    }

    public ImageView getIvImage() {
        return mIvImage;
    }

    public void setIVBackground(@ColorInt int color) {
        mSketchView.setBackground(new ColorDrawable(color));
    }

    private View.OnClickListener mInputClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vId = v.getId();
            if (vId == R.id.id_btn_dialog_confirm) {// 完成文字输入
                String inputText = mInputEditText.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    showToast(mInputEditText.getHint().toString());
                    return;
                }
                mDragContainer.setVisibility(View.VISIBLE);
                mTvDraw.setText(mInputEditText.getText());
                dismissDialog();
            } else if (vId == R.id.id_btn_dialog_cancel) {// 取消文字输入
                dismissDialog();
            } else if (vId == R.id.id_btn_text_confirm) {// 完成拖拽,绘制到画布上
                effectiveDragText();
            } else if (vId == R.id.id_btn_text_delete) {// 删除文字
                // release
                releaseDragView();
            }
        }
    };

    @Override
    public void savePenWidth() {
        mSketchView.savePenWidth();
    }

    @Override
    public void setSketchToolbarVisible(int visible) {
        mToolPan.setVisibility(visible);
    }

    @Override
    public void hideToolMenu() {
        if (mPresenter.isToolPanShowing())
            mPresenter.displayToolPan(mToolPan, mToolInner);
    }

    @Override
    public void effectiveDragText() {
        try {
            if (TextUtils.isEmpty(mTvDraw.getText())) {
                return;
            }
            float x = mDragContainer.getX();
            float y = mDragContainer.getY();
            float x1 = mTvDraw.getX();
            float y1 = mTvDraw.getY();

            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mDragContainer.getLayoutParams();
            LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mTvDraw.getLayoutParams();
            int marginCount = lp1.leftMargin + lp1.rightMargin + lp2.leftMargin + lp2.rightMargin;

            mSketchView.drawText(mTvDraw.getText().toString(), mCurrTextSize, x + x1, y + y1, marginCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // release
        releaseDragView();
    }

    /**
     * 恢复文字输入默认状态
     */
    private void releaseDragView() {
        mTvDraw.setText("");
        mDragContainer.setVisibility(View.GONE);
    }

    private void dismissDialog() {
        int height = mInputContainer.getHeight();
        ObjectAnimator anim = ObjectAnimator.ofFloat(mInputContainer, "translationY", mInputContainer.getTranslationY(), -height);
        anim.setDuration(400);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mInputEditText.setText("");
            }
        });
        MyTextUtils.hideSoftInput(mInputEditText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
