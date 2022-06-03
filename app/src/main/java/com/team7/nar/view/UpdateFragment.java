package com.team7.nar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.team7.nar.R;
import com.team7.nar.model.WiFi;
import com.team7.nar.viewModel.WiFiViewModel;

public class UpdateFragment extends DialogFragment {
    private WiFiViewModel viewModel;
    private WiFi wifi;
    String newName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
        if (getArguments() != null){
            wifi = (WiFi) getArguments().getSerializable("wifi");
        }

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View alertView = layoutInflater.inflate(R.layout.update_fragment, null);
        EditText editText = (EditText) alertView.findViewById(R.id.update_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("수정")
                .setMessage(wifi.getName() + "를 수정하시겠습니까?")
                .setView(alertView)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // YES
                        newName = editText.getText().toString();
                        wifi.setName(newName);

                        new Thread(() -> viewModel.update(wifi)).start();
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