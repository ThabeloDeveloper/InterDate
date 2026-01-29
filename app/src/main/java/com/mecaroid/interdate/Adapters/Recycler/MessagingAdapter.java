package com.mecaroid.interdate.Adapters.Recycler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Messages;
import com.mecaroid.interdate.Models.MessagingModel;
import com.mecaroid.interdate.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.myviewholder>{

    List<MessagingModel> data;

    public MessagingAdapter(List<MessagingModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messaging,parent,false);

        return new myviewholder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        RelativeLayout sentLayout = holder.itemView.findViewById(R.id.sentLayout);

        TextView sentText = holder.itemView.findViewById(R.id.sentText);
        RelativeLayout receivedLayout = holder.itemView.findViewById(R.id.recicevedLayout);

        TextView receivedMsg = holder.itemView.findViewById(R.id.receivedText);
        Context context = holder.itemView.getContext();
        ImageView status = holder.itemView.findViewById(R.id.status);
        if (Objects.equals(data.get(position).getReceiver(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
            receivedLayout.setVisibility(View.VISIBLE);
            if (!data.get(position).getMessage().isEmpty()){
                receivedLayout.setVisibility(View.VISIBLE);
                receivedMsg.setText(data.get(position).getMessage());
            }
            if (Objects.equals(data.get(position).getStatus(), "delivered") || Objects.equals(data.get(position).getStatus(), "sent")){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(data.get(position).getSender()).child(data.get(position).getTime());
                HashMap<String,Object> map = new HashMap<>();
                map.put("status","seen");
                reference.updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        ref.onDisconnect();
                    }
                });
            }




        }

        if (Objects.equals(data.get(position).getSender(), FirebaseAuth.getInstance().getCurrentUser().getUid())){
            sentLayout.setVisibility(View.VISIBLE);
            if (!data.get(position).getMessage().isEmpty()){
                sentLayout.setVisibility(View.VISIBLE);
                sentText.setText(data.get(position).getMessage());

            }
            if (Objects.equals(data.get(position).getStatus(), "sent")){
                status.setImageDrawable(context.getDrawable(R.drawable.done));
                status.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
            } else if (Objects.equals(data.get(position).getStatus(), "delivered")) {
                status.setImageDrawable(context.getDrawable(R.drawable.done_all_del));
                status.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
            } else if (Objects.equals(data.get(position).getStatus(), "seen")) {
                status.setImageDrawable(context.getDrawable(R.drawable.done_all_del));
                status.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.blue)));
            }

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
