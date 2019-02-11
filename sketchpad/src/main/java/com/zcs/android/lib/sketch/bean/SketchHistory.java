package com.zcs.android.lib.sketch.bean;

import android.graphics.Path;
import android.graphics.RectF;

import com.zcs.android.lib.sketch.utils.SketchMode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZengCS on 2017/10/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class SketchHistory implements Serializable {
    public static final int MAX_UNDO_TIMES = 10;// 最多只允许撤销10次

    private SketchMode sketchMode;
    private List<DrawPoint> pointList;
    private int color;
    private int width;
    private CustomPath customPath;
    private Path path;
    private RectF rectF;
    private TextParams textParams;
    // 图片列表
    private List<PhotoRecord> photoRecordList;

    public SketchHistory() {
    }

    public SketchHistory(SketchMode sketchMode, int color, List<DrawPoint> pointList) {
        this.sketchMode = sketchMode;
        this.pointList = pointList;
        this.color = color;
    }

    public SketchHistory(SketchMode sketchMode, int color, RectF rectF) {
        this.sketchMode = sketchMode;
        this.color = color;
        this.rectF = rectF;
    }

    public SketchHistory(SketchMode sketchMode, int color, CustomPath customPath) {
        this.sketchMode = sketchMode;
        this.color = color;
        this.customPath = customPath;
    }

    public SketchHistory(SketchMode sketchMode, int color, int width, Path path) {
        this.sketchMode = sketchMode;
        this.color = color;
        this.path = path;
        this.width = width;
    }

    public SketchHistory(SketchMode sketchMode, int color, TextParams textParams) {
        this.sketchMode = sketchMode;
        this.color = color;
        this.textParams = textParams;
    }

    public SketchMode getSketchMode() {
        return sketchMode;
    }

    public int getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<DrawPoint> getPointList() {
        return pointList;
    }

    public CustomPath getCustomPath() {
        return customPath;
    }

    public RectF getRectF() {
        return rectF;
    }

    public TextParams getTextParams() {
        return textParams;
    }

    public List<PhotoRecord> getPhotoRecordList() {
        return photoRecordList;
    }

    public void setPhotoRecordList(List<PhotoRecord> photoRecordList) {
        this.photoRecordList = photoRecordList;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public static class CustomPath {
        List<Point> points;

        public CustomPath(List<Point> points) {
            this.points = points;
        }

        public List<Point> getPoints() {
            return points;
        }
    }

    public static class TextParams {
        private String text;
        private int textSize;
        private float x;
        private float y;
        private int marginCount;

        public TextParams() {
        }

        public TextParams(String text, int textSize, float x, float y, int marginCount) {
            this.text = text;
            this.textSize = textSize;
            this.x = x;
            this.y = y;
            this.marginCount = marginCount;
        }

        public String getText() {
            return text;
        }

        public int getTextSize() {
            return textSize;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public int getMarginCount() {
            return marginCount;
        }
    }

    public static class Point {
        float x;
        float y;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
