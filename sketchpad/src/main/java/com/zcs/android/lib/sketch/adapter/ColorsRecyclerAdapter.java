package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.ToolColorBean;
import com.zcs.android.lib.sketch.config.SketchConfig;
import com.zcs.android.lib.sketch.view.CircleColorView;

import java.util.List;

/**
 * Created by ZengCS on 2016/7/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class ColorsRecyclerAdapter extends BaseRecyclerAdapter<ToolColorBean> {
    public ColorsRecyclerAdapter(Context context, List<ToolColorBean> mItems) {
        super(context, mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_tool_color, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToolColorBean bean = mItems.get(position);
        if (null == bean) return;

        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        viewHolder.circleColorView.setColor(bean.getColor());
        viewHolder.ivChecked.setVisibility(bean.isChecked() ? View.VISIBLE : View.GONE);
    }

    /**
     * 自定义ViewHolder
     */
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        CircleColorView circleColorView;
        ImageView ivChecked;

        private CustomViewHolder(View itemView) {
            super(itemView);
            circleColorView = (CircleColorView) itemView.findViewById(R.id.id_circle_color);
            ivChecked = (ImageView) itemView.findViewById(R.id.id_iv_checked);
            circleColorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    removeChecked();
                    ToolColorBean bean = mItems.get(position);
                    bean.setChecked(true);
                    SketchConfig.currColorStr = bean.getColorString();
                    SketchConfig.currColor = bean.getColor();
                    notifyDataSetChanged();

                    onItemClick(position);
                }
            });
        }
    }

    private void removeChecked() {
        for (ToolColorBean bean : mItems)
            bean.setChecked(false);
    }
}
