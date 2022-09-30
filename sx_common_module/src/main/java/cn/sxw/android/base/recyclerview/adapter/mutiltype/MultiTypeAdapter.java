package cn.sxw.android.base.recyclerview.adapter.mutiltype;

/**
 * Created by Alex.Tang on 2017-05-10.
 */
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import cn.sxw.android.base.recyclerview.adapter.viewhodler.RecyclerViewHolder;

/**
 * RecyclerView 多类型数据适配器
 * 使用步骤：
 * 1. 创建实体类 Bean
 * 2. 创建对应的 provider 并继承自 BaseViewProvider， 在对应的 provider 的 onBindView 里面处理内容
 * 3. 使用 adapter.register(bean, provider.class) 来将数据实体和 provider 对应起来
 * 4. 将数据 data 使用 ArrayList<Object> 类型存储起来， 使用 adapter.addDatas(data) 添加数据
 * 5. 大功告成
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
        implements TypePool {

    private List<Object> mItems = new ArrayList<>();
    @NonNull private MultiTypePool mTypePool;

    public MultiTypeAdapter() {
        mTypePool = new MultiTypePool();
    }

    @Override
    public int getItemViewType(int position) {
        assert mItems != null;
        Object item = mItems.get(position);
        int index = mTypePool.indexOf(item.getClass());
        if (index >= 0) {
            return index;
        }
        return mTypePool.indexOf(item.getClass());
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int indexViewType) {
        BaseViewProvider provider = getProviderByIndex(indexViewType);
        return provider.onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        assert mItems != null;
        Object item = mItems.get(position);
        BaseViewProvider provider = getProviderByClass(item.getClass());
        provider.onBindView(holder, item);
    }

    @Override
    public int getItemCount() {
        assert mItems != null;
        // Logger.e("getItemCount" + mItems.size());
        return mItems.size();
    }

    @Override
    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider) {
        mTypePool.register(clazz, provider);
    }

    @Override
    public int indexOf(@NonNull Class<?> clazz) {
        return mTypePool.indexOf(clazz);
    }

    @Override
    public List<BaseViewProvider> getProviders() {
        return mTypePool.getProviders();
    }

    @Override
    public BaseViewProvider getProviderByIndex(int index) {
        return mTypePool.getProviderByIndex(index);
    }

    @Override
    public <T extends BaseViewProvider> T getProviderByClass(@NonNull Class<?> clazz) {
        return mTypePool.getProviderByClass(clazz);
    }

    public void addDatas(List<?> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public List<Object> getDatas() {
        return mItems;
    }

    public void clearDatas() {
        this.mItems.clear();
        notifyDataSetChanged();
    }
}
