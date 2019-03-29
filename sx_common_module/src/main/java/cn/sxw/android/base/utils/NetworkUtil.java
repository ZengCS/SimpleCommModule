package cn.sxw.android.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.sxw.android.base.ui.BaseApplication;

/**
 * Created by Alex.Tang on 2017-07-11.
 */
public class NetworkUtil {

    private NetworkUtil() {
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static final String TAG = "NetworkUtil";

    /**
     * 3G : mConnectivityManager.TYPE_MOBILE mobile 0 WIFI:
     * mConnectivityManager.TYPE_WIFI wifi 1
     */
    public static boolean isConnected() {
        Context context = BaseApplication.getContext();
        if (context == null)
            return false;

        String service = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(service);
        if (mConnectivityManager == null)
            return false;
        NetworkInfo oNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = (oNetworkInfo != null) && (oNetworkInfo.isConnectedOrConnecting());
        LogUtil.w("是否有网络：" + isConnected);
        if (isConnected) {
            LogUtil.w("当前网络的类型的名字是：" + oNetworkInfo.getTypeName());
        }
        return isConnected;
    }

    /**
     * 0 mobile 1 wifi
     *
     * @param context
     * @return
     */
    public static boolean isWIFI(Context context) {
        String service = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(service);
        NetworkInfo oNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = (oNetworkInfo != null) && (oNetworkInfo.isConnectedOrConnecting());
        if (isConnected) {
            LogUtil.w("当前网络的类型是：" + oNetworkInfo.getType());
            LogUtil.w("当前网络的类型的名字是：" + oNetworkInfo.getTypeName());
            if (oNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
