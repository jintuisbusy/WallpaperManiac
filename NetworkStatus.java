package com.jackapps.wallpaper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Jack on 2/28/2017.
 */
public class NetworkStatus {

    Context context;

    NetworkStatus(Context context) {
            this.context = context;
    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo ni = connectivity.getActiveNetworkInfo();
            if(ni!=null && ni.isConnected() && ni.isConnectedOrConnecting() && ni.isAvailable())
                return true;
        }
        return false;
    }
}

