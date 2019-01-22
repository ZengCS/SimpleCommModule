package cn.sxw.android.lib.mvp.view;

import android.widget.TextView;

import java.util.List;

import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.mvp.IViewAdvance;

/**
 * Created by ZengCS on 2017/9/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public interface IEmptyView extends IViewAdvance {
    void onRequestSuccess(List<BlankBean> list);

    void onFailed(String msg);

    void onLoginResult(boolean success, String msg);

    TextView getTipsTextView();
}
