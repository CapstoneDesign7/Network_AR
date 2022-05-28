package com.team7.nar.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_list")
public class WiFi {
    @Override
    public String toString() {
        return "WiFi{" +
                "ssid='" + ssid + '\'' +
                ", name='" + name + '\'' +
                ", linkSpeed=" + linkSpeed +
                ", rssiLevel=" + rssiLevel +
                ", time='" + time + '\'' +
                '}';
    }

    public WiFi(){
        this.ssid = "Dummy";
        this.name = "Dummy";
        this.linkSpeed = 127;
        this.rssiLevel = 127;
        this.time = "20220528";
    }


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ssid")
    private String ssid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "link_speed")
    private int linkSpeed;

    @ColumnInfo(name = "rssi_level")
    private int rssiLevel;

    @ColumnInfo(name = "time")
    private String time;

    public WiFi(@NonNull String ssid, String name, int linkSpeed, int rssiLevel, String time){
        this.ssid = ssid;
        this.name = name;
        this.linkSpeed = linkSpeed;
        this.rssiLevel = rssiLevel;
        this.time = time;
    }

    @NonNull
    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(@NonNull String ssid) {
        this.ssid = ssid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLinkSpeed() {
        return this.linkSpeed;
    }

    public void setLinkSpeed(int linkSpeed) {
        this.linkSpeed = linkSpeed;
    }

    public int getRssiLevel() {
        return this.rssiLevel;
    }

    public void setRssiLevel(int rssiLevel) {
        this.rssiLevel = rssiLevel;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
