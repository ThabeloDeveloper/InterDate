package com.mecaroid.interdate.Authentication;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.ActivityResetPasswordBinding;

public class Reset_Password extends AppCompatActivity {

    ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.email.getText().toString().isEmpty()){
                    Toast.makeText(Reset_Password.this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                }else {
                    ProgressDialog progressDialog = new ProgressDialog(v.getContext(),R.style.CustomProgressDialogStyle);
                    progressDialog.setMessage(getString(R.string.please_wait));
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(binding.email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage(getString(R.string.reset_email_link_sent) + binding.email.getText().toString() + " Please follow instructions to proceed with password reset. Link will expire if not accessed within a limited time.");
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();

                                }
                            });
                            alertDialog.create();
                            alertDialog.show();

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
}