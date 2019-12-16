package cn.sxw.android.base.recyclerview.adapter.mutiltype;

/**
 * Created by Alex.Tang on 2017-05-10.
 */

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.sxw.android.base.utils.LogUtil;

/**
 * 类型池
 */
public class MultiTypePool implements TypePool {
    private final String TAG = MultiTypePool.class.getSimpleName();

    private final List<Class<?>> mContents;
    private final List<BaseViewProvider> mProviders;

    public MultiTypePool() {
        mContents = new ArrayList<>();
        mProviders = new ArrayList<>();
    }

    /**
     * 将数据类型和 provider 关联起来
     *
     * @param clazz    数据类型
     * @param provider provider
     */
    @Override
    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider) {
        if (!mContents.contains(clazz)) {
            mContents.add(clazz);
            mProviders.add(provider);
        } else {
            int index = mContents.indexOf(clazz);
            mProviders.set(index, provider);
            LogUtil.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder.");
        }
    }

    @Override
    public int indexOf(@NonNull final Class<?> clazz) throws ProviderNotFoundException {
        int index = mContents.indexOf(clazz);
        if (index >= 0) {
            return index;
        }
        for (int i = 0; i < mContents.size(); i++) {
            if (mContents.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        throw new ProviderNotFoundException(clazz);
    }

    @Override
    public BaseViewProvider getProviderByIndex(int index) {
        return mProviders.get(index);
    }

    @Override
    public <T extends BaseViewProvider> T getProviderByClass(@NonNull final Class<?> clazz) {
        return (T) getProviderByIndex(indexOf(clazz));
    }

    public List<Class<?>> getContents() {
        return mContents;
    }

    public List<BaseViewProvider> getProviders() {
        return mProviders;
    }


    public class ProviderNotFoundException extends RuntimeException {

        public ProviderNotFoundException(@NonNull Class<?> clazz) {
            super("Do you have registered the provider for {className}.class in the adapter/pool?"
                    .replace("{className}", clazz.getSimpleName()));
        }
    }
}
