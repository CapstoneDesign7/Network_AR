package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;

import com.team7.nar.model.WiFi;
import com.team7.nar.viewModel.WiFiViewModel;

import java.io.File;

public class DeleteFragment extends DialogFragment {
    private WiFiViewModel viewModel;
    private WiFi wifi;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
        if (getArguments() != null){
            wifi = (WiFi) getArguments().getSerializable("wifi");
        }
        Log.d("Serialize Object", wifi.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("삭제")
                .setMessage(wifi.getName()+"를 삭제하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // YES
                        new Thread(() -> viewModel.delete(wifi)).start();
                        String path = getActivity().getExternalFilesDir(null).toString()+"/"+wifi.getSsid();
                        File parentDir = new File(path);
                        File[] children = parentDir.listFiles();
                        if (parentDir.exists()) {
                            for (File child : children) {
                                Log.d("delete", child.toString());
                                child.delete();
                            }
                            Log.d("delete", parentDir.toString());
                            parentDir.delete();
                        }
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