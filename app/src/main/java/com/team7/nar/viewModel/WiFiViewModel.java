package com.team7.nar.viewModel;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

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
    private boolean check = false;
    WiFiRoomDatabase db;

    private final LiveData<List<WiFi>> mAllWiFi;
    private MutableLiveData<WiFi> currentWifi;
    private WiFiDao mWiFiDao;

    public MutableLiveData<WiFi> getCurrentWifi() {
        if (currentWifi == null) {
            currentWifi = new MutableLiveData<WiFi>();
        }
        return currentWifi;
    }

    public WiFi statusCheck(Context context){
        tmpWifi = wifiScanner.statusCheck(context);

        if (tmpWifi != null){
            currentWifi = getCurrentWifi();
            currentWifi.setValue(tmpWifi);
            check = true;
        }else{
            check = false;
        }
        return tmpWifi;
    }

    public WiFiViewModel(@NonNull Application application) {
        super(application);
        db = WiFiRoomDatabase.getDatabase(application);
        mWiFiDao = db.wifiDao();
        mAllWiFi = mWiFiDao.getAll();
    }

    public void save(){
        if (check){
            currentWifi = getCurrentWifi();
            tmpWifi = currentWifi.getValue();
            if (tmpWifi != null) {
                insert(tmpWifi);
            }
            else {
                Log.d("save", "Insert but wifi is Null");
            }
        }
    }

    LiveData<List<WiFi>> getAllWiFi() { return mAllWiFi; }

    public void insert(WiFi wifi) { mWiFiDao.insert(wifi); }

    public void update(WiFi wifi) { mWiFiDao.update(wifi); }

    public void delete(WiFi wifi) { mWiFiDao.delete(wifi); }

    public void getWiFi(String ssid) { mWiFiDao.getWiFi(ssid); }

}
