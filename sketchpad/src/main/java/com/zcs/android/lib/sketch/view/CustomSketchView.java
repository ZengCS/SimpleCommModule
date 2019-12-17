//package com.zcs.android.lib.sketch.view;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.CustomPath;
//import android.graphics.PointF;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.text.Layout;
//import android.text.StaticLayout;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.widget.Toast;
//
//import com.zcs.android.lib.sketch.R;
//import com.zcs.android.lib.sketch.base.IBaseSketchView;
//import com.zcs.android.lib.sketch.bean.Bezier;
//import com.zcs.android.lib.sketch.bean.ControlTimedPoints;
//import com.zcs.android.lib.sketch.bean.DashRectangle;
//import com.zcs.android.lib.sketch.bean.DrawPoint;
//import com.zcs.android.lib.sketch.bean.PhotoRecord;
//import com.zcs.android.lib.sketch.bean.SketchHistory;
//import com.zcs.android.lib.sketch.bean.TimedPoint;
//import com.zcs.android.lib.sketch.config.SketchConfig;
//import com.zcs.android.lib.sketch.helper.ISketchPenHelper;
//import com.zcs.android.lib.sketch.helper.ISketchViewHelper;
//import com.zcs.android.lib.sketch.helper.SketchPenHelper;
//import com.zcs.android.lib.sketch.helper.SketchViewHelper;
//import com.zcs.android.lib.sketch.utils.BitmapUtil;
//import com.zcs.android.lib.sketch.utils.SketchMode;
//import com.zhy.autolayout.utils.AutoUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class CustomSketchView extends View implements IBaseSketchView {
//    private static final String TAG = "CustomSketchView";
//    private int mOperateType = SketchMode.OperateType.OPERATE_DRAW;
//    private int mResizeDirection = DashRectangle.Direction.OUTSIDE;
//    private int mTouchOutsideTimes = 0;// 点击外面的次数
//    private long mLastTouchOutsideTime = 0;// 上次点击外面的时间
//    private static final int MIN_DRAG_DISTANCE = 2;// 至少移动X个像素才做拖拽处理
//    private Context mContext;
//    // Helper
//    private ISketchViewHelper mSketchViewHelper;
//    private ISketchPenHelper mSketchPenHelper;
//
//    //Configurable parameters
//    private float mLastVelocity;
//    private float mLastWidth;
//
//    private static Random random = new Random();
//
//    //View state
//    private List<TimedPoint> mPoints;
//    private float mLastDownX = 0f, mLastDownY = 0f;// 画图时,第一次点击的坐标
//    private float mDragDownX = 0f, mDragDownY = 0f;// 拖拽时,第一次点击的坐标
//    private float mDragMoveX = 0f, mDragMoveY = 0f;// 上一次移动的坐标
//    private RectF mDirtyRect;
//
//    private SketchMode mSketchMode;
//
//    // Dash Rectangle
//    private DashRectangle mDashRect;
//
//    // Cache
//    private List<TimedPoint> mPointsCache = new ArrayList<>();
//    private ControlTimedPoints mControlTimedPointsCached = new ControlTimedPoints();
//    private Bezier mBezierCached = new Bezier();
//    private List<DrawPoint> mDrawPointList = new ArrayList<>();
//    private List<SketchHistory> mHistoryList = new ArrayList<>();
//    private List<SketchHistory> mRedoList = new ArrayList<>();
//    private int mCanUndoTimes = 0;// 剩余可撤销次数
//    private int mCanRedoTimes = 0;// 剩余可恢复次数
//
//    //Configurable parameters
//    private float mWidth = 10;
//    private float mLastPressure = -1f;
//    private List<Float> mPressureList = new ArrayList<>();
//    private boolean isLastEvent = false;// 是否是最后一次触发事件
//
//    // CustomPath
//    private CustomPath mGeometricPath;
//    private CustomPath mDashLinePath;
//
//    // Paint
//    private Paint mPenPaint;
//    private Paint mExtraPointPaint;
//    private Paint mEraserPaint;
//    private Paint mGeometricPaint;
//    private Paint mSpotlightPaint;
//    private TextPaint mTextPaint;
//
//    private Bitmap mBgBitmap = null;
//    // 勾画相关
//    private Bitmap mSketchBitmap = null;
//    private Canvas mSketchCanvas = null;
//    // 矢量图相关
//    private Bitmap mGeometricBitmap = null;
//    private Canvas mGeometricCanvas = null;
//    // 聚光灯相关
//    private Bitmap mSpotlightBitmap = null;
//    private Canvas mSpotlightCanvas = null;
//
//    public CustomSketchView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        init();
//
//        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
//            @Override
//            public boolean onScale(ScaleGestureDetector detector) {
//                onScaleAction(detector);
//                return true;
//            }
//
//            @Override
//            public boolean onScaleBegin(ScaleGestureDetector detector) {
//                return true;
//            }
//
//            @Override
//            public void onScaleEnd(ScaleGestureDetector detector) {
//
//            }
//        });
//    }
//
//    public void onScaleAction(ScaleGestureDetector detector) {
//        float[] photoCorners = calculateCorners(curPhotoRecord);
//        //目前图片对角线长度
//        float len = (float) Math.sqrt(Math.pow(photoCorners[0] - photoCorners[4], 2) + Math.pow(photoCorners[1] - photoCorners[5], 2));
//        double photoLen = Math.sqrt(Math.pow(curPhotoRecord.photoRectSrc.width(), 2) + Math.pow(curPhotoRecord.photoRectSrc.height(), 2));
//        float scaleFactor = detector.getScaleFactor();
//        //设置Matrix缩放参数
//        if ((scaleFactor < 1 && len >= photoLen * SCALE_MIN && len >= SCALE_MIN_LEN) || (scaleFactor > 1 && len <= photoLen * SCALE_MAX)) {
//            Log.e(scaleFactor + "", scaleFactor + "");
//            curPhotoRecord.matrix.postScale(scaleFactor, scaleFactor, photoCorners[8], photoCorners[9]);
//        }
//    }
//
//    private void init() {
//        mSketchViewHelper = new SketchViewHelper();
//        mSketchPenHelper = new SketchPenHelper(this);
//
//        // 默认为钢笔
//        mSketchMode = new SketchMode(SketchMode.Mode.PEN_WRITE, SketchMode.Pen.PENCIL);
//
//        // 初始化画笔
//        mPenPaint = new Paint();
//        mPenPaint.setAntiAlias(true);
//        mPenPaint.setStyle(Paint.Style.STROKE);
//        mPenPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPenPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPenPaint.setColor(SketchConfig.currColor);
//
//        // 边框画笔
//        boardPaint = new Paint();
//        boardPaint.setColor(Color.GRAY);
//        boardPaint.setStrokeWidth(AutoUtils.getPercentWidth1px());
//        boardPaint.setStyle(Paint.Style.STROKE);
//
//        // 初始化画笔
//        mTextPaint = new TextPaint();
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setStyle(Paint.Style.FILL);
//        mTextPaint.setColor(SketchConfig.currColor);
//
//        // 初始化图形画笔
//        mGeometricPaint = new Paint();
//        mGeometricPaint.setAntiAlias(true); //消除锯齿
//        mGeometricPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
//        mGeometricPaint.setStrokeWidth(SketchConfig.GEOMETRIC_SIZE);
//        mGeometricPaint.setStrokeJoin(Paint.Join.MITER);
//
//        mGeometricPath = new CustomPath();
//        mDashLinePath = new CustomPath();
//
//        // 初始化橡皮擦
//        mEraserPaint = new Paint();
//        mEraserPaint.setAlpha(0);
//        //这个属性是设置paint为橡皮擦重中之重
//        //这是重点
//        //下面这句代码是橡皮擦设置的重点
//        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
//        mEraserPaint.setAntiAlias(true);
//        mEraserPaint.setDither(true);
//        mEraserPaint.setStyle(Paint.Style.STROKE);
//        mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
//        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
//        mEraserPaint.setStrokeWidth(SketchConfig.currWidth);
//
//        // 初始化聚光灯画笔
//        mSpotlightPaint = new Paint();
//        mSpotlightPaint.setAlpha(0);
//        //这个属性是设置paint为橡皮擦重中之重
//        //这是重点
//        //下面这句代码是橡皮擦设置的重点
//        mSpotlightPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
//        mSpotlightPaint.setAntiAlias(true);
//        mSpotlightPaint.setDither(true);
//        mSpotlightPaint.setStyle(Paint.Style.FILL);
//        mSpotlightPaint.setStrokeCap(Paint.Cap.ROUND);
//        mSpotlightPaint.setStrokeJoin(Paint.Join.ROUND);
//        // mSpotlightPaint.setStrokeWidth(SketchConfig.currWidth);
//
//        // 干扰点画笔
//        mExtraPointPaint = new Paint();
////        mExtraPointPaint.setAlpha(0);
////        mExtraPointPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
////        mExtraPointPaint.setDither(true);
//        mExtraPointPaint.setAntiAlias(true);
//        mExtraPointPaint.setStyle(Paint.Style.FILL);
//        mExtraPointPaint.setStrokeCap(Paint.Cap.ROUND);
//        mExtraPointPaint.setStrokeJoin(Paint.Join.ROUND);
//        mExtraPointPaint.setStrokeWidth(SketchConfig.currWidth);
//        mExtraPointPaint.setColor(SketchConfig.getExtraColor());
//
//        // Dirty rectangle to update only the changed portion of the view
//        mDirtyRect = new RectF();
//
//        // Dash Rectangle
//        mDashRect = new DashRectangle();
//
//        clearView(true);
//
//        mSketchPenHelper.init(mPenPaint);
//    }
//
//    private void clearForSpotlight() {
//        mSpotlightBitmap = null;
//        ensureSpotlightBitmap();
//        ensureSketchBitmap();
//        mGeometricPath.reset();
//        mDashLinePath.reset();
//    }
//
//    private void clearForGeometric() {
//        mGeometricBitmap = null;
//        ensureGeometricBitmap();
//        ensureSketchBitmap();
//        mGeometricPath.reset();
//        mDashLinePath.reset();
//        mGeometricPaint.setColor(SketchConfig.currColor);
//    }
//
//    private void clearView(boolean isClearBg) {
//        // 是否清空背景图
//        if (isClearBg) {
//            if (mBgBitmap != null && !mBgBitmap.isRecycled()) {
//                // 回收并且置为null
//                mBgBitmap.recycle();
//                mBgBitmap = null;
//            }
//        }
//        clearForUndo();
//    }
//
//    private void clearForUndo() {
//        mSketchPenHelper.cleanPoints(true);
//        mPoints = new ArrayList<>();
//
//        if (mSketchBitmap != null) {
//            mSketchBitmap.recycle();
//            mSketchBitmap = null;
//            ensureSketchBitmap();
//        }
//        if (mGeometricBitmap != null) {
//            mGeometricBitmap.recycle();
//            mGeometricBitmap = null;
//            ensureGeometricBitmap();
//        }
//        if (mSpotlightBitmap != null) {
//            mSpotlightBitmap.recycle();
//            mSpotlightBitmap = null;
//        }
//        System.gc();
//        invalidate();
//    }
//
//    int[] location = new int[2];
//    public float downX, downY, preX, preY, curX, curY;
//    public int actionMode;
//    public static final int ACTION_NONE = 0;
//    public static final int ACTION_DRAG = 1;
//    public static final int ACTION_SCALE = 2;
//    public static final int ACTION_ROTATE = 3;
//
//    private boolean customEditPicEvent(MotionEvent event) {
//        getLocationInWindow(location); //获取在当前窗口内的绝对坐标
//        curX = (event.getRawX() - location[0]) / drawDensity;
//        curY = (event.getRawY() - location[1]) / drawDensity;
//        int toolType = event.getToolType(0);
////        //检测到手指点击自动进入拖动图片模式
////        if (toolType == MotionEvent.TOOL_TYPE_FINGER&&curSketchData.editMode == EDIT_STROKE) {
////            curSketchData.editMode = EDIT_PHOTO;
////        } else if (toolType == MotionEvent.TOOL_TYPE_STYLUS){//检测到手写板开始绘画则自动进入绘画模式
////            curSketchData.editMode = EDIT_STROKE;
////        }
////        Log.d(getClass().getSimpleName(), "onTouch======" + toolType);
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_POINTER_DOWN:
//                float downDistance = spacing(event);
//                if (actionMode == ACTION_DRAG && downDistance > 10)//防止误触
//                    actionMode = ACTION_SCALE;
//                break;
//            case MotionEvent.ACTION_DOWN:
//                touchDown();
//                invalidate();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                touchMove(event);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                touchUp();
//                invalidate();
//                break;
//        }
//        preX = curX;
//        preY = curY;
//        return true;
//    }
//
//    public void touchUp() {
//    }
//
//    public void touchMove(MotionEvent event) {
//        if (mSketchMode.getModeType() == SketchMode.Mode.EDIT_PIC && curPhotoRecord != null) {
//            if (actionMode == ACTION_DRAG) {
//                onDragAction((curX - preX) * drawDensity, (curY - preY) * drawDensity);
//            } else if (actionMode == ACTION_ROTATE) {
//                onRotateAction(curPhotoRecord);
//            } else if (actionMode == ACTION_SCALE) {
//                mScaleGestureDetector.onTouchEvent(event);
//            }
//        }
//        preX = curX;
//        preY = curY;
//    }
//
//    public void onRotateAction(PhotoRecord record) {
//        float[] corners = calculateCorners(record);
//        //放大
//        //目前触摸点与图片显示中心距离,curX*drawDensity为还原缩小密度点数值
//        float a = (float) Math.sqrt(Math.pow(curX * drawDensity - corners[8], 2) + Math.pow(curY * drawDensity - corners[9], 2));
//        //目前上次旋转图标与图片显示中心距离
//        float b = (float) Math.sqrt(Math.pow(corners[4] - corners[0], 2) + Math.pow(corners[5] - corners[1], 2)) / 2;
////        Log.e(TAG, "onRotateAction: a=" + a + ";b=" + b);
//        //设置Matrix缩放参数
//        double photoLen = Math.sqrt(Math.pow(record.photoRectSrc.width(), 2) + Math.pow(record.photoRectSrc.height(), 2));
//        if (a >= photoLen / 2 * SCALE_MIN && a >= SCALE_MIN_LEN && a <= photoLen / 2 * SCALE_MAX) {
//            //这种计算方法可以保持旋转图标坐标与触摸点同步缩放
//            float scale = a / b;
//            record.matrix.postScale(scale, scale, corners[8], corners[9]);
//        }
//
//        //旋转
//        //根据移动坐标的变化构建两个向量，以便计算两个向量角度.
//        PointF preVector = new PointF();
//        PointF curVector = new PointF();
//        preVector.set((preX * drawDensity - corners[8]), preY * drawDensity - corners[9]);//旋转后向量
//        curVector.set(curX * drawDensity - corners[8], curY * drawDensity - corners[9]);//旋转前向量
//        //计算向量长度
//        double preVectorLen = getVectorLength(preVector);
//        double curVectorLen = getVectorLength(curVector);
//        //计算两个向量的夹角.
//        double cosAlpha = (preVector.x * curVector.x + preVector.y * curVector.y)
//                / (preVectorLen * curVectorLen);
//        //由于计算误差，可能会带来略大于1的cos，例如
//        if (cosAlpha > 1.0f) {
//            cosAlpha = 1.0f;
//        }
//        //本次的角度已经计算出来。
//        double dAngle = Math.acos(cosAlpha) * 180.0 / Math.PI;
//        // 判断顺时针和逆时针.
//        //判断方法其实很简单，这里的v1v2其实相差角度很小的。
//        //先转换成单位向量
//        preVector.x /= preVectorLen;
//        preVector.y /= preVectorLen;
//        curVector.x /= curVectorLen;
//        curVector.y /= curVectorLen;
//        //作curVector的逆时针垂直向量。
//        PointF verticalVec = new PointF(curVector.y, -curVector.x);
//
//        //判断这个垂直向量和v1的点积，点积>0表示俩向量夹角锐角。=0表示垂直，<0表示钝角
//        float vDot = preVector.x * verticalVec.x + preVector.y * verticalVec.y;
//        if (vDot > 0) {
//            //v2的逆时针垂直向量和v1是锐角关系，说明v1在v2的逆时针方向。
//        } else {
//            dAngle = -dAngle;
//        }
//        record.matrix.postRotate((float) dAngle, corners[8], corners[9]);
//    }
//
//    /**
//     * 获取p1到p2的线段的长度
//     *
//     * @return
//     */
//    public double getVectorLength(PointF vector) {
//        return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
//    }
//
//    public void onDragAction(float distanceX, float distanceY) {
//        curPhotoRecord.matrix.postTranslate((int) distanceX, (int) distanceY);
//    }
//
//    public void touchDown() {
//        downX = curX;
//        downY = curY;
//        if (mSketchMode.getModeType() == SketchMode.Mode.EDIT_PIC) {
//            float[] downPoint = new float[]{downX * drawDensity, downY * drawDensity};//还原点倍数
//            if (isInMarkRect(downPoint)) {// 先判操作标记区域
//                return;
//            }
//            if (isInPhotoRect(curPhotoRecord, downPoint)) {//再判断是否点击了当前图片
//                actionMode = ACTION_DRAG;
//                return;
//            }
//            selectPhoto(downPoint);//最后判断是否点击了其他图片
//        }
//    }
//
//    public void setCurPhotoRecord(PhotoRecord record) {
//        photoRecordList.remove(record);
//        photoRecordList.add(record);
//        curPhotoRecord = record;
//        invalidate();
//    }
//
//    //judge click which photo，then can edit the photo
//    public void selectPhoto(float[] downPoint) {
//        PhotoRecord clickRecord = null;
//        for (int i = photoRecordList.size() - 1; i >= 0; i--) {
//            PhotoRecord record = photoRecordList.get(i);
//            if (isInPhotoRect(record, downPoint)) {
//                clickRecord = record;
//                break;
//            }
//        }
//        if (clickRecord != null) {
//            setCurPhotoRecord(clickRecord);
//            actionMode = ACTION_DRAG;
//        } else {
//            actionMode = ACTION_NONE;
//        }
//    }
//
//
//    public boolean isInPhotoRect(PhotoRecord record, float[] downPoint) {
//        if (record != null) {
//            float[] invertPoint = new float[2];
//            Matrix invertMatrix = new Matrix();
//            record.matrix.invert(invertMatrix);
//            invertMatrix.mapPoints(invertPoint, downPoint);
//            return record.photoRectSrc.contains(invertPoint[0], invertPoint[1]);
//        }
//        return false;
//    }
//
//    public boolean isInMarkRect(float[] downPoint) {
//        if (markerRotateRect.contains(downPoint[0], (int) downPoint[1])) {//判断是否在区域内
//            actionMode = ACTION_ROTATE;
//            return true;
//        }
//        if (markerDeleteRect.contains(downPoint[0], (int) downPoint[1])) {//判断是否在区域内
//            photoRecordList.remove(curPhotoRecord);
//            setCurPhotoRecord(null);
//            actionMode = ACTION_NONE;
//            return true;
//        }
//        if (markerCopyRect.contains(downPoint[0], (int) downPoint[1])) {//判断是否在区域内
//            PhotoRecord newRecord = initPhotoRecord(curPhotoRecord.bitmap);
//            newRecord.matrix = new Matrix(curPhotoRecord.matrix);
//            newRecord.matrix.postTranslate(AutoUtils.getPercentWidthSize(40), AutoUtils.getPercentWidthSize(40));//偏移小段距离以分辨新复制的图片
//            setCurPhotoRecord(newRecord);
//            actionMode = ACTION_NONE;
//            return true;
//        }
//        if (markerResetRect.contains(downPoint[0], (int) downPoint[1])) {//判断是否在区域内
//            curPhotoRecord.matrix.reset();
//            curPhotoRecord.matrix.setTranslate(getWidth() / 2 - curPhotoRecord.photoRectSrc.width() / 2,
//                    getHeight() / 2 - curPhotoRecord.photoRectSrc.height() / 2);
//            actionMode = ACTION_NONE;
//            return true;
//        }
//        return false;
//    }
//
//    public float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!isEnabled())
//            return false;
//        isLastEvent = false;
//        if (mSketchMode.getModeType() == SketchMode.Mode.EDIT_PIC) {
//            customEditPicEvent(event);
//        } else if (mOperateType == SketchMode.OperateType.OPERATE_DRAW) {
//            // 绘制图形
//            boolean result = customDrawEvent(event);
//            if (!result)
//                return false;
//        } else {
//            // 拖拽图形
//            int result = customDragGeometric(event);
//            if (result != 1) {
//                return false;
//            }
//        }
//        invalidate((int) (mDirtyRect.left - mWidth), (int) (mDirtyRect.top - mWidth),
//                (int) (mDirtyRect.right + mWidth), (int) (mDirtyRect.bottom + mWidth));
//
//        return true;
//    }
//
//    private boolean customDrawEvent(MotionEvent event) {
//        // 压感指数 0-1
//        float realPressure = event.getPressure();
//
//        float eventX = event.getX();
//        float eventY = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:// -----------按下
//                mLastDownX = eventX;
//                mLastDownY = eventY;
//                mLastPressure = realPressure;
//
//                if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE || mSketchMode.getModeType() == SketchMode.Mode.ERASER) {
//                    mPressureList.clear();
//
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                    // 清空点
//                    mPoints.clear();
//                    mSketchPenHelper.cleanPoints(false);
//                    mDrawPointList.clear();
//                    addPoint(eventX, eventY);
//                }
//            case MotionEvent.ACTION_MOVE:// -----------移动
//                resetDirtyRect(eventX, eventY);
//                if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE || mSketchMode.getModeType() == SketchMode.Mode.ERASER) {
//                    addPoint(eventX, eventY);
//                    // 保证平滑过渡
//                    calcLastPressure(realPressure);
////                    if (mLastPressure - realPressure > 0.1)
////                        mLastPressure = mLastPressure - 0.1f;
////                    else
////                        mLastPressure = realPressure;
//
//                    mPressureList.add(mLastPressure);
//                } else if (mSketchMode.getModeType() == SketchMode.Mode.GEOMETRIC
//                        || mSketchMode.getModeType() == SketchMode.Mode.SPOTLIGHT) {
//                    // 缓存虚线范围虚线
//                    mDashRect.update(mLastDownX, mLastDownY, eventX, eventY);
//                    drawGeometric(mDashRect, true);
//                }
//                break;
//            case MotionEvent.ACTION_UP:// -----------离开
//                isLastEvent = true;
//                resetDirtyRect(eventX, eventY);
//
//                if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE ||
//                        mSketchMode.getModeType() == SketchMode.Mode.ERASER) {
//                    mLastPressure = getSuitablePressure();
//                    addPoint(eventX, eventY);
//                    getParent().requestDisallowInterceptTouchEvent(true);
//
//                    List<DrawPoint> tempPoints = new ArrayList<>();
//                    tempPoints.addAll(mDrawPointList);
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, tempPoints));
//                } else if (mSketchMode.getModeType() == SketchMode.Mode.GEOMETRIC
//                        || mSketchMode.getModeType() == SketchMode.Mode.SPOTLIGHT) {
//                    // 缓存虚线范围虚线
//                    mDashRect.update(mLastDownX, mLastDownY, eventX, eventY);
//                    drawGeometric(mDashRect, true);
//                    // 画完后，进入拖拽模式
//                    mOperateType = SketchMode.OperateType.OPERATE_DRAG;
//                }
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    /**
//     * 重绘临时图形
//     */
//    @Override
//    public void invalidateTempGeometric() {
//        if (mOperateType == SketchMode.OperateType.OPERATE_DRAG && mDashRect != null && !mDashRect.isDisable()) {
//            drawGeometric(mDashRect, true);
//            invalidate((int) (mDirtyRect.left - mWidth), (int) (mDirtyRect.top - mWidth),
//                    (int) (mDirtyRect.right + mWidth), (int) (mDirtyRect.bottom + mWidth));
//        }
//    }
//
//    /**
//     * 滤波算法
//     */
//    private void calcLastPressure(float realPressure) {
//        int CALC_COUNT = 20;// 每次都取最后20次进行求平均值
//        int len = mPressureList.size();
//        float total = realPressure;
//        float avgPressure;
//        if (len < CALC_COUNT) {
//            for (float f : mPressureList) {
//                total += f;
//            }
//            avgPressure = total / (len + 1);
//        } else {
//            for (int i = len - 1; i > len - CALC_COUNT; i--) {
//                total += mPressureList.get(i);
//            }
//            avgPressure = total / CALC_COUNT;
//        }
//        // 加权求平均值
//        float PRESSURE_FILTER_WEIGHT = 0.05f;
//        mLastPressure = PRESSURE_FILTER_WEIGHT * realPressure +
//                PRESSURE_FILTER_WEIGHT * mLastPressure +
//                (1 - PRESSURE_FILTER_WEIGHT * 2) * avgPressure;
//
//        Log.d(TAG, "calcLastPressure: --------------------------------");
//        Log.d(TAG, "calcLastPressure: len = " + (len + 1));
//        Log.d(TAG, "calcLastPressure: total = " + total);
//        Log.i(TAG, "calcLastPressure: realPressure = " + realPressure);
//        Log.w(TAG, "calcLastPressure: avgPressure = " + avgPressure);
//        Log.e(TAG, "calcLastPressure: mLastPressure = " + mLastPressure);
//    }
//
//    /**
//     * 自定义拖拽
//     */
//    private int customDragGeometric(MotionEvent event) {
//        float eventX = event.getX();
//        float eventY = event.getY();
//
//        float disX, disY;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG, "customDragGeometric: ACTION_DOWN");
//                if (mDashRect == null || !mDashRect.isContain(eventX, eventY)) {
//                    Log.w(TAG, "onTouchEvent: 当前点击的位置不在虚线范围内~");
//
//                    mResizeDirection = mDashRect.getDirection(eventX, eventY);
//                    // 根据方向做不同的事情
//                    Log.w(TAG, "customDragGeometric: mResizeDirection = " + mResizeDirection);
//                    if (mResizeDirection == DashRectangle.Direction.OUTSIDE) {
//                        int state = 0;
//
//                        long timeout = System.currentTimeMillis() - mLastTouchOutsideTime;
//                        Log.d(TAG, "customDragGeometric: mTouchOutsideTimes ==> timeout = " + timeout);
//                        if (timeout < 200) {
//                            mTouchOutsideTimes++;
//                        } else {
//                            mTouchOutsideTimes = 1;
//                        }
//                        Log.d(TAG, "customDragGeometric: mTouchOutsideTimes = " + mTouchOutsideTimes);
//                        if (mTouchOutsideTimes == 2) {// 双击图形外区域
//                            mTouchOutsideTimes = 0;
//                            mOperateType = SketchMode.OperateType.OPERATE_DRAW;
//                            // 把图形画到真实的画布上面
//                            drawGeometric(mDashRect, false);
//                            invalidate((int) (mDirtyRect.left - mWidth), (int) (mDirtyRect.top - mWidth),
//                                    (int) (mDirtyRect.right + mWidth), (int) (mDirtyRect.bottom + mWidth));
//                            state = 2;
//                        }
//                        mLastTouchOutsideTime = System.currentTimeMillis();
//                        return state;
//                    }
//                    mOperateType = SketchMode.OperateType.OPERATE_RESIZE;
//                } else {
//                    mOperateType = SketchMode.OperateType.OPERATE_DRAG;
//                }
//                // 缓存坐标信息
//                mDragDownX = eventX;
//                mDragDownY = eventY;
//                mDragMoveX = eventX;
//                mDragMoveY = eventY;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.w(TAG, "customDragGeometric: ====================ACTION_MOVE");
//                float thisTimeDisX = eventX - mDragMoveX;
//                float thisTimeDisY = eventY - mDragMoveY;
//                if (Math.abs(thisTimeDisX) < MIN_DRAG_DISTANCE && Math.abs(thisTimeDisY) < MIN_DRAG_DISTANCE) {
//                    Log.w(TAG, "customDragGeometric: ==========本次操作的距离太短，不做处理~");
//                    Log.w(TAG, "customDragGeometric: thisTimeDisX = " + thisTimeDisX);
//                    Log.w(TAG, "customDragGeometric: thisTimeDisY = " + thisTimeDisY);
//                    return 1;
//                }
//                disX = eventX - mDragDownX;
//                disY = eventY - mDragDownY;
//                Log.d(TAG, "customDragGeometric: disX = " + disX);
//                Log.d(TAG, "customDragGeometric: disY = " + disY);
//
//                // 更新点位信息
//                mDragMoveX = eventX;
//                mDragMoveY = eventY;
//
//                // 刷新区域范围
//                resetDirtyRect(eventX, eventY);
//
//                if (mOperateType == SketchMode.OperateType.OPERATE_DRAG) {// 拖拽图形
//                    dragGeometric(disX, disY);
//                } else if (mOperateType == SketchMode.OperateType.OPERATE_RESIZE) {// 改变图形大小
//                    Log.d(TAG, "customDragGeometric: 改变图形大小");
//                    int resizeGeometricResult = resizeGeometric(eventX, eventY, thisTimeDisX, thisTimeDisY);
//                    Log.d(TAG, "customDragGeometric: resizeGeometricResult = " + resizeGeometricResult);
//                    if (resizeGeometricResult == 0) {
//                        mOperateType = SketchMode.OperateType.OPERATE_WAIT;
//                        return 0;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                isLastEvent = true;
//                Log.w(TAG, "customDragGeometric: ACTION_UP");
//                // 刷新区域范围
//                resetDirtyRect(eventX, eventY);
//                if (mOperateType == SketchMode.OperateType.OPERATE_DRAG) {// 拖拽图形
//                    mDashRect.move(eventX - mDragDownX, eventY - mDragDownY);
//                    drawGeometric(mDashRect, true);
//                }
//                break;
//        }
//        return 1;
//    }
//
//    /**
//     * 改变图形大小
//     */
//    private int resizeGeometric(float eventX, float eventY, float disX, float disY) {
//        Log.d(TAG, "resizeGeometric() called with: eventX = [" + eventX + "], eventY = [" + eventY + "], disX = [" + disX + "], disY = [" + disY + "]");
////        DashRectangle tempDashRect = mDashRect.cloneRect();
//
//        switch (mResizeDirection) {
//            case DashRectangle.Direction.LEFT:
//                if (eventX + DashRectangle.RANGE_OFFSET > mDashRect.getMaxX())
//                    return 0;
//                mDashRect.setMinX(mDashRect.getMinX() + disX);
//                break;
//            case DashRectangle.Direction.RIGHT:
//                if (eventX - DashRectangle.RANGE_OFFSET < mDashRect.getMinX())
//                    return 0;
//                mDashRect.setMaxX(mDashRect.getMaxX() + disX);
//                break;
//            case DashRectangle.Direction.TOP:
//                if (eventY + DashRectangle.RANGE_OFFSET > mDashRect.getMaxY())
//                    return 0;
//                mDashRect.setMinY(mDashRect.getMinY() + disY);
//                break;
//            case DashRectangle.Direction.BOTTOM:
//                if (eventY - DashRectangle.RANGE_OFFSET < mDashRect.getMinY())
//                    return 0;
//                mDashRect.setMaxY(mDashRect.getMaxY() + disY);
//                break;
//        }
//        drawGeometric(mDashRect, true);
//        return 1;
//    }
//
//    /**
//     * 改变图形位置
//     */
//    private void dragGeometric(float disX, float disY) {
//        DashRectangle tempDashRect = new DashRectangle();
//        tempDashRect.update(
//                mDashRect.getStartX() + disX,
//                mDashRect.getStartY() + disY,
//                mDashRect.getEndX() + disX,
//                mDashRect.getEndY() + disY);
//        drawGeometric(tempDashRect, true);
//    }
//
//    private void drawGeometric(DashRectangle dashRectangle, boolean withDashRect) {
//        if (mSketchMode.getModeType() == SketchMode.Mode.SPOTLIGHT) {
//            drawSpotlightCircle(dashRectangle, withDashRect);
//            return;
//        }
//
//        clearForGeometric();
//
//        switch (mSketchMode.getDrawType()) {
//            case SketchMode.Geometric.RECTANGLE:// 画矩形
//                if (withDashRect)
//                    mSketchViewHelper.drawRectangle(mGeometricCanvas, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    RectF rectF = mSketchViewHelper.drawRectangle(mSketchCanvas, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, rectF));
//                }
//                break;
//            case SketchMode.Geometric.CIRCLE:// 画圆
//                if (withDashRect)
//                    mSketchViewHelper.drawCircle(mGeometricCanvas, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    RectF rectF = mSketchViewHelper.drawCircle(mSketchCanvas, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, rectF));
//                }
//                break;
//            case SketchMode.Geometric.PARALLELOGRAM:// 画平行四边形
//                if (withDashRect)
//                    mSketchViewHelper.drawParallelogram(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawParallelogram(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.TRIANGLE:// 画普通三角形
//                if (withDashRect)
//                    mSketchViewHelper.drawTriangle(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawTriangle(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.RA_TRIANGLE:// 画直角三角形
//                if (withDashRect)
//                    mSketchViewHelper.drawRATriangle(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawRATriangle(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.ECHELON:// 画梯形
//                if (withDashRect)
//                    mSketchViewHelper.drawEchelon(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawEchelon(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.DIAMOND:// 画菱形
//                if (withDashRect) {
//                    mSketchViewHelper.drawDiamond(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                } else {
//                    List<DrawPoint> pointList = mSketchViewHelper.drawDiamond(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, pointList));
//                }
//                break;
//            case SketchMode.Geometric.PENTAGON:// 画五边形
//                if (withDashRect)
//                    mSketchViewHelper.drawPentagon(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawPentagon(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.LINE:// 画直线
//                if (withDashRect)
//                    mSketchViewHelper.drawLine(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawLine(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.QUAD:// 画曲线
//                if (withDashRect)
//                    mSketchViewHelper.drawQuadLine(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawQuadLine(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.LINE_WITH_ARROW:// 直线箭头
//                if (withDashRect)
//                    mSketchViewHelper.drawLineWithArrow(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawLineWithArrow(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//            case SketchMode.Geometric.COORDINATE_AXIS:// 坐标轴
//                if (withDashRect)
//                    mSketchViewHelper.drawCoordinateAxis(mGeometricCanvas, mGeometricPath, mGeometricPaint, dashRectangle, mDashLinePath);
//                else {
//                    List<DrawPoint> drawPoints = mSketchViewHelper.drawCoordinateAxis(mSketchCanvas, mGeometricPath, mGeometricPaint, dashRectangle, null);
//                    // 添加进绘画历史记录，用于撤销和恢复
//                    addHistory(new SketchHistory(mSketchMode, SketchConfig.currColor, drawPoints));
//                }
//                break;
//        }
//    }
//
//    /**
//     * 画文本
//     */
//    private void customDrawText(SketchHistory history) {
//        SketchHistory.TextParams tp = history.getTextParams();
//        if (tp == null)
//            return;
//        ensureSketchBitmap();
//
//        mTextPaint.setTextSize(tp.getTextSize());
//        mTextPaint.setColor(history.getColor());
//
//        int allowWidth = getWidth() - tp.getMarginCount();
//
//        StaticLayout layout = new StaticLayout(tp.getText(), mTextPaint, allowWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//        mSketchCanvas.save();
//        mSketchCanvas.translate(tp.getX(), tp.getY());
//        layout.draw(mSketchCanvas);
//
//        mSketchCanvas.restore();// 别忘了restore
//    }
//
//    private void addHistory(SketchHistory sketchHistory) {
//        // 添加历史记录的时候,清空上次可恢复内容
//        mCanRedoTimes = 0;
//        mRedoList.clear();
//        mCanUndoTimes++;
//        if (mCanUndoTimes > SketchHistory.MAX_UNDO_TIMES)
//            mCanUndoTimes = SketchHistory.MAX_UNDO_TIMES;
//
//        mHistoryList.add(sketchHistory);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        ensureSketchBitmap();
//        // 第一步：画背景
//        if (mBgBitmap != null)
//            drawBackground(canvas);
//
//        // 第二步:画图片
//        if (photoRecordList != null && photoRecordList.size() > 0)
//            drawRecord(canvas);
//
//        // 第三步:画笔迹
//        if (mSketchBitmap != null)
//            canvas.drawBitmap(mSketchBitmap, 0, 0, mPenPaint);
//
//        // 第四步:画矢量图
//        if (mGeometricBitmap != null)
//            canvas.drawBitmap(mGeometricBitmap, 0, 0, mGeometricPaint);
//
//        // 画聚光灯
//        if (mSpotlightBitmap != null)
//            canvas.drawBitmap(mSpotlightBitmap, 0, 0, null);
//    }
//
//    @Override
//    public void cleanSpotlight() {
//        if (mSpotlightBitmap != null) {
//            mSpotlightBitmap.recycle();
//            mSpotlightBitmap = null;
//        }
//        invalidate();
//    }
//
//    @Override
//    public void savePenWidth() {
//        if (mSketchMode != null && mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE) {
//            setPenType(mSketchMode.getDrawType());
//        }
//    }
//
//    @Override
//    public void switchSpotlight() {
//        clearForSpotlight();
//        mSpotlightCanvas.drawColor(SketchConfig.BG_SPOTLIGHT);
//        invalidate();
//    }
//
//    private void drawSpotlightCircle(DashRectangle dashRectangle, boolean withDashRect) {
//        clearForSpotlight();
//        mSpotlightCanvas.drawColor(SketchConfig.BG_SPOTLIGHT);
//
//        // 设置高亮区域
//        mSketchViewHelper.drawCircle(mSpotlightCanvas, mSpotlightPaint, dashRectangle, withDashRect ? mDashLinePath : null);
//    }
//
//    /**
//     * 绘制背景图片
//     */
//    private void drawBackground(Canvas canvas) {
//        float wScale = (float) canvas.getWidth() / mBgBitmap.getWidth();
//        float hScale = (float) canvas.getHeight() / mBgBitmap.getHeight();
//        Matrix mMatrix = new Matrix();
//        mMatrix.postScale(wScale, hScale);
//        canvas.drawBitmap(mBgBitmap, mMatrix, null);
//    }
//
//    public void drawRecord(Canvas canvas) {
//        drawRecord(canvas, true);
//    }
//
//    public Bitmap tempBitmap;// 临时绘制的bitmap
//    public Canvas tempCanvas;
//    public Bitmap tempHoldBitmap;// 保存已固化的笔画bitmap
//    public Canvas tempHoldCanvas;
//    public int drawDensity = 2;// 绘制密度,数值越高图像质量越低、性能越好
//    /**
//     * 缩放手势
//     */
//    public ScaleGestureDetector mScaleGestureDetector = null;
//
//    public Paint boardPaint;
//
//    public Bitmap mirrorMarkBM = BitmapFactory.decodeResource(getResources(), R.drawable.mark_copy);
//    public Bitmap deleteMarkBM = BitmapFactory.decodeResource(getResources(), R.drawable.mark_delete);
//    public Bitmap rotateMarkBM = BitmapFactory.decodeResource(getResources(), R.drawable.mark_rotate);
//    public Bitmap resetMarkBM = BitmapFactory.decodeResource(getResources(), R.drawable.mark_reset);
//
//    public RectF markerCopyRect = new RectF(0, 0, mirrorMarkBM.getWidth(), mirrorMarkBM.getHeight());//镜像标记边界
//    public RectF markerDeleteRect = new RectF(0, 0, deleteMarkBM.getWidth(), deleteMarkBM.getHeight());//删除标记边界
//    public RectF markerRotateRect = new RectF(0, 0, rotateMarkBM.getWidth(), rotateMarkBM.getHeight());//旋转标记边界
//    public RectF markerResetRect = new RectF(0, 0, resetMarkBM.getWidth(), resetMarkBM.getHeight());//旋转标记边界
//
//    public static float SCALE_MAX = 4.0f;
//    public static float SCALE_MIN = 0.2f;
//    public static float SCALE_MIN_LEN;
//
//    public void drawRecord(Canvas canvas, boolean isDrawBoard) {
//        if (photoRecordList == null || photoRecordList.size() == 0) {
//            return;
//        }
//        for (PhotoRecord record : photoRecordList) {
//            if (record != null) {
//                // Log.d(getClass().getSimpleName(), "drawRecord --> " + record.bitmap.toString());
//                canvas.drawBitmap(record.bitmap, record.matrix, null);
//            }
//        }
//        if (isDrawBoard && mSketchMode.getModeType() == SketchMode.Mode.EDIT_PIC && curPhotoRecord != null) {
//            SCALE_MAX = curPhotoRecord.scaleMax;
//            float[] photoCorners = calculateCorners(curPhotoRecord);//计算图片四个角点和中心点
//            drawBoard(canvas, photoCorners);//绘制图形边线
//            drawMarks(canvas, photoCorners);//绘制边角图片
//        }
//        //新建一个临时画布，以便橡皮擦生效
//        if (tempBitmap == null) {
//            tempBitmap = Bitmap.createBitmap(getWidth() / drawDensity, getHeight() / drawDensity, Bitmap.Config.ARGB_4444);
//            tempCanvas = new Canvas(tempBitmap);
//        }
//        //新建一个临时画布，以便保存过多的画笔
//        if (tempHoldBitmap == null) {
//            tempHoldBitmap = Bitmap.createBitmap(getWidth() / drawDensity, getHeight() / drawDensity, Bitmap.Config.ARGB_4444);
//            tempHoldCanvas = new Canvas(tempHoldBitmap);
//        }
////            Canvas tempCanvas = new Canvas(tempBitmap);
//        //把十个操作以前的笔画全都画进固化层
////        while (curSketchData.strokeRecordList.size() > 10) {
////            StrokeRecord record = curSketchData.strokeRecordList.get(0);
////            int type = record.type;
////            if (type == StrokeRecord.STROKE_TYPE_ERASER) {//橡皮擦需要在固化层也绘制
////                tempHoldCanvas.drawPath(record.path, record.paint);
////            } else if (type == StrokeRecord.STROKE_TYPE_DRAW || type == StrokeRecord.STROKE_TYPE_LINE) {
////                tempHoldCanvas.drawPath(record.path, record.paint);
////            } else if (type == STROKE_TYPE_CIRCLE) {
////                tempHoldCanvas.drawOval(record.rect, record.paint);
////            } else if (type == STROKE_TYPE_RECTANGLE) {
////                tempHoldCanvas.drawRect(record.rect, record.paint);
////            } else if (type == STROKE_TYPE_TEXT) {
////                if (record.text != null) {
////                    StaticLayout layout = new StaticLayout(record.text, record.textPaint, record.textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
////                    tempHoldCanvas.translate(record.textOffX, record.textOffY);
////                    layout.draw(tempHoldCanvas);
////                    tempHoldCanvas.translate(-record.textOffX, -record.textOffY);
////                }
////            }
////            curSketchData.strokeRecordList.remove(0);
////        }
//        clearCanvas(tempCanvas);//清空画布
//        tempCanvas.drawColor(Color.TRANSPARENT);
////        tempCanvas.drawBitmap(tempHoldBitmap, new Rect(0, 0, tempHoldBitmap.getWidth(), tempHoldBitmap.getHeight()), new Rect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight()), null);
////        for (StrokeRecord record : curSketchData.strokeRecordList) {
////            int type = record.type;
////            if (type == StrokeRecord.STROKE_TYPE_ERASER) {//橡皮擦需要在固化层也绘制
////                tempCanvas.drawPath(record.path, record.paint);
////                tempHoldCanvas.drawPath(record.path, record.paint);
////            } else if (type == StrokeRecord.STROKE_TYPE_DRAW || type == StrokeRecord.STROKE_TYPE_LINE) {
////                tempCanvas.drawPath(record.path, record.paint);
////            } else if (type == STROKE_TYPE_CIRCLE) {
////                tempCanvas.drawOval(record.rect, record.paint);
////            } else if (type == STROKE_TYPE_RECTANGLE) {
////                tempCanvas.drawRect(record.rect, record.paint);
////            } else if (type == STROKE_TYPE_TEXT) {
////                if (record.text != null) {
////                    StaticLayout layout = new StaticLayout(record.text, record.textPaint, record.textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
////                    tempCanvas.translate(record.textOffX, record.textOffY);
////                    layout.draw(tempCanvas);
////                    tempCanvas.translate(-record.textOffX, -record.textOffY);
////                }
////            }
////        }
//        canvas.drawBitmap(tempBitmap, new Rect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight()), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
//    }
//
//    /**
//     * 清理画布canvas
//     *
//     * @param temptCanvas
//     */
//    public void clearCanvas(Canvas temptCanvas) {
//        Paint p = new Paint();
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        temptCanvas.drawPaint(p);
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//    }
//
//    //绘制图像边线（由于图形旋转或不一定是矩形，所以用Path绘制边线）
//    public void drawBoard(Canvas canvas, float[] photoCorners) {
//        CustomPath photoBorderPath = new CustomPath();
//        photoBorderPath.moveTo(photoCorners[0], photoCorners[1]);
//        photoBorderPath.lineTo(photoCorners[2], photoCorners[3]);
//        photoBorderPath.lineTo(photoCorners[4], photoCorners[5]);
//        photoBorderPath.lineTo(photoCorners[6], photoCorners[7]);
//        photoBorderPath.lineTo(photoCorners[0], photoCorners[1]);
//        canvas.drawPath(photoBorderPath, boardPaint);
//    }
//
//    //绘制边角操作图标
//    public void drawMarks(Canvas canvas, float[] photoCorners) {
//        float x;
//        float y;
//        x = photoCorners[0] - markerCopyRect.width() / 2;
//        y = photoCorners[1] - markerCopyRect.height() / 2;
//        markerCopyRect.offsetTo(x, y);
//        canvas.drawBitmap(mirrorMarkBM, x, y, null);
//
//        x = photoCorners[2] - markerDeleteRect.width() / 2;
//        y = photoCorners[3] - markerDeleteRect.height() / 2;
//        markerDeleteRect.offsetTo(x, y);
//        canvas.drawBitmap(deleteMarkBM, x, y, null);
//
//        x = photoCorners[4] - markerRotateRect.width() / 2;
//        y = photoCorners[5] - markerRotateRect.height() / 2;
//        markerRotateRect.offsetTo(x, y);
//        canvas.drawBitmap(rotateMarkBM, x, y, null);
//
//        x = photoCorners[6] - markerResetRect.width() / 2;
//        y = photoCorners[7] - markerResetRect.height() / 2;
//        markerResetRect.offsetTo(x, y);
//        canvas.drawBitmap(resetMarkBM, x, y, null);
//    }
//
//    public float[] calculateCorners(PhotoRecord record) {
//        float[] photoCornersSrc = new float[10];//0,1代表左上角点XY，2,3代表右上角点XY，4,5代表右下角点XY，6,7代表左下角点XY，8,9代表中心点XY
//        float[] photoCorners = new float[10];//0,1代表左上角点XY，2,3代表右上角点XY，4,5代表右下角点XY，6,7代表左下角点XY，8,9代表中心点XY
//        RectF rectF = record.photoRectSrc;
//        photoCornersSrc[0] = rectF.left;
//        photoCornersSrc[1] = rectF.top;
//        photoCornersSrc[2] = rectF.right;
//        photoCornersSrc[3] = rectF.top;
//        photoCornersSrc[4] = rectF.right;
//        photoCornersSrc[5] = rectF.bottom;
//        photoCornersSrc[6] = rectF.left;
//        photoCornersSrc[7] = rectF.bottom;
//        photoCornersSrc[8] = rectF.centerX();
//        photoCornersSrc[9] = rectF.centerY();
//        curPhotoRecord.matrix.mapPoints(photoCorners, photoCornersSrc);
//        return photoCorners;
//    }
//
//    private TimedPoint getNewPoint(float x, float y) {
//        int mCacheSize = mPointsCache.size();
//        TimedPoint timedPoint;
//        if (mCacheSize == 0) {
//            // Cache is empty, create a new point
//            timedPoint = new TimedPoint();
//        } else {
//            // Get point from cache
//            timedPoint = mPointsCache.remove(mCacheSize - 1);
//        }
//
//        return timedPoint.set(x, y);
//    }
//
//    private void recyclePoint(TimedPoint point) {
//        mPointsCache.add(point);
//    }
//
//    private void addPoint(float x, float y) {
//        if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE
//                && mSketchMode.getDrawType() == SketchMode.Pen.PEN) {// 钢笔：线条粗细由速度决定
//            addPointUseVelocity(getNewPoint(x, y));
//        } else {
//            addPointUsePressure(getNewPoint(x, y));
//        }
//    }
//
//    /**
//     * 根据速度绘制点：适用钢笔
//     */
//    private void addPointUseVelocity(TimedPoint newPoint) {
//        mPoints.add(newPoint);
//
//        int pointsCount = mPoints.size();
//        if (pointsCount > 3) {
//            ControlTimedPoints tmp = calculateCurveControlPoints(mPoints.get(0), mPoints.get(1), mPoints.get(2));
//            TimedPoint c2 = tmp.c2;
//            recyclePoint(tmp.c1);
//
//            tmp = calculateCurveControlPoints(mPoints.get(1), mPoints.get(2), mPoints.get(3));
//            TimedPoint c3 = tmp.c1;
//            recyclePoint(tmp.c2);
//
//            Bezier curve = mBezierCached.set(mPoints.get(1), c2, c3, mPoints.get(2));
//
//            TimedPoint startPoint = curve.startPoint;
//            TimedPoint endPoint = curve.endPoint;
//
//            float velocity = endPoint.velocityFrom(startPoint);
//            velocity = Float.isNaN(velocity) ? 0.0f : velocity;
//
//            // 按权求速度
//            float mVelocityFilterWeight = 0.9f;
//            velocity = mVelocityFilterWeight * velocity + (1 - mVelocityFilterWeight) * mLastVelocity;
//
//            float newWidth = strokeWidth(velocity);
//            addBezier(curve, mLastWidth, newWidth);
//
//            mLastVelocity = velocity;
//            mLastWidth = newWidth;
//
//            recyclePoint(mPoints.remove(0));
//
//            recyclePoint(c2);
//            recyclePoint(c3);
//        } else if (pointsCount == 1) {
//            TimedPoint firstPoint = mPoints.get(0);
//            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y));
//        }
//    }
//
//    private void addPointUsePressure(TimedPoint newPoint) {
//        mPoints.add(newPoint);
//
//        int pointsCount = mPoints.size();
//        if (pointsCount > 3) {
//            ControlTimedPoints tmp = calculateCurveControlPoints(mPoints.get(0), mPoints.get(1), mPoints.get(2));
//            TimedPoint c2 = tmp.c2;
//            recyclePoint(tmp.c1);
//
//            tmp = calculateCurveControlPoints(mPoints.get(1), mPoints.get(2), mPoints.get(3));
//            TimedPoint c3 = tmp.c1;
//            recyclePoint(tmp.c2);
//
//            Bezier curve = mBezierCached.set(mPoints.get(1), c2, c3, mPoints.get(2));
//
//            // start and end mPoints.
//            if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE
//                    && (mSketchMode.getDrawType() == SketchMode.Pen.PENCIL
//                    || mSketchMode.getDrawType() == SketchMode.Pen.BRUSH)) {// 铅笔和毛笔才有笔锋
//                if (isLastEvent) {
//                    // 颜色转换
//                    addBezier(curve, mWidth * mLastPressure, 0);
//                } else {
//                    addBezier(curve, mWidth * mLastPressure, mWidth * mLastPressure);
//                }
//            } else {// 粉笔
//                addBezier(curve, -1, -1);
//            }
//
//            recyclePoint(mPoints.remove(0));
//
//            recyclePoint(c2);
//            recyclePoint(c3);
//        } else if (pointsCount == 1) {
//            TimedPoint firstPoint = mPoints.get(0);
//            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y));
//        }
//    }
//
//    /**
//     * 添加粉笔干扰点
//     */
//    private void addChalkPoint(float x, float y, float w) {
//        // 画真的线
//        // mSketchCanvas.drawPoint(x, y, mPenPaint);
//        float each = w / 8;
//
//        mExtraPointPaint.setStrokeWidth(each);
//        mExtraPointPaint.setColor(SketchConfig.currColor);
//        for (int i = -3; i <= 3; i += 2) {
//            float scale = each * i * random.nextFloat();
//            float x1 = x + scale, y1 = y + scale;
//            float x2 = x + scale, y2 = y;
//            float x3 = x, y3 = y + scale;
//
////            if (random.nextBoolean()) {
////                int position = random.nextInt(3) + 1;
////                if (position == 1) {
////                    mSketchCanvas.drawPoint(x1 + scale, y1 + scale, mExtraPointPaint);
////                } else if (position == 2) {
////                    mSketchCanvas.drawPoint(x2 + scale, y2, mExtraPointPaint);
////                } else if (position == 3) {
////                    mSketchCanvas.drawPoint(x3, y3 + scale, mExtraPointPaint);
////                }
////            }
//
////            if (v < 0.1f) {
////                if (random.nextBoolean())
////                    mSketchCanvas.drawRect(x1 - each / 4, y1 - each / 4, x1 + each / 4, y1 + each / 4, mExtraPointPaint);
////                if (random.nextBoolean())
////                    mSketchCanvas.drawRect(x2 - each / 4, y2 - each / 4, x2 + each / 4, y2 + each / 4, mExtraPointPaint);
////                if (random.nextBoolean())
////                    mSketchCanvas.drawRect(x3 - each / 4, y3 - each / 4, x3 + each / 4, y3 + each / 4, mExtraPointPaint);
////            }
//
//            if (((int) (x1 + y1)) % 2 == 0)
//                mSketchCanvas.drawPoint(x1 + scale, y1 + scale, mExtraPointPaint);
//            if (((int) (x2 + y2)) % 2 == 0)
//                mSketchCanvas.drawPoint(x2 + scale, y2, mExtraPointPaint);
//            if (((int) (x3 + y3)) % 2 == 0)
//                mSketchCanvas.drawPoint(x3, y3 + scale, mExtraPointPaint);
//        }
//    }
//
//    /**
//     * 绘制干扰点
//     */
//    private void addBrushPoint(float x, float y, float w) {
//        float scale = w / 2.2f;
//        mExtraPointPaint.setStrokeWidth(scale);
//        mExtraPointPaint.setColor(SketchConfig.getExtraColor());
//        mSketchCanvas.drawPoint(x + scale, y + scale, mExtraPointPaint);
//        mSketchCanvas.drawPoint(x - scale, y - scale, mExtraPointPaint);
//        mSketchCanvas.drawPoint(x, y + scale, mExtraPointPaint);
//        mSketchCanvas.drawPoint(x + scale, y, mExtraPointPaint);
//    }
//
//    private void addBezier(Bezier curve, float startWidth, float endWidth) {
//        ensureSketchBitmap();
//
//        float originalWidth = -1f;
//        float widthDelta = -1f;
//        if (startWidth != -1 && endWidth != -1) {// 粉笔
//            originalWidth = mPenPaint.getStrokeWidth();
//            widthDelta = endWidth - startWidth;
//        }
//
//        float drawSteps = (float) Math.floor(curve.length());
//        for (int i = 0; i < drawSteps; i++) {
//            // Calculate the Bezier (x, y) coordinate for this step.
//            float t = ((float) i) / drawSteps;
//            float tt = t * t;
//            float ttt = tt * t;
//            float u = 1 - t;
//            float uu = u * u;
//            float uuu = uu * u;
//
//            float x = uuu * curve.startPoint.x;
//            x += 3 * u * tt * curve.control2.x;
//            x += 3 * uu * t * curve.control1.x;
//            x += ttt * curve.endPoint.x;
//
//            float y = uuu * curve.startPoint.y;
//            y += 3 * uu * t * curve.control1.y;
//            y += 3 * u * tt * curve.control2.y;
//            y += ttt * curve.endPoint.y;
//
//            // Set the incremental stroke width and draw.
//            if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE) {
//                float realWidth = mWidth;
//                if (startWidth != -1 && endWidth != -1) {
//                    realWidth = startWidth + ttt * widthDelta;
//                    mPenPaint.setStrokeWidth(realWidth);
//                }
//
//                mPenPaint.setStrokeWidth(realWidth);
//                mPenPaint.setColor(SketchConfig.currColor);
//                if (mSketchMode.getDrawType() == SketchMode.Pen.CHALK) {
//                    // 添加粉笔干扰点
//                    addChalkPoint(x, y, mPenPaint.getStrokeWidth());
//                } else {
//                    mSketchCanvas.drawPoint(x, y, mPenPaint);
//                    if (mSketchMode.getDrawType() == SketchMode.Pen.BRUSH) {
//                        // 添加毛笔干扰点
//                        addBrushPoint(x, y, realWidth);
//                    }
//                }
//
//                // 记录真实的点位
//                mDrawPointList.add(new DrawPoint(x, y, realWidth));
//            } else {// 橡皮
//                mSketchCanvas.drawPoint(x, y, mEraserPaint);
//
//                // 记录真实的点位
//                mDrawPointList.add(new DrawPoint(x, y, mWidth));
//            }
//            expandDirtyRect(x, y);
//        }
//
//        // 恢复尺寸
//        if (mSketchMode.getModeType() == SketchMode.Mode.PEN_WRITE) {
//            if (originalWidth > 0)
//                mPenPaint.setStrokeWidth(originalWidth);
//            else
//                mPenPaint.setStrokeWidth(mWidth);
//        }
//    }
//
//    private ControlTimedPoints calculateCurveControlPoints(TimedPoint s1, TimedPoint s2, TimedPoint s3) {
//        float dx1 = s1.x - s2.x;
//        float dy1 = s1.y - s2.y;
//        float dx2 = s2.x - s3.x;
//        float dy2 = s2.y - s3.y;
//
//        float m1X = (s1.x + s2.x) / 2.0f;
//        float m1Y = (s1.y + s2.y) / 2.0f;
//        float m2X = (s2.x + s3.x) / 2.0f;
//        float m2Y = (s2.y + s3.y) / 2.0f;
//
//        float l1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
//        float l2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);
//
//        float dxm = (m1X - m2X);
//        float dym = (m1Y - m2Y);
//        float k = l2 / (l1 + l2);
//        if (Float.isNaN(k)) k = 0.0f;
//        float cmX = m2X + dxm * k;
//        float cmY = m2Y + dym * k;
//
//        float tx = s2.x - cmX;
//        float ty = s2.y - cmY;
//
//        return mControlTimedPointsCached.set(getNewPoint(m1X + tx, m1Y + ty), getNewPoint(m2X + tx, m2Y + ty));
//    }
//
//    private void expandDirtyRect(float historicalX, float historicalY) {
//        if (historicalX < mDirtyRect.left) {
//            mDirtyRect.left = historicalX;
//        } else if (historicalX > mDirtyRect.right) {
//            mDirtyRect.right = historicalX;
//        }
//        if (historicalY < mDirtyRect.top) {
//            mDirtyRect.top = historicalY;
//        } else if (historicalY > mDirtyRect.bottom) {
//            mDirtyRect.bottom = historicalY;
//        }
//    }
//
//    private void resetDirtyRect(float eventX, float eventY) {
//        mDirtyRect.left = Math.min(mLastDownX, eventX);
//        mDirtyRect.right = Math.max(mLastDownX, eventX);
//        mDirtyRect.top = Math.min(mLastDownY, eventY);
//        mDirtyRect.bottom = Math.max(mLastDownY, eventY);
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        // 尺寸变化了才重新绘制
//        if (w != oldw || h != oldh) {
//            Log.w(TAG, "onSizeChanged: 画布尺寸发生变化，进行重绘。");
//            mHandler.removeMessages(WHAT_MEASURE);
//            mHandler.sendEmptyMessageDelayed(WHAT_MEASURE, 50);
//        }
//    }
//
//    private static final int WHAT_MEASURE = 0;
//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            if (msg.what == WHAT_MEASURE) {
//                if (mHistoryList.size() == 0)
//                    return false;
//                clearForUndo();
//                invalidateByHistory();
//            }
//            return false;
//        }
//    });
//
//    private void ensureSketchBitmap() {
//        if (mSketchBitmap == null) {
//            mSketchBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            mSketchCanvas = new Canvas(mSketchBitmap);
//        }
//    }
//
//    private void ensureGeometricBitmap() {
//        if (mGeometricBitmap == null) {
//            mGeometricBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            mGeometricCanvas = new Canvas(mGeometricBitmap);
//        }
//    }
//
//    private void ensureSpotlightBitmap() {
//        if (mSpotlightBitmap == null) {
//            mSpotlightBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            mSpotlightCanvas = new Canvas(mSpotlightBitmap);
//        }
//    }
//
//    public float getSuitablePressure() {
//        float f;
//        if (mPressureList.size() >= 2) {
//            f = mPressureList.get(mPressureList.size() - 2);
//        } else {
//            f = mPressureList.get(mPressureList.size() - 1);
//        }
//        return f;
//    }
//
//    /**
//     * 设置笔形
//     */
//    private void setPenType(int penType) {
//        mWidth = SketchConfig.PEN_WIDTH_SCALE[penType] * SketchConfig.currLineWidth;
//
////        switch (penType) {
////            case SketchMode.Pen.PENCIL:// 尖笔
////            case SketchMode.Pen.PEN:// 圆笔
////                mPenPaint.setStrokeCap(Paint.Cap.ROUND);
////                mPenPaint.setStrokeJoin(Paint.Join.ROUND);
////                break;
////            case SketchMode.Pen.BRUSH:// 刷子
////            case SketchMode.Pen.CHALK:// 刷子
////                mPenPaint.setStrokeCap(Paint.Cap.SQUARE);
////                mPenPaint.setStrokeJoin(Paint.Join.MITER);
////                break;
////        }
//    }
//
//    private void setEraserType(int eraserType) {
//        mWidth = SketchConfig.ERASER_WIDTH[eraserType];
//        mEraserPaint.setStrokeWidth(mWidth);
//    }
//
//    private Bitmap getTransparentSketchBitmap() {
//        ensureSketchBitmap();
//        return mSketchBitmap;
//    }
//
//    private void customDrawPoint(SketchHistory history, DrawPoint p) {
//        SketchMode sketchMode = history.getSketchMode();
//        try {
//            switch (sketchMode.getModeType()) {
//                case SketchMode.Mode.PEN_WRITE:
////                    if (sketchMode.getDrawType() == SketchMode.Pen.BRUSH ||
////                            sketchMode.getDrawType() == SketchMode.Pen.CHALK) {// 刷子
////                        mPenPaint.setStrokeCap(Paint.Cap.SQUARE);
////                        mPenPaint.setStrokeJoin(Paint.Join.MITER);
////                    } else {
////                        mPenPaint.setStrokeCap(Paint.Cap.ROUND);
////                        mPenPaint.setStrokeJoin(Paint.Join.ROUND);
////                    }
//                    mPenPaint.setColor(history.getColor());
//                    mPenPaint.setStrokeWidth(p.getWidth());
//                    if (sketchMode.getDrawType() == SketchMode.Pen.CHALK) {
//                        addChalkPoint(p.getX(), p.getY(), p.getWidth());
//                    } else {
//                        mSketchCanvas.drawPoint(p.getX(), p.getY(), mPenPaint);
//                        // 添加毛笔干扰点
//                        if (sketchMode.getDrawType() == SketchMode.Pen.BRUSH) {
//                            addBrushPoint(p.getX(), p.getY(), p.getWidth());
//                        }
//                    }
//                    break;
//                case SketchMode.Mode.ERASER:
//                    mEraserPaint.setStrokeWidth(p.getWidth());
//                    mSketchCanvas.drawPoint(p.getX(), p.getY(), mEraserPaint);
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 根据历史记录进行重绘
//     */
//    private void invalidateByHistory() {
//        for (SketchHistory history : mHistoryList) {
//            if (history == null) continue;
//
//            if (history.getSketchMode().getModeType() == SketchMode.Mode.PEN_WRITE ||
//                    history.getSketchMode().getModeType() == SketchMode.Mode.ERASER) {
//                if (history.getPointList() == null || history.getPointList().size() == 0)
//                    continue;
//                for (DrawPoint drawPoint : history.getPointList()) {
//                    customDrawPoint(history, drawPoint);
//                }
//            } else if (history.getSketchMode().getModeType() == SketchMode.Mode.GEOMETRIC) {
//                drawGeometricByHistory(history);
//            } else if (history.getSketchMode().getModeType() == SketchMode.Mode.TEXT) {
//                customDrawText(history);
//            }
//        }
//    }
//
//    private void drawGeometricByHistory(SketchHistory history) {
//        mGeometricPaint.setColor(history.getColor());
//
//        switch (history.getSketchMode().getDrawType()) {
//            case SketchMode.Geometric.RECTANGLE:// 矩形
//                mSketchCanvas.drawRect(history.getRectF(), mGeometricPaint);
//                break;
//            case SketchMode.Geometric.CIRCLE:// 圆形
//                mSketchCanvas.drawOval(history.getRectF(), mGeometricPaint);
//                break;
//            case SketchMode.Geometric.QUAD:// 曲线
//                List<DrawPoint> quadPoints = history.getPointList();
//                if (quadPoints == null || quadPoints.size() == 0)
//                    return;
//                mGeometricPath.reset();
//                if (quadPoints.size() == 5) {
//                    DrawPoint startPoint = quadPoints.get(0);
//                    DrawPoint quadPoint1 = quadPoints.get(1);
//                    DrawPoint centerPoint = quadPoints.get(2);
//                    DrawPoint quadPoint2 = quadPoints.get(3);
//                    DrawPoint endPoint = quadPoints.get(4);
//
//                    mGeometricPath.moveTo(startPoint.getX(), startPoint.getY());
//                    mGeometricPath.quadTo(quadPoint1.getX(), quadPoint1.getY(), centerPoint.getX(), centerPoint.getY());
//                    mGeometricPath.quadTo(quadPoint2.getX(), quadPoint2.getY(), endPoint.getX(), endPoint.getY());
//                    mSketchCanvas.drawPath(mGeometricPath, mGeometricPaint);
//                }
//                break;
//            case SketchMode.Geometric.LINE_WITH_ARROW:// 直线箭头
//            case SketchMode.Geometric.COORDINATE_AXIS:// 坐标轴
//                mGeometricPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 绘制实心
//                reDrawFromPointList(history);
//                mGeometricPaint.setStyle(Paint.Style.STROKE); // 恢复空心
//                break;
//            case SketchMode.Geometric.TRIANGLE:// 三角形
//            case SketchMode.Geometric.RA_TRIANGLE:// 直角三角形
//            case SketchMode.Geometric.PARALLELOGRAM:// 平行四边形
//            case SketchMode.Geometric.ECHELON:// 梯形
//            case SketchMode.Geometric.DIAMOND:// 菱形
//            case SketchMode.Geometric.PENTAGON:// 五边形
//            case SketchMode.Geometric.LINE:// 直线
//                reDrawFromPointList(history);
//                break;
//        }
//    }
//
//    /**
//     * 根据点列表重绘图形
//     */
//    private void reDrawFromPointList(SketchHistory history) {
//        List<DrawPoint> pointList = history.getPointList();
//        if (pointList == null || pointList.size() == 0)
//            return;
//        mGeometricPath.reset();
//        int c = 0;
//        for (DrawPoint drawPoint : pointList) {
//            if (c == 0) {
//                mGeometricPath.moveTo(drawPoint.getX(), drawPoint.getY());
//            } else {
//                mGeometricPath.lineTo(drawPoint.getX(), drawPoint.getY());
//            }
//            c++;
//        }
//        if (c > 2)
//            mGeometricPath.close();
//        mSketchCanvas.drawPath(mGeometricPath, mGeometricPaint);
//    }
//
//    // ******************************************** public method ********************************************
//
//    /**
//     * 当前画布是否为空
//     */
//    public boolean isEmpty() {
//        return mBgBitmap == null && mCanUndoTimes == 0 && mHistoryList.size() == 0;
//    }
//
//    @Override
//    public Bitmap getSketchBitmap() {
//        if (isEmpty())
//            return null;
//        Bitmap originalBitmap = getTransparentSketchBitmap();
//        Bitmap whiteBgBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(whiteBgBitmap);
//        canvas.drawColor(SketchConfig.bgColor);
//        if (mBgBitmap != null) {
//            drawBackground(canvas);
//        }
//        drawRecord(canvas, false);
//        canvas.drawBitmap(originalBitmap, 0, 0, null);
//        if (mSpotlightBitmap != null) {
//            canvas.drawBitmap(mSpotlightBitmap, 0, 0, null);
//        }
//        return whiteBgBitmap;
//    }
//
//    /**
//     * 撤销
//     */
//    @Override
//    public String undo() {
//        if (mCanUndoTimes == 0 || mHistoryList.size() == 0)
//            return "没有可以撤销的笔迹啦~";
//        int len = mHistoryList.size();
//        mCanUndoTimes--;
//        mCanRedoTimes++;
//        SketchHistory targetHistory = mHistoryList.get(len - 1);
//        mRedoList.add(0, targetHistory);
//        mHistoryList.remove(len - 1);
//        clearForUndo();
//        if (mHistoryList.size() == 0)
//            return "";
//        invalidateByHistory();
//        return "";
//    }
//
//    /**
//     * 恢复
//     */
//    @Override
//    public String redo() {
//        if (mCanRedoTimes == 0 || mRedoList.size() == 0)
//            return "没有可恢复的笔迹啦~";
//        mCanUndoTimes++;
//        SketchHistory targetHistory = mRedoList.get(0);
//        if (targetHistory != null) {
//            mHistoryList.add(targetHistory);
//            if (targetHistory.getSketchMode().getModeType() == SketchMode.Mode.PEN_WRITE ||
//                    targetHistory.getSketchMode().getModeType() == SketchMode.Mode.ERASER) {
//                if (targetHistory.getPointList() == null || targetHistory.getPointList().size() == 0)
//                    return "";
//                for (DrawPoint drawPoint : targetHistory.getPointList()) {
//                    customDrawPoint(targetHistory, drawPoint);
//                }
//            } else if (targetHistory.getSketchMode().getModeType() == SketchMode.Mode.GEOMETRIC) {
//                drawGeometricByHistory(targetHistory);
//            } else if (targetHistory.getSketchMode().getModeType() == SketchMode.Mode.TEXT) {
//                customDrawText(targetHistory);
//            }
//
//            mRedoList.remove(0);
//            invalidate();
//        }
//        return "";
//    }
//
//    /**
//     * 设置绘画模式
//     */
//    @Override
//    public void setSketchMode(@NonNull SketchMode sketchMode) {
//        if (this.mSketchMode != null &&
//                this.mSketchMode.getModeType() == SketchMode.Mode.GEOMETRIC) {
//            if (this.mSketchMode.getModeType() == sketchMode.getModeType() &&
//                    this.mSketchMode.getDrawType() == sketchMode.getDrawType()) {
//                return;
//            }
//
//            if (mOperateType != SketchMode.OperateType.OPERATE_DRAW) {
//                // 把图形画到真实的画布上面
//                drawGeometric(mDashRect, false);
//                invalidate((int) (mDirtyRect.left - mWidth), (int) (mDirtyRect.top - mWidth),
//                        (int) (mDirtyRect.right + mWidth), (int) (mDirtyRect.bottom + mWidth));
//            }
//        }
//
//        this.mSketchMode = sketchMode;
//        mOperateType = SketchMode.OperateType.OPERATE_DRAW;
//        if (sketchMode.getModeType() == SketchMode.Mode.PEN_WRITE) {
//            setPenType(sketchMode.getDrawType());
//        } else if (sketchMode.getModeType() == SketchMode.Mode.ERASER) {
//            setEraserType(sketchMode.getDrawType());
//        } else if (sketchMode.getModeType() == SketchMode.Mode.SPOTLIGHT) {
//            switchSpotlight();
//        }
//    }
//
//    @Override
//    public void clear() {
//        // 默认不清除背景
//        clear(false);
//    }
//
//    /**
//     * 清空画布
//     */
//    @Override
//    public void clear(boolean isClearBg) {
//        // 先判断是否已经回收
//        for (PhotoRecord record : photoRecordList) {
//            if (record != null && record.bitmap != null && !record.bitmap.isRecycled()) {
//                record.bitmap.recycle();
//                record.bitmap = null;
//            }
//        }
//        photoRecordList.clear();
//        curPhotoRecord = null;
//
//        tempCanvas = null;
//        if (tempBitmap != null)
//            tempBitmap.recycle();
//        tempBitmap = null;
//        tempHoldCanvas = null;
//        if (tempHoldBitmap != null)
//            tempHoldBitmap.recycle();
//        tempHoldBitmap = null;
//
//        mHistoryList.clear();
//        mRedoList.clear();
//        mCanUndoTimes = 0;
//        mCanRedoTimes = 0;
//        mOperateType = SketchMode.OperateType.OPERATE_DRAW;
//        mDashRect.reset();
//
//        this.clearView(isClearBg);
//    }
//
//    /**
//     * 获取笔记历史
//     */
//    public List<SketchHistory> getHistoryList() {
//        return mHistoryList;
//    }
//
//    /**
//     * 设置背景资源
//     */
//    @Override
//    public void setBackgroundResource(int resource) {
//        if (resource == 0) {
//            // 如果resource=0,则表示清空背景图片
//            setBackgroundBitmap(null);
//            return;
//        }
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resource).copy(Bitmap.Config.ARGB_8888, true);
//        if (bitmap != null)
//            setBackgroundBitmap(bitmap);
//    }
//
//    /**
//     * 设置背景Bitmap
//     */
//    @Override
//    public void setBackgroundBitmap(Bitmap bitmap) {
//        // 如果bitmap=null,则表示清空背景图片
//        mBgBitmap = bitmap;
//        invalidate();
//    }
//
//    @Override
//    public void setBackgroundByPath(String path) {
//        Bitmap sampleBM = getSampleBitMap(path);
//        if (sampleBM != null) {
//            setBackgroundBitmap(sampleBM);
//        } else {
//            Toast.makeText(mContext, "图片文件路径有误！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private List<PhotoRecord> photoRecordList = new ArrayList<>();
//    public PhotoRecord curPhotoRecord;
//
//    @Override
//    public void addPicBitmap(Bitmap bitmap) {
//        PhotoRecord photoRecord = initPhotoRecord(bitmap);
//        photoRecordList.remove(photoRecord);
//        photoRecordList.add(photoRecord);
//        setCurPhotoRecord(photoRecord);
//    }
//
//    @Override
//    public void addPicByPath(String path) {
//        Bitmap sampleBM = getSampleBitMap(path);
//        if (sampleBM != null) {
//            addPicBitmap(sampleBM);
//        } else {
//            Toast.makeText(mContext, "图片文件路径有误！", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @NonNull
//    public PhotoRecord initPhotoRecord(Bitmap bitmap) {
//        PhotoRecord newRecord = new PhotoRecord();
//        newRecord.bitmap = bitmap;
//        newRecord.photoRectSrc = new RectF(0, 0, newRecord.bitmap.getWidth(), newRecord.bitmap.getHeight());
//        newRecord.scaleMax = getMaxScale(newRecord.photoRectSrc);//放大倍数
//        newRecord.matrix = new Matrix();
//        newRecord.matrix.postTranslate(getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2);
//        return newRecord;
//    }
//
//    public float getMaxScale(RectF photoSrc) {
//        return Math.max(getWidth(), getHeight()) / Math.max(photoSrc.width(), photoSrc.height());
////        SCALE_MIN = SCALE_MAX / 5;
//    }
//
//    public Bitmap getSampleBitMap(String path) {
//        Bitmap sampleBM;
//        if (path.contains(Environment.getExternalStorageDirectory().toString())) {
//            sampleBM = getSDCardPhoto(path);
//        } else {
//            sampleBM = BitmapUtil.getBitmapFromAssets(mContext, path);
//        }
//        return sampleBM;
//    }
//
//    public Bitmap getSDCardPhoto(String path) {
//        File file = new File(path);
//        if (file.exists()) {
//            return BitmapUtil.decodeSampleBitMapFromFile(mContext, path, 0.5f);
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 绘制文本
//     */
//    @Override
//    public void drawText(String text, int textSize, float x, float y, int marginCount) {
//        SketchHistory.TextParams textParams = new SketchHistory.TextParams(text, textSize, x, y, marginCount);
//        SketchHistory history = new SketchHistory(mSketchMode, SketchConfig.currColor, textParams);
//
//        customDrawText(history);
//
//        // 添加历史记录
//        addHistory(history);
//
//        invalidate();
//    }
//
//    public void drawByHistoryJson(List<SketchHistory> historyList) {
//        this.clear();
//
//        this.mHistoryList.addAll(historyList);
//        invalidateByHistory();
//    }
//
//    private float strokeWidth(float velocity) {
//        float mMaxWidth = mWidth * 1.4f;
//        float mMinWidth = mWidth * 0.6f;
//        return Math.max(mMaxWidth / (velocity + 1), mMinWidth);
//    }
//
//    // for Helper
//    @Override
//    public void ensureSketchBitmap4Helper() {
//        ensureSketchBitmap();
//    }
//
//    @Override
//    public void expandDirtyRect4Helper(float x, float y) {
//        expandDirtyRect(x, y);
//    }
//}
