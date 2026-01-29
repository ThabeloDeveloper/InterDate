package com.mecaroid.interdate.Adapters.Recycler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
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
import com.mecaroid.interdate.Models.RequestReceivedModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.ReturningValues.TimeStamp;
import com.mecaroid.interdate.ViewProfile_In_Uid;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RequestReceivedAdapter extends RecyclerView.Adapter<RequestReceivedAdapter.myviewholder> {
    List<RequestReceivedModel> datata;

    public RequestReceivedAdapter(List<RequestReceivedModel> datata) {
        this.datata = datata;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        ShimmerFrameLayout shima = holder.itemView.findViewById(R.id.shima);
        LinearLayout layoutMain = holder.itemView.findViewById(R.id.layoutMain);

        shima.startShimmer();
        ImageView profiler = holder.itemView.findViewById(R.id.imageProfile);
        TextView name,gender,ager,location;
        AppCompatButton confirm = holder.itemView.findViewById(R.id.confirmRequest);
        AppCompatButton delete = holder.itemView.findViewById(R.id.deleteRequest);
        name = holder.itemView.findViewById(R.id.names);
        gender = holder.itemView.findViewById(R.id.gender);
        ager = holder.itemView.findViewById(R.id.age);
        location = holder.itemView.findViewById(R.id.location);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(datata.get(position).getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    shima.stopShimmer();
                    shima.setVisibility(View.GONE);
                    layoutMain.setVisibility(View.VISIBLE);
                    String profile = data.child("profile").getValue(String.class);
                    String usernamer = data.child("username").getValue(String.class);
                    String age = data.child("age").getValue(String.class);
                    String country = data.child("country").getValue(String.class);
                    String province = data.child("province").getValue(String.class);
                    String city = data.child("city").getValue(String.class);
                    String town = data.child("town").getValue(String.class);
                    Glide.with(profiler).load(profile).into(profiler);
                    name.setText(usernamer);
                    ager.setText(age);
                    location.setText(country +","+province+","+city+","+town);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog confirmingProgress = new ProgressDialog(v.getContext());
                confirmingProgress.setCancelable(false);
                confirmingProgress.setMessage(v.getContext().getString(R.string.please_wait));
                confirmingProgress.show();
                DatabaseReference accetanceCheck = FirebaseDatabase.getInstance().getReference("Request")
                        .child(datata.get(position).getUser_id()).child("Sent");
                Query queryCheck = accetanceCheck.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                queryCheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            accetanceCheck.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DatabaseReference removeOnMe = FirebaseDatabase.getInstance().getReference("Request")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Received");
                                    Query cheToRemove = removeOnMe.orderByChild("user_id").equalTo(datata.get(position).getUser_id()).limitToFirst(1);
                                    cheToRemove.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                DatabaseReference addToOtherFriends = FirebaseDatabase.getInstance()
                                                        .getReference("Friends").child(datata.get(position).getUser_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                HashMap<Object,String> mapToOther = new HashMap<>();
                                                mapToOther.put("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                mapToOther.put("timestamp", TimeStamp.timeStamp());
                                                addToOtherFriends.setValue(mapToOther).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        DatabaseReference addToMe = FirebaseDatabase.getInstance().getReference("Friends")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(datata.get(position).getUser_id());
                                                        HashMap<Object,String> mapToMe = new HashMap<>();
                                                        mapToMe.put("user_id",datata.get(position).getUser_id());
                                                        mapToMe.put("timestamp", TimeStamp.timeStamp());
                                                        addToMe.setValue(mapToMe).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                DatabaseReference removeOnThen = FirebaseDatabase.getInstance().getReference("Request")
                                                                        .child(datata.get(position).getUser_id()).child("Sent").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                Query query1 = removeOnThen.orderByChild("user_id").equalTo(datata.get(position).getUser_id()).limitToFirst(1);
                                                                query1.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                removeOnMe.child(datata.get(position).getUser_id()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        confirmingProgress.dismiss();
                                                                                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.added_to_friends), Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        confirmingProgress.dismiss();
                                                                                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                                                        alert.setCancelable(true);
                                                                                        alert.setTitle(v.getContext().getString(R.string.failed));
                                                                                        alert.setMessage(e.getLocalizedMessage());
                                                                                        alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                dialog.dismiss();
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                confirmingProgress.dismiss();
                                                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                                                alert.setCancelable(true);
                                                                                alert.setTitle(v.getContext().getString(R.string.failed));
                                                                                alert.setMessage(e.getLocalizedMessage());
                                                                                alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });

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
                                                                confirmingProgress.dismiss();
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                                alert.setCancelable(true);
                                                                alert.setTitle(v.getContext().getString(R.string.failed));
                                                                alert.setMessage(e.getLocalizedMessage());
                                                                alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });

                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        confirmingProgress.dismiss();
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                        alert.setCancelable(true);
                                                        alert.setTitle(v.getContext().getString(R.string.failed));
                                                        alert.setMessage(e.getLocalizedMessage());
                                                        alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });

                                            }else {
                                                confirmingProgress.dismiss();
                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                alert.setCancelable(true);
                                                alert.setTitle(v.getContext().getString(R.string.failed));
                                                alert.setMessage(v.getContext().getString(R.string.acceptence_failed)+", " +holder.itemView.getContext().getString(R.string.you) +" "+ v.getContext().getString(R.string.may_have_removed));
                                                alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    confirmingProgress.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                    alert.setCancelable(true);
                                    alert.setTitle(v.getContext().getString(R.string.failed));
                                    alert.setMessage(v.getContext().getString(R.string.acceptence_failed)+", " +"You" +" "+ v.getContext().getString(R.string.may_have_removed));
                                    alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            });
                        }else {
                            confirmingProgress.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setCancelable(true);
                            alert.setTitle(v.getContext().getString(R.string.failed));
                            alert.setMessage(v.getContext().getString(R.string.acceptence_failed));
                            alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog confirmingProgress = new ProgressDialog(v.getContext());
                confirmingProgress.setCancelable(false);
                confirmingProgress.setMessage(v.getContext().getString(R.string.please_wait));
                confirmingProgress.show();
                DatabaseReference accetanceCheck = FirebaseDatabase.getInstance().getReference("Request").child(datata.get(position).getUser_id()).child("Sent").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Query queryCheck = accetanceCheck.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                queryCheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            accetanceCheck.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DatabaseReference removeOnMe = FirebaseDatabase.getInstance().getReference("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Received").child(datata.get(position).getUser_id());
                                    Query cheToRemove = removeOnMe.orderByChild("user_id").equalTo(datata.get(position).getUser_id());
                                    cheToRemove.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                removeOnMe.child(datata.get(position).getUser_id()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        confirmingProgress.dismiss();
                                                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.delete_succesfull), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        confirmingProgress.dismiss();
                                                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                        alert.setCancelable(true);
                                                        alert.setTitle(v.getContext().getString(R.string.failed));
                                                        alert.setMessage(e.getLocalizedMessage());
                                                        alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                    }
                                                });
                                            }else {
                                                confirmingProgress.dismiss();
                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                alert.setCancelable(true);
                                                alert.setTitle(v.getContext().getString(R.string.failed));
                                                alert.setMessage(v.getContext().getString(R.string.acceptence_failed)+", " +"You" +" "+ v.getContext().getString(R.string.may_have_removed));
                                                alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    confirmingProgress.dismiss();
                                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                    alert.setCancelable(true);
                                    alert.setTitle(v.getContext().getString(R.string.failed));
                                    alert.setMessage(v.getContext().getString(R.string.acceptence_failed)+", " +"You" +" "+ v.getContext().getString(R.string.may_have_removed));
                                    alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            });
                        }else {
                            confirmingProgress.dismiss();
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setCancelable(true);
                            alert.setTitle(v.getContext().getString(R.string.failed));
                            alert.setMessage(v.getContext().getString(R.string.acceptence_failed));
                            alert.setPositiveButton(v.getContext().getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        profiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ViewProfile_In_Uid.class);
                intent.putExtra("uid", datata.get(position).getUser_id());
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return datata.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        public myviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }



}
