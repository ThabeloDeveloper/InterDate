package com.mecaroid.interdate.Adapters.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecaroid.interdate.Models.ShimaModel;
import com.mecaroid.interdate.R;

import java.util.ArrayList;

public class ShimaGridAdapter extends RecyclerView.Adapter<ShimaGridAdapter.myviewholder> {
    ArrayList<ShimaModel> data;

    public ShimaGridAdapter(ArrayList<ShimaModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shima_mingle_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        public myviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
