package cn.sxw.android.base.mvp;


import org.greenrobot.eventbus.EventBus;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    protected static final int PAGE_SIZE = 15;

    protected final String TAG = this.getClass().getSimpleName();

    protected M mModel;

    protected V mRootView;

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
        onAttach();
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onAttach();
    }

    public BasePresenter() {
        onAttach();
    }


    @Override
    public void onAttach() {
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册eventbus
    }

    @Override
    public void onDetach() {
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);//解除注册eventbus
        if (mModel != null)
            mModel.onDestroy();
        this.mModel = null;
        this.mRootView = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public abstract boolean useEventBus();
}
