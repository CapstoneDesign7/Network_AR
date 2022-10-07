package com.team7.nar.view;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.team7.nar.MainActivity;
import com.team7.nar.R;
import com.team7.nar.WifiBroadcastListener;
import com.team7.nar.WifiReceiver;
import com.team7.nar.databinding.FragmentMainBinding;
import com.team7.nar.model.WiFi;
import com.team7.nar.viewModel.WiFiViewModel;
import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements WifiBroadcastListener {
    private FragmentMainBinding binding;
    private WiFiViewModel viewModel;
    FrameLayout frameLayoutForUnity;
    DisconnectedFragment disconnectedFragment;
    ConnectedFragment connectedFragment;
    ScanResultFragment scanResultFragment;
    EmptyResultFragment emptyResultFragment;
    WifiReceiver receiver = new WifiReceiver(this);
    protected UnityPlayer unityPlayer;
    private HashMap<String, Integer> recommendFlag = new HashMap<String, Integer>();
    public MainFragment() {

    }

    @Override
    public void wifiConnected() {
        viewModel.setCurrentWifi(this.getContext());
    }


    public static MainFragment newInstance(String param1, String param2) {
        Log.d("lifecycle","newInstance");
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("lifecycle","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle","onCreateView");
        unityPlayer = ((MainActivity) getActivity()).mUnityPlayer;
        int glesMode = unityPlayer.getSettings().getInt("gles_mode", 1);
        unityPlayer.init(glesMode, false);

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.frameLayoutForUnity = (FrameLayout) binding.unityContainer;


        return view;
    }
//    @Override
//    public void onDestroy() {
//        unityPlayer.quit();
//        super.onDestroy();
//    }

    @Override
    public void onDestroyView() {
        Log.d("lifecycle","onDestroyView");
        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("lifecycle","onViewCreated");
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
                        if (wiFi != null){
                            if (!recommendFlag.containsKey(wiFi.getSsid())){
                                int flag = viewModel.setRecommend(wiFi);
                                recommendFlag.put(wiFi.getSsid(), flag);
                            }else{
                                if (recommendFlag.get(wiFi.getSsid()) == 0) {
                                    int flag = viewModel.setRecommend(wiFi);
                                    recommendFlag.put(wiFi.getSsid(), flag);
                                }
                            }
                        }
                        //showScanResult(wiFi);
                    }
                }
        );

        binding.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "List clicked", Toast.LENGTH_LONG).show();
                new Thread(() -> viewModel.getAllWifi()).start();
                ((MainActivity) getActivity()).overlayList();
                //Navigation.findNavController(view).navigate(MainFragmentDirections.actionMainFragmentToWiFiList());
            }
        });

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> viewModel.scan(getContext())).start();
                unityPlayer.UnitySendMessage("Spawner", "PlaceObjectByTouch", "");
                Log.d("clicked", "scanbutton clicked");
                Log.d("picture directory", getActivity().getExternalFilesDir(null).toString());
                String path = getActivity().getExternalFilesDir(null).toString();
                Log.d("Files", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();
                Log.d("Files", "Size: "+ files.length);
                for (int i = 0; i < files.length; i++)
                {
                    Log.d("Files", "FileName:" + files[i].getName());
                }
                Log.d("Hashes", recommendFlag.toString());
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Save clicked", Toast.LENGTH_LONG).show();
                new Thread(() -> viewModel.save()).start();
                unityPlayer.UnitySendMessage("Spawner","ScreenShot","");
            }
        });
    }

    @Override
    public void onResume() {
        Log.d("lifecycle","onResume");
        super.onResume();
        this.frameLayoutForUnity.addView(unityPlayer.getView(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        unityPlayer.requestFocus();
        unityPlayer.windowFocusChanged(true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStart() {
        Log.d("lifecycle","onstart");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d("lifecycle","onPause");
        super.onPause();
        this.frameLayoutForUnity.removeAllViews();
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

    public void showScanResult(WiFi scannedWifi) {

    }
}