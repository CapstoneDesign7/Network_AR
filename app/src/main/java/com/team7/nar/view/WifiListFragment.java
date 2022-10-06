package com.team7.nar.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.databinding.WifiRecyclerviewBinding;
import com.team7.nar.model.WiFi;
import com.team7.nar.model.WiFiRoomDatabase;
import com.team7.nar.model.WifiAdapter;
import com.team7.nar.viewModel.WiFiViewModel;

import java.io.File;
import java.util.List;

public class WifiListFragment extends Fragment implements FragmentAdapter {
    private WifiRecyclerviewBinding binding;
    private WiFiViewModel viewModel;
    private WiFiRoomDatabase database;
    private Context mycontext;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = WifiRecyclerviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mycontext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
        RecyclerView recyclerView = binding.recyclerView;
        viewModel.allWifi.observe(getViewLifecycleOwner(), new Observer<List<WiFi>>() {
            @Override
            public void onChanged(List<WiFi> wiFis) {
                recyclerView.setAdapter(
//                        new WifiAdapter(mycontext, wiFis, viewModel, WifiListFragment.this)
                          new WifiAdapter(mycontext, wiFis, WifiListFragment.this, new WifiAdapter.fragmentListner() {
                              @Override
                              public void listen(List<File> files,File dir) {
                                  viewModel.screenshots.setValue(files);
                                  viewModel.parentPath.setValue(dir);
                              }
                          })
                );
            }
        });
    }

    @Override
    public FragmentManager getAdapterFragmentManager(){
        return getParentFragmentManager();
    }

    public void deletePopup(WiFi wifi) {
        Bundle bundle = new Bundle();

        bundle.putString("ssid", wifi.getSsid());
        bundle.putString("name", wifi.getName());
        bundle.putString("rssi", String.valueOf(wifi.getRssiLevel()));
        bundle.putString("speed", String.valueOf(wifi.getLinkSpeed()));
        bundle.putString("time", String.valueOf(wifi.getTime()));

        DeleteFragment deleteFragment = new DeleteFragment();
        deleteFragment.setArguments(bundle);

        deleteFragment.show(getParentFragmentManager(), "delete Popup");
    }
}

