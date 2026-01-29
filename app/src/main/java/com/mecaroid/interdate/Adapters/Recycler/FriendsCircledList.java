package com.mecaroid.interdate.Adapters.Recycler;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Messages;
import com.mecaroid.interdate.Models.AcquaintanceModel;
import com.mecaroid.interdate.Models.FriendsCircledModel;
import com.mecaroid.interdate.Public.AdsServices;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.FriendscircledBinding;

import java.util.ArrayList;
import java.util.Objects;

public class FriendsCircledList extends RecyclerView.Adapter<FriendsCircledList.myViewHolder> {

    ArrayList<FriendsCircledModel> dataa;

    public FriendsCircledList(ArrayList<FriendsCircledModel> dataa) {
        this.dataa = dataa;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendscircled,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        getResources(context,position,holder.itemView);
        ChangeStatus(position,holder.itemView);




    }
    private void clickAnimationFunctions(View view,int position,String username){
        Intent intent = new Intent(view.getContext(), Messages.class);
        intent.putExtra("uid", dataa.get(position).getUser_id());
        intent.putExtra("username", username);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }
                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        view.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animator) {
                                AdsServices services =  new AdsServices();
                                services.showMyAd(view.getContext(), intent,view);

                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animator) {

                            }
                        }).start();


                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                }).start();
            }
        });
    }

    private void getResources(Context context,int position,View itemView){
        ImageView imageProfile = itemView.findViewById(R.id.imageProfile);
        LinearLayout circledMain = itemView.findViewById(R.id.circledMain);
        TextView username = itemView.findViewById(R.id.username);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(dataa.get(position).getUser_id()).child("profile");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(context).load(snapshot.getValue(String.class)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        circledMain.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).start();
                        return false;
                    }
                }).into(imageProfile);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+dataa.get(position).getUser_id()+"/username");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        username.setText(snapshot.getValue(String.class));
                        clickAnimationFunctions(itemView,position,snapshot.getValue(String.class));
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
    private void ChangeStatus(int position,View view){
        MaterialCardView onlineStatus = view.findViewById(R.id.onlineStatus);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Presence").child(dataa.get(position).getUser_id());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userStatus = snapshot.getValue(String.class);
                    if (Objects.equals(userStatus, "online")){
                        onlineStatus.setBackgroundTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.LightGreen)));


                    }
                    if (Objects.equals(userStatus, "offline")){
                        onlineStatus.setBackgroundTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.Gray)));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataa.size();
    }

     class myViewHolder extends RecyclerView.ViewHolder {
        public myViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}
