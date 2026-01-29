package com.mecaroid.interdate.Adapters.Recycler;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mecaroid.interdate.ImagesIndex;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.ViewImage;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PostsImagesAdapter extends RecyclerView.Adapter<PostsImagesAdapter.myviewholder> {
    List<ImageModel> data;
    String username;

    public PostsImagesAdapter(List<ImageModel> data, String username) {
        this.data = data;
        this.username = username;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_holder_recycler,parent,false);
        return new  myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.image);
        Glide.with(imageView).load(data.get(position).getUri()).into(imageView);
        ImageButton deleteImage = holder.itemView.findViewById(R.id.deleteImage);
        ImageButton makeProfile = holder.itemView.findViewById(R.id.makeProfile);
        deleteImage.setVisibility(View.GONE);
        makeProfile.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesIndex.currentIndex = position;
                Intent intent = new Intent(v.getContext(), ViewImage.class);
                intent.putExtra("LIST",(Serializable) data);
                intent.putExtra("username",username);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

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
