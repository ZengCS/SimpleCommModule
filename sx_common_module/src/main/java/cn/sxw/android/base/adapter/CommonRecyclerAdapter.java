package cn.sxw.android.base.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * XtAdapter
 *
 * @author zcs@sxw.cn
 * @version v1.0
 * @date 2018/10/29 23:34
 */
public abstract class CommonRecyclerAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public CommonRecyclerAdapter(@LayoutRes int resId, @Nullable List<T> data) {
        super(resId, data);
    }
}
