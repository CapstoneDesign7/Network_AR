package com.team7.nar.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team7.nar.R;
import com.team7.nar.databinding.WifiRecyclerviewBinding;
import com.team7.nar.model.WiFi;
import com.team7.nar.model.WifiAdapter;
import com.team7.nar.viewModel.WiFiViewModel;

import java.util.ArrayList;
import java.util.List;

public class WiFiList extends Fragment {
    private WifiRecyclerviewBinding binding;
    private WiFiViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private WifiAdapter wifiAdapter;
    private Context mycontext;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = WifiRecyclerviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mycontext = container.getContext();

        //LinearLayoutManager manager = new LinearLayoutManager(mycontext, LinearLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(manager);
        //recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(mycontext);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.scrollToPosition(0);
        //wifiAdapter = new WifiAdapter();
        //recyclerView.setAdapter(wifiAdapter);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        // add live data to here
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
        RecyclerView recyclerView = binding.recyclerView;

        viewModel.getAllWifi().observe(getViewLifecycleOwner(), new Observer<List<WiFi>>() {
            @Override
            public void onChanged(List<WiFi> wiFis) {
                recyclerView.setAdapter(
                        new WifiAdapter(mycontext, wiFis)
                );
            }
        });
    }
}

