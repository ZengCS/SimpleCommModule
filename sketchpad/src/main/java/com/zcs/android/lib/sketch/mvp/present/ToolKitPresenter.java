package com.zcs.android.lib.sketch.mvp.present;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.adapter.CellRecyclerAdapter;
import com.zcs.android.lib.sketch.adapter.ColorsRecyclerAdapter;
import com.zcs.android.lib.sketch.adapter.GeometricRecyclerAdapter;
import com.zcs.android.lib.sketch.adapter.PicRecyclerAdapter;
import com.zcs.android.lib.sketch.adapter.SeekBarChangedAdapter;
import com.zcs.android.lib.sketch.adapter.SimpleViewPagerAdapter;
import com.zcs.android.lib.sketch.adapter.ToolOptionsRecyclerAdapter;
import com.zcs.android.lib.sketch.bean.CellBean;
import com.zcs.android.lib.sketch.bean.GeometricBean;
import com.zcs.android.lib.sketch.bean.ToolColorBean;
import com.zcs.android.lib.sketch.bean.ToolOptionBean;
import com.zcs.android.lib.sketch.config.SketchConfig;
import com.zcs.android.lib.sketch.listener.OnItemClickListener;
import com.zcs.android.lib.sketch.mvp.base.BasePresenter;
import com.zcs.android.lib.sketch.mvp.view.ISketchView;
import com.zcs.android.lib.sketch.utils.SketchMode;
import com.zcs.android.lib.sketch.view.NoScrollViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZengCS on 2017/10/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ToolKitPresenter extends BasePresenter {
    private ISketchView mRootView;

    private int mPenType = SketchMode.Pen.PENCIL;
    private int mGeometricType = SketchMode.Geometric.RECTANGLE;
    private int mTextType = SketchMode.Text.NONE;
    // 工具
    private ToolOptionsRecyclerAdapter mToolOptionsRecyclerAdapter;
    private List<ToolOptionBean> mToolOptionList = new ArrayList<>();

    // Geometric
    private GeometricRecyclerAdapter mGeometricRecyclerAdapter;
    private List<GeometricBean> mGeometricItems = new ArrayList<>();

    private SketchMode mSketchModePen = new SketchMode(SketchMode.Mode.PEN_WRITE, SketchMode.Pen.PENCIL);
    private SketchMode mSketchModeText = new SketchMode(SketchMode.Mode.TEXT, SketchMode.Text.NORMAL);
    private SketchMode mSketchModeGeometric = new SketchMode(SketchMode.Mode.GEOMETRIC, SketchMode.Geometric.RECTANGLE);

    // Pic
    private RecyclerView mPicRecyclerView;
    private PicRecyclerAdapter mPicAdapter;
    private List<CellBean> mPicList = new ArrayList<>();

    // Eraser
    private View mNewEraser, mLight;
    private RecyclerView mCellRecyclerView;
    private CellRecyclerAdapter mCellRecyclerAdapter;
    private List<CellBean> mCellList = new ArrayList<>();

    // 颜色列表
    private List<ToolColorBean> mItems = new ArrayList<>();
    private ColorsRecyclerAdapter mAdapter;

    // Pen
    private static final int[] PEN_NORMAL_IDS = {
            R.drawable.ic_pen_1_normal,
            R.drawable.ic_pen_2_normal,
            R.drawable.ic_pen_3_normal,
            R.drawable.ic_pen_4_normal
    };

    private static final int[] PEN_CHECKED_IDS = {
            R.drawable.ic_pen_1_checked,
            R.drawable.ic_pen_2_checked,
            R.drawable.ic_pen_3_checked,
            R.drawable.ic_pen_4_checked,
    };
    private ImageView[] mPens = new ImageView[4];
    // Text
    private TextView[] mTextArr = new TextView[3];

    public ToolKitPresenter(Context mContext, ISketchView sketchView) {
        super(mContext);
        this.mRootView = sketchView;
    }

    /**
     * 初始化颜色列表
     */
    private void initColorList(RecyclerView mRecyclerView) {
        for (String color : SketchConfig.COLOR_LIST) {
            int c = Color.parseColor(color);
            mItems.add(new ToolColorBean(c, color));
        }
        ToolColorBean firstColorBean = mItems.get(0);
        if (firstColorBean != null) {
            firstColorBean.setChecked(true);
            SketchConfig.currColorStr = firstColorBean.getColorString();
            SketchConfig.currColor = firstColorBean.getColor();
        }
        invalidateColors(mRecyclerView);
    }

    private void invalidateColors(RecyclerView mRecyclerView) {
        if (mAdapter == null) {
            // 初始化Adapter
            mAdapter = new ColorsRecyclerAdapter(mContext, mItems);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    changeColor();
                }
            });

            // 初始化RecyclerView
            mRecyclerView.setAdapter(mAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            mAdapter.notifyDataSetChanged(mItems);
        }
    }

    private void invalidatePics() {
        if (mPicAdapter == null) {
            // 初始化Adapter
            mPicAdapter = new PicRecyclerAdapter(mContext, mPicList);
            mPicAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    CellBean cellBean = mPicList.get(position);
                    mRootView.addPicByPath(cellBean.getPath());
                }
            });

            // 初始化RecyclerView
            mPicRecyclerView.setAdapter(mPicAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mPicRecyclerView.setLayoutManager(layoutManager);
        } else {
            mPicAdapter.notifyDataSetChanged(mPicList);
        }
    }

    private void invalidateCells() {
        if (mCellRecyclerAdapter == null) {
            // 初始化Adapter
            mCellRecyclerAdapter = new CellRecyclerAdapter(mContext, mCellList);
            mCellRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 10000) {// 执行清空操作
                        mRootView.sketchSetBgByPath(SketchConfig.TAG_CLEAR_CELL_BG);
                        return;
                    }
                    CellBean cellBean = mCellList.get(position);
                    try {
                        mRootView.sketchSetBgByPath(cellBean.getBgPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "设置背景失败，" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 初始化RecyclerView
            mCellRecyclerView.setAdapter(mCellRecyclerAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mCellRecyclerView.setLayoutManager(layoutManager);
        } else {
            mCellRecyclerAdapter.notifyDataSetChanged(mCellList);
        }
    }

    /**
     * 初始化图形列表
     */
    private void initGeometric(RecyclerView geoRecyclerView) {
        if (mGeometricItems.size() > 0)
            mGeometricItems.clear();
        // 默认选中第一个
        mGeometricItems.addAll(SketchConfig.getGeometricToolList());

        if (mGeometricRecyclerAdapter == null) {
            mGeometricRecyclerAdapter = new GeometricRecyclerAdapter(mContext, mGeometricItems);
            mGeometricRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    try {
                        GeometricBean geometricBean = mGeometricItems.get(position);
                        updateGeoChecked(geometricBean.getType());
                        updateEraser(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            geoRecyclerView.setLayoutManager(layoutManager);
            geoRecyclerView.setAdapter(mGeometricRecyclerAdapter);
        } else {
            mGeometricRecyclerAdapter.notifyDataSetChanged(mGeometricItems);
        }
    }

    /**
     * 更新绘画模式
     */
    private void updateSketchMode(int modeType, int drawType) {
        SketchMode sketchMode = new SketchMode(modeType, drawType);
        switch (modeType) {
            case SketchMode.Mode.PEN_WRITE:
                this.mSketchModePen = sketchMode;
                break;
            case SketchMode.Mode.TEXT:
                this.mSketchModeText = sketchMode;
                break;
            case SketchMode.Mode.GEOMETRIC:
                this.mSketchModeGeometric = sketchMode;
                break;
        }
        mRootView.setSketchMode(sketchMode);
    }

    private void updateTextChecked(int textType) {
        boolean isEmpty = mRootView.updateFontSize(textType);

        if (mTextType == textType && textType != -1) {
            if (isEmpty)
                updateSketchMode(SketchMode.Mode.TEXT, textType);
            return;
        }
        mTextType = textType;

        for (int i = 0; i < 3; i++) {
            if (i == mTextType) {
                mTextArr[i].setTextColor(SketchConfig.currColor);
                mTextArr[i].setBackgroundResource(R.drawable.bg_geometric_checked);
            } else {
                mTextArr[i].setBackgroundResource(0);
                mTextArr[i].setTextColor(SketchConfig.darkColor);
            }
        }
        if (isEmpty && textType != -1)
            updateSketchMode(SketchMode.Mode.TEXT, textType);
    }

    private void updatePenChecked(int penType) {
        if (mPenType == penType) return;
        mPenType = penType;
        for (int i = 0; i < 4; i++) {
            if (i == penType) {
                mPens[i].setImageResource(PEN_CHECKED_IDS[i]);
            } else {
                mPens[i].setImageResource(PEN_NORMAL_IDS[i]);
            }
        }
        if (penType != -1)
            updateSketchMode(SketchMode.Mode.PEN_WRITE, penType);
    }

    private void updateGeoChecked(int geometricType) {
        mGeometricType = geometricType;
        updateSketchMode(SketchMode.Mode.GEOMETRIC, geometricType);
    }

    private void updateEraser(boolean effective) {
        mNewEraser.setBackgroundResource(0);
        // 生效文字
        if (effective) {
            mRootView.effectiveDragText();
            mTextType = -1;
        }
    }

    public void updateToolKit(int option) {
        switch (option) {
            case 0:// 笔形
                updateEraser(false);
                if (mTextType != -1)
                    updateSketchMode(SketchMode.Mode.TEXT, mTextType);
                else
                    updateSketchMode(SketchMode.Mode.PEN_WRITE, mPenType);
                break;
            case 1:// 属性
                updateEraser(false);
                break;
            case 2:// 图形
                updateEraser(true);
                updateSketchMode(SketchMode.Mode.GEOMETRIC, mGeometricType);
                break;
            case 3:// 图片
                updateEraser(true);
                updateSketchMode(SketchMode.Mode.EDIT_PIC, SketchMode.MODE_DFT);
                break;
            case 4:// 其他工具
                updateEraser(true);
                break;
        }
    }

    private void changeColor() {
        invalidateGeometric();

        for (int i = 0; i < 3; i++) {
            if (i == mTextType)
                mTextArr[i].setTextColor(SketchConfig.currColor);
            else
                mTextArr[i].setTextColor(SketchConfig.darkColor);
        }
        mRootView.onColorChanged();
    }

    private void invalidateGeometric() {
        if (mGeometricRecyclerAdapter != null)
            mGeometricRecyclerAdapter.notifyDataSetChanged(mGeometricItems);
    }

    /**
     * 初始化工具页
     */
    public void initSketchToolKit(NoScrollViewPager toolViewPager) {
        List<View> pageList = new ArrayList<>();
        View p1 = LayoutInflater.from(mContext).inflate(R.layout.page_tool_input, null);
        View p2 = LayoutInflater.from(mContext).inflate(R.layout.page_tool_line, null);
        View p3 = LayoutInflater.from(mContext).inflate(R.layout.page_tool_geometric, null);
        View p4 = LayoutInflater.from(mContext).inflate(R.layout.page_tool_pic, null);
        View p5 = LayoutInflater.from(mContext).inflate(R.layout.page_tool_other, null);
        pageList.add(p1);
        pageList.add(p2);
        pageList.add(p3);
        pageList.add(p4);
        pageList.add(p5);

        // 初始化笔形
        initPens(p1);

        // 初始字体
        initFonts(p1);

        // 初始化颜色列表
        initColorAndLine(p2);

        // Geometric
        RecyclerView geoRecyclerView = (RecyclerView) p3.findViewById(R.id.id_rv_tool_geometric);
        initGeometric(geoRecyclerView);

        // 图片
        initPicTools(p4);

        // 初始化橡皮擦 & 聚光灯 & 背景格子
        initOtherTools(p5);

        toolViewPager.setNoScroll(true);
        toolViewPager.setAdapter(new SimpleViewPagerAdapter(pageList));
    }

    private void initColorAndLine(View view) {
        RecyclerView colorRecyclerView = view.findViewById(R.id.id_rv_tool_colors);
        final TextView textView = view.findViewById(R.id.id_tv_line_size);

        initColorList(colorRecyclerView);
        SeekBar seekBar = view.findViewById(R.id.id_sb_line_size);
        seekBar.setProgress(SketchConfig.getCurrLineWidth(mContext));
        textView.setText(String.valueOf(SketchConfig.getCurrLineWidth(mContext)));

        seekBar.setOnSeekBarChangeListener(new SeekBarChangedAdapter() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 修改笔形
                mRootView.savePenWidth();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < SketchConfig.MIN_WIDTH) {
                    seekBar.setProgress(SketchConfig.MIN_WIDTH);
                    return;
                }
                SketchConfig.setCurrLineWidth(mContext, progress);
                textView.setText(String.valueOf(progress));
            }
        });
    }

    private void initPicTools(View view) {
        mPicRecyclerView = view.findViewById(R.id.id_rv_tool_pic);
        try {
            String assetsPath = "obj";
            String[] list = mContext.getAssets().list(assetsPath);
            for (String s : list) {
                CellBean cellBean = new CellBean();
                cellBean.setName("assets/" + s);
                cellBean.setPath(assetsPath + "/" + s);
                mPicList.add(cellBean);
            }
            invalidatePics();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOtherTools(View view) {
        mNewEraser = view.findViewById(R.id.id_btn_eraser);
        mLight = view.findViewById(R.id.id_btn_light);
        mCellRecyclerView = view.findViewById(R.id.id_rv_tool_cell);
        initBgCell();
        // 添加点击事件
        mNewEraser.setOnClickListener(mOtherToolClickListener);
        mLight.setOnClickListener(mOtherToolClickListener);
    }

    private void initBgCell() {
        try {
            String assetsPath = "cell";
            String[] list = mContext.getAssets().list(assetsPath);
            for (String s : list) {
                CellBean cellBean = new CellBean();
                cellBean.setName("assets/" + s);
                cellBean.setPath(assetsPath + "/" + s);
                mCellList.add(cellBean);
            }
            invalidateCells();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化字体
     */
    private void initFonts(View view) {
        TextView textView1 = (TextView) view.findViewById(R.id.id_btn_text_1);
        TextView textView2 = (TextView) view.findViewById(R.id.id_btn_text_2);
        TextView textView3 = (TextView) view.findViewById(R.id.id_btn_text_3);
        textView1.setOnClickListener(mFontClickListener);
        textView2.setOnClickListener(mFontClickListener);
        textView3.setOnClickListener(mFontClickListener);
        mTextArr[0] = textView1;
        mTextArr[1] = textView2;
        mTextArr[2] = textView3;
        // 默认选中第一个
        // textView1.setBackgroundResource(R.drawable.bg_geometric_checked);
        // textView1.setTextColor(SketchConfig.currColor);
    }

    /**
     * 初始化笔形
     */
    private void initPens(View view) {
        ImageView pen1 = (ImageView) view.findViewById(R.id.id_btn_pen_1);
        ImageView pen2 = (ImageView) view.findViewById(R.id.id_btn_pen_2);
        ImageView pen3 = (ImageView) view.findViewById(R.id.id_btn_pen_3);
        ImageView pen4 = (ImageView) view.findViewById(R.id.id_btn_pen_4);
        pen1.setOnClickListener(mPenClickListener);
        pen2.setOnClickListener(mPenClickListener);
        pen3.setOnClickListener(mPenClickListener);
        pen4.setOnClickListener(mPenClickListener);
        mPens[0] = pen1;
        mPens[1] = pen2;
        mPens[2] = pen3;
        mPens[3] = pen4;
    }

    // ---------------------------- 点击事件监听 ----------------------------
    /**
     * 其他工具点击事件监听
     */
    private View.OnClickListener mOtherToolClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_btn_eraser) {// 橡皮擦
                mRootView.setSketchMode(new SketchMode(SketchMode.Mode.ERASER, SketchMode.Eraser.LARGE));
                mNewEraser.setBackgroundResource(R.drawable.bg_geometric_checked);
                mLight.setBackgroundResource(0);
                CharSequence lastFlag = mLight.getContentDescription();
                if ("1".equals(lastFlag))
                    mLight.setContentDescription("2");
            } else if (v.getId() == R.id.id_btn_light) {// 聚光灯
                CharSequence flag = mLight.getContentDescription();
                if ("0".equals(flag)) {// 当前未选中
                    mLight.setContentDescription("1");
                    mRootView.setSketchMode(new SketchMode(SketchMode.Mode.SPOTLIGHT, SketchMode.MODE_DFT));
                    mLight.setBackgroundResource(R.drawable.bg_geometric_checked);
                } else if ("1".equals(flag)) {// 当前已选中
                    cleanSpotlightState();
                } else {// 被动
                    mLight.setContentDescription("1");
                    mLight.setBackgroundResource(R.drawable.bg_geometric_checked);
                }
                mNewEraser.setBackgroundResource(0);
            }
        }
    };

    /**
     * 清除聚光灯状态
     */
    public void cleanSpotlightState() {
        try {
            CharSequence flag = mLight.getContentDescription();
            if ("1".equals(flag)) {// 当前已选中
                mLight.setContentDescription("0");
                mRootView.cleanSpotlight();
                mLight.setBackgroundResource(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //不设置默认画笔
    public void noPenDefault() {
        updatePenChecked(-1);
    }

    /**
     * 笔形点击事件监听
     */
    private View.OnClickListener mPenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_btn_pen_1) {
                updatePenChecked(SketchMode.Pen.PENCIL);
            } else if (v.getId() == R.id.id_btn_pen_2) {
                updatePenChecked(SketchMode.Pen.PEN);
            } else if (v.getId() == R.id.id_btn_pen_3) {
                updatePenChecked(SketchMode.Pen.BRUSH);
            } else if (v.getId() == R.id.id_btn_pen_4) {
                updatePenChecked(SketchMode.Pen.CHALK);
            }
            // 取消橡皮擦
            updateEraser(true);
            // 取消字体
            updateTextChecked(-1);
        }
    };

    /**
     * 字体点击事件监听
     */
    private View.OnClickListener mFontClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.id_btn_text_1) {
                updateTextChecked(SketchMode.Text.NORMAL);
            } else if (v.getId() == R.id.id_btn_text_2) {
                updateTextChecked(SketchMode.Text.BIG);
            } else if (v.getId() == R.id.id_btn_text_3) {
                updateTextChecked(SketchMode.Text.LARGE);
            }
            // 取消橡皮擦
            updateEraser(false);
            // 取消笔形
            updatePenChecked(-1);
        }
    };

    /**
     * 初始化工具选项
     */
    public void initToolOptions(RecyclerView recyclerView) {
        mToolOptionList.clear();
        mToolOptionList.addAll(SketchConfig.getToolOptionList(true));
        if (mToolOptionsRecyclerAdapter == null) {
            mToolOptionsRecyclerAdapter = new ToolOptionsRecyclerAdapter(mContext, mToolOptionList);
            mToolOptionsRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    onToolClickEvent(position);
                }
            });
            recyclerView.setAdapter(mToolOptionsRecyclerAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            mToolOptionsRecyclerAdapter.notifyDataSetChanged(mToolOptionList);
        }
    }

    public void onToolClickEvent(int position) {
        try {
            ToolOptionBean bean = mToolOptionList.get(position);
            mRootView.onOptionChanged(bean.getOption(), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invalidateToolOptions(boolean showAll) {
        mToolOptionList.clear();
        mToolOptionList.addAll(SketchConfig.getToolOptionList(showAll));
        if (mToolOptionsRecyclerAdapter != null)
            mToolOptionsRecyclerAdapter.notifyDataSetChanged(mToolOptionList);
    }
}
