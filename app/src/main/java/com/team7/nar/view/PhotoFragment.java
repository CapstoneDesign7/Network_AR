package com.team7.nar.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.MainActivity;
import com.team7.nar.R;
import com.team7.nar.databinding.FragmentMainBinding;
import com.team7.nar.databinding.ScreenshotRecyclerviewBinding;
import com.team7.nar.databinding.WifiRecyclerviewBinding;
import com.team7.nar.model.ScreenshotAdapter;
import com.team7.nar.model.WiFi;
import com.team7.nar.model.WiFiRoomDatabase;
import com.team7.nar.model.WifiAdapter;
import com.team7.nar.viewModel.WiFiViewModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PhotoFragment extends Fragment implements FragmentAdapter {
    private ScreenshotRecyclerviewBinding binding;
    private WiFiViewModel viewModel;
    private Context mycontext;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = ScreenshotRecyclerviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mycontext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(WiFiViewModel.class);
        RecyclerView recyclerView = binding.screenRecyclerView;
        TextView tvEmpty = binding.tvEmpty;
        viewModel.screenshots.observe(getViewLifecycleOwner(), new Observer<List<File>>() {
            @Override
            public void onChanged(List<File> files) {
                recyclerView.setAdapter(
                        new ScreenshotAdapter(mycontext, files, new ScreenshotAdapter.deleteListener() {
                            @Override
                            public void delete(File file) {
                                if (file.exists()){
                                    file.delete();
                                    Log.d("delete","file deleted");
                                }
                                else{
                                    Log.d("delete","file unexists no delete");
                                }
                                viewModel.screenshots.setValue(Arrays.asList(viewModel.parentPath.getValue().listFiles()));
                            }
                        })

                );
                if (files.size()==0){
                    tvEmpty.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.VISIBLE);

                }
                else{
//                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public FragmentManager getAdapterFragmentManager(){
        return getParentFragmentManager();
    }


}

