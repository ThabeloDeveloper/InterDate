package com.mecaroid.interdate;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.Models.Shared.ShareUid;
import com.mecaroid.interdate.Public.AdsServices;
import com.mecaroid.interdate.databinding.ActivityUserDetailsBinding;
import com.mecaroid.interdate.databinding.MessagingBinding;
import com.mecaroid.interdate.databinding.PurchaseListBinding;
import com.mecaroid.interdate.databinding.SubscriptionListBinding;
import com.mecaroid.interdate.databinding.SubscriptionPurchaseBinding;
import com.mecaroid.interdate.databinding.WatchAdsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserDetails extends AppCompatActivity {
    ActivityUserDetailsBinding binding;
    List<MingleModel> data;

    MingleModel currentHolder;

    List<ImageModel> DATATA;
    PostsImagesAdapter adapter;


    ShareUid shareUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DATATA = new ArrayList<>();
        if (!DATATA.isEmpty()) {
            DATATA.clear();
        }
        binding.shimaImages.startShimmer();

        data = ((ArrayList<MingleModel>) getIntent().getSerializableExtra("LIST"));


        if (data !=null){
            checkFriend();
            checkBlock();
            setResourcesWithUser();
        }
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void showAds(Button button,DatabaseReference reference) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.RewardedAd),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        button.setEnabled(false);
                        ShowInter(reference,button);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        button.setEnabled(true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.show(UserDetails.this, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int userStatus = Integer.parseInt(snapshot.getValue(String.class));
                                                int add = userStatus +1;
                                                reference.setValue(String.valueOf(add));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Toast.makeText(UserDetails.this, rewardItem.getAmount() +" " +rewardItem.getType() +getString(R.string.earned) , Toast.LENGTH_SHORT).show();


                                    }
                                });
                            }
                        });

                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });


                    }
                });

    }
    private void ShowInter(DatabaseReference reference,Button button){
        RewardedInterstitialAd.load(UserDetails.this, "ca-app-pub-3940256099942544/5354046379",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        button.setEnabled(true);

                        ad.show(UserDetails.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int userStatus = Integer.parseInt(snapshot.getValue(String.class));
                                        int add = userStatus +1;
                                        reference.setValue(String.valueOf(add));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Toast.makeText(UserDetails.this, rewardItem.getAmount() +" " +rewardItem.getType() +getString(R.string.earned) , Toast.LENGTH_SHORT).show();


                            }
                        });

                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });

                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Toast.makeText(UserDetails.this, "Ad Load Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    ////////////////////////////////SET RESOURCES WITH PAGES/////////////////////

    private void setResourcesWithUser() {
        DATATA = new ArrayList<>();
        if (DATATA.size() >0){
            DATATA.clear();
        }

        currentHolder = data.get(MingleIndex.currentIndex);
        shareUid = new ViewModelProvider(this).get(ShareUid.class);
        Glide.with(this).load(currentHolder.getProfile()).into(binding.ImageProfile);
        binding.toolBar.setTitle(currentHolder.getUsername());
        binding.username.setText(currentHolder.getUsername());
        binding.race.setText(currentHolder.getRace());
        binding.religion.setText(currentHolder.getReligion());
        binding.preRace.setText(currentHolder.getPreferredRace());
        binding.preReligion.setText(currentHolder.getPreferredReligion());
        binding.usernamePost.setText(currentHolder.getUsername() + getString(R.string.s) + getString(R.string.space) + getString(R.string.posts));
        binding.age.setText(currentHolder.getAge());
        binding.gender.setText(currentHolder.getGender());
        binding.basicInfor.setText(currentHolder.getBasically());
        SendTokenToAdmin sendTokenToAdmin = new SendTokenToAdmin();
        sendTokenToAdmin.getUserToken(currentHolder.getUser_id(),"view");
        if(binding.topAbout !=null){
            binding.topAbout.setText(currentHolder.getAbout_user());
        }
        binding.basiclyyText.setText(getString(R.string.bacic) + " " + currentHolder.getUsername() +" "+getString(R.string.is)+" " + getString(R.string.lookingfor));
        binding.ImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ViewProfile.class);
                intent.putExtra("url",currentHolder.getProfile());
                intent.putExtra("username",currentHolder.getUsername());
                startActivity(intent);
            }
        });
        adapter = new PostsImagesAdapter(DATATA,currentHolder.getUsername());
        if (currentHolder.getRace() !=null){
            binding.race.setText(currentHolder.getRace());
        }
        shareUid.setText(currentHolder.getUser_id());
        binding.languages.setText(currentHolder.getLanguages());
        if (currentHolder.getReligion() !=null){
            binding.religion.setText(currentHolder.getReligion());
        }
        binding.occupation.setText(currentHolder.getOccupation());
        binding.hobbies.setText(currentHolder.getHobbies());
        binding.kids.setText(currentHolder.getKids());
        binding.about.setText(currentHolder.getAbout_user());

        ////////////////LOCATIONS//////////////
        binding.country.setText(currentHolder.getCountry());
        binding.province.setText(currentHolder.getProvince());
        binding.city.setText(currentHolder.getCity());
        binding.town.setText(currentHolder.getTown());
        binding.UserRelocation.setText(currentHolder.getUsertorelocate());
        binding.Qualifications.setText(currentHolder.getQualifications());
        binding.studentAt.setText(currentHolder.getStudentAt());
        //////////////////////Preference/////////////
        binding.preAgeFrom.setText(currentHolder.getPreferredAgeMin());
        binding.preAgeTo.setText(currentHolder.getPreferredAgeMax());
        binding.preGender.setText(currentHolder.getPreferredGender());
        binding.preRelocate.setText(currentHolder.getPreferToRelocate());
        if (currentHolder.getBasically() !=null){
            binding.basicInfor.setText(currentHolder.getBasically());

        }
        checkRequest(currentHolder.getUser_id());


        ////////////////////IMAGES//////////////////////
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(currentHolder.getUser_id());
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


   private void checkFriend(){
        DATATA = new ArrayList<>();
        currentHolder = data.get(MingleIndex.currentIndex);
        binding.addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog addChat = new ProgressDialog(v.getContext());
                addChat.setMessage(getString(R.string.please_wait));
                addChat.setCancelable(false);
                addChat.show();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Entries").child(
                        FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int userStatus = Integer.parseInt(snapshot.getValue(String.class));

                            if (userStatus >0){
                                int Minus = userStatus - 1;
                                String left = String.valueOf(Minus);
                                reference1.setValue(left);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends")
                                        .child(currentHolder.getUser_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Query queryFriends = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                queryFriends.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            addChat.dismiss();
                                            BottomSheetDialog message = new BottomSheetDialog(v.getContext());
                                            MessagingBinding messagingBinding = MessagingBinding.inflate(getLayoutInflater());
                                            message.setCancelable(true);
                                            message.setContentView(messagingBinding.getRoot());
                                            Glide.with(messagingBinding.profileImage).load(currentHolder.getProfile()).into(messagingBinding.profileImage);
                                            message.show();
                                            messagingBinding.send.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (messagingBinding.message.getText().toString().isEmpty()){
                                                        Toast.makeText(UserDetails.this, getString(R.string.type_message), Toast.LENGTH_SHORT).show();
                                                    }else {

                                                        ProgressDialog sendingMessage = new ProgressDialog(v.getContext());
                                                        sendingMessage.setCancelable(false);
                                                        sendingMessage.setMessage(getString(R.string.sendingmessage));
                                                        Calendar calendar = Calendar.getInstance();
                                                        String year = String.valueOf(calendar.get(Calendar.YEAR));
                                                        String month = String.valueOf(calendar.get(Calendar.MONTH));
                                                        String day = String.valueOf(calendar.get(Calendar.DATE));
                                                        String hour = String.valueOf(calendar.get(Calendar.HOUR));
                                                        String time = String.valueOf(calendar.get(Calendar.MINUTE));
                                                        String milis = String.valueOf(calendar.get(Calendar.MILLISECOND));
                                                        String path = year+month+day+hour+time+milis;
                                                        HashMap<Object,String> map = new HashMap<>();
                                                        map.put("message",messagingBinding.message.getText().toString());
                                                        map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        map.put("receiver",currentHolder.getUser_id());
                                                        map.put("status","sent");
                                                        DatabaseReference sendMessage = FirebaseDatabase.getInstance().getReference("Chats")
                                                                .child(currentHolder.getUser_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(path);
                                                        sendMessage.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                HashMap<Object,String> map = new HashMap<>();
                                                                map.put("message",messagingBinding.message.getText().toString());
                                                                map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                map.put("receiver",currentHolder.getUser_id());
                                                                map.put("status","sent");
                                                                DatabaseReference sendToMe = FirebaseDatabase.getInstance().getReference("Chats")
                                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentHolder.getUser_id()).child(path);
                                                                sendToMe.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        sendingMessage.dismiss();
                                                                        message.dismiss();
                                                                        SendTokenToAdmin sendTokenToAdmin = new SendTokenToAdmin();
                                                                        sendTokenToAdmin.getUserToken(currentHolder.getUser_id(), "message");
                                                                        Toast.makeText(UserDetails.this, getString(R.string.msgSent), Toast.LENGTH_SHORT).show();
                                                                        AdsServices services =  new AdsServices();
                                                                        services.showMyAd(UserDetails.this, null,null);

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        sendingMessage.dismiss();
                                                                        message.dismiss();
                                                                        Toast.makeText(UserDetails.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                sendingMessage.dismiss();
                                                                message.dismiss();
                                                                Toast.makeText(UserDetails.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }
                                                }
                                            });



                                        }else {
                                            Toast.makeText(UserDetails.this, getString(R.string.add_user_to_chat), Toast.LENGTH_SHORT).show();
                                            addChat.dismiss();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                WatchAdsBinding adsBinding = WatchAdsBinding.inflate(getLayoutInflater());
                                BottomSheetDialog sheetDialog = new BottomSheetDialog(UserDetails.this);
                                sheetDialog.setCancelable(true);
                                sheetDialog.setContentView(adsBinding.getRoot());
                                sheetDialog.show();
                                adsBinding.Purchase.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SubscriptionPurchaseBinding subscriptionBinding = SubscriptionPurchaseBinding.inflate(getLayoutInflater());
                                        sheetDialog.dismiss();
                                        sheetDialog.setContentView(subscriptionBinding.getRoot());
                                        sheetDialog.show();
                                        subscriptionBinding.subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                SubscriptionListBinding subscriptionListBinding = SubscriptionListBinding.inflate(getLayoutInflater());
                                                BottomSheetDialog sublitBot = new BottomSheetDialog(UserDetails.this);
                                                sublitBot.setCancelable(true);
                                                sublitBot.setContentView(subscriptionListBinding.getRoot());
                                                sublitBot.show();

                                            }
                                        });
                                        subscriptionBinding.Purchase.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sheetDialog.dismiss();
                                                PurchaseListBinding listBinding = PurchaseListBinding.inflate(getLayoutInflater());
                                                sheetDialog.setContentView(listBinding.getRoot());
                                                sheetDialog.show();


                                            }
                                        });

                                    }
                                });
                                showAds(adsBinding.yes,reference1);
                                Toast.makeText(UserDetails.this, getString(R.string.insufficient_inters), Toast.LENGTH_SHORT).show();
                                addChat.dismiss();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        binding.addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog addChat = new ProgressDialog(v.getContext());
                addChat.setMessage(getString(R.string.requesting));
                addChat.setCancelable(false);
                addChat.show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Entries")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().onDisconnect();
                        if (snapshot.exists()){
                            int userStatus = Integer.parseInt(snapshot.getValue(String.class));

                            if (userStatus >0){
                                int Minus = userStatus - 1;
                                String left = String.valueOf(Minus);
                                reference.setValue(left);
                                DatabaseReference referenceFriends = FirebaseDatabase.getInstance().getReference("Friends").child(currentHolder.getUser_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Query queryFriends = referenceFriends.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                queryFriends.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().onDisconnect();
                                        if (snapshot.exists()){
                                            addChat.dismiss();
                                            Toast.makeText(UserDetails.this, getString(R.string.already_friends), Toast.LENGTH_SHORT).show();
                                            binding.friendsIcon.setImageDrawable(getResources().getDrawable(R.drawable.done));

                                        }else {
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(currentHolder.getUser_id())
                                                    .child("Received").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            Query query = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    snapshot.getRef().onDisconnect();
                                                    if (snapshot.exists()){
                                                        addChat.dismiss();
                                                        Toast.makeText(UserDetails.this, getString(R.string.request_all_sent), Toast.LENGTH_SHORT).show();

                                                    }else {
                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Request")
                                                                .child(currentHolder.getUser_id()).child("Received").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        HashMap<Object,String> map = new HashMap<>();
                                                        map.put("user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        reference1.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                DatabaseReference referenceMe = FirebaseDatabase.getInstance().getReference("Request")
                                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sent").child(currentHolder.getUser_id());
                                                                HashMap<Object,String> map = new HashMap<>();
                                                                map.put("user_id",currentHolder.getUser_id());
                                                                referenceMe.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        SendTokenToAdmin sendTokenToAdmin = new SendTokenToAdmin();
                                                                        sendTokenToAdmin.getUserToken(currentHolder.getUser_id(),"request");

                                                                        addChat.dismiss();
                                                                        Toast.makeText(UserDetails.this, getString(R.string.Sent), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        addChat.dismiss();
                                                                        Toast.makeText(UserDetails.this, getString(R.string.requestFailed), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                });


                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                addChat.dismiss();
                                                                Toast.makeText(UserDetails.this, getString(R.string.requestFailed), Toast.LENGTH_SHORT).show();

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

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else{
                                WatchAdsBinding adsBinding = WatchAdsBinding.inflate(getLayoutInflater());
                                BottomSheetDialog sheetDialog = new BottomSheetDialog(UserDetails.this);
                                sheetDialog.setCancelable(true);
                                sheetDialog.setContentView(adsBinding.getRoot());
                                sheetDialog.show();
                                showAds(adsBinding.yes,reference);
                                Toast.makeText(UserDetails.this, getString(R.string.insufficient_inters), Toast.LENGTH_SHORT).show();
                                addChat.dismiss();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
    }
    private void checkRequest(String user_uid){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(uid).child("Sent");
        Query query = reference.orderByChild("user_id").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.addChat.setVisibility(View.GONE);
                }else{
                    binding.addChat.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkBlock(){
        DATATA = new ArrayList<>();
        currentHolder = data.get(MingleIndex.currentIndex);
        binding.addBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(v.getContext(),R.style.CustomProgressDialogStyle);
                deleteAlert.setTitle(R.string.blocking);
                deleteAlert.setMessage(getString(R.string.blocking_user));
                deleteAlert.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                deleteAlert.setNegativeButton(getString(R.string.continuee), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog deleteProgress = new ProgressDialog(v.getContext());
                        deleteProgress.setCancelable(false);
                        deleteProgress.setMessage(getString(R.string.please_wait));
                        deleteProgress.show();
                        dialog.dismiss();

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Entries").child(
                                FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    int userStatus = Integer.parseInt(snapshot.getValue(String.class));
                                    if (userStatus >0){
                                        int Minus = userStatus - 1;
                                        String left = String.valueOf(Minus);
                                        reference1.setValue(left);
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocklist")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentHolder.getUser_id());
                                        HashMap<Object,String> mapBlock = new HashMap<>();
                                        mapBlock.put("user_id",currentHolder.getUser_id());
                                        reference.setValue(mapBlock).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                deleteProgress.dismiss();
                                                Toast.makeText(v.getContext(), getString(R.string.added_to_blocklist), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                deleteProgress.dismiss();
                                                Toast.makeText(v.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }else{
                                        deleteProgress.dismiss();
                                        WatchAdsBinding adsBinding = WatchAdsBinding.inflate(getLayoutInflater());
                                        BottomSheetDialog sheetDialog = new BottomSheetDialog(UserDetails.this);
                                        sheetDialog.setCancelable(true);
                                        sheetDialog.setContentView(adsBinding.getRoot());
                                        sheetDialog.show();
                                        showAds(adsBinding.yes,reference1);
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
                deleteAlert.create();
                deleteAlert.show();
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocklist").child(currentHolder.getUser_id()).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query queryBlock = reference.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        queryBlock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.NoInteract.setVisibility(View.GONE);
                }else {
                    binding.NoInteract.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




}