package com.mecaroid.interdate.State;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CheckState {

    String user_id;

    public CheckState() {
    }

    public CheckState(String user_id) {
        this.user_id = user_id;
    }
    private static boolean friend;
    private static String my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public boolean isFriend(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends").child(my_uid);
        Query query = reference.orderByChild(user_id).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    friend = true;
                }else{
                    friend = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return friend;

    }
    ///Video////VoiceCall//RequestReceived///RequestSent///
    private static boolean hasBlock;
    public boolean hasBlock(){
        DatabaseReference blockRef = FirebaseDatabase.getInstance().getReference("Blocklist").child(user_id);
        Query query =  blockRef.orderByChild(my_uid).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    hasBlock = true;
                }else{
                    hasBlock = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return hasBlock;

    }
    private static boolean isBlocked;
    public boolean isBlocked(){
        DatabaseReference blockRef = FirebaseDatabase.getInstance().getReference("Blocklist").child(my_uid);
        Query query =  blockRef.orderByChild(user_id).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    isBlocked = true;
                }else{
                    isBlocked = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isBlocked;

    }
    private static boolean isVideoCallAvailable;
    public boolean isVideoCallAvailable(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CallAvailability/NoGEcSKr6NFnBjqKuOy/VideoCall");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.getValue(String.class), "true")){
                    isVideoCallAvailable = true;
                }else {
                    isVideoCallAvailable =false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isVideoCallAvailable;

    }

    private static boolean isVoiceCallAvailable;
    public boolean isVoiceCallAvailable(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CallAvailability/NoGEcSKr6NFnBjqKuOy/VoiceCall");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.getValue(String.class), "true")){
                    isVoiceCallAvailable = true;
                }else {
                    isVoiceCallAvailable =false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isVoiceCallAvailable;

    }
    
}
