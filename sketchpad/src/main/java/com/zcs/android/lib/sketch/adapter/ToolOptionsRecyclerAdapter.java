package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.ToolOptionBean;

import java.util.List;

/**
 * Created by ZengCS on 2016/7/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class ToolOptionsRecyclerAdapter extends BaseRecyclerAdapter<ToolOptionBean> {
    public ToolOptionsRecyclerAdapter(Context context, List<ToolOptionBean> mItems) {
        super(context, mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_tool_option, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToolOptionBean bean = mItems.get(position);
        if (null == bean) return;

        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        viewHolder.ivOption.setImageResource(bean.getResId());
    }

    /**
     * 自定义ViewHolder
     */
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOption;

        private CustomViewHolder(View itemView) {
            super(itemView);
            ivOption = itemView.findViewById(R.id.id_iv_tool_option);

            itemView.findViewById(R.id.id_container_tool_option).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClick(position);
                }
            });
        }
    }
}
