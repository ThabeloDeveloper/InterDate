package com.mecaroid.interdate.Adapters.Pagers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.common.images.ImageManager;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.R;

import java.util.List;

public class ImagesPagerAdapter extends RecyclerView.Adapter<ImagesPagerAdapter.myViewHolder> {

    List<ImageModel> list;

    public ImagesPagerAdapter(List<ImageModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ImagesPagerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images_pager,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesPagerAdapter.myViewHolder holder, int position) {
        PhotoView photoView = holder.itemView.findViewById(R.id.photo);
        TextView textView = holder.itemView.findViewById(R.id.onMind);
        Context context = holder.itemView.getContext();
        if (list.get(position).getOnMind() !=null && !list.get(position).getOnMind().isEmpty()){
            textView.setText(list.get(position).getOnMind());
        }
        setResources(position,context,photoView);

    }

    private void setResources(int position, Context context,PhotoView view){
        Glide.with(context).load(list.get(position).getUri()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



}
