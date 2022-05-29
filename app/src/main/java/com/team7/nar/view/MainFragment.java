package com.team7.nar.view;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.team7.nar.R;
import com.team7.nar.WifiBroadcastListener;
import com.team7.nar.WifiReceiver;
import com.team7.nar.databinding.FragmentMainBinding;
import com.team7.nar.model.WiFi;
import com.team7.nar.viewModel.WiFiViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements WifiBroadcastListener {
    private FragmentMainBinding binding;
    private WiFiViewModel viewModel;

    DisconnectedFragment disconnectedFragment;
    ConnectedFragment connectedFragment;
    ScanResultFragment scanResultFragment;
    EmptyResultFragment emptyResultFragment;
    WifiReceiver receiver = new WifiReceiver(this);

    public MainFragment() {

    }

    @Override
    public void wifiConnected() {
        viewModel.setCurrentWifi(this.getContext());
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);

        changeFragment(0, "default disconnected");

        viewModel.getCurrentWifi().observe(getViewLifecycleOwner(),
                new Observer<WiFi>() {
                    @Override
                    public void onChanged(WiFi wiFi) {
                        if (wiFi == null) {
                            changeFragment(0, "disconnected");
                        } else {
                            changeFragment(1, wiFi.getName());
                        }
                    }
                }
        );

        viewModel.getRecommendedWifi().observe(getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        recommendPopup(s);
                    }
                }
        );

        viewModel.getScanResultWifi().observe(getViewLifecycleOwner(),
                new Observer<WiFi>() {
                    @Override
                    public void onChanged(WiFi wiFi) {
                        if ( wiFi != null){
                            viewModel.setRecommend(wiFi);
                        }
                        showScanResult(wiFi);
                    }
                }
        );

        binding.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "List clicked", Toast.LENGTH_LONG).show();
                new Thread(() -> viewModel.getAllWifi()).start();
                Navigation.findNavController(view).navigate(MainFragmentDirections.actionMainFragmentToWiFiList());
            }
        });

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> viewModel.scan(getContext())).start();
                Log.d("clicked", "scanbutton clicked");
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Save clicked", Toast.LENGTH_LONG).show();
                new Thread(() -> viewModel.save()).start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    public void changeFragment(int flag, String name) {
        // Connected
        if (flag == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            connectedFragment = new ConnectedFragment();
            connectedFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.statusContainer, connectedFragment).commit();
        }
        // Disconnected
        else {
            disconnectedFragment = new DisconnectedFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.statusContainer, disconnectedFragment).commit();
        }
    }

    public void recommendPopup(String ssid) {
        Bundle bundle = new Bundle();
        bundle.putString("ssid", ssid);

        Log.d("Recommend", "Main Fragment 에러");

        RecommendFragment recommendFragment = new RecommendFragment();
        recommendFragment.setArguments(bundle);

        recommendFragment.show(getParentFragmentManager(), "recommend Popup");

    }

    public void showScanResult(WiFi scannedWifi){
        if (scannedWifi != null){
            Bundle bundle = new Bundle();

            bundle.putString("ssid", scannedWifi.getSsid());
            bundle.putString("name", scannedWifi.getName());
            bundle.putString("rssi", String.valueOf(scannedWifi.getRssiLevel()));
            bundle.putString("speed", String.valueOf(scannedWifi.getLinkSpeed()));
            bundle.putString("time", scannedWifi.getTime());

            scanResultFragment = new ScanResultFragment();
            scanResultFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction().replace(R.id.scanResultContainer, scanResultFragment).commit();
        }
        else{
            Log.d("scan result", "null scan");
            emptyResultFragment = new EmptyResultFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.scanResultContainer, emptyResultFragment).commit();
        }
    }
}