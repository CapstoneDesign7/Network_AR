package com.team7.nar.model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WifiScanner {
    private WifiManager wifiManager;

    private String getNameFromSSID(String SSID){
        return SSID;
    }

    public WiFi getCurrentWifi(Context context){
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

    public WiFi scan(Context context){

        List<WiFi> scannedWifis = new ArrayList<>();
        WiFi scannedWifi = new WiFi();
        // 5 Times Scan
        for (int i=0; i<5; i++){
            scannedWifi = getCurrentWifi(context);
            if (scannedWifi != null){
                scannedWifis.add(scannedWifi);
            }
        }

        // Use Average Value
        int avgRSSI = 0;
        int avgLinkspeed = 0;
        for (WiFi scanned : scannedWifis){
            avgRSSI += scanned.getRssiLevel();
            avgLinkspeed += scanned.getLinkSpeed();
        }

        avgRSSI /= scannedWifis.size();
        avgLinkspeed /= scannedWifis.size();

        scannedWifi.setRssiLevel(avgRSSI);
        scannedWifi.setLinkSpeed(avgLinkspeed);

        return scannedWifi;
    }
}
