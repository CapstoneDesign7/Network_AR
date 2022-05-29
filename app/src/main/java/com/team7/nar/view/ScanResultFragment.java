package com.team7.nar.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.team7.nar.databinding.ScanResultBinding;
import com.team7.nar.viewModel.WiFiViewModel;

public class ScanResultFragment extends Fragment {
    private ScanResultBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = ScanResultBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.scannedSSID.setText(getArguments().getString("ssid"));
        binding.scannedName.setText(getArguments().getString("name"));
        binding.scannedRSSI.setText(getArguments().getString("rssi"));
        binding.scannedSpeed.setText(getArguments().getString("speed"));
        binding.scannedTime.setText(getArguments().getString("time"));

        return rootView;
    }
}

