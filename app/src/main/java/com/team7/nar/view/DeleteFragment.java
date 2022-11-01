package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team7.nar.R;
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

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        TextView text;

        text = (TextView) view.findViewById(R.id.dialogtext);
        text.setText(wifi.getName()+"를 삭제하시겠습니까?");

        Button yes = view.findViewById(R.id.dialog_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                dismiss();
            }
        });
        Button no = view.findViewById(R.id.dialog_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(view);

//        builder.setMessage(wifi.getName()+"를 삭제하시겠습니까?")
//                .setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // YES
//                        new Thread(() -> viewModel.delete(wifi)).start();
//                        String path = getActivity().getExternalFilesDir(null).toString()+"/"+wifi.getSsid();
//                        File parentDir = new File(path);
//                        File[] children = parentDir.listFiles();
//                        if (parentDir.exists()) {
//                            for (File child : children) {
//                                Log.d("delete", child.toString());
//                                child.delete();
//                            }
//                            Log.d("delete", parentDir.toString());
//                            parentDir.delete();
//                        }
//                    }
//                })
//                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // No
//
//                    }
//                });

        return builder.create();
    }
}