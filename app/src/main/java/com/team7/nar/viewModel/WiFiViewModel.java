package com.team7.nar.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team7.nar.model.WiFi;
import com.team7.nar.model.WiFiDao;
import com.team7.nar.model.WiFiRoomDatabase;

import java.util.List;

public class WiFiViewModel extends AndroidViewModel {

    private final LiveData<List<WiFi>> mAllWiFi;

    private WiFiDao mWiFiDao;

    public WiFiViewModel(@NonNull Application application) {
        super(application);
        WiFiRoomDatabase db = WiFiRoomDatabase.getDatabase(application);
        mWiFiDao = db.wifiDao();
        mAllWiFi = mWiFiDao.getAll();
    }

    LiveData<List<WiFi>> getAllWiFi() { return mAllWiFi; }

    public void insert(WiFi wifi) { mWiFiDao.insert(wifi); }

    public void update(WiFi wifi) { mWiFiDao.update(wifi); }

    public void delete(WiFi wifi) { mWiFiDao.delete(wifi); }

    public void getWiFi(String ssid) { mWiFiDao.getWiFi(ssid); }

}
