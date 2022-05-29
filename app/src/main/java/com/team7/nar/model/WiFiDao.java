package com.team7.nar.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WiFiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WiFi wifi);

    @Update
    void update(WiFi wifi);

    @Delete
    void delete(WiFi wifi);

    @Query("SELECT * FROM wifi_list")
    List<WiFi> getAll();

    @Query("SELECT * FROM wifi_list WHERE ssid = :ssid")
    LiveData<WiFi> getWiFi(String ssid);

    //@Query()
}
