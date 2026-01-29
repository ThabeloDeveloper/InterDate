package com.mecaroid.interdate.Authentication;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mecaroid.interdate.R;
import com.mecaroid.interdate.databinding.ActivityVerifyEmailBinding;

public class VerifyEmail extends AppCompatActivity {
    ActivityVerifyEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        binding.email.setText(intent.getStringExtra("email"));
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SignIn.class));
                finish();
            }
        });
    }
}