package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.EditText;

import com.team7.nar.R;

import androidx.fragment.app.DialogFragment;

public class ChangeWifiFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        EditText password = new EditText(getContext());

        String recommendSSID = getArguments().getString("ssid");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(recommendSSID + " 연결")
                .setMessage("비밀번호를 입력하세요.")
                .setView(password)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // YES
                        changeWiFi(recommendSSID, password.getText().toString());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NO

                    }
                });
        return builder.create();
    }

    public void changeWiFi(String ssid, String password){
        WifiNetworkSpecifier wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                .setSsid(ssid) //SSID 이름
                .setWpa2Passphrase(password) //비밀번호, 보안설정 WPA2
                //wifiInfo에서 public int getCurrentSecurityType () 가져오기
                .build();

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) //연결 Type
                .setNetworkSpecifier(wifiNetworkSpecifier)
                .build();

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                Log.d("onAvailable", "onAvailable");
                super.onAvailable(network);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // To make sure that requests don't go over mobile data
                    connectivityManager.bindProcessToNetwork(network);
                } else {
                    connectivityManager.setProcessDefaultNetwork(network);
                }
            }
            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d("onAvailable", "onUnavailable");
            }
        };

        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }


}