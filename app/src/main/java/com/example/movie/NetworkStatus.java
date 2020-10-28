package com.example.movie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkStatus {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

//    public static int getConnectivityStatus(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        if (networkInfo != null) {
//            int type = networkInfo.getType();
//            if (type == ConnectivityManager.TYPE_MOBILE) {
//                return TYPE_MOBILE;
//            } else if (type == ConnectivityManager.TYPE_WIFI) {
//                return TYPE_WIFI;
//            }
//        }
//        return TYPE_NOT_CONNECTED;
//
//    }
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nwc = cm.getNetworkCapabilities(cm.getActiveNetwork());

        int result = TYPE_NOT_CONNECTED;
        if (nwc != null) {
            if (nwc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                result = TYPE_WIFI;
            }
            else if (nwc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                result = TYPE_MOBILE;
            }
        }
        return result;
    }

}