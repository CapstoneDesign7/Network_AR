package com.team7.nar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
    private WifiBroadcastListener listener;
    public WifiReceiver(WifiBroadcastListener listener){
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null ) {
            listener.wifiConnected();
            // Do your work.
            // e.g. To check the Network Name or other info:

        }
    }
}