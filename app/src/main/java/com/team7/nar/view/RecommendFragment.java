package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.fragment.app.DialogFragment;

public class RecommendFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String recommendSSID = getArguments().getString("ssid");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("더 좋은 Wi-Fi가 있습니다.")
                .setMessage(recommendSSID+"로 연결을 변경하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // YES
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NO
                    }
                });
        return builder.create();
    }
}