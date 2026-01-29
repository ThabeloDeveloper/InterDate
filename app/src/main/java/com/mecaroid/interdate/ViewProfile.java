package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mecaroid.interdate.databinding.ViewProfileBinding;

public class ViewProfile extends AppCompatActivity {
    ViewProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("url")).into(binding.imageMain);
        binding.username.setTitle(intent.getStringExtra("username"));
        binding.username.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}