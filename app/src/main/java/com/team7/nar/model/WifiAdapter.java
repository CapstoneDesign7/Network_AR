package com.team7.nar.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.R;
import com.team7.nar.view.DeleteFragment;
import com.team7.nar.view.UpdateFragment;
import com.team7.nar.view.WifiListFragment;
import com.team7.nar.viewModel.WiFiViewModel;

import java.util.List;
import java.util.Objects;


public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder>{
    private Context context;
    private List<WiFi> wifis;
    private WiFiViewModel viewModel;
    private WifiListFragment wiFiList;
    private FragmentManager fragmentManager;
    private DeleteFragment deleteFragment;


    public WifiAdapter(Context context, List<WiFi> wifis, WiFiViewModel viewModel, FragmentAdapter fragmentAdapter){
        this.context=context;
        this.wifis=wifis;
        this.viewModel=viewModel;
        this.fragmentManager = fragmentAdapter.getAdapterFragmentManager();
    }

    public WifiAdapter(Context context, List<WiFi> wifis, FragmentAdapter fragmentAdapter){
        this.context=context;
        this.wifis=wifis;
        this.fragmentManager = fragmentAdapter.getAdapterFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.wifi_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WiFi wifi = wifis.get(position);
        holder.setItem(wifi);


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePopup(wifi);
            }
        });

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                updatePopup(wifi);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return wifis.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView wifiName;
        public TextView wifiSignal;
        public TextView wifiSpeed;
        public View card;
        public ImageView deleteButton;

        public ViewHolder(View itemView){
            super(itemView);

            wifiName = itemView.findViewById(R.id.wifi_name);
            wifiSignal = itemView.findViewById(R.id.wifi_signal);
            wifiSpeed= itemView.findViewById(R.id.wifi_speed);
            card = itemView.findViewById(R.id.card);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void setItem(WiFi wifi){
            wifiName.setText(wifi.getName());
            wifiSignal.setText(String.valueOf(wifi.getRssiLevel()));
            wifiSpeed.setText(String.valueOf(wifi.getLinkSpeed()));
        }
    }

    public void deletePopup(WiFi wifi) {
        Bundle bundle = new Bundle();
        bundle.putString("ssid", wifi.getSsid());
        bundle.putString("name", wifi.getName());
        bundle.putString("rssi", String.valueOf(wifi.getRssiLevel()));
        bundle.putString("speed", String.valueOf(wifi.getLinkSpeed()));
        bundle.putString("time", wifi.getTime());

        DeleteFragment deleteFragment = new DeleteFragment();
        deleteFragment.setArguments(bundle);
        deleteFragment.show(fragmentManager, "delete Popup");
    }

    public void updatePopup(WiFi wifi){
        Bundle bundle = new Bundle();
        bundle.putString("ssid", wifi.getSsid());
        bundle.putString("name", wifi.getName());
        bundle.putString("rssi", String.valueOf(wifi.getRssiLevel()));
        bundle.putString("speed", String.valueOf(wifi.getLinkSpeed()));
        bundle.putString("time", wifi.getTime());

        UpdateFragment updateFragment = new UpdateFragment();
        updateFragment.setArguments(bundle);
        updateFragment.show(fragmentManager, "update Popup");
    }

}
