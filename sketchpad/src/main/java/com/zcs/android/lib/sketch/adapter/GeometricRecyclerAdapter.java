package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.BaseBean;
import com.zcs.android.lib.sketch.bean.GeometricBean;
import com.zcs.android.lib.sketch.view.GeometricView;

import java.util.List;

/**
 * Created by ZengCS on 2016/7/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class GeometricRecyclerAdapter extends BaseRecyclerAdapter<GeometricBean> {
    public GeometricRecyclerAdapter(Context context, List<GeometricBean> mItems) {
        super(context, mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_tool_geometric, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GeometricBean bean = mItems.get(position);
        if (null == bean) return;

        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        viewHolder.geometricView.setGeometricType(bean.getType());
        viewHolder.geometricView.setBackgroundResource(
                bean.isChecked() ? R.drawable.bg_geometric_checked : 0
        );
        viewHolder.geometricView.setLightMode(bean.isChecked());
    }

    /**
     * 自定义ViewHolder
     */
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        GeometricView geometricView;

        private CustomViewHolder(View itemView) {
            super(itemView);
            geometricView = (GeometricView) itemView.findViewById(R.id.id_geo_tool_item);
            geometricView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position < 0)
                        return;
                    GeometricBean bean = mItems.get(position);
                    if (bean.isChecked())
                        return;

                    removeChecked();
                    bean.setChecked(true);
                    notifyDataSetChanged();

                    onItemClick(position);
                }
            });
        }
    }

    private void removeChecked() {
        for (BaseBean bean : mItems)
            bean.setChecked(false);
    }

}
