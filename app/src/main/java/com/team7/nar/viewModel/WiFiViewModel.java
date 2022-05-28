package com.team7.nar.viewModel;

import android.app.Application;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team7.nar.model.WiFi;
import com.team7.nar.model.WiFiDao;
import com.team7.nar.model.WiFiRoomDatabase;
import com.team7.nar.model.WifiScanner;

import java.util.List;

public class WiFiViewModel extends AndroidViewModel {
    static WifiScanner wifiScanner = new WifiScanner();
    WifiManager wifiManager;
    WifiInfo connectionInfo;
    WiFi tmpWifi;
    WiFiRoomDatabase db;

    private final LiveData<List<WiFi>> mAllWiFi;
    public MutableLiveData<String> recommendedWiFi = new MutableLiveData<String>();
    private MutableLiveData<WiFi> currentWifi;
    private WiFiDao mWiFiDao;

    public MutableLiveData<WiFi> getCurrentWifi() {
        if (currentWifi == null) {
            currentWifi = new MutableLiveData<WiFi>();
        }
        return currentWifi;
    }

    public void setCurrentWifi(Context context){
        tmpWifi = wifiScanner.getCurrentWifi(context);

        if (tmpWifi != null){
            currentWifi = getCurrentWifi();
            currentWifi.setValue(tmpWifi);
        }else{
            currentWifi.setValue(null);
        }
    }

    public WiFiViewModel(@NonNull Application application) {
        super(application);
        db = WiFiRoomDatabase.getDatabase(application);
        mWiFiDao = db.wifiDao();
        mAllWiFi = mWiFiDao.getAll();
    }

    public void save(){
        if (getCurrentWifi().getValue() != null){
            tmpWifi = getCurrentWifi().getValue();

            if (tmpWifi != null) {
                insert(tmpWifi);
            }
            else {
                Log.d("save", "Insert but wifi is Null");
            }
        }
    }

    public void recommend(WiFi curWifi){
        wifiManager = (WifiManager) getApplication().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wifiManager.getScanResults();
        String tmp_ssid = "";
        int max_rssi = -127;

        for(ScanResult scanResult : results) {
            if (max_rssi < scanResult.level) {
                max_rssi = scanResult.level;
                tmp_ssid = scanResult.SSID;
            }
        }
        if (max_rssi > curWifi.getRssiLevel()) {

            recommendedWiFi.setValue(tmp_ssid);
        }
    }

    LiveData<List<WiFi>> getAllWiFi() { return mAllWiFi; }

    public void insert(WiFi wifi) { mWiFiDao.insert(wifi); }

    public void update(WiFi wifi) { mWiFiDao.update(wifi); }

    public void delete(WiFi wifi) { mWiFiDao.delete(wifi); }

    public void getWiFi(String ssid) { mWiFiDao.getWiFi(ssid); }

}
