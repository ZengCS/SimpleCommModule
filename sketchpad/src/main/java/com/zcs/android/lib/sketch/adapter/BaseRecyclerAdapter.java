package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.zcs.android.lib.sketch.listener.OnItemClickListener;

import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mItems;
    private OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter() {
    }

    public BaseRecyclerAdapter(Context context, List<T> mItems) {
        this.mContext = context;
        this.mItems = mItems;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    public void notifyDataSetChanged(List<T> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<T> getItems() {
        return mItems;
    }

    protected void onItemClick(int position) {
        if (position == -1) return;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position);
        }
    }

}
