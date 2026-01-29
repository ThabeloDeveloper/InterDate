package com.mecaroid.interdate.Public;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.R;

import java.util.Objects;
import java.util.Random;

public class AdsServices {
    public void showMyAd(Context context, @Nullable Intent intent,@Nullable View v){
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
                                            if(intent !=null){
                                                context.startActivity((Intent) intent);
                                            }


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
                                            checkImpressionO(rewardedInterstitialAd, (View) v, (Intent) intent);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            Activity activity = (Activity) context;
                            interstitialAd.show(activity);
                            checkImpression(interstitialAd, (View) v, (Intent) intent);
                        }
                    });
        }else{
            if (intent != null){
                context.startActivity((Intent) intent);
            }

        }
    }

    private int randomNumber(){
        Random random = new Random();

        return random.nextInt(5);

    }
    private void checkImpressionO(RewardedInterstitialAd interstitionAd, View view, Intent intent){
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
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);


            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                if (intent !=null){
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
    private void checkImpression(InterstitialAd interstitionAd, View view, Intent intent){
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
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                if (intent !=null){
                    view.getContext().startActivity(intent);
                }
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                if (intent !=null){
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
}
