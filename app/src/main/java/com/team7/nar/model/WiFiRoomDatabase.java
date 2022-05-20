package com.team7.nar.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WiFi.class}, version = 1, exportSchema = false)
public abstract class WiFiRoomDatabase extends RoomDatabase {

    public abstract WiFiDao wifiDao();

    /*
      volatile : 변수를 메인 메모리에 저장
      서로 다른 두개의 스레드에서 동일한 변수 사용 시 Race condition 발생
      메인 메모리에 적재함으로서 Race Condition 해결
    */
    private static volatile WiFiRoomDatabase INSTANCE;

    public static WiFiRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (WiFiRoomDatabase.class){
                // Singleton Pattern
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WiFiRoomDatabase.class, "wifi_list").build();
                }
            }
        }
        return INSTANCE;
    }
}
