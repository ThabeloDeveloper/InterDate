package com.mecaroid.interdate.Adapters.Recycler;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Models.BlockListModel;
import com.mecaroid.interdate.R;

import java.util.ArrayList;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.myviewholer> {

    ArrayList<BlockListModel> data;

    public BlockListAdapter(ArrayList<BlockListModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blocklist,parent,false);
        return new myviewholer(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholer holder, int position) {
        LinearLayout realLayout,shimaLayout;
        realLayout = holder.itemView.findViewById(R.id.realLayout);
        shimaLayout = holder.itemView.findViewById(R.id.shimaLayout);
        ShimmerFrameLayout shima = holder.itemView.findViewById(R.id.shima);
        shima.startShimmer();
        TextView username = holder.itemView.findViewById(R.id.username);
        TextView age = holder.itemView.findViewById(R.id.age);
        TextView location = holder.itemView.findViewById(R.id.location);
        ImageView profileImage = holder.itemView.findViewById(R.id.profile);
        AppCompatButton ublock = holder.itemView.findViewById(R.id.unblock);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(data.get(position).getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    String user = snapshot1.child("username").getValue(String.class);
                    String profile = snapshot1.child("profile").getValue(String.class);
                    String agee = snapshot1.child("age").getValue(String.class);
                    username.setText(user);
                    age.setText(agee);
                    Glide.with(profileImage).load(profile).into(profileImage);
                    String country = snapshot1.child("country").getValue(String.class);
                    location.setText(country);
                    shima.stopShimmer();
                    shimaLayout.setVisibility(View.GONE);
                    realLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ublock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(holder.itemView.getContext());
                progressDialog.setMessage(holder.itemView.getContext().getString(R.string.please_wait));
                progressDialog.show();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Blocklist")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(data.get(position).getUser_id());
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(holder.itemView.getContext(), holder.itemView.getContext().getString(R.string.unblock_succesfull), Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(holder.itemView.getContext(), holder.itemView.getContext().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myviewholer extends RecyclerView.ViewHolder{

        public myviewholer(@NonNull View itemView) {
            super(itemView);
        }
    }


}
