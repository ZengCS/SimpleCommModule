package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.EmptyEntity;

import java.util.List;

/**
 * Created by ZengCS on 2016/7/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class EmptyRecyclerAdapter extends BaseRecyclerAdapter<EmptyEntity> {
    public EmptyRecyclerAdapter(Context context, List<EmptyEntity> mItems) {
        super(context, mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_empty, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EmptyEntity bean = mItems.get(position);
        if (null == bean) return;

        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        viewHolder.tvName.setText("Position:" + position);
    }

    /**
     * 自定义ViewHolder
     */
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        private CustomViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_empty_name);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Position:" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
