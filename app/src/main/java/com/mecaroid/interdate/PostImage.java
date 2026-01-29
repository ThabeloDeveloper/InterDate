package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mecaroid.interdate.Authentication.SignIn;
import com.mecaroid.interdate.databinding.ActivityPostImageBinding;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class PostImage extends AppCompatActivity {

    ActivityPostImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostImageBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String uri = intent.getStringExtra("Uri");
        binding.toUploadImage.setImageURI(Uri.parse(uri));
        binding.tooBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.privateImage.setImageDrawable(getDrawable(R.drawable.private_post));
                }else {
                    binding.privateImage.setImageDrawable(getDrawable(R.drawable.public_));
                }
            }
        });
        binding.uploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.please_wait));
                progressDialog.show();
                StorageReference reference = FirebaseStorage.getInstance().getReference("Posts").child(Objects.requireNonNull(Uri.parse(uri).getLastPathSegment()));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String link = uri.toString();
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("Uri",link);
                                map.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                if (binding.autoSwitch.isChecked()
                                ){
                                    map.put("privacy","private");
                                }else{
                                    map.put("privacy","public");
                                }
                                if (!binding.onMind.getText().toString().trim().isEmpty()){
                                    map.put("onMind",binding.onMind.getText().toString().trim());
                                }
                                Calendar calendar = Calendar.getInstance();
                                String time = calendar.getTimeZone().toString() + calendar.getTime() + calendar.getTimeInMillis();
                                String path;

                                map.put("time",String.valueOf(calendar.getTimeInMillis()));
                                database.getReference().child("Posts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(calendar.getTimeInMillis())).setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                                                alertDialog.setCancelable(false);
                                                alertDialog.setMessage(e.getLocalizedMessage());
                                                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        finish();


                                                    }
                                                });
                                                alertDialog.create();
                                                alertDialog.show();

                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {

                                            }
                                        });






                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage(e.getLocalizedMessage());
                                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(v.getContext(), SignIn.class));
                                        finish();
                                        dialog.dismiss();

                                    }
                                });
                                alertDialog.create();
                                alertDialog.show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(e.getLocalizedMessage());
                        alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(v.getContext(), SignIn.class));
                                finish();
                                dialog.dismiss();

                            }
                        });
                        alertDialog.create();
                        alertDialog.show();
                    }
                });

            }
        });
    }
}