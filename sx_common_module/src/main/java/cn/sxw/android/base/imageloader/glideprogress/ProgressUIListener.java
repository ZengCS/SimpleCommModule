package cn.sxw.android.base.imageloader.glideprogress;

/**
 * 通知UI进度
 */
public interface ProgressUIListener {
    void update(int bytesRead, int contentLength);
}
