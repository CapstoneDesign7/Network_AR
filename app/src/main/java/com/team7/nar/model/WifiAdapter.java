package com.team7.nar.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.team7.nar.R;

import java.util.ArrayList;


public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    ArrayList<WiFi> wifis = new ArrayList<WiFi>();
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
        TextView wifiName;
        TextView wifiSignal;
        TextView wifiSpeed;
        public ViewHolder(View itemView){
            super(itemView);

            wifiName = itemView.findViewById(R.id.wifi_name);
            wifiSignal = itemView.findViewById(R.id.wifi_signal);
            wifiSpeed= itemView.findViewById(R.id.wifi_speed);
        }

        public void setItem(WiFi wifi){
            wifiName.setText(wifi.getName());
            wifiSignal.setText(wifi.getRssiLevel());
            wifiSpeed.setText(wifi.getLinkSpeed());
        }
    }
}
