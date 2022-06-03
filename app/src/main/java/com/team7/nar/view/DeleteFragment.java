package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.R;
import com.team7.nar.model.WiFi;
import com.team7.nar.model.WifiAdapter;
import com.team7.nar.viewModel.WiFiViewModel;

import java.io.Serializable;
import java.util.List;

public class DeleteFragment extends DialogFragment {
    private WiFiViewModel viewModel;
    private WiFi wifi;
    private WifiAdapter wifiAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String deleteSsid = getArguments().getString("ssid").replaceAll("^\"|\"$", "");
        String deleteName = getArguments().getString("name").replaceAll("^\"|\"$", "");
        int deleteRssi = Integer.parseInt(getArguments().getString("rssi"));
        int deleteSpeed = Integer.parseInt(getArguments().getString("speed"));
        String deleteTime = getArguments().getString("time");

//        Log.d("ssid", deleteSsid);
//        Log.d("name", deleteName);
//        Log.d("rssi", String.valueOf(deleteRssi));
//        Log.d("speed", String.valueOf(deleteSpeed));
//        Log.d("time", deleteTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("삭제")
                .setMessage(deleteSsid+"를 삭제하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // YES
                        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
                        wifi = new WiFi(deleteSsid, deleteName, deleteSpeed, deleteRssi, deleteTime);
//                        Log.d("delete fragment", wifi.toString());
//                        new Thread(() -> viewModel.delete(wifi));
                        wifiAdapter = new WifiAdapter();
                        wifiAdapter.deleteThread(wifi, viewModel);
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // No

                    }
                });

        return builder.create();
    }
}