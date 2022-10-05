package com.team7.nar.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Transaction;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.R;
import com.team7.nar.view.DeleteFragment;
import com.team7.nar.view.PhotoFragment;
import com.team7.nar.view.UpdateFragment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder>{
    private Context context;
    private List<WiFi> wifis;
    private FragmentManager fragmentManager;
    private DeleteFragment deleteFragment;
    private UpdateFragment updateFragment;

    WifiAdapter.fragmentListner mlistener;
    public interface fragmentListner{
        void listen(List<File> files,File dir);
    }

    public WifiAdapter(Context context, List<WiFi> wifis, FragmentAdapter fragmentAdapter,fragmentListner listner){
        this.context=context;
        this.wifis=wifis;
        this.fragmentManager = fragmentAdapter.getAdapterFragmentManager();
        this.mlistener = listner;
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

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = ((AppCompatActivity)context).getExternalFilesDir(null).toString()+"/"+wifi.getSsid();
                Log.d("holder path:",path);
                File directory = new File(path);
                if(directory.exists()){
                    mlistener.listen(Arrays.asList(directory.listFiles()),directory);
                    overlayPhoto();
                }


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
        bundle.putSerializable("wifi", wifi);

        deleteFragment = new DeleteFragment();
        deleteFragment.setArguments(bundle);
        deleteFragment.show(fragmentManager, "delete Popup");
    }

    public void updatePopup(WiFi wifi){
        Bundle bundle = new Bundle();
        bundle.putSerializable("wifi", wifi);

        updateFragment = new UpdateFragment();
        updateFragment.setArguments(bundle);
        updateFragment.show(fragmentManager, "update Popup");
    }
    public void overlayPhoto() {
        PhotoFragment photoFragment = new PhotoFragment();
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, photoFragment).addToBackStack(null).commit();
    }

}
