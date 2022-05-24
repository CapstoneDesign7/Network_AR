package com.team7.nar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.team7.nar.R;
import com.team7.nar.databinding.ActivityMainBinding;
import com.team7.nar.viewModel.WiFiViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private WiFiViewModel viewModel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WiFiViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        onCheckPermission();
        NavHostFragment navHostFragment =
                (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 777)
        {
            //권한을 하나라도 허용하지 않는다면 앱 종료
            for(int i=0; i<grantResults.length; i++) {
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"앱 권한 설정이 필요합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}