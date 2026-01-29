package com.mecaroid.interdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mecaroid.interdate.Adapters.Recycler.OwnerImagesAdapter;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.databinding.ActivityUpdateUserPublicInfoBinding;
import com.mecaroid.interdate.databinding.EditchoicesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Update_User_Public_Info extends AppCompatActivity {

    ActivityUpdateUserPublicInfoBinding binding;
    BottomSheetDialog sheetDialog;
    EditchoicesBinding sheetLayout;
    List<ImageModel> DATATA;
    OwnerImagesAdapter adapter;
    String Username;
    String profile_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserPublicInfoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        sheetLayout = EditchoicesBinding.inflate(getLayoutInflater());
        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(sheetLayout.getRoot());
        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()){
                    String username = data.child("username").getValue(String.class);
                    String preferredAgeMin = data.child("preferredAgeMin").getValue(String.class);
                    String preferredAgeMax = data.child("preferredAgeMax").getValue(String.class);
                    String preferredGender = data.child("preferredGender").getValue(String.class);
                    String preferredRace = data.child("preferredRace").getValue(String.class);
                    String preferToRelocate = data.child("preferToRelocate").getValue(String.class);
                    String UserVerified = data.child("UserVerified").getValue(String.class);
                    Glide.with(getApplicationContext()).load(data.child("profile").getValue(String.class)).into(binding.ImageProfile);
                    binding.toolBar.setTitle(username);
                    binding.names.setText(username);
                    binding.age.setText(data.child("age").getValue(String.class));
                    binding.gender.setText(data.child("gender").getValue(String.class));
                    binding.religion.setText(data.child("religion").getValue(String.class));
                    binding.race.setText(data.child("race").getValue(String.class));
                    binding.languages.setText(data.child("languages").getValue(String.class));
                    binding.occupations.setText(data.child("occupation").getValue(String.class));
                    binding.hobbies.setText(data.child("hobbies").getValue(String.class));
                    binding.about.setText(data.child("about_user").getValue(String.class));
                    binding.ifKidsYes.setText(data.child("kids").getValue(String.class));
                    binding.country.setText(data.child("country").getValue(String.class));
                    binding.state.setText(data.child("province").getValue(String.class));
                    binding.city.setText(data.child("city").getValue(String.class));
                    binding.town.setText(data.child("town").getValue(String.class));
                    binding.reLocate.setText(data.child("usertorelocate").getValue(String.class));
                    binding.studentAt.setText(data.child("studentAt").getValue(String.class));
                    binding.qualifications.setText(data.child("qualifications").getValue(String.class));
                    profile_link = data.child("profile").getValue(String.class);

                    //////////////////////NEW//////////////////
                    binding.basics.setText(data.child("basically").getValue(String.class));
                    binding.usernamePost.setText(username + getString(R.string.s) + getString(R.string.space) + getString(R.string.posts));

                    binding.prefferedreligion.setText(data.child("preferredReligion").getValue(String.class));
                    binding.prelocate.setText(preferToRelocate);
                    binding.prefferedrace.setText(preferredRace);
                    binding.preferedgender.setText(preferredGender);
                    binding.ageto.setText(preferredAgeMax);
                    binding.agefrom.setText(preferredAgeMin);
                    binding.haveKid.setVisibility(View.GONE);
                    binding.NothaveKid.setVisibility(View.GONE);
                    binding.save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ProgressDialog dialog = new ProgressDialog(v.getContext());
                            dialog.setCancelable(false);
                            dialog.setMessage(getString(R.string.please_wait));
                            dialog.show();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("profile", data.child("profile").getValue(String.class));
                            if (!binding.names.getText().toString().trim().equals(data.child("username").getValue(String.class))) {
                                map.put("username", binding.names.getText().toString().trim());
                            }if (!binding.age.getText().toString().trim().equals(data.child("age").getValue(String.class))) {
                                map.put("age", binding.age.getText().toString().trim());
                            }if (!binding.gender.getText().toString().trim().equals(data.child("gender").getValue(String.class))){
                                map.put("gender",binding.gender.getText().toString().trim());
                            }if (binding.preferedgender.getText().toString().trim().equals(getString(R.string.male))){
                                map.put("genderCode","6255533");
                            }if (binding.preferedgender.getText().toString().trim().equals(getString(R.string.female))) {
                                map.put("genderCode","333336255533");
                            }if(binding.preferedgender.getText().toString().equals(getString(R.string.Other))){
                                map.put("genderCode","66684433777");
                            }
                            if (!binding.race.getText().toString().trim().equals(data.child("race").getValue(String.class))) {
                                map.put("race",binding.race.getText().toString().trim());
                            }if (!binding.religion.getText().toString().trim().equals(data.child("religion").getValue(String.class))){
                                map.put("religion",binding.religion.getText().toString().trim());
                            }if (!binding.languages.getText().toString().trim().equals(data.child("languages").getValue(String.class))){
                                map.put("languages", binding.languages.getText().toString().trim());
                            }if (!binding.occupations.getText().toString().trim().equals(data.child("occupation").getValue(String.class))){
                                map.put("occupation", binding.occupations.getText().toString().trim());
                            }if (!binding.hobbies.getText().toString().trim().equals(data.child("hobbies").getValue(String.class))){
                                map.put("hobbies", binding.hobbies.getText().toString().trim());
                            }if (!binding.about.getText().toString().trim().equals(data.child("about_user").getValue(String.class))){
                                map.put("about_user", binding.about.getText().toString().trim());
                            }if (!binding.ifKidsYes.getText().toString().trim().equals(data.child("Kids").getValue(String.class))){
                                map.put("Kids",binding.ifKidsYes.getText().toString().trim());
                            }if (!Objects.requireNonNull(binding.country.getText()).toString().trim().equals(data.child("country").getValue(String.class))){
                                map.put("country", binding.country.getText().toString().trim());
                            }if (!binding.state.getText().toString().trim().equals(data.child("province").getValue(String.class))){
                                map.put("province", binding.state.getText().toString().trim());
                            }if (!binding.city.getText().toString().trim().equals(data.child("city").getValue(String.class))){
                                map.put("city", binding.city.getText().toString().trim());
                            }if (!binding.town.getText().toString().trim().equals(data.child("town").getValue(String.class))){
                                map.put("town", binding.town.getText().toString().trim());
                            }if (!binding.reLocate.getText().toString().trim().equals(data.child("usertorelocate").getValue(String.class))){
                                map.put("usertorelocate", binding.reLocate.getText().toString().trim());
                            }if (!binding.studentAt.getText().toString().trim().equals(data.child("studentAt").getValue(String.class))){
                                map.put("studentAt",binding.studentAt.getText().toString().trim());
                            }if (!binding.qualifications.getText().toString().trim().equals(data.child("qualifications").getValue(String.class))){
                                map.put("qualifications", binding.qualifications.getText().toString().trim());
                            }if (!binding.agefrom.getText().toString().trim().equals(data.child("preferredAgeMin").getValue(String.class))){
                                map.put("preferredAgeMin", binding.agefrom.getText().toString().trim());
                            }if (!binding.ageto.getText().toString().trim().equals(data.child("preferredAgeMax").getValue(String.class))){
                                map.put("preferredAgeMax", binding.ageto.getText().toString().trim());
                            }if (!binding.preferedgender.getText().toString().trim().equals(data.child("preferredGender").getValue(String.class))){
                                map.put("preferredGender",binding.preferedgender.getText().toString().trim());

                            }if (binding.preferedgender.getText().toString().trim().equals(getString(R.string.male))){
                                map.put("preGenderCode","6255533");
                            }if (binding.preferedgender.getText().toString().trim().equals(getString(R.string.female))) {
                                map.put("preGenderCode","333336255533");
                            }if(binding.preferedgender.getText().toString().equals(getString(R.string.Other))){
                                map.put("preGenderCode","66684433777");
                            }if (!binding.prefferedrace.getText().toString().trim().equals(data.child("preferredRace").getValue(String.class))){
                                map.put("preferredRace", binding.prefferedrace.getText().toString().trim());
                            }if (!Objects.requireNonNull(binding.prefferedreligion.getText()).toString().trim().equals(data.child("preferredReligion").getValue(String.class))){
                                map.put("preferredReligion", Objects.requireNonNull(binding.prefferedreligion.getText()).toString().trim());
                            }if (!binding.prelocate.getText().toString().trim().equals(data.child("preferToRelocate").getValue(String.class))){
                                map.put("preferToRelocate", binding.prelocate.getText().toString().trim());
                            }if (!binding.basics.getText().toString().trim().equals(data.child("basically").getValue(String.class))){
                                map.put("basically",binding.basics.getText().toString().trim());
                            }
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(Update_User_Public_Info.this, "An error has occurred", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                    Update_User_Public_Info.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!binding.names.getText().toString().equals(username) ||
                                    !binding.age.getText().toString().equals(data.child("age").getValue(String.class)) ||
                                    !binding.gender.getText().toString().equals(data.child("gender").getValue(String.class))
                                    || !binding.religion.getText().toString().equals(data.child("religion").getValue(String.class))
                                    || !binding.race.getText().toString().equals(data.child("race").getValue(String.class)) ||
                                    !binding.languages.getText().toString().equals(data.child("languages").getValue(String.class))
                                    || !binding.occupations.getText().toString().equals(data.child("occupation").getValue(String.class)) ||
                                    !binding.hobbies.getText().toString().equals(data.child("hobbies").getValue(String.class)) ||
                                    !binding.about.getText().toString().equals(data.child("about_user").getValue(String.class))
                                    || !binding.ifKidsYes.getText().toString().equals(data.child("kids").getValue(String.class))
                                    || !binding.country.getText().toString().equals(data.child("country").getValue(String.class)) ||
                                    !binding.state.getText().toString().equals(data.child("province").getValue(String.class)) ||
                                    !binding.city.getText().toString().equals(data.child("city").getValue(String.class))
                                    || !binding.town.getText().toString().trim().equals(data.child("town").getValue(String.class))
                                    || !binding.reLocate.getText().toString().equals(data.child("usertorelocate").getValue(String.class))
                                    || !binding.studentAt.getText().toString().equals(data.child("studentAt").getValue(String.class))
                                    || !binding.qualifications.getText().toString().equals(data.child("qualifications").getValue(String.class)) ||
                                    !binding.agefrom.getText().toString().equals(data.child("preferredAgeMin").getValue(String.class)) ||
                                    !binding.ageto.getText().toString().equals(data.child("preferredAgeMax").getValue(String.class))
                                    || !binding.preferedgender.getText().toString().equals(data.child("preferredGender").getValue(String.class))
                                    || !binding.prefferedrace.getText().toString().equals(data.child("preferredRace").getValue(String.class))
                                    || !binding.prelocate.getText().toString().equals(data.child("preferToRelocate").getValue(String.class))
                                    || !binding.basics.getText().toString().equals(data.child("basically").getValue(String.class))){

                                binding.savingLayout.setVisibility(View.VISIBLE);


                            }else{
                                binding.savingLayout.setVisibility(View.GONE);

                            }
                            new Handler().postDelayed(this,1000);
                        }
                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        OnClicks();
        getImage(binding.toolBar);


    }
    Uri profileImage;
    private void getImage(MaterialToolbar toolbar){
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.CustomProgressDialogStyle);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                o -> {
                    if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                        Intent data = o.getData();
                        profileImage = data.getData();
                        progressDialog.setTitle(getString(R.string.please_wait));
                        progressDialog.show();
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile");
                        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String currentProfile = snapshot.getValue(String.class);
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImageProfile").child(currentProfile);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        StorageReference reference = FirebaseStorage.getInstance().getReference("ImagesProfile").child(Objects.requireNonNull(imageUri.getLastPathSegment()));
                                        reference.putFile(profileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                                        Map<String,Object> map = new HashMap<>();
                                                        map.put("profile",uri.toString());
                                                        databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                progressDialog.dismiss();
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                                                                builder.setCancelable(true)
                                                                        .setMessage(getString(R.string.updatesucc))
                                                                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                builder.create().show();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                                                                builder.setCancelable(true)
                                                                        .setMessage(getString(R.string.something_went_wrong))
                                                                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                builder.create().show();

                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                                                        builder.setCancelable(true)
                                                                .setMessage(getString(R.string.something_went_wrong))
                                                                .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                        builder.create().show();

                                                    }
                                                });


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                                                builder.setCancelable(true)
                                                        .setMessage(getString(R.string.something_went_wrong))
                                                        .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builder.create().show();

                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                                        builder.setCancelable(true)
                                                .setMessage(getString(R.string.something_went_wrong))
                                                .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.create().show();


                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
        );
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    activityResultLauncher.launch(intent);
                } else {
                    startActivityForResult(intent, 1);
                }
                return true;
            }
        });

    }
    private void OnClicks(){



        binding.Updatenames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetLayout.ChoiceHint.setHint(getString(R.string.names));
                sheetLayout.specify.setText(getString(R.string.entername));
                sheetDialog.show();
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entername), Toast.LENGTH_SHORT).show();
                        }else{
                            binding.names.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }


                    }
                });
            }
        });
        binding.Updateage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetLayout.ChoiceHint.setHint(getString(R.string.age));
                sheetLayout.specify.setText(getString(R.string.enterage));
                sheetDialog.show();
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.enterage), Toast.LENGTH_SHORT).show();
                        }else{
                            binding.age.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }


                    }
                });
            }
        });
        binding.userGender.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.female){
                  binding.gender.setText(getString(R.string.female));
                } else if (item.getItemId() == R.id.male) {
                    binding.gender.setText(getString(R.string.male));
                    binding.gender.setText(getString(R.string.male));
                } else if (item.getItemId() == R.id.others) {
                    sheetLayout.choice.setText("");
                    sheetDialog.show();
                    sheetLayout.specify.setText(getString(R.string.entergender));
                    sheetLayout.ChoiceHint.setHint(getString(R.string.gender));
                    sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sheetLayout.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            }else {
                                binding.gender.setText(sheetLayout.choice.getText().toString());
                                sheetDialog.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });
        binding.Updatereligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetLayout.ChoiceHint.setHint(getString(R.string.religion));
                sheetLayout.specify.setText(getString(R.string.enterreligion));
                sheetDialog.show();
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.enterage), Toast.LENGTH_SHORT).show();
                        }else{
                            binding.religion.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }


                    }
                });

            }
        });
        binding.userRace.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.black){
                    binding.race.setText(getString(R.string.black));
                } else if (item.getItemId() == R.id.coloured) {
                    binding.race.setText(getString(R.string.coloured));

                }else if(item.getItemId() == R.id.indian){
                    binding.race.setText(getString(R.string.indian));

                } else if (item.getItemId() == R.id.white) {
                    binding.race.setText(getString(R.string.white));

                } else if (item.getItemId() == R.id.others) {
                    sheetLayout.choice.setText("");
                    sheetDialog.show();
                    sheetLayout.specify.setText(getString(R.string.enterrace));
                    sheetLayout.ChoiceHint.setHint(getString(R.string.race));
                    sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sheetLayout.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            }else {
                                binding.race.setText(sheetLayout.choice.getText().toString());
                                sheetDialog.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });
        binding.Updatelanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterlanguages));
                sheetLayout.ChoiceHint.setHint(getString(R.string.languages));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.language));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.languages.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });

            }
        });
        binding.Updateoccupations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterocupations));
                sheetLayout.ChoiceHint.setHint(getString(R.string.occupations));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.work));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.occupations.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });

            }
        });
        binding.Updatehobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterhobbies));
                sheetLayout.ChoiceHint.setHint(getString(R.string.hobbies));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.sports_handball));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.hobbies.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });

            }
        });
        binding.Updateabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterabout));
                sheetLayout.ChoiceHint.setHint(getString(R.string.about));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.info));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.about.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.UpdateifKidsYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.how_many));
                sheetLayout.ChoiceHint.setHint(getString(R.string.kids));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.ifKidsYes.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.Updatecountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.entercountry));
                sheetLayout.ChoiceHint.setHint(getString(R.string.country));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.my_location));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.country.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.Updatestate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterprovince));
                sheetLayout.ChoiceHint.setHint(getString(R.string.state));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.my_location));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.state.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.Updatecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.entercity));
                sheetLayout.ChoiceHint.setHint(getString(R.string.city));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.city));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.city.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.Updatetown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.entertown));
                sheetLayout.ChoiceHint.setHint(getString(R.string.town));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.my_location));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.town.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.UpdatereLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.willing_re_locate));
                sheetLayout.ChoiceHint.setHint(getString(R.string.about));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.reLocate.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.UpdatestudentAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.where_are_you_studet_at));
                sheetLayout.ChoiceHint.setHint(getString(R.string.studentAt));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.studentAt.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });

        binding.Updatequalifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enterqualifications));
                sheetLayout.ChoiceHint.setHint(getString(R.string.qualifications));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.qualifications.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.preffredage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enteragemin));
                sheetLayout.ChoiceHint.setHint(getString(R.string.age));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.round_elderly));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.agefrom.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.preffredageMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.enteragemax));
                sheetLayout.ChoiceHint.setHint(getString(R.string.age));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.round_elderly));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.ageto.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.preferedGend.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.female){
                    binding.preferedgender.setText(getString(R.string.female));
                } else if (item.getItemId() == R.id.male) {
                    binding.preferedgender.setText(getString(R.string.male));
                    binding.preferedgender.setText(getString(R.string.male));
                } else if (item.getItemId() == R.id.others) {
                    sheetLayout.choice.setText("");
                    sheetDialog.show();
                    sheetLayout.specify.setText(getString(R.string.entergender));
                    sheetLayout.ChoiceHint.setHint(getString(R.string.gender));
                    sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.gender_icon));
                    sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sheetLayout.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            }else {
                                binding.preferedgender.setText(sheetLayout.choice.getText().toString());
                                sheetDialog.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });
        binding.prefferedRa.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.black){
                    binding.prefferedrace.setText(getString(R.string.black));
                } else if (item.getItemId() == R.id.coloured) {
                    binding.prefferedrace.setText(getString(R.string.coloured));

                }else if(item.getItemId() == R.id.indian){
                    binding.prefferedrace.setText(getString(R.string.indian));

                } else if (item.getItemId() == R.id.white) {
                    binding.prefferedrace.setText(getString(R.string.white));

                } else if (item.getItemId() == R.id.others) {
                    sheetLayout.choice.setText("");
                    sheetDialog.show();
                    sheetLayout.specify.setText(getString(R.string.enterrace));
                    sheetLayout.ChoiceHint.setHint(getString(R.string.race));
                    sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sheetLayout.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                            }else {
                                binding.prefferedrace.setText(sheetLayout.choice.getText().toString());
                                sheetDialog.dismiss();
                            }

                        }
                    });

                }
                return true;
            }
        });
        binding.Updateprereligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetLayout.ChoiceHint.setHint(getString(R.string.religion));
                sheetLayout.specify.setText(getString(R.string.enterreligion));
                sheetDialog.show();
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.enterage), Toast.LENGTH_SHORT).show();
                        }else{
                            binding.prefferedreligion.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }


                    }
                });

            }
        });
        binding.patnereLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.wantpatnerrelocate));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.my_location));
                sheetLayout.ChoiceHint.setHint(getString(R.string.re_locate));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.prelocate.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });
        binding.basicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetLayout.choice.setText("");
                sheetDialog.show();
                sheetLayout.specify.setText(getString(R.string.bacic));
                sheetLayout.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.my_location));
                sheetLayout.ChoiceHint.setHint(getString(R.string.bacically_looking_for));
                sheetLayout.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sheetLayout.choice.getText().toString().isEmpty()){
                            Toast.makeText(Update_User_Public_Info.this, getString(R.string.entergender), Toast.LENGTH_SHORT).show();
                        }else {
                            binding.basics.setText(sheetLayout.choice.getText().toString());
                            sheetDialog.dismiss();
                        }

                    }
                });
            }
        });

        DATATA = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter = new OwnerImagesAdapter(DATATA,Username);
                DATATA.clear();
                binding.shimaImages.stopShimmer();
                binding.shimaImages.setVisibility(View.INVISIBLE);
                if (snapshot.exists()){
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        ImageModel user = userSnapshot.getValue(ImageModel.class);
                        DATATA.add(user);
                        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        binding.postRecycler.setLayoutManager(HorizontalLayout);
                        binding.postRecycler.setAdapter(adapter);

                    }
                    adapter.notifyDataSetChanged();
                }else {
                    binding.postRecycler.setVisibility(View.GONE);
                    binding.noPost.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new OwnerImagesAdapter(DATATA,Username);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    Uri imageUri;
    private void changeProfile(String link){
        binding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_profile){
                    if (!link.isEmpty()){
                        ProgressDialog progressDialog = new ProgressDialog(Update_User_Public_Info.this,R.style.CustomProgressDialogStyle);
                        progressDialog.setMessage(getString(R.string.please_wait));
                        progressDialog.show();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(link);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                                        new ActivityResultContracts.StartActivityForResult(),
                                        o -> {
                                            if (o.getResultCode() == RESULT_OK && o.getData() != null) {
                                                Intent data = o.getData();
                                                imageUri = data.getData();
                                                StorageReference storageRef = FirebaseStorage.getInstance().getReference("ImagesProfile").child(imageUri.getLastPathSegment());
                                                storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"profile");
                                                                reference.setValue(uri).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        progressDialog.dismiss();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Update_User_Public_Info.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                
                                            }
                                        }
                                );
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    activityResultLauncher.launch(intent);
                                } else {
                                    startActivityForResult(intent, 1);
                                }
                                
                                

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Update_User_Public_Info.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                            }
                        });
                        
                    }
                                        
                    
                }
                return true;
            }
        });
    }

}