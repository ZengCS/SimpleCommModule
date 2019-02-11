package com.zcs.android.lib.sketch.config;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.GeometricBean;
import com.zcs.android.lib.sketch.bean.ToolOptionBean;
import com.zcs.android.lib.sketch.utils.SPKeyMap;
import com.zcs.android.lib.sketch.utils.SPUtils;
import com.zcs.android.lib.sketch.utils.SketchMode;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZengCS on 2017/10/10.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 * <p>
 * 画板自定义配置
 */
public class SketchConfig {
    public static final String TAG_CLEAR_CELL_BG = "TAG_CLEAR_CELL_BG";
    // 添加页面，最多可添加到4张
    public static final int GRID_MAX_SIZE = 4;
    public static final int MIN_WIDTH = 5;

    /**
     * 配置可用的一级工具选项，不需要的工具直接注释即可
     * <p>
     * !!!请勿注释掉所有
     */
    public static List<ToolOptionBean> getToolOptionList(boolean isAll) {
        List<ToolOptionBean> list = new ArrayList<>();
        if (isAll) {
            // 1.输入工具，可设置笔形和字号
            list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_INPUT, R.drawable.ic_sketch_input));
            // 2.属性工具，可设置线条粗细和颜色
            list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_ATTR, R.drawable.ic_sketch_line));
            // 3.图形工具，可绘制各种几何图形
            list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_GEO, R.drawable.ic_sketch_geometric));
            // 4.图片工具，可添加指定和自定义图片
            list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_PIC, R.drawable.ic_sketch_pic));
            // 5.其他工具，包括：橡皮擦，聚光灯
            list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_OTHER, R.drawable.ic_sketch_tool));
        }
        // 6.隐藏和显示工具条
        list.add(new ToolOptionBean(ToolOptionBean.TOOL_OPTION_HIDE, R.drawable.ic_sketch_more));
        return list;
    }

    /**
     * 配置可选的图形，不需要的图形注释掉即可
     * <p>
     * !!!请勿注释掉所有
     */
    public static List<GeometricBean> getGeometricToolList() {
        List<GeometricBean> list = new ArrayList<>();

        list.add(new GeometricBean(SketchMode.Geometric.RECTANGLE));// 矩形
        list.add(new GeometricBean(SketchMode.Geometric.CIRCLE));// 圆形
        list.add(new GeometricBean(SketchMode.Geometric.TRIANGLE));// 正三角形
        list.add(new GeometricBean(SketchMode.Geometric.RA_TRIANGLE));// 直角三角形
        list.add(new GeometricBean(SketchMode.Geometric.PARALLELOGRAM));// 平行四边形
        list.add(new GeometricBean(SketchMode.Geometric.ECHELON));// 梯形
        list.add(new GeometricBean(SketchMode.Geometric.DIAMOND));// 菱形
        list.add(new GeometricBean(SketchMode.Geometric.PENTAGON));// 五边形
        list.add(new GeometricBean(SketchMode.Geometric.LINE));// 直线
        list.add(new GeometricBean(SketchMode.Geometric.QUAD));// 曲线
        list.add(new GeometricBean(SketchMode.Geometric.LINE_WITH_ARROW));// 直线箭头
        list.add(new GeometricBean(SketchMode.Geometric.COORDINATE_AXIS));// 坐标轴

        // 默认选中第一个
        if (list.size() > 0)
            list.get(0).setChecked(true);

        return list;
    }

    // 1.图片保存地址
    public static final String SKETCH_SAVE_DIR = Environment.getExternalStorageDirectory() +
            File.separator + "sxw" + File.separator + "sketch" + File.separator;

    // 2.缩略图保存地址
    public static final String SKETCH_THUMB_DIR = Environment.getExternalStorageDirectory() +
            File.separator + "sxw" + File.separator + "sketch" + File.separator + "thumb" + File.separator;

    // 3.图形线条宽度，单位：像素
    public static final int GEOMETRIC_SIZE = AutoUtils.getPercentWidthSize(5);

    // 4.笔的宽度
    public static float[] PEN_WIDTH_SCALE = {1.0f, 1.4f, 2f, 2f};

    // 5.橡皮擦的宽度
    public static int[] ERASER_WIDTH = {20, 40};

    // 6.暗色
    public static int darkColor = Color.parseColor("#424242");

    // 7.背景色
    public static int bgColor = Color.WHITE;

    // 8.聚光灯边框线条色
    public static final int SPOTLIGHT_BORDER_COLOR = Color.parseColor("#999999");

    // 9.聚光灯背景
    public static final int BG_SPOTLIGHT = Color.argb(128, 0, 0, 0);

    // 10.当前色
    public static String currColorStr = "#fe0100";
    public static int currColor = Color.parseColor(currColorStr);
    public static int currWidth = 5;// 默认的画笔宽度
    private static int currLineWidth = 5;// 默认的线条宽度

    public static int getCurrLineWidth(Context context) {
        currLineWidth = SPUtils.getInt(context, SPKeyMap.KEY_LAST_PEN_WIDTH, 5);
        return currLineWidth;
    }

    public static void setCurrLineWidth(Context context, int newWidth) {
        SPUtils.setParam(context, SPKeyMap.KEY_LAST_PEN_WIDTH, newWidth);
        currLineWidth = newWidth;
    }

    // 11.颜色选择器
    public static final String[] COLOR_LIST = {
            // 第一页
            "#fe0100", "#2a2a2a", "#f8b651", "#ede03c",
            "#b4d565", "#009a43", "#009f96",
            // 第二页
            "#fe0100", "#2a2a2a", "#f8b651", "#ede03c",
            "#b4d565", "#009a43", "#009f96",
            // 第三页
            "#fe0100", "#2a2a2a", "#f8b651", "#ede03c",
            "#b4d565", "#009a43", "#009f96",
    };

    public static int getExtraColor() {
        try {
            String newColor = String.valueOf(currColorStr);
            if (newColor.startsWith("#")) {
                newColor = newColor.replace("#", "#30");
                return Color.parseColor(newColor);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
