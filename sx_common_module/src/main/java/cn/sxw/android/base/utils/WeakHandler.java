package cn.sxw.android.base.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Alex.Tang on 2017-05-02.
 */

public abstract class WeakHandler extends Handler {

    WeakReference<Context> mContext;

    public WeakHandler(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        Context theContext = mContext.get();
        if(theContext != null){
            myHandleMessage(msg);
        }else{
            removeCallbacksAndMessages(null);
        }
    }

    protected abstract void myHandleMessage(Message msg);
}

