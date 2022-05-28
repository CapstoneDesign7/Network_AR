package com.team7.nar.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.team7.nar.R;

import java.util.ArrayList;
import java.util.List;


public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private Context context;
    private List<WiFi> wifis;

    public WifiAdapter(Context context, List<WiFi> wifis){
        this.context=context;
        this.wifis=wifis;
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
    }

    @Override
    public int getItemCount() {
        return wifis.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView wifiName;
        public TextView wifiSignal;
        public TextView wifiSpeed;
        public ViewHolder(View itemView){
            super(itemView);

            wifiName = itemView.findViewById(R.id.wifi_name);
            wifiSignal = itemView.findViewById(R.id.wifi_signal);
            wifiSpeed= itemView.findViewById(R.id.wifi_speed);
        }

        public void setItem(WiFi wifi){
            wifiName.setText(wifi.getName());
            wifiSignal.setText(String.valueOf(wifi.getRssiLevel()));
            wifiSpeed.setText(String.valueOf(wifi.getLinkSpeed()));
        }
    }
}
