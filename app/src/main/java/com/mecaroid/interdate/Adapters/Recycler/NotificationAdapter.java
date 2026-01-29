package com.mecaroid.interdate.Adapters.Recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Messages;
import com.mecaroid.interdate.Models.CallSModel;
import com.mecaroid.interdate.Models.NotificationsModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.ViewProfile_In_Uid;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.myviewholder> {

    ArrayList<NotificationsModel> data;

    public NotificationAdapter(ArrayList<NotificationsModel> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_holder,parent,false);
        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Context context = holder.itemView.getContext();
        TextView name = holder.itemView.findViewById(R.id.title);
        TextView body = holder.itemView.findViewById(R.id.body);
        name.setText(data.get(position).getTitle());
        body.setText(data.get(position).getBody());
        holder.itemView.setOnClickListener(v->{
            if(Objects.equals(data.get(position).getType(), "view")) {
                Intent intent = new Intent(context, ViewProfile_In_Uid.class);
                intent.putExtra("uid", data.get(position).getUid());
                context.startActivity(intent);
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
