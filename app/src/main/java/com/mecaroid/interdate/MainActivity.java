package com.mecaroid.interdate;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Pagers.PagerAdapter;
import com.mecaroid.interdate.Adapters.Pagers.RequestPagerAdapter;
import com.mecaroid.interdate.Adapters.Recycler.FriendsCircledList;
import com.mecaroid.interdate.Adapters.Recycler.SearchFriendAdapter;
import com.mecaroid.interdate.Models.FriendsCircledModel;
import com.mecaroid.interdate.databinding.AppUpdateBinding;
import com.mecaroid.interdate.databinding.DonationMessageBinding;
import com.mecaroid.interdate.databinding.SubscriptionPurchaseBinding;
import com.mecaroid.interdate.databinding.WatchAdsBinding;
//import com.paypal.checkout.approve.Approval;
//import com.paypal.checkout.approve.OnApprove;
//import com.paypal.checkout.createorder.CreateOrder;
//import com.paypal.checkout.createorder.CreateOrderActions;
//import com.paypal.checkout.createorder.CurrencyCode;
//import com.paypal.checkout.createorder.OrderIntent;
//import com.paypal.checkout.createorder.UserAction;
//import com.paypal.checkout.order.Amount;
//import com.paypal.checkout.order.AppContext;
//import com.paypal.checkout.order.CaptureOrderResult;
//import com.paypal.checkout.order.OnCaptureComplete;
//import com.paypal.checkout.order.OrderRequest;
//import com.paypal.checkout.order.PurchaseUnit;
//import com.permissionx.guolindev.PermissionX;
//import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {
    PagerAdapter adapter;

    ViewPager pager;
    FloatingActionButton actionButton,actionFriends;
    Toolbar toolbar,camera;
    String oldCountry,oldProvince,oldCity,oldTown = null;
    DatabaseReference EntriesReference;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Presence");
    TextView interS,requestCount;
    ImageView coinImage;
    AdView adView;
    BottomNavigationView navigationView;
    MaterialCardView requestsCard,dismiss;
    ViewPager requestPager;
    TabLayout tabLayout;
    RelativeLayout mainLayout,subMain;
    LinearLayout translatedLayout;
    MaterialCardView searchButton,addButton,request_countCard;
    RecyclerView searchRecyclerView;
    EditText searchEditor;
    BadgeDrawable drawable;
    CardView addCoins;
    TextView textMsg;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this);
        textMsg = findViewById(R.id.textMsg);
        addCoins = findViewById(R.id.add_coins);
        requestCount = findViewById(R.id.requestCount);
        request_countCard = findViewById(R.id.request_countCard);
        dismiss = findViewById(R.id.dismiss);
        searchEditor = findViewById(R.id.searchEditor);
        searchRecyclerView = findViewById(R.id.searchRecycler);
        mainLayout = findViewById(R.id.mainLayout);
        subMain = findViewById(R.id.subMain);
        searchEditorCard = findViewById(R.id.searchEditorCard);
        searchButton = findViewById(R.id.switchSearch);
        searchImage = findViewById(R.id.searchImage);
        searchCancel = findViewById(R.id.searchCancel);
        addButton = findViewById(R.id.add_new);
        translatedLayout = findViewById(R.id.friends_search);
        requestsCard = findViewById(R.id.requestsCard);
        interS = findViewById(R.id.intersCount);
        coinImage = findViewById(R.id.imageCoins);
        coinImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        adView = findViewById(R.id.adView);
        navigationView = findViewById(R.id.nav);
        drawable = navigationView.getOrCreateBadge(R.id.chats);
        actionButton = findViewById(R.id.floatingHome);
        actionFriends = findViewById(R.id.FriendHome);
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        camera = findViewById(R.id.camera);
        toolbar = findViewById(R.id.toolBar);
        navigationView.setSelectedItemId(R.id.chats);
        pager.setTranslationY(searchEditorCard.getHeight() - searchEditorCard.getHeight());
        request_countCard.setVisibility(View.GONE);
        dismissCard(pager);
        dismissCard(subMain);
        dismissCard(navigationView);
        showAdBanner(adView);
        requestHandler();
        SentMessagesCounter();
        AppOpenAds();
        getFriendsList();
        TranslateFriendsLayout();
        dismiss.setOnClickListener(v -> {
            dismissRequestsCard();
        });
        getRequestsCount();
        showDonationMessage();


        EntriesReference = FirebaseDatabase.getInstance().getReference("Entries")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        coinImage.setClickable(true);
        coinImage.setOnClickListener(v -> {
            WatchAdsBinding adsBinding = WatchAdsBinding.inflate(getLayoutInflater());
            BottomSheetDialog sheetDialog = new BottomSheetDialog(MainActivity.this);
            sheetDialog.setCancelable(true);
            sheetDialog.setContentView(adsBinding.getRoot());
            sheetDialog.show();
            adsBinding.Purchase.setOnClickListener(v12 -> {
                SubscriptionPurchaseBinding subscriptionBinding = SubscriptionPurchaseBinding.inflate(getLayoutInflater());
                sheetDialog.dismiss();
                sheetDialog.setContentView(subscriptionBinding.getRoot());
                sheetDialog.show();
                subscriptionBinding.Purchase.setOnClickListener(view ->{
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.paypal.com/ncp/payment/NGP843J43DRBE"));
                    startActivity(intent);
                });

            });
            ShoAllAds(adsBinding.yes,EntriesReference);

        });

        interS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int counter = Integer.parseInt(s.toString());
                if (counter < 5){
                    addCoins.setVisibility(View.VISIBLE);
                }else{
                    addCoins.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addCoins.setOnClickListener(v->{
            chooseAd();
        });

        navigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.global){
                pager.setCurrentItem(0,true);
            }if (menuItem.getItemId() == R.id.chats){
                drawable.setBadgeTextColor(getColor(R.color.white));
                drawable.setBackgroundColor(getColor(R.color.LightGreen));
                pager.setCurrentItem(1,true);
            }else{
                drawable.setBadgeTextColor(getColor(R.color.white));
                drawable.setBackgroundColor(getColor(R.color.darkGreen));

            } if (menuItem.getItemId() == R.id.mingle){
                pager.setCurrentItem(2,true);
            }if (menuItem.getItemId() == R.id.calls){
                pager.setCurrentItem(3,true);
            }
            return true;
        });







        EntriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userStatus = snapshot.getValue(String.class);
                    interS.setText(userStatus);
                    assert userStatus != null;
                    if (Integer.parseInt(userStatus) < 10){
                        coinImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    }else{
                        coinImage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        startServices(intent.getStringExtra("username"));
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).onDisconnect().setValue("offline");

        actionFriends.setRippleColor(getResources().getColor(R.color.white));
        actionButton.setRippleColor(getResources().getColor(R.color.white));
        actionButton.setOnClickListener(v ->{
            //////////////////////////////////////////gfjjjjjjjjfvh///////////////
            animateRequests();


        });
        pager.setSaveEnabled(true);
        pager.setScrollbarFadingEnabled(true);
        pager.setScrollBarFadeDuration(200);
        pager.setScrollbarFadingEnabled(true);
        pager.setClipChildren(true);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    navigationView.setSelectedItemId(R.id.global);
                }if (position == 1){
                    navigationView.setSelectedItemId(R.id.chats);
                }if (position == 2){
                    navigationView.setSelectedItemId(R.id.mingle);
                }if (position == 3){
                    navigationView.setSelectedItemId(R.id.calls);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        camera.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.camera_icon){
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this,camera);
                    popupMenu.getMenuInflater().inflate(R.menu.camera_gallery_options,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.gallery){
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent,1);

                            }


                            return true;
                        }
                    });

                    popupMenu.show();

                }
                if (item.getItemId() == R.id.search_icon) {
                    Intent intentSearch = new Intent(MainActivity.this,Search_Country.class);
                    intentSearch.putExtra("gender",getIntent().getStringExtra("gender"));
                    intentSearch.putExtra("ageMax",getIntent().getStringExtra("ageMax"));
                    intentSearch.putExtra("town",getIntent().getStringExtra("town"));
                    intentSearch.putExtra("city",getIntent().getStringExtra("city"));
                    intentSearch.putExtra("province",getIntent().getStringExtra("province"));
                    intentSearch.putExtra("country",getIntent().getStringExtra("country"));
                    startActivity(intentSearch);
                }

                return true;
            }
        });


        actionButton.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        actionFriends.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        requestPermissions();



        Intent intent1 = new Intent(this,Account.class);
        ////////////////CHECKING DATA AND LOCATION//////////////
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settingsOption){
                    startActivity(intent1);
                }
                if(item.getItemId() == R.id.notifications){
                    Intent intent = new Intent(MainActivity.this,NotificationsActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });


    }

    private void TranslateFriendsLayout() {
       searchButton.setOnClickListener(v->{
           ClickAnimation(searchButton,null);

       });
       addButton.setOnClickListener(v ->{
           ClickAnimation(addButton,null);
       });

    }
    private void showDonationMessage(){
        DonationMessageBinding messageBinding = DonationMessageBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(messageBinding.getRoot());
        messageBinding.visitPayPal.setOnClickListener(view->{
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.paypal.com/ncp/payment/BSLH7A69QY8VQ"));
            startActivity(intent);
        });
        dialog.show();
    }
    private void BuyCoins(){

    }
    SearchFriendAdapter searchAdapter;

    ImageView searchImage,searchCancel;
    MaterialCardView searchEditorCard;
    RecyclerView searchRecycler;
    private void ClickAnimation(View view, @Nullable Void v){
        searchRecycler = findViewById(R.id.searchRecycler);
        if (view.getScaleX() == 1.0f && view.getScaleY() == 1.0f){
            view.animate().scaleY(0.5f).scaleX(0.5f).setDuration(100).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {

                }
                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    if(view == searchButton){
                        animateLayout();
                        if (searchRecycler.getVisibility() == View.VISIBLE){
                            searchRecycler.setVisibility(View.GONE);
                        }else{
                            searchRecycler.setVisibility(View.VISIBLE);
                        }

                    }
                    if (view == addButton){
                        animateRequests();
                    }
                    view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {


                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    }).start();

                }

                @Override
                public void onAnimationCancel(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animator) {

                }
            });
        }

    }
    private void dismissCard(View view){
        view.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                if (requestsCard.getVisibility() == View.VISIBLE){
                    removeCard();
                }
                return true;
            }
        });
        view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (requestsCard.getVisibility() == View.VISIBLE){
                    removeCard();
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestsCard.getVisibility() == View.VISIBLE){
                    removeCard();
                }
            }
        });
    }
    private void removeCard(){
        requestsCard.animate().scaleX(0.0f).scaleY(0.0f).setDuration(200).setUpdateListener(valueAnimator -> {
            if (valueAnimator.getCurrentPlayTime() >= valueAnimator.getTotalDuration()/2){
                requestsCard.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        requestsCard.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                }).start();
            }

        }).start();

    }
    private void animateLayout(){
        searchCancel.setRotation(45);
        if (searchImage.getAlpha() == 1.0f){
            searchImage.setAlpha(0.0f);
            searchCancel.setAlpha(1.0f);
            pager.animate().translationY(searchEditorCard.getHeight() - searchEditorCard.getHeight()).setDuration(200).start();
            searchEditorCard.setVisibility(View.VISIBLE);
            searchEditorCard.animate().scaleY(1.0f).setDuration(200).start();
        }else {
            searchImage.setAlpha(1.0f);
            searchCancel.setAlpha(0.0f);

            searchEditorCard.animate().scaleY(0.0f).setDuration(200).start();
            pager.setTranslationY(0);
            searchEditorCard.setVisibility(View.GONE);



        }


    }



    FriendsCircledModel chats;
    RecyclerView friendRecycler;
    FriendsCircledList Friendsadapter;
    ArrayList<FriendsCircledModel> dataa;
    private void getFriendsList(){
        friendRecycler = findViewById(R.id.friendsRecycler);
        dataa = new ArrayList<>();
        Friendsadapter = new FriendsCircledList(dataa);
        friendRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        friendRecycler.setAdapter(Friendsadapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataa.clear();
                if (snapshot.exists()){
                    for (DataSnapshot LastSent : snapshot.getChildren()){
                        if (!Objects.requireNonNull(LastSent.child("user_id").getValue(String.class)).isEmpty()){
                            chats = LastSent.getValue(FriendsCircledModel.class);
                            dataa.add(chats);
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            drawable.setNumber(0);
                            dataa.forEach(friendsCircledModel -> {
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats").child(uid).child(friendsCircledModel.getUser_id());
                                        Query query = reference1.limitToFirst(1);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                if (snapshot1.exists()){
                                                    if (Objects.equals(snapshot1.child("status").getValue(String.class) , "delivered") ||
                                                        Objects.equals(snapshot1.child("status").getValue(String.class), "sent")) {
                                                        drawable.setMaxCharacterCount(2);
                                                        drawable.setNumber(Integer.parseInt(drawable.getText()) + 1);
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }

                            );
                        }




                    }
                }



                Friendsadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void requestHandler(){
        RequestPagerAdapter adapter;
        requestPager = findViewById(R.id.requestPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new RequestPagerAdapter(getSupportFragmentManager());
        requestPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(requestPager,true);
        mainLayout = findViewById(R.id.mainLayout);




    }

    private void animateRequests(){
        if (requestsCard.getVisibility() == View.VISIBLE){
            requestsCard.animate().scaleX(0.0f).scaleY(0.0f).setDuration(200).setUpdateListener(valueAnimator -> {
                if (valueAnimator.getCurrentPlayTime() >= valueAnimator.getTotalDuration()/2){
                    requestsCard.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            requestsCard.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    }).start();
                }

            }).start();
        }if (requestsCard.getVisibility() == View.GONE){
            requestsCard.animate().alpha(1.0f).setDuration(200).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    requestsCard.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(@NonNull Animator animator) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animator) {

                }
            }).setUpdateListener(valueAnimator -> {
                if (valueAnimator.getCurrentPlayTime() >= valueAnimator.getTotalDuration()/2){
                    requestsCard.animate().scaleX(1.0f).scaleY(1.0f).setDuration(250).start();
                }

            }).start();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
            case 2:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    if (data.getData()!=null) {
                        Uri selectedImage = data.getData();
                        assert selectedImage != null;
                        String uriString = selectedImage.toString();
                        Intent intent = new Intent(MainActivity.this, PostImage.class);
                        intent.putExtra("Uri", uriString);
                        startActivity(intent);
                    }
                }
                break;

        }


    }
    protected void showAdBanner(AdView adView){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private static final int PERMISSION_REQUEST_CODE =1;
    private final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.POST_NOTIFICATIONS
    };
    private boolean arePermissionGranted(){
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }

        }
        return true;
    }
    private void requestPermissions(){
        if (!arePermissionGranted()){
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_CODE);
        }else{
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    assert location != null;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                        if (addresses.size() > 0){
                            Address address = addresses.get(0);
                            String newCountry = address.getCountryName();
                            String newProvince = address.getAdminArea();
                            String newCity = address.getSubAdminArea();
                            String newtown = address.getLocality();
                            if (newCountry!=null && newProvince!=null && newCity !=null && newtown !=null){
                                if (oldCountry!=null && oldCity!=null && oldProvince !=null && oldTown !=null){
                                    if (!newCountry.equals(oldCountry) || !newProvince.equals(oldProvince) || !newCity.equals(oldCity) || !newtown.equals(oldTown)){

                                    }
                                }

                            }
                        }


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE){
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    assert location != null;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                        if (addresses.size() > 0){
                            Address address = addresses.get(0);


                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
    private void dismissRequestsCard(){
        requestsCard.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500).setUpdateListener(valueAnimator -> {
            if (valueAnimator.getCurrentPlayTime() >= valueAnimator.getTotalDuration()/2){
                requestsCard.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        requestsCard.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                }).start();
            }

        }).start();

    }


    @Override
    public void onBackPressed() {
        if (requestsCard.getVisibility() == View.VISIBLE){
            requestsCard.animate().scaleX(0.0f).scaleY(0.0f).setDuration(500).setUpdateListener(valueAnimator -> {
                if (valueAnimator.getCurrentPlayTime() >= valueAnimator.getTotalDuration()/2){
                    requestsCard.animate().alpha(0.0f).setDuration(250).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            requestsCard.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    }).start();
                }

            }).start();
        }else if (pager.getCurrentItem() != 1){
            pager.setCurrentItem(1,true);
            navigationView.setSelectedItemId(R.id.chats);
        }else {
            super.onBackPressed();
        }

    }


    private void ShoAllAds(Button button,DatabaseReference reference){
        Random randomAds = new Random();
        int AdChoice = randomAds.nextInt(2);
        if (AdChoice == 0){
            showAds(button,reference);
        }
        if (AdChoice == 1){
            ShowInter(reference,button);
        }
    }
    LinearLayout layout;
    private void chooseAd(){
        textMsg.setVisibility(View.GONE);
        layout = findViewById(R.id.layout_ads_progress);
        layout.setVisibility(View.VISIBLE);
        Random random = new Random();
        int randomNumber = random.nextInt(2);
        if (randomNumber == 0){
            showCardAdd();
        }else{
            showCardInter();
        }
    }

    private void showCardAdd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.RewardedAd),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        layout.setVisibility(View.GONE);
                        textMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, getString(R.string.ad_load_failed), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        layout.setVisibility(View.VISIBLE);
                        textMsg.setVisibility(View.GONE);
                        ad.setImmersiveMode(true);
                        ad.show(MainActivity.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

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
                                layout.setVisibility(View.VISIBLE);
                                textMsg.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                EntriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int userStatus = Integer.parseInt(snapshot.getValue(String.class));
                                        int add = userStatus +1;
                                        EntriesReference.setValue(String.valueOf(add));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });


                    }
                });

    }
    private void showCardInter(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(this, getString(R.string.RewardedAd),
                adRequest, new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        layout.setVisibility(View.GONE);
                        textMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, getString(R.string.ad_load_failed), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        layout.setVisibility(View.VISIBLE);
                        textMsg.setVisibility(View.GONE);
                        ad.setImmersiveMode(true);
                        ad.show(MainActivity.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

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
                                layout.setVisibility(View.VISIBLE);
                                textMsg.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                EntriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int userStatus = Integer.parseInt(snapshot.getValue(String.class));
                                        int add = userStatus +1;
                                        EntriesReference.setValue(String.valueOf(add));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });


                    }
                });

    }

    private void showAds(Button button, DatabaseReference reference) {
        button.setEnabled(false);
        button.setText(getString(R.string.loading)+ " Ad...");
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.RewardedAd),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        button.setEnabled(false);
                        Toast.makeText(MainActivity.this, getString(R.string.ad_load_failed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        button.setText(getString(R.string.watch_ads));
                        button.setEnabled(true);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.show(MainActivity.this, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int userStatus = Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class)));
                                                int add = userStatus +1;
                                                reference.setValue(String.valueOf(add));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Toast.makeText(MainActivity.this, rewardItem.getAmount() +" " +rewardItem.getType() +getString(R.string.earned) , Toast.LENGTH_SHORT).show();


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
        button.setEnabled(false);
        button.setText(getString(R.string.loading)+ " Ad...");

        RewardedInterstitialAd.load(MainActivity.this, getString(R.string.RewardedInterstitialAd_UnitId),
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        button.setText(getString(R.string.watch_ads));
                        button.setEnabled(true);
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
                                Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad.show(MainActivity.this, new OnUserEarnedRewardListener() {
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
                                        Toast.makeText(MainActivity.this, rewardItem.getAmount() +" " +rewardItem.getType() +getString(R.string.earned) , Toast.LENGTH_SHORT).show();


                                    }
                                });
                            }
                        });





                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Toast.makeText(MainActivity.this, "Ad Load Failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void SentMessagesCounter(){
        SharedPreferences checking = getSharedPreferences("MessageSentCounter",MODE_PRIVATE);
        int count = checking.getInt("MessagesSentCounter",0);

        if (count == 5){
            SharedPreferences sharedPreferences = getSharedPreferences("MessageSentCounter",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("MessagesSentCounter",0);
            editor.apply();
        }
    }
    private void getRequestsCount(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Long count = snapshot.getChildrenCount();

                    request_countCard.setVisibility(View.VISIBLE);
                    requestCount.setText(countToString(count));

                }else{
                    request_countCard.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.onDisconnect();
    }
    private  String countToString(Long count){
        String countString = "";
        if (count > 99){
            countString = String.valueOf(count) + "+";
        }
        return countString;
    }



    private void AppOpenAds(){
        AppOpenAd.load(getApplicationContext(), getString(R.string.AdAppOpen_UnitId), new AdRequest.Builder().build(), new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
                appOpenAd.show(MainActivity.this);
            }
        });
    }





}