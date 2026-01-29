package com.mecaroid.interdate.Authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.GetInformation;
import com.mecaroid.interdate.MainActivity;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.Splash_Screen;
import com.mecaroid.interdate.databinding.SignInBinding;

import java.util.Objects;

public class SignIn extends AppCompatActivity {



    FirebaseAuth auth;
    SignInBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignInBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        Intent intent = getIntent();
        if (intent.getStringExtra("purpose") != null && intent.getStringExtra("purpose") == "SignOut"){
            ProgressDialog signingOut = new ProgressDialog(SignIn.this);
            signingOut.setMessage(getString(R.string.please_wait));
            signingOut.show();
            FirebaseAuth.getInstance().signOut();
            signingOut.dismiss();
            binding.email.requestFocus();
        }
        if (intent.getStringExtra("purpose") !=null && intent.getStringExtra("purpose") == "DeleteAcount"){
            ProgressDialog signingOut = new ProgressDialog(SignIn.this);
            signingOut.setMessage(getString(R.string.please_wait));
            signingOut.show();
            FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    signingOut.dismiss();
                    binding.email.requestFocus();
                }
            });
        }
        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Reset_Password.class));
            }
        });
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateAnAccount.class));
                finish();
            }
        });

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty()){
                    Toast.makeText(SignIn.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(binding.password.getText()).toString().isEmpty()){
                    Toast.makeText(SignIn.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                }else {
                    ProgressDialog progressDialog = new ProgressDialog(v.getContext(),R.style.CustomProgressDialogStyle);
                    progressDialog.setMessage(getString(R.string.please_wait));
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    auth.signInWithEmailAndPassword(binding.email.getText().toString().trim(),binding.password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                progressDialog.setMessage(getString(R.string.sendingveremail));
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(v.getContext(), VerifyEmail.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage(e.getLocalizedMessage());
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                        alertDialog.create();
                                        alertDialog.show();

                                    }
                                });
                            }else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                Query query = reference.orderByChild("user_id").equalTo(user.getUid());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot data : snapshot.getChildren()){
                                                String username = data.child("username").getValue(String.class);
                                                String profile = data.child("profile").getValue(String.class);
                                                String about = data.child("about_user").getValue(String.class);
                                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                                intent.putExtra("username",username);
                                                intent.putExtra("profile",profile);
                                                intent.putExtra("about",about);
                                                intent.putExtra("gender",data.child("preferredGender").getValue(String.class));
                                                intent.putExtra("ageMax",data.child("preferredAgeMax").getValue(String.class));
                                                intent.putExtra("country",data.child("country").getValue(String.class));
                                                intent.putExtra("province",data.child("province").getValue(String.class));
                                                intent.putExtra("city",data.child("city").getValue(String.class));
                                                intent.putExtra("town",data.child("town").getValue(String.class));
                                                startActivity(intent);
                                                finish();
                                            }
                                        }else {
                                            startActivity(new Intent(getApplicationContext(), GetInformation.class));
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(),R.style.CustomProgressDialogStyle);
                                        alertDialog.setCancelable(false);
                                        alertDialog.setMessage(error.getMessage());
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
                                });


                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage(e.getLocalizedMessage());
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onStart() {
        super.onStart();

    }
}