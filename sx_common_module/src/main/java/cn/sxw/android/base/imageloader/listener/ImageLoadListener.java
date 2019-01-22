package cn.sxw.android.base.imageloader.listener;

/**
 * @Description
 * @Author kk20
 * @Date 2018/5/18
 * @Version V1.0.0
 */
public interface ImageLoadListener {
    void loadImageSuccess();

    void loadImageFail(String msg);
}
