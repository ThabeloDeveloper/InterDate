package com.mecaroid.interdate.Adapters.Recycler;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.Models.RequestSentModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RequestSentAdapter extends RecyclerView.Adapter<RequestSentAdapter.myviewholder> {
    List<RequestSentModel> data;

    public RequestSentAdapter(List<RequestSentModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_sent,parent,false);

        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        TextView username,locaition,ager;

        ImageView pro = holder.itemView.findViewById(R.id.profile);
        ShimmerFrameLayout shima = holder.itemView.findViewById(R.id.shima);
        shima.startShimmer();
        LinearLayout realData = holder.itemView.findViewById(R.id.RealData);
        username = holder.itemView.findViewById(R.id.name);
        locaition = holder.itemView.findViewById(R.id.location);
        ager = holder.itemView.findViewById(R.id.age);
        Button delete = holder.itemView.findViewById(R.id.delete);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(data.get(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datata : snapshot.getChildren()){
                    shima.stopShimmer();
                    shima.setVisibility(View.GONE);
                    realData.setVisibility(View.VISIBLE);
                    String profile = datata.child("profile").getValue(String.class);
                    String usernamer = datata.child("username").getValue(String.class);
                    String age = datata.child("age").getValue(String.class);
                    String country = datata.child("country").getValue(String.class);
                    String province = datata.child("province").getValue(String.class);
                    String city = datata.child("city").getValue(String.class);
                    String town = datata.child("town").getValue(String.class);
                    Glide.with(pro).load(profile).into(pro);
                    username.setText(usernamer);
                    ager.setText(age);
                    locaition.setText(country +","+province+", "+city+town);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProgressDialog progressDialog = new ProgressDialog(holder.itemView.getContext());
                            progressDialog.setMessage(holder.itemView.getContext().getString(R.string.please_wait));
                            progressDialog.show();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(data.get(position).getUser_id())
                                    .child("Received").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Query query1 = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                    .getReference("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sent").child(data.get(position).getUser_id());

                                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressDialog.dismiss();

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


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
