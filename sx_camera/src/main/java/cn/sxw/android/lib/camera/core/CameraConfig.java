package cn.sxw.android.lib.camera.core;

/**
 * Created by ZengCS on 2019/5/13.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public interface CameraConfig {
    int PROGRESS_COLOR = 0xFF01AC8E;// 进度条颜色
    int OUTSIDE_COLOR = 0xEEE8E8E8; // 外圆背景色
    int INSIDE_COLOR = 0xFFFFFFFF;  // 内圆背景色
    int INSIDE_RECORD_COLOR = 0xFFFF6262;  // 录像时内圆颜色
    int CANCEL_COLOR = 0xFF333333;  // 取消箭头颜色

    int MAX_RECORD_DURATION = 2 * 60 * 60 * 1000;// 默认录制视频最长时间:2小时
    int MIN_RECORD_DURATION = 1_000;// 默认录制视频最断时间:1秒钟
}
