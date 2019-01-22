package cn.sxw.android.base.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Alex.Tang
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}

