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

import java.util.ArrayList;
import java.util.List;


public class WiFiViewModel extends AndroidViewModel {
    static WifiScanner wifiScanner = new WifiScanner();
    WifiManager wifiManager;
    WiFi tmpWifi;
    WiFiRoomDatabase db;

    public LiveData<List<WiFi>> allWifi = new MutableLiveData<List<WiFi>>(dummuList());
    public MutableLiveData<String> recommendedWiFi = new MutableLiveData<String>();
    private MutableLiveData<WiFi> currentWifi; // Current WiFi ssid
    private MutableLiveData<WiFi> scanResultWifi;  // Current WiFi for Scan
    private WiFiDao mWiFiDao;

    private List<WiFi> dummuList(){
        List<WiFi> list = new ArrayList<>();
        list.add(new WiFi());
        return list;
    }

    public LiveData<List<WiFi>> getAllWifi(){
        //allWifi.postValue(getAllWifiFromDB());
        allWifi = getAllWifiFromDB();
        if (allWifi == null) {
            allWifi = new MutableLiveData<List<WiFi>> ();
        }
        return allWifi;
    }

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

    public MutableLiveData<String> getRecommendedWifi() {
        if (recommendedWiFi == null){
            recommendedWiFi = new MutableLiveData<>();
        }
        return recommendedWiFi;
    }

    public MutableLiveData<WiFi> getScanResultWifi(){
        if (scanResultWifi == null){
            scanResultWifi = new MutableLiveData<>();
        }
        return scanResultWifi;
    }

    public void setRecommend(WiFi curWifi){
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

        if (max_rssi > curWifi.getRssiLevel()){
            // Except Current WiFi
            if (!tmp_ssid.equals(curWifi.getSsid().replaceAll("^\"|\"$", ""))) {
                recommendedWiFi.setValue(tmp_ssid);
            }
        }
    }


    public WiFiViewModel(@NonNull Application application) {
        super(application);
        db = WiFiRoomDatabase.getDatabase(application);
        mWiFiDao = db.wifiDao();
    }

    public void scan(Context context){
        WiFi avgWifi;
        avgWifi = wifiScanner.scan(context);

        scanResultWifi.postValue(avgWifi);
    }

    public void save(){
        if (getCurrentWifi().getValue() != null){
            tmpWifi = getCurrentWifi().getValue();
            tmpWifi.setSsid(tmpWifi.getSsid().replaceAll("^\"|\"$", ""));
            tmpWifi.setName(tmpWifi.getName().replaceAll("^\"|\"$", ""));

            if (tmpWifi != null) {
                insert(tmpWifi);
            }
            else {
                Log.d("save", "Insert but wifi is Null");
            }
        }
    }

    LiveData<List<WiFi>> getAllWifiFromDB() { return mWiFiDao.getAll();}

    public void insert(WiFi wifi) { mWiFiDao.insert(wifi); }

    public void update(WiFi wifi) { mWiFiDao.update(wifi); }

    public void delete(WiFi wifi) { mWiFiDao.delete(wifi); }

    public void getWiFi(String ssid) { mWiFiDao.getWiFi(ssid); }

}
