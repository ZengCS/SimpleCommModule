package cn.sxw.android.base.mvp;

public interface IModel {
    void onDestroy();

    interface DataCallbackToUi<T> {
        void onSuccess(T objects);

        void onFail(String msg);
    }
}
