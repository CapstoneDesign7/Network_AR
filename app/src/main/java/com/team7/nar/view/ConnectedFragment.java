package com.team7.nar.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.team7.nar.R;

public class ConnectedFragment extends Fragment {
    private TextView statusName;
    private String name;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.connected, container, false);

        statusName = rootView.findViewById(R.id.statusName);
        if (getArguments() != null){
            name = getArguments().getString("name");
            statusName.setText(name);
        }
        return rootView;
   }
}

