package com.team7.nar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.team7.nar.databinding.ActivityMainBinding;
import com.team7.nar.view.MainFragment;
import com.team7.nar.view.PhotoFragment;
import com.team7.nar.view.WifiListFragment;
import com.team7.nar.viewModel.WiFiViewModel;
import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private WiFiViewModel viewModel;
    public UnityPlayer mUnityPlayer;
    public FragmentManager mFragmentManager;
    public MainFragment mainFragment;
    public WifiListFragment wifiListFragment;
    public PhotoFragment photoFragment;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activitylifecycle","onCreate");
        mFragmentManager = getSupportFragmentManager();
        mainFragment = new MainFragment();
        wifiListFragment = new WifiListFragment();
        photoFragment = new PhotoFragment();
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WiFiViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        onCheckPermission();
        mUnityPlayer = new UnityPlayer(this);
        transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.container, mainFragment).commit();
    }
    /** FOR UNITY **/
    @Override
    protected void onPause() {
        Log.d("activitylifecycle","onPause");
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        Log.d("activitylifecycle","onResume");
        super.onResume();
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }



    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.

    @Override
    protected void onDestroy() {
        Log.d("activitylifecycle","onDestroy");
        super.onDestroy();
        mUnityPlayer.quit();
    }

    private void onCheckPermission() {
        String[] permission_list = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };
        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int reqPer = checkCallingOrSelfPermission(permission);
            if(reqPer == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,777);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int isPermission = 0;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 777)
        {
            // 하나라도 허용 안된 권한이 있으면 isPermission = 1
            for(int i=0; i<grantResults.length; i++) {
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                    isPermission = 1;
                }
            }
            // 하나라도 허용 안된 권한이 있으면 설정창으로 넘어간다
            if(isPermission == 1){
                Toast.makeText(getApplicationContext(),"앱 권한 설정이 필요합니다.\n권한에서 모두 허용해주세요.", Toast.LENGTH_LONG).show();
                showDialogGuideForPermissionSettingGuide();
                finish();
            }
        }
    }
    private void showDialogGuideForPermissionSettingGuide() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        overridePendingTransition(0,0);
    }


    public void overlayList() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, wifiListFragment).addToBackStack(null).commit(); ;
    }
    public void overlayPhoto() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, photoFragment).addToBackStack(null).commit(); ;
    }






}