package com.mecaroid.interdate.Adapters.Recycler;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Messages;
import com.mecaroid.interdate.Models.AcquaintanceModel;
import com.mecaroid.interdate.Models.FriendsCircledModel;
import com.mecaroid.interdate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FriendsOnChatsAdapter extends RecyclerView.Adapter<FriendsOnChatsAdapter.myviewholder> {
    ArrayList<AcquaintanceModel> data;
    String message, status, receiver, sender, time;
    FriendsCircledList Friendsadapter;

    public FriendsOnChatsAdapter(ArrayList<AcquaintanceModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatlist, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Toolbar toolbar = holder.itemView.findViewById(R.id.chat_menu);
        Context context = holder.itemView.getContext();
        ImageView profileR = holder.itemView.findViewById(R.id.profile);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TextView username = holder.itemView.findViewById(R.id.username);
        TextView messagess = holder.itemView.findViewById(R.id.lastMessage);
        ShimmerFrameLayout shima = holder.itemView.findViewById(R.id.shima);
        MaterialCardView realLayout = holder.itemView.findViewById(R.id.RealLayout);
        CardView cardView = holder.itemView.findViewById(R.id.turn_card);
        shima.startShimmer();
        ImageView statuss = holder.itemView.findViewById(R.id.status);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(data.get(position).getUser_id()).child("profile");
        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("Users").child(data.get(position).getUser_id()).child("username");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profile = snapshot.getValue(String.class);
                Glide.with(profileR).load(profile).into(profileR);
                Intent intent = new Intent(holder.itemView.getContext(), Messages.class);
                intent.putExtra("uid", data.get(position).getUser_id());

                usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String usernamer = snapshot.getValue(String.class);
                        intent.putExtra("username", usernamer);
                        username.setText(usernamer);
                        realLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.itemView.getContext().startActivity(intent);

                            }
                        });
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.delete) {
                                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(holder.itemView.getContext(),R.style.CustomProgressDialogStyle);
                                    deleteAlert.setTitle(R.string.delete_your_side_msg);
                                    deleteAlert.setMessage(holder.itemView.getContext().getString(R.string.about_to_de_conv));
                                    deleteAlert.setPositiveButton(holder.itemView.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    deleteAlert.setNegativeButton(holder.itemView.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog deleteProgress = new ProgressDialog(holder.itemView.getContext(),R.style.CustomProgressDialogStyle);
                                            deleteProgress.setCancelable(false);
                                            deleteProgress.setMessage(holder.itemView.getContext().getString(R.string.please_wait));
                                            deleteProgress.show();
                                            dialog.dismiss();
                                            DatabaseReference MylastSent = FirebaseDatabase.getInstance()
                                                    .getReference("Chats/"+Objects.requireNonNull(data.get(position).getUser_id())+"/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            MylastSent.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    deleteProgress.dismiss();
                                                    dialog.dismiss();
                                                    Toast.makeText(context, context.getString(R.string.delete_succesfull), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    deleteProgress.dismiss();
                                                    Toast.makeText(context, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });
                                    deleteAlert.create();
                                    deleteAlert.show();
                                }

                                if (item.getItemId() == R.id.block) {
                                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(holder.itemView.getContext(),R.style.CustomProgressDialogStyle);
                                    deleteAlert.setTitle(R.string.blocking);
                                    deleteAlert.setMessage(holder.itemView.getContext().getString(R.string.blocking_user));
                                    deleteAlert.setPositiveButton(holder.itemView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    deleteAlert.setNegativeButton(holder.itemView.getContext().getString(R.string.continuee), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProgressDialog deleteProgress = new ProgressDialog(holder.itemView.getContext(),R.style.CustomProgressDialogStyle);
                                            deleteProgress.setCancelable(false);
                                            deleteProgress.setMessage(holder.itemView.getContext().getString(R.string.please_wait));
                                            deleteProgress.show();
                                            dialog.dismiss();
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocklist")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(data.get(position).getUser_id());
                                            HashMap<Object, String> mapBlock = new HashMap<>();
                                            mapBlock.put("user_id", data.get(position).getUser_id());
                                            reference.setValue(mapBlock).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    deleteProgress.dismiss();
                                                    Toast.makeText(context, context.getString(R.string.added_to_blocklist), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    deleteProgress.dismiss();
                                                    Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    });
                                    deleteAlert.create();
                                    deleteAlert.show();


                                }
                                if (item.getItemId() == R.id.report) {
                                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(holder.itemView.getContext(),R.style.CustomProgressDialogStyle);
                                    deleteAlert.setTitle(R.string.report);
                                    deleteAlert.setMessage(holder.itemView.getContext().getString(R.string.reporting_user));
                                    deleteAlert.setPositiveButton(holder.itemView.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    deleteAlert.setNegativeButton(holder.itemView.getContext().getString(R.string.continuee), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                                            intent.setData(Uri.parse("mailto:"));
                                            String recipient = "thabelodeveloper@gmail.com";
                                            String subject = "Abuse Report@InterDate.Android";
                                            String body = context.getString(R.string.i) + FirebaseAuth.getInstance().getCurrentUser().getEmail() +context.getString(R.string.here_by_report_of_user ) + username + context.getString(R.string.for_that_please_modify_as_needed);
                                            intent.putExtra(Intent.EXTRA_EMAIL, recipient);
                                            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                            intent.putExtra(Intent.EXTRA_TEXT, body);
                                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                                context.startActivity(intent);

                                            }


                                        }
                                    });
                                    deleteAlert.create();
                                    deleteAlert.show();

                                }
                                return true;
                            }
                        });
                        DatabaseReference MylastSent = FirebaseDatabase.getInstance().getReference("Chats")
                                .child(Objects.requireNonNull(data.get(position).getUser_id()))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Query messages = MylastSent.limitToLast(1);
                        messages.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                shima.stopShimmer();
                                realLayout.setVisibility(View.VISIBLE);
                                shima.setVisibility(View.GONE);
                                profileR.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f).start();
                                if (snapshot.exists()) {
                                    for (DataSnapshot snaLast : snapshot.getChildren()) {
                                        message = snaLast.child("message").getValue(String.class);
                                        status = snaLast.child("status").getValue(String.class);
                                        receiver = snaLast.child("receiver").getValue(String.class);
                                        sender = snaLast.child("sender").getValue(String.class);
                                        time = snaLast.child("time").getValue(String.class);
                                        messagess.setText(message);
                                        if (Objects.equals(receiver, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            notification(usernamer, message, profile, context, position);
                                        }


                                    }

                                }
                                if (message == null) {
                                    messagess.setText(holder.itemView.getContext().getString(R.string.tapToChat));
                                    statuss.setVisibility(View.GONE);
                                }
                                if (Objects.equals(receiver, currentUser)) {
                                    cardView.setVisibility(View.VISIBLE);
                                    statuss.setVisibility(View.GONE);
                                    if (Objects.equals(status, "sent")) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(sender).child(time);
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("status", "delivered");
                                        reference.updateChildren(map, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                ref.onDisconnect();
                                            }
                                        });
                                    }
                                }else{
                                    cardView.setVisibility(View.GONE);
                                }
                                if (Objects.equals(sender, currentUser)) {
                                    statuss.setVisibility(View.VISIBLE);
                                    if (Objects.equals(status, "sent")) {
                                        statuss.setImageDrawable(context.getDrawable(R.drawable.done));
                                        statuss.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.Gray)));
                                    } else if (Objects.equals(status, "delivered")) {
                                        statuss.setImageDrawable(context.getDrawable(R.drawable.done_all_del));
                                        statuss.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.Gray)));
                                    } else if (Objects.equals(status, "seen")) {
                                        statuss.setImageDrawable(context.getDrawable(R.drawable.done_all_del_own));

                                    }
                                }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void notification(String username, String message, String profile, Context context, int position) {
        String CHANNEL_ID = context.getString(R.string.app_name);
        RemoteViews customNotificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        int RequestCode = 0;
        Intent intent = new Intent(context, Messages.class);
        intent.putExtra("uid", data.get(position).getUser_id());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, RequestCode, intent, PendingIntent.FLAG_MUTABLE);
        customNotificationLayout.setTextViewText(R.id.tittle, username);
        customNotificationLayout.setTextViewText(R.id.message, message);
        Uri uri = Uri.parse(profile);
        customNotificationLayout.setImageViewUri(R.id.ImageView, uri);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setCustomContentView(customNotificationLayout)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManagerCompat.notify(1, notification);

        }else{
            notificationManagerCompat.notify(1, notification);
        }




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
