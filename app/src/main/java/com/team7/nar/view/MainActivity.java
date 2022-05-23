package com.team7.nar.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.team7.nar.R;

public class MainActivity extends AppCompatActivity {
    DisconnectedFragment disconnectedFragment;
    ConnectedFragment connectedFragment;

    String[] permission_list = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCheckPermission();

        changeFragment();
    }

    public void changeFragment(){
//        disconnectedFragment = new DisconnectedFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.statusContainer, disconnectedFragment).commit();
//
        connectedFragment = new ConnectedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.statusContainer, connectedFragment).commit();
    }

    private void onCheckPermission() {
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