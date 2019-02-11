package com.zcs.android.lib.sketch.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    protected void showToastOnUi(String msg) {
        if (TextUtils.isEmpty(msg))
            return;
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public abstract void init();
}
