package com.mecaroid.interdate.Adapters.Recycler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mecaroid.interdate.ImagesIndex;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.ViewImage;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class OwnerImagesAdapter extends RecyclerView.Adapter<OwnerImagesAdapter.viewHolder>{

    List<ImageModel> data;
    String username;

    public OwnerImagesAdapter(List<ImageModel> data, String username) {
        this.data = data;
        this.username = username;
    }

    @NonNull
    @Override
    public OwnerImagesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_holder_recycler,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerImagesAdapter.viewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.image);
        ImageButton deleteImage = holder.itemView.findViewById(R.id.deleteImage);
        ImageButton makeProfile = holder.itemView.findViewById(R.id.makeProfile);
        deleteImage.setVisibility(View.VISIBLE);
        makeProfile.setVisibility(View.VISIBLE);
        Glide.with(imageView).load(data.get(position).getUri()).into(imageView);
        Context context = holder.itemView.getContext();
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage(context,position);
            }
        });
        makeProfile.setOnClickListener((view -> {
            makeProfile(context,position);
        }));
        deleteImage.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
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

    private void makeProfile(Context context, int position){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.CustomProgressDialogStyle);
        alertDialog.setMessage(context.getString(R.string.makeProfile));
        alertDialog.setTitle(context.getString(R.string.delete));
        alertDialog.setPositiveButton(R.string.yes,((dialogInterface, i) -> {
            dialogInterface.dismiss();
            performProfiling(context, position);

        }));
        alertDialog.setNegativeButton(R.string.no,(dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        alertDialog.create().show();

    }
    private void performProfiling(Context context,int position){
        ProgressDialog progressDialog = new ProgressDialog(context,R.style.CustomProgressDialogStyle);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile");
        ref.setValue(data.get(position).getUri()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.makeProfileComplete), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context,context.getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteImage(Context context,int position){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.CustomProgressDialogStyle);
        alertDialog.setMessage(context.getString(R.string.delete_image_warning));
        alertDialog.setTitle(context.getString(R.string.delete));
        alertDialog.setPositiveButton(R.string.yes,((dialogInterface, i) -> {
            dialogInterface.dismiss();
            performDeleting(context, position);
        }));
        alertDialog.setNegativeButton(R.string.no,(dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        alertDialog.create().show();

    }
    private void performDeleting(Context context,int position){
        ProgressDialog progressDialog = new ProgressDialog(context,R.style.CustomProgressDialogStyle);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.show();
        StorageReference reference= FirebaseStorage.getInstance().getReferenceFromUrl(data.get(position).getUri());
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Posts/"+FirebaseAuth.getInstance().getCurrentUser().getUid() +"/" +
                        data.get(position).getTime());
                reference1.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null){
                            progressDialog.dismiss();
                            Toast.makeText(context,context.getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(context, context.getString(R.string.delete_succesfull), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,context.getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
