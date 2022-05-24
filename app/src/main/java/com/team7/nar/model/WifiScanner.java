package com.team7.nar.model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Calendar;
import java.util.Date;

public class WifiScanner {
    private WifiManager wifiManager;

    private String getNameFromSSID(String SSID){
        return SSID;
    }

    public WiFi scan(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        String tmp = connectionInfo.getSupplicantState().toString();
        if (tmp.equals("COMPLETED")){
            return new WiFi(connectionInfo.getSSID(),getNameFromSSID(connectionInfo.getSSID()),connectionInfo.getLinkSpeed(),
                    connectionInfo.getRssi(), (Calendar. getInstance(). getTime()).toString());
        }
        else{
            return null;
        }
    }
}
