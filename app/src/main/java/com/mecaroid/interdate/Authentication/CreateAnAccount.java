package com.mecaroid.interdate.Authentication;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.ActivityCreateAnAccountBinding;

import java.net.URI;
import java.util.Objects;

public class CreateAnAccount extends AppCompatActivity {
    ActivityCreateAnAccountBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAnAccountBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityURL();

            }
        });
        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityURL();
            }
        });

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(binding.email.getText()).toString().isEmpty()){
                    binding.email.requestFocus();
                    Toast.makeText(CreateAnAccount.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                } else if (binding.password.getText().toString().isEmpty()) {
                    binding.password.requestFocus();
                    Toast.makeText(CreateAnAccount.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                } else if (binding.confirmPassword.getText().toString().isEmpty()) {
                    binding.confirmPassword.requestFocus();
                    Toast.makeText(CreateAnAccount.this, getString(R.string.confirmPassword) +"!", Toast.LENGTH_SHORT).show();
                } else if (!binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())) {
                    binding.confirmPassword.requestFocus();
                    Toast.makeText(CreateAnAccount.this, getString(R.string.passmissmatch), Toast.LENGTH_SHORT).show();
                } else if (binding.password.getText().length() <8) {
                    binding.password.requestFocus();
                    Toast.makeText(CreateAnAccount.this, getString(R.string.minpass), Toast.LENGTH_SHORT).show();

                }else {
                    binding.signUp.setEnabled(false);
                    ProgressDialog progressDialog = new ProgressDialog(v.getContext(),R.style.CustomProgressDialogStyle);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(getString(R.string.please_wait));
                    progressDialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.email.getText().toString().trim(),binding.password.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.setMessage(getString(R.string.sendingveremail));
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.signUp.setEnabled(true);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(v.getContext(),VerifyEmail.class);
                                    FirebaseAuth.getInstance().signOut();
                                    intent.putExtra("email",binding.email.getText().toString());
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    binding.signUp.setEnabled(true);
                                    progressDialog.dismiss();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
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
                                    binding.signUp.setEnabled(true);
                                    progressDialog.dismiss();
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
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
                                    binding.signUp.setEnabled(true);
                                    progressDialog.dismiss();

                                }
                            });
                }
            }
        });
    }
    private void startActivityURL(){
        Uri uri = Uri.parse(getString(R.string.privacy_policy_link));
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}