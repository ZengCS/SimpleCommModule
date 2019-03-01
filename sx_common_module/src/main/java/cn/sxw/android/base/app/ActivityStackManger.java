package cn.sxw.android.base.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import cn.sxw.android.base.utils.LogUtil;

/**
 * Activity堆栈管理
 *
 * @author ZengCS
 * @since 2014年8月16日
 */
public class ActivityStackManger {
    private static final String TAG = "ActivityStackManger";
    private static ActivityStackManger mSingleInstance;
    private Stack<Activity> mActivityStack;

    private ActivityStackManger() {
        mActivityStack = new Stack<Activity>();
    }

    /**
     * 获取单例对象
     */
    public static ActivityStackManger getInstance() {
        if (null == mSingleInstance) {
            mSingleInstance = new ActivityStackManger();
        }
        return mSingleInstance;
    }

    public Stack<Activity> getStack() {
        return mActivityStack;
    }

    /**
     * 入栈
     */
    public void addActivity(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 出栈
     */
    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取当前的Activity(最后一个入栈的Activity)
     */
    public Activity getCurrentActivity() {
        if (mActivityStack.size() == 0)
            return null;
        return mActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（最后一个入栈的Activity）
     */
    public void finishCurrentActivity() {
        Activity activity = mActivityStack.lastElement();
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        LogUtil.methodStep("结束所有Activity");
        Activity activity;
        while (!mActivityStack.empty()) {
            activity = mActivityStack.pop();
            if (activity != null)
                activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public boolean finishActivity(Activity activity) {
        if (null != activity) {
            mActivityStack.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
            return true;
        }
        return false;
    }

    /**
     * 结束指定的Activity
     */
    public boolean finishActivity(Class<? extends Activity> actCls) {
        Activity activity = findActivityByClass(actCls);
        return finishActivity(activity);
    }

    /**
     * 根据类名查找Activity
     */
    public Activity findActivityByClass(Class<? extends Activity> actCls) {
        Activity tempActivity = null;
        Iterator<Activity> itr = mActivityStack.iterator();
        while (itr.hasNext()) {
            tempActivity = itr.next();
            if (null != tempActivity && tempActivity.getClass().getName().equals(actCls.getName())) {
                break;
            }
            tempActivity = null;
        }
        return tempActivity;
    }

    /**
     * finish指定的Activity之上的所有Activity
     */
    public boolean finishToActivity(Class<? extends Activity> actCls, boolean isIncludeSelf) {
        List<Activity> buf = new ArrayList<Activity>();
        int size = mActivityStack.size();
        Activity activity = null;
        for (int i = size - 1; i >= 0; i--) {
            activity = mActivityStack.get(i);
            if (activity.getClass().isAssignableFrom(actCls)) {
                if (isIncludeSelf) {
                    buf.add(activity);
                }
                for (Activity a : buf) {
                    a.finish();
                }
                return true;
            } else if (i == size - 1 && isIncludeSelf) {
                buf.add(activity);
            } else if (i != size - 1) {
                buf.add(activity);
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        LogUtil.w("退出应用程序");
        finishAllActivity();
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.gc();
    }
}
