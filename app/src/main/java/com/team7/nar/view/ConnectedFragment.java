package com.team7.nar.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.team7.nar.R;

public class ConnectedFragment extends Fragment {
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.connected, container, false);
       return rootView;
   }
}
