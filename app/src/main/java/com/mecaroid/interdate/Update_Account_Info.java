package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Authentication.SignIn;
import com.mecaroid.interdate.databinding.ActivityUpdateAccountInfoBinding;
import com.mecaroid.interdate.databinding.EditchoicesBinding;

import java.util.Objects;

public class Update_Account_Info extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton actionButton;
    ActivityUpdateAccountInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateAccountInfoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("profile");
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            referenceProfile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(binding.ImageProfile).load(snapshot.getValue(String.class)).into(binding.ImageProfile);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.toolBar.setTitle(getIntent().getStringExtra("username"));
            binding.deleteAccount.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            binding.signOut.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            binding.signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                    alert.setTitle(getString(R.string.signing_out));
                    alert.setMessage(R.string.about_to_signout);
                    alert.setCancelable(true);
                    alert.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setMessage(getString(R.string.please_wait));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            dialog.dismiss();
                            Intent intent = new Intent(v.getContext(),SignIn.class);
                            intent.putExtra("purpose","SignOut");
                            startActivity(new Intent(v.getContext(), SignIn.class));

                        }
                    });
                    alert.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.create();
                    alert.show();


                }
            });
            binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                    alert.setMessage(R.string.suretodelete);
                    alert.setTitle(getString(R.string.delete_account));
                    alert.setCancelable(true);

                    alert.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditchoicesBinding userchoicesBinding = EditchoicesBinding.inflate(getLayoutInflater());
                            BottomSheetDialog specieficRace = new BottomSheetDialog(Update_Account_Info.this);
                            specieficRace.setCancelable(true);
                            specieficRace.setContentView(userchoicesBinding.getRoot());
                            specieficRace.show();
                            userchoicesBinding.ChoiceHint.setHint(getString(R.string.prompt_password));
                            userchoicesBinding.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.lock));
                            userchoicesBinding.specify.setText(getString(R.string.enter_password));
                            userchoicesBinding.submit.setTextColor(getColor(R.color.white));
                            userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (userchoicesBinding.choice.getText().toString().isEmpty()){
                                        Toast.makeText(Update_Account_Info.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                                    }else {
                                        ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                                        progressDialog.setMessage(getString(R.string.please_wait));
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(FirebaseAuth.getInstance().getCurrentUser().getEmail(),userchoicesBinding.choice.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        DatabaseReference delete = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        delete.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Intent intent = new Intent(v.getContext(), SignIn.class);
                                                                        intent.putExtra("purpose","DeleteAccount");
                                                                        startActivity(intent);
                                                                        finishAffinity();

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(Update_Account_Info.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

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
                                                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.setMessage(e.getLocalizedMessage());
                                                        alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();

                                                            }
                                                        });
                                                        alertDialog.create();
                                                        alertDialog.show();

                                                    }
                                                });

                                    }

                                }
                            });


                        }
                    });
                    alert.create();
                    alert.show();

                }
            });
            binding.emailHolder.setHint(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            binding.Updateemailar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditchoicesBinding userchoicesBinding = EditchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficRace = new BottomSheetDialog(Update_Account_Info.this);
                    specieficRace.setCancelable(true);
                    specieficRace.setContentView(userchoicesBinding.getRoot());
                    specieficRace.show();
                    userchoicesBinding.ChoiceHint.setHint(getString(R.string.prompt_password));
                    userchoicesBinding.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.lock));
                    userchoicesBinding.specify.setText(getString(R.string.enter_password));
                    userchoicesBinding.submit.setTextColor(getColor(R.color.white));
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (userchoicesBinding.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_Account_Info.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                            }else {
                                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                                progressDialog.setMessage(getString(R.string.please_wait));
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()),userchoicesBinding.choice.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                progressDialog.dismiss();
                                                specieficRace.dismiss();
                                                EditchoicesBinding userchoicesBinding = EditchoicesBinding.inflate(getLayoutInflater());
                                                BottomSheetDialog updateEmail = new BottomSheetDialog(Update_Account_Info.this);
                                                updateEmail.setCancelable(true);
                                                userchoicesBinding.submit.setTextColor(getColor(R.color.white));
                                                updateEmail.setContentView(userchoicesBinding.getRoot());
                                                updateEmail.show();
                                                userchoicesBinding.ChoiceHint.setHint(getString(R.string.email));
                                                userchoicesBinding.specify.setText(getString(R.string.enter_email));
                                                userchoicesBinding.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.email));
                                                userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Objects.requireNonNull(userchoicesBinding.choice.getText()).toString().isEmpty()){
                                                            Toast.makeText(Update_Account_Info.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            progressDialog.show();
                                                            FirebaseAuth.getInstance().getCurrentUser().verifyBeforeUpdateEmail(userchoicesBinding.choice.getText().toString())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            progressDialog.dismiss();

                                                                            Toast.makeText(Update_Account_Info.this, getString(R.string.updatesucc), Toast.LENGTH_SHORT).show();
                                                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                                                            alertDialog.setMessage(R.string.reset_email_link_sent + userchoicesBinding.choice.getText().toString());
                                                                            alertDialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();

                                                                                }
                                                                            });
                                                                            alertDialog.create().show();
                                                                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                                @Override
                                                                                public void onDismiss(DialogInterface dialog) {
                                                                                    updateEmail.dismiss();
                                                                                }
                                                                            });


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(v.getContext());
                                                                            alertDialog.setCancelable(false);
                                                                            alertDialog.setMessage(e.getLocalizedMessage());
                                                                            alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();

                                                                                }
                                                                            });
                                                                            alertDialog.create();
                                                                            alertDialog.show();
                                                                        }
                                                                    }).addOnCanceledListener(new OnCanceledListener() {
                                                                        @Override
                                                                        public void onCanceled() {
                                                                            progressDialog.dismiss();

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                                alertDialog.setCancelable(false);
                                                alertDialog.setMessage(e.getLocalizedMessage());
                                                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                });
                                                alertDialog.create();
                                                alertDialog.show();
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                progressDialog.dismiss();

                                            }
                                        });
                            }

                        }
                    });
                }
            });
            binding.updatePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditchoicesBinding userchoicesBinding = EditchoicesBinding.inflate(getLayoutInflater());
                    BottomSheetDialog specieficRace = new BottomSheetDialog(Update_Account_Info.this);
                    specieficRace.setCancelable(true);
                    specieficRace.setContentView(userchoicesBinding.getRoot());
                    specieficRace.show();
                    userchoicesBinding.ChoiceHint.setHint(getString(R.string.prompt_password));
                    userchoicesBinding.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.lock));
                    userchoicesBinding.specify.setText(getString(R.string.enter_current_password));
                    userchoicesBinding.submit.setTextColor(getColor(R.color.white));
                    userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (userchoicesBinding.choice.getText().toString().isEmpty()){
                                Toast.makeText(Update_Account_Info.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                            }else {
                                ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                                progressDialog.setMessage(getString(R.string.please_wait));
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()),userchoicesBinding.choice.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                progressDialog.dismiss();
                                                specieficRace.dismiss();
                                                EditchoicesBinding userchoicesBinding = EditchoicesBinding.inflate(getLayoutInflater());
                                                BottomSheetDialog updateEmail = new BottomSheetDialog(Update_Account_Info.this);
                                                updateEmail.setCancelable(true);
                                                userchoicesBinding.submit.setTextColor(getColor(R.color.white));
                                                updateEmail.setContentView(userchoicesBinding.getRoot());
                                                updateEmail.show();
                                                userchoicesBinding.ChoiceHint.setHint(getString(R.string.prompt_password));
                                                userchoicesBinding.specify.setText(getString(R.string.enter_new_password));
                                                userchoicesBinding.ChoiceHint.setStartIconDrawable(getDrawable(R.drawable.lock));
                                                userchoicesBinding.submit.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (Objects.requireNonNull(userchoicesBinding.choice.getText()).toString().isEmpty()){
                                                            Toast.makeText(Update_Account_Info.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            progressDialog.show();
                                                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(userchoicesBinding.choice.getText().toString())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            progressDialog.dismiss();
                                                                            updateEmail.dismiss();
                                                                            Toast.makeText(Update_Account_Info.this, getString(R.string.updatesucc), Toast.LENGTH_SHORT).show();


                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                                                            alertDialog.setCancelable(false);
                                                                            alertDialog.setMessage(e.getLocalizedMessage());
                                                                            alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();

                                                                                }
                                                                            });
                                                                            alertDialog.create();
                                                                            alertDialog.show();
                                                                        }
                                                                    }).addOnCanceledListener(new OnCanceledListener() {
                                                                        @Override
                                                                        public void onCanceled() {
                                                                            progressDialog.dismiss();

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                                alertDialog.setCancelable(false);
                                                alertDialog.setMessage(e.getLocalizedMessage());
                                                alertDialog.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                });
                                                alertDialog.create();
                                                alertDialog.show();
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                progressDialog.dismiss();

                                            }
                                        });
                            }

                        }
                    });
                }
            });
            binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
}