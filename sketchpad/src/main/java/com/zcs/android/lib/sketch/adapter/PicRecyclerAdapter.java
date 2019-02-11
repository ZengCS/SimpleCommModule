package com.zcs.android.lib.sketch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.CellBean;
import com.zcs.android.lib.sketch.utils.BitmapUtil;

import java.util.List;

/**
 * Created by ZengCS on 2016/7/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class PicRecyclerAdapter extends BaseRecyclerAdapter<CellBean> {
    public PicRecyclerAdapter(Context context, List<CellBean> mItems) {
        super(context, mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(mInflater.inflate(R.layout.item_tool_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CellBean bean = mItems.get(position);
        if (null == bean) return;

        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        viewHolder.imageView.setImageBitmap(BitmapUtil.getBitmapFromAssets(mContext, bean.getPath()));
    }

    /**
     * 自定义ViewHolder
     */
    private class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        private CustomViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_iv_bg_cell);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
