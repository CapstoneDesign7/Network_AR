package com.team7.nar.model;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.team7.nar.FragmentAdapter;
import com.team7.nar.R;
import com.team7.nar.view.DeleteFragment;
import com.team7.nar.view.DetailFragment;
import com.team7.nar.view.PhotoFragment;
import com.team7.nar.view.UpdateFragment;
import com.team7.nar.viewModel.WiFiViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ViewHolder> {
    private Context context;
    private List<File> files;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<File> selectList = new ArrayList<>();
    deleteListener mlistener;
    public interface deleteListener{
        void delete(File file);
    }
    public ScreenshotAdapter(Context context, List<File> files, deleteListener listener) {
        this.context = context;
        this.files = files;
        this.mlistener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.screenshot_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = files.get(position);
        holder.setItem(file);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEnable){
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater= mode.getMenuInflater();
                            // inflate menu
                            menuInflater.inflate(R.menu.menu,menu);
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // when action mode is prepare
                            // set isEnable true
                            isEnable=true;
                            // create method
                            ClickItem(holder);
                            // set observer on getText method
                            // return true
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            // when click on action mode item
                            // get item  id
                            int id=item.getItemId();
                            // use switch condition
                            switch(id)
                            {
                                case R.id.menu_delete:
                                    Log.d("files size",String.valueOf(files.size()));
                                    // when click on delete
                                    // use for loop
                                    for(File s:selectList)
                                    {
                                        Log.d("on delete",s.getName());
                                        mlistener.delete(s);
                                        // remove selected item list

                                    }
                                    // check condition

                                    // finish action mode
                                    mode.finish();
                                    for (File f: selectList){
                                        Log.d("selectedlist",f.getName());
                                    }
                                    break;

                                case R.id.menu_select_all:
                                    // when click on select all
                                    // check condition
                                    if(selectList.size()==files.size())
                                    {
                                        // when all item selected
                                        // set isselectall false
                                        isSelectAll=false;
                                        // create select array list
                                        selectList.clear();
                                    }
                                    else
                                    {
                                        // when  all item unselected
                                        // set isSelectALL true
                                        isSelectAll=true;
                                        // clear select array list
                                        selectList.clear();
                                        // add value in select array list
                                        selectList.addAll(files);
                                    }
                                    // set text on view model
                                    // notify adapter
                                    notifyDataSetChanged();
                                    break;
                            }
                            // return true
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // when action mode is destroy
                            // set isEnable false
                            isEnable=false;
                            // set isSelectAll false
                            isSelectAll=false;
                            // clear select array list
                            selectList.clear();
                            // notify adapter
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                }
                else{
                    ClickItem(holder);
                }
                return true;
            }

        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnable)
                {
                    // when action mode is enable
                    // call method
                    ClickItem(holder);
                }
                else
                {
                    // when action mode is not enable
                    // display toast
//                    Toast.makeText(context.getApplicationContext(), "You Clicked"+files.get(holder.getAdapterPosition()),
//                            Toast.LENGTH_SHORT).show();
                    Log.d("overLay", files.get(holder.getAdapterPosition()).toString());
                    overLayDetail(holder);
                }
            }
        });
        // check condition
        if(isSelectAll)
        {
            // when value selected
            // visible all check boc image
            holder.checkbox.setVisibility(View.VISIBLE);
            //set background color

        }
        else
        {
            // when all value unselected
            // hide all check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
        }
    }
    private void ClickItem(ViewHolder holder) {

        // get selected item value
        File f=files.get(holder.getAdapterPosition());
        // check condition
        if(holder.checkbox.getVisibility()==View.GONE)
        {
            // when item not selected
            // visible check box image
            holder.checkbox.setVisibility(View.VISIBLE);
            // set background color
            // add value in select array list
            selectList.add(f);
            Log.d("selected",f.getName());
        }
        else
        {
            // when item selected
            // hide check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            // remove value from select arrayList
            selectList.remove(f);
            Log.d("unselected",f.getName());

        }
        // set text on view model
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView name;
        public View layout;
        public ImageView image;
        public ImageView checkbox;
        public TextView description;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.screenshot_item_layout);
            description = itemView.findViewById(R.id.file_description);
            image = itemView.findViewById(R.id.screenshot_imageview);
            checkbox = itemView.findViewById(R.id.check_box);

        }

        public void setItem(File file) {
            // name.setText(file.getName());
            //image.setBackground(Drawable.createFromPath(file.getPath()));
            image.setImageDrawable(Drawable.createFromPath(file.getPath()));
            description.setText(file.getName().toString());
        }
    }

    public void overLayDetail(ViewHolder holder) {
        DetailFragment detailFragment = new DetailFragment(files.get(holder.getAdapterPosition()).toString());
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, detailFragment).addToBackStack(null).commit();
    }
}
