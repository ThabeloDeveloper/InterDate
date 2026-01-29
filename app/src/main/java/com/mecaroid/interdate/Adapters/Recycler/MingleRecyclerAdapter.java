package com.mecaroid.interdate.Adapters.Recycler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Calulators.DistanceCalculator;
import com.mecaroid.interdate.MingleIndex;
import com.mecaroid.interdate.Models.MingleModel;
import com.mecaroid.interdate.R;
import com.mecaroid.interdate.UserDetails;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MingleRecyclerAdapter  extends RecyclerView.Adapter<MingleRecyclerAdapter.myviewholder> {
    private final ArrayList<MingleModel> data;
     Context context;

    public MingleRecyclerAdapter(ArrayList<MingleModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mingle,parent,false);


        return new myviewholder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        MingleModel user = data.get(position);
        CardView withCustomize = holder.itemView.findViewById(R.id.cardWith);
        ImageView verified = holder.itemView.findViewById(R.id.verify);
        ImageView profile = holder.itemView.findViewById(R.id.profileImage);
        TextView username = holder.itemView.findViewById(R.id.username);
        TextView country = holder.itemView.findViewById(R.id.country);
        TextView cityTown = holder.itemView.findViewById(R.id.cityandtown);
        TextView age = holder.itemView.findViewById(R.id.age);
        TextView distanceText = holder.itemView.findViewById(R.id.distanceText);
        TextView fromTo = holder.itemView.findViewById(R.id.age_from_to);
        ProgressBar progressBar = holder.itemView.findViewById(R.id.progress_bar);
        Glide.with(context).load(user.getProfile()).into(profile);
        username.setText(user.getUsername());
        country.setText(user.getCountry());
        age.setText(user.getAge());
        fromTo.setText(data.get(position).getPreferredAgeMin() + "~" + data.get(position).getPreferredAgeMax());
        if (Objects.equals(user.getUserVerified(), "true")){
            verified.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.darkGreen)));
        }else{
            verified.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
        }
        cityTown.setText(user.getProvince() +","+user.getCity());
        CardView status = holder.itemView.findViewById(R.id.onlineStatus);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Presence").child(data.get(position).getUser_id());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userStatus = snapshot.getValue(String.class);
                    if (Objects.equals(userStatus, "online")){
                        status.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getContext().getResources().getColor(R.color.LightGreen)));


                    }
                    if (Objects.equals(userStatus, "offline")){
                        status.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getContext().getResources().getColor(R.color.Gray)));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (!Objects.equals(data.get(position).getUser_id(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
            holder.setIsRecyclable(true);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                MingleIndex.getInstance().reset();
                MingleIndex.currentIndex = position;
                Intent intent = new Intent(v.getContext(), UserDetails.class);
                intent.putExtra("LIST", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (randomNumber()== 0 || randomNumber() == 2 || randomNumber() == 4){
                    AdRequest request = new AdRequest.Builder().build();
                    InterstitialAd.load(context,
                            context.getString(R.string.InterstitialAd_UnitId),
                            request, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);

                                    RewardedInterstitialAd.load(
                                            context, context.getString(R.string.RewardedInterstitialAd_UnitId),
                                            request,
                                            new RewardedInterstitialAdLoadCallback() {
                                                @Override
                                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                                    super.onAdFailedToLoad(loadAdError);
                                                    progressBar.setVisibility(View.GONE);
                                                    v.getContext().startActivity(intent);

                                                }

                                                @Override
                                                public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                                                    super.onAdLoaded(rewardedInterstitialAd);
                                                    Activity activity = (Activity) context;
                                                    rewardedInterstitialAd.show(activity, new OnUserEarnedRewardListener() {
                                                        @Override
                                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                                            DatabaseReference EntriesReference = FirebaseDatabase.getInstance().getReference("Entries")
                                                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                                            EntriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    int userStatus = Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class)));
                                                                    int add = userStatus +1;
                                                                    EntriesReference.setValue(String.valueOf(add));

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                            Toast.makeText(context, rewardItem.getAmount() +" " +rewardItem.getType() +context.getString(R.string.earned) , Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    checkImpressionO(rewardedInterstitialAd,v,intent,  progressBar);
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                    );
                                }

                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    super.onAdLoaded(interstitialAd);
                                    Activity activity = (Activity) context;
                                    interstitialAd.show(activity);
                                    progressBar.setVisibility(View.GONE);
                                    checkImpression(interstitialAd,v,intent,  progressBar);
                                }
                            });
                }else{
                    progressBar.setVisibility(View.GONE);
                    v.getContext().startActivity(intent);
                }

            }
        });
        if (lat>0 || lat <0 && lat2 >0 || lat2<0 && lon >0 ||lon<0 && lon2>0 || lon<0){
            String kilometers = String.valueOf(getLocationDetails(holder.itemView.getContext(),data.get(position).getUser_id()));
            distanceText.setText(kilometers +"~ Km");
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class myviewholder  extends RecyclerView.ViewHolder{
        public myviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private void checkImpressionO(RewardedInterstitialAd interstitionAd, View view, Intent intent, ProgressBar bar){
        interstitionAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                bar.setVisibility(View.GONE);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                bar.setVisibility(View.GONE);
                view.getContext().startActivity(intent);
            }
        });
    }
    private void checkImpression(InterstitialAd interstitionAd, View view, Intent intent, ProgressBar bar){
        interstitionAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                bar.setVisibility(View.GONE);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                bar.setVisibility(View.GONE);
                view.getContext().startActivity(intent);
            }
        });
    }

    LocationManager locationManager;
    double lat;
    double lon;
    double lat2;
    double lon2;
     private double getLocationDetails(Context context,String mingleUid){
        DatabaseReference mingleReference = FirebaseDatabase.getInstance().getReference("Locations").child(mingleUid);
        mingleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    lat2 = Objects.requireNonNull(snapshot.getValue(Double.class));
                    lon2 = Objects.requireNonNull(snapshot.getValue(Double.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED ){

        }else {
            Location locationGps;
            locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGps !=null){
                lat = locationGps.getLatitude();
                lon = locationGps.getLongitude();

            }else{
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Locations").child(uid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            lat = Objects.requireNonNull(snapshot.getValue(Double.class));
                            lon = Objects.requireNonNull(snapshot.getValue(Double.class));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
        return DistanceCalculator.ofLatLongInKm(lat,lon,lat2,lon2);
    }

    private int randomNumber(){
        Random random = new Random();

        return random.nextInt(5);

    }

}

