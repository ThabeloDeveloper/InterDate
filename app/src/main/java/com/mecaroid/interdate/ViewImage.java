package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mecaroid.interdate.Adapters.Pagers.ImagesPagerAdapter;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.databinding.ActivityViewImageBinding;

import java.util.List;
import java.util.Objects;

public class ViewImage extends AppCompatActivity {


    ActivityViewImageBinding binding;
    ImageModel currentHolder;
    List<ImageModel> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        data = ((List<ImageModel>) getIntent().getSerializableExtra("LIST"));

        setResourcesWithUser();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        binding.username.setTitle(username);
        binding.username.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    ImagesPagerAdapter adapter;

    private void setResourcesWithUser() {
        adapter = new ImagesPagerAdapter(data);
        currentHolder = data.get(ImagesIndex.currentIndex);
        if (Objects.equals(currentHolder.getPrivacy(), "private")){

        }
        binding.imageMain.setAdapter(adapter);
        binding.imageMain.setCurrentItem(ImagesIndex.currentIndex);
        binding.imageMain.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }



}