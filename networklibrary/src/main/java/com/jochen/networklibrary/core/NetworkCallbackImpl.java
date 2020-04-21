package com.jochen.networklibrary.core;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jochen.networklibrary.NetworkManager;
import com.jochen.networklibrary.bean.NetworkType;
import com.jochen.networklibrary.utils.Constants;

/**
 * Created by JoChen on 2020/4/21.
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        Log.i(Constants.LOG_TAG, "网络已连接了");
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Log.i(Constants.LOG_TAG, "网络即将中断");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Log.i(Constants.LOG_TAG, "网络已中断");
        NetworkManager.getDefault().post(NetworkType.NONE);
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Log.i(Constants.LOG_TAG, "网络不可用");
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i(Constants.LOG_TAG, "网络发生变更，类型为:wifi");
                NetworkManager.getDefault().post(NetworkType.WIFI);
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i(Constants.LOG_TAG, "网络发生变更，类型为:GPRS");
                NetworkManager.getDefault().post(NetworkType.CMWAP);
            } else {
                NetworkManager.getDefault().post(NetworkType.AUTO);
            }
        }
    }
}
