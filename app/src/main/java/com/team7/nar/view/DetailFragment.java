package com.team7.nar.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.R;
import com.team7.nar.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment implements FragmentAdapter {
    private FragmentDetailBinding binding;
    private Context myContext;
    public String path;
    public DetailFragment(String path) {
        this.path = path;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        myContext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.detailImage.setImageDrawable(Drawable.createFromPath(path));
        String[] array = path.split("/");

        String year = array[9].substring(0, 4);
        String month = array[9].substring(4, 6);
        String day = array[9].substring(6, 8);
        String hour = array[9].substring(8, 10);
        String minute = array[9].substring(10, 12);
        String second = array[9].substring(12, 14);

        Log.d("regex", year+"년"+month+"월"+day+"일"+hour+"시"+minute+"분"+second+"초");
        String description = year+"."+month+"."+day+"  "+hour+":"+minute+":"+second;
        binding.fileDescription.setText(description);
    }

    @Override
    public FragmentManager getAdapterFragmentManager(){
        return getParentFragmentManager();
    }
}