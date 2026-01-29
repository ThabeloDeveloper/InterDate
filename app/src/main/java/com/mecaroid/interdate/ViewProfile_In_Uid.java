package com.mecaroid.interdate;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.PostsImagesAdapter;
import com.mecaroid.interdate.Models.ImageModel;
import com.mecaroid.interdate.Models.Shared.ShareUid;
import com.mecaroid.interdate.databinding.ActivityViewProfileInUidBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewProfile_In_Uid extends AppCompatActivity {


    ActivityViewProfileInUidBinding binding;
    PostsImagesAdapter adapter;
    List<ImageModel> DATATA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileInUidBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        DATATA = new ArrayList<>();
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.shimaImages.startShimmer();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("user_id").equalTo(getIntent().getStringExtra("uid")).limitToFirst(1);
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
                    binding.username.setText(username);
                    binding.age.setText(data.child("age").getValue(String.class));
                    binding.gender.setText(data.child("gender").getValue(String.class));
                    binding.race.setText(data.child("race").getValue(String.class));
                    binding.religion.setText(data.child("religion").getValue(String.class));
                    binding.languages.setText(data.child("languages").getValue(String.class));
                    binding.occupation.setText(data.child("occupation").getValue(String.class));
                    binding.hobbies.setText(data.child("hobbies").getValue(String.class));
                    binding.about.setText(data.child("about_user").getValue(String.class));
                    binding.kids.setText(data.child("kids").getValue(String.class));
                    binding.country.setText(data.child("country").getValue(String.class));
                    binding.province.setText(data.child("province").getValue(String.class));
                    binding.city.setText(data.child("city").getValue(String.class));
                    binding.town.setText(data.child("town").getValue(String.class));
                    binding.UserRelocation.setText(data.child("usertorelocate").getValue(String.class));
                    binding.Qualifications.setText(data.child("qualifications").getValue(String.class));
                    binding.toolBar.setTitle(username);

                    binding.studentAt.setText(data.child("studentAt").getValue(String.class));
                    binding.preAgeFrom.setText(data.child("preferredAgeMin").getValue(String.class));
                    binding.preAgeTo.setText(data.child("preferredAgeMax").getValue(String.class));
                    binding.preGender.setText(data.child("preferredGender").getValue(String.class));
                    binding.preRace.setText(data.child("preferredRace").getValue(String.class));
                    binding.preRelocate.setText(data.child("preferToRelocate").getValue(String.class));
                    binding.preReligion.setText(data.child("preferredReligion").getValue(String.class));
                    binding.basicInfor.setText(data.child("basiclly").getValue(String.class));
                    Glide.with(ViewProfile_In_Uid.this).load(data.child("profile").getValue(String.class)).into(binding.ImageProfile);
                    binding.toolBar.setTitle(username);
                    binding.usernamePost.setText(username + getString(R.string.s) + getString(R.string.space) + getString(R.string.posts));
                    adapter = new PostsImagesAdapter(DATATA,username);
                    binding.ImageProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(),ViewProfile.class);
                            intent.putExtra("url",data.child("profile").getValue(String.class));
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }
                    });
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(getIntent().getStringExtra("uid"));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DATATA.clear();
                            binding.shimaImages.stopShimmer();
                            binding.shimaImages.setVisibility(View.INVISIBLE);
                            if (snapshot.exists()){
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    ImageModel user = userSnapshot.getValue(ImageModel.class);
                                    DATATA.add(user);
                                    LinearLayoutManager HorizontalLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                                    binding.postRecycler.setLayoutManager(HorizontalLayout);
                                    binding.postRecycler.setItemAnimator(new DefaultItemAnimator());
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

}