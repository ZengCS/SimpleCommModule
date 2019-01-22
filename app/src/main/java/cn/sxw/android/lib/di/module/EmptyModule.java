package cn.sxw.android.lib.di.module;

import cn.sxw.android.base.di.scope.PerActivity;
import cn.sxw.android.lib.mvp.model.empty.EmptyModelImp;
import cn.sxw.android.lib.mvp.model.empty.IEmptyModel;
import cn.sxw.android.lib.mvp.view.IEmptyView;
import dagger.Module;
import dagger.Provides;

@Module
public class EmptyModule {
    private IEmptyView view;

    public EmptyModule(IEmptyView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    IEmptyView provideEmptyView() {
        return this.view;
    }

    @PerActivity
    @Provides
    IEmptyModel provideEmptyModel(EmptyModelImp model) {
        return model;
    }
}
