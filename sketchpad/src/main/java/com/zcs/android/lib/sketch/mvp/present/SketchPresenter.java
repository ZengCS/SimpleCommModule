package com.zcs.android.lib.sketch.mvp.present;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.mvp.base.BasePresenter;
import com.zcs.android.lib.sketch.mvp.view.ISketchView;
import com.zcs.android.lib.sketch.utils.BitmapUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class SketchPresenter extends BasePresenter {
    private static final String TAG = "SketchPresenter";
    private boolean isToolPanShowing = true;
    private boolean isAniming = false;
    private ISketchView mRootView;
    private int currPosition = 0;
    private RelativeLayout mToolMenusLayout;
    private View mToolMenusCursor;
    private float mToolMenuLayoutWidth;
    private static final int MIN_WIDTH = AutoUtils.getPercentWidthSize(120);

    // 颜色列表
//    private List<ToolColorBean> mItems = new ArrayList<>();
//    private ColorsRecyclerAdapter mAdapter;

    public SketchPresenter(Context mContext, ISketchView rootView) {
        super(mContext);
        this.mRootView = rootView;
    }

    public boolean saveBitmap(String filePath, Bitmap bitmap) {
        if (bitmap == null) {
            // mRootView.showToast("请勾画后再保存！");
            return false;
        }

        boolean success = BitmapUtil.saveBitmap(bitmap, filePath);
        if (!success) {
            mRootView.showToast("图片保存失败!");
        }
        return success;
    }

    public void saveThumb(String filePath, Bitmap bitmap) {
        if (bitmap == null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        BitmapUtil.saveBitmap(bitmap, filePath, 440, 330);
    }

    public boolean moveCursor(View cursor, int position) {
        if (isAniming || cursor == null || position == currPosition) return false;

        currPosition = position;
        int width = cursor.getWidth();

        float translationX = cursor.getTranslationX();
        float targetX = width * position;
        ObjectAnimator anim = ObjectAnimator.ofFloat(cursor, "translationX", translationX, targetX);
        anim.setDuration(180);
        anim.start();
        return true;
    }

    public void displayToolPan(View pan, View inner) {
        if (isAniming) return;
        isAniming = true;
        if (mToolMenusLayout == null)
            mToolMenusLayout = pan.findViewById(R.id.id_container_tool_menus);
        if (mToolMenusCursor == null)
            mToolMenusCursor = pan.findViewById(R.id.id_container_cursor);
        if (mToolMenusLayout == null || mToolMenusCursor == null)
            return;
        if (mToolMenuLayoutWidth <= 0)
            mToolMenuLayoutWidth = mToolMenusLayout.getWidth();

        float transY = pan.getHeight() * 0.6f;
        float innerTransY = pan.getHeight() * 0.3f;
        float transX = pan.getWidth() / 2 - MIN_WIDTH / 1.5f;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        AnimatorSet set2 = new AnimatorSet();
        set2.setDuration(300);
        ObjectAnimator panTyAnim, innerTyAnim, toolTxAnim;
        ValueAnimator resizeAnim;

        if (isToolPanShowing) {// 请隐藏
            isToolPanShowing = false;
            panTyAnim = ObjectAnimator.ofFloat(pan, "translationY", pan.getTranslationY(), transY);
            innerTyAnim = ObjectAnimator.ofFloat(inner, "translationY", inner.getTranslationY(), innerTransY);
            resizeAnim = ValueAnimator.ofFloat(1, 0);
            toolTxAnim = ObjectAnimator.ofFloat(mToolMenusLayout, "translationX", mToolMenusLayout.getTranslationX(), transX);

//            set.play(innerTyAnim).after(panTyAnim);

            set.play(resizeAnim).with(toolTxAnim).after(innerTyAnim).after(panTyAnim);

            // 改变大小和位置
//            set2.play(resizeAnim).with(toolTxAnim);
//            set2.start();
        } else {// 请显示
            isToolPanShowing = true;
            panTyAnim = ObjectAnimator.ofFloat(pan, "translationY", transY, 0);
            innerTyAnim = ObjectAnimator.ofFloat(inner, "translationY", innerTransY, 0);
            resizeAnim = ValueAnimator.ofFloat(0, 1);
            toolTxAnim = ObjectAnimator.ofFloat(mToolMenusLayout, "translationX", transX, 0);

            set.play(panTyAnim).after(innerTyAnim);

            // 改变大小和位置
            set2.play(resizeAnim).with(toolTxAnim);
            set2.start();
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAniming = false;
            }
        });
        set.start();

        resizeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mToolMenuLayoutWidth <= 0)
                    mToolMenuLayoutWidth = mToolMenusLayout.getWidth();

                float val = (float) animation.getAnimatedValue();
                Log.w(TAG, "onAnimationUpdate: val = " + val);
                mToolMenusCursor.setAlpha(val);
                if (val == 0) {
                    mRootView.invalidateToolMenus(isToolPanShowing);
                }

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mToolMenusLayout.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                float newWidth = mToolMenuLayoutWidth * val;
                Log.w(TAG, "onAnimationUpdate: newWidth = " + newWidth);
                if (newWidth < MIN_WIDTH)
                    newWidth = MIN_WIDTH;
                layoutParams.width = (int) (newWidth);

                mToolMenusLayout.setLayoutParams(layoutParams);
                Log.w(TAG, "onAnimationUpdate: getTranslationX = " + mToolMenusLayout.getTranslationX());
            }
        });
    }

    public boolean isToolPanShowing() {
        return isToolPanShowing;
    }

    /**
     * 初始化颜色列表
     */
//    private void initColorList(RecyclerView mRecyclerView) {
//        for (String color : SketchConfig.COLOR_LIST) {
//            int c = Color.parseColor(color);
//            mItems.add(new ToolColorBean(c));
//        }
//        mItems.get(0).setChecked(true);
//        SketchConfig.currColor = mItems.get(0).getColor();
//        invalidateColors(mRecyclerView);
//    }

//    private void invalidateColors(RecyclerView mRecyclerView) {
//        if (mAdapter == null) {
//            // 初始化Adapter
//            mAdapter = new ColorsRecyclerAdapter(mContext, mItems);
//            mAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    mRootView.onColorChanged();
//                }
//            });
//
//            // 初始化RecyclerView
//            mRecyclerView.setAdapter(mAdapter);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            mRecyclerView.setLayoutManager(layoutManager);
//        } else {
//            mAdapter.notifyDataSetChanged(mItems);
//        }
//    }
}
