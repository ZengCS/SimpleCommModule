package cn.sxw.android.lib.di.component;

import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.di.scope.PerActivity;
import cn.sxw.android.lib.di.module.EmptyModule;
import cn.sxw.android.lib.mvp.ui.activity.EmptyActivity;
import dagger.Component;

@PerActivity
@Component(modules = EmptyModule.class,dependencies = AppComponent.class )
public interface EmptyComponent {
     void inject(EmptyActivity activity);
}
