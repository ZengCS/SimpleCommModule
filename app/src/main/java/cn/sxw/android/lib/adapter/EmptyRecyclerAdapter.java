package cn.sxw.android.lib.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.utils.LogUtil;
import cn.sxw.android.lib.R;


/**
 * XtAdapter
 *
 * @author zcs@sxw.cn
 * @version v1.0
 * @date 2018/10/29 23:34
 */
public class EmptyRecyclerAdapter extends BaseQuickAdapter<BlankBean, BaseViewHolder> {
    public EmptyRecyclerAdapter(@Nullable List<BlankBean> data) {
        super(R.layout.item_blank, data);

        setEnableLoadMore(true);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlankBean item) {
        // 绑定数据
        helper.setText(R.id.id_tv_blank_name, item.getName());
        helper.setText(R.id.id_tv_blank_desc, item.getDesc());
        // 加载网络图片
        Glide.with(mContext).load(item.getPic()).crossFade().into((ImageView) helper.getView(R.id.id_iv_blank_pic));
        // 绑定子View点击事件
        helper.getView(R.id.id_btn_inner).setOnClickListener(v -> {
            // TODO 编写具体代码
            int position = helper.getAdapterPosition();
            LogUtil.d("我是元素内的按钮，position = " + position);
        });
        // 设置可见性 true for VISIBLE, false for GONE.
        helper.setGone(R.id.id_view_new, !item.isNewest());
        // true for VISIBLE, false for INVISIBLE.
        // helper.setVisible(R.id.id_view_new, !item.isNewest());
    }
}
