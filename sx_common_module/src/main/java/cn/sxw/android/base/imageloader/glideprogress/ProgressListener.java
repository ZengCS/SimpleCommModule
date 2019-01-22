package cn.sxw.android.base.imageloader.glideprogress;

/**
 *下载进度监听
 */
interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
