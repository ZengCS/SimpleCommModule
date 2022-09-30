package cn.sxw.android.base.recyclerview.adapter.mutiltype;

/**
 * Created by Alex.Tang on 2017-05-10.
 */

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 类型池
 */
public interface TypePool {

    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider);

    public int indexOf(@NonNull final Class<?> clazz);

    public List<BaseViewProvider> getProviders();

    public BaseViewProvider getProviderByIndex(int index);

    public <T extends BaseViewProvider> T getProviderByClass(@NonNull final Class<?> clazz);
}
