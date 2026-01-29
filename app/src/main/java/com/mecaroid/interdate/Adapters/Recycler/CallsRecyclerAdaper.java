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
import com.mecaroid.interdate.R;

import java.util.ArrayList;
import java.util.Objects;

public class CallsRecyclerAdaper extends RecyclerView.Adapter<CallsRecyclerAdaper.myviewholder> {

    ArrayList<CallSModel> data;

    public CallsRecyclerAdaper(ArrayList<CallSModel> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_calls_list,parent,false);
        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        ShimmerFrameLayout shima = holder.itemView.findViewById(R.id.shima);
        Context context = holder.itemView.getContext();
        MaterialCardView realLayout = holder.itemView.findViewById(R.id.RealLayout);
        TextView name = holder.itemView.findViewById(R.id.username);
        ImageView profileImage = holder.itemView.findViewById(R.id.profile);
        ImageView ImageStatus = holder.itemView.findViewById(R.id.status);
        Toolbar calls  = holder.itemView.findViewById(R.id.call_menu);
        TextView status = holder.itemView.findViewById(R.id.lastMessage);
        shima.startShimmer();
        getUserInfo(context,position,shima,realLayout,name,profileImage,ImageStatus,calls,status);

    }

    private void getUserInfo(Context context,int position,ShimmerFrameLayout shima,CardView realLayout,TextView name,ImageView profileImage,ImageView ImageStatus,Toolbar calls,TextView status){
        String userId = data.get(position).getUser_id();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("username");
        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("profile");
        proRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                String profile = snapshot1.getValue(String.class);
                Glide.with(context).load(profile).into(profileImage);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.getValue(String.class);
                        updateUi(context, position, username, shima, realLayout, name, ImageStatus, calls, status);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateUi(Context context,int position,String username,ShimmerFrameLayout shima,CardView realLayout,TextView name,ImageView ImageStatus,Toolbar calls,TextView status){
        shima.stopShimmer();
        shima.setVisibility(View.GONE);
        name.setText(username);
        realLayout.setVisibility(View.VISIBLE);
        if (Objects.equals(data.get(position).getStatus(), "incoming")){
            status.setText(context.getString(R.string.incomingcall));
            ImageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.incoming_call));
        }else {
            status.setText(context.getString(R.string.outgoingcall));
            ImageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.outgoing_call));

        }
        calls.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.videoCall){
                    Intent intent = new Intent(context, Messages.class);
                    intent.putExtra("uid",data.get(position).getUser_id());
                    intent.putExtra("username",username);
                    intent.putExtra("type","Video");
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, Messages.class);
                    intent.putExtra("uid",data.get(position).getUser_id());
                    intent.putExtra("username",username);
                    intent.putExtra("type","Voice");
                    context.startActivity(intent);
                }
                return true;
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
