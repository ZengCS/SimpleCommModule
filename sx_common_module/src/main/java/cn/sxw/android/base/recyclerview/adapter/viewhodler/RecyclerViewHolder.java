package cn.sxw.android.base.recyclerview.adapter.viewhodler;

/**
 * Created by Alex.Tang on 2017-05-10.
 */

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * 布局持有者
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private View mRootView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
        AutoUtils.autoSize(itemView);//适配
    }

    public View getRootView() {
        return mRootView;
    }

    private final SparseArray<View> mViews = new SparseArray<>();

    private <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) mRootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }

    public void setOnClickListener(View.OnClickListener l, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(l);
        }
    }

    public void setText(int id, String text) {
        TextView textView = get(id);
        textView.setText(text);
    }
}