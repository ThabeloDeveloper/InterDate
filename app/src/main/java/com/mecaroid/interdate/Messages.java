package com.mecaroid.interdate;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mecaroid.interdate.Adapters.Recycler.MessagingAdapter;
import com.mecaroid.interdate.Models.MessagingModel;
import com.mecaroid.interdate.Public.AdsServices;
import com.mecaroid.interdate.databinding.ActivityMessagesBinding;
import com.mecaroid.interdate.databinding.CallsOptionsLayoutBinding;
import com.mecaroid.interdate.databinding.DeleteconversationSinglemessageBinding;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Messages extends ComponentActivity {



    ActivityMessagesBinding binding;
    MessagingAdapter adapter;
    List<MessagingModel> data;
    BottomSheetDialog callsOptions;
    String callType = null;
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        data = new ArrayList<>();
        adapter = new MessagingAdapter(data);
        Intent intent = getIntent();
        binding.Msg.setEnabled(false);
        binding.RecyclerViews.setItemAnimator(new DefaultItemAnimator());
        binding.RecyclerViews.setLayoutManager(new LinearLayoutManager(this));
        binding.RecyclerViews.setAdapter(adapter);
        binding.RecyclerViews.smoothScrollToPosition(binding.RecyclerViews.getAdapter().getItemCount());
        callsOptions = new BottomSheetDialog(this);
        CallsOptionsLayoutBinding callsOptionsLayoutBinding = CallsOptionsLayoutBinding.inflate(getLayoutInflater());
        callsOptions.setContentView(callsOptionsLayoutBinding.getRoot());
        callsOptions.setCancelable(true);
        binding.tabLayout.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.profile_to){
                    Intent intents = new Intent(Messages.this, ViewProfile_In_Uid.class);
                    intents.putExtra("uid",intent.getStringExtra("uid"));
                    startActivity(intents);
                }else if (item.getItemId() == R.id.block){
                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(Messages.this);
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
                            ProgressDialog deleteProgress = new ProgressDialog(Messages.this);
                            deleteProgress.setCancelable(false);
                            deleteProgress.setMessage(getString(R.string.please_wait));
                            deleteProgress.show();
                            dialog.dismiss();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Blocklist")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            HashMap<Object,String> mapBlock = new HashMap<>();
                            mapBlock.put("user_id",intent.getStringExtra("uid"));
                            reference.push().setValue(mapBlock).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    deleteProgress.dismiss();
                                    Toast.makeText(Messages.this, getString(R.string.added_to_blocklist), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    deleteProgress.dismiss();
                                    Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
                    deleteAlert.create();
                    deleteAlert.show();


                }else if (item.getItemId() == R.id.deleteCon) {
                    DeleteconversationSinglemessageBinding binding1 = DeleteconversationSinglemessageBinding.inflate(getLayoutInflater());
                    BottomSheetDialog delete = new BottomSheetDialog(Messages.this);
                    delete.setContentView(binding1.getRoot());
                    delete.setCancelable(true);
                    delete.show();
                    binding1.deleteForEveryone.setEnabled(false);
                    binding1.deleteForEveryone.setVisibility(View.GONE);
                    binding1.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete.dismiss();
                        }
                    });
                    binding1.deleteForMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder deleteAlert = new AlertDialog.Builder(v.getContext());
                            deleteAlert.setTitle(R.string.delete_your_side_msg);
                            deleteAlert.setMessage(getString(R.string.about_to_de_conv));
                            deleteAlert.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                            deleteAlert.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ProgressDialog deleteProgress = new ProgressDialog(v.getContext());
                                    deleteProgress.setCancelable(false);
                                    deleteProgress.setMessage(getString(R.string.please_wait));
                                    deleteProgress.show();
                                    dialog.dismiss();
                                    DatabaseReference MylastSent = FirebaseDatabase.getInstance().getReference("Chats")
                                            .child(Objects.requireNonNull(intent.getStringExtra("uid")))
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    MylastSent.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            MylastSent.getRef().keepSynced(false);
                                            deleteProgress.dismiss();
                                            dialog.dismiss();
                                            Toast.makeText(v.getContext(), getString(R.string.delete_succesfull), Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                                }
                            });
                            deleteAlert.create();
                            deleteAlert.show();

                        }
                    });




                }
                return true;
            }
        });

        binding.tabLayout.setTitle(intent.getStringExtra("username"));
        binding.tabLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.Msg.setEnabled(false);
        binding.ProgressCard.setVisibility(View.VISIBLE);
        CheckBlockage(intent);
        message = binding.Msg.getText().toString().trim();

        GetMessages(intent);
        GetMessagingPoints();
        binding.ButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateSendButton(view);
            }
        });

    }
    private void CheckBlockage(Intent intent){
        DatabaseReference checkBlockage = FirebaseDatabase.getInstance().getReference("Blocklist").child(intent.getStringExtra("uid"));
        Query queryBlock = checkBlockage.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        queryBlock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.Msg.setEnabled(false);
                    binding.tabLayout.setVisibility(View.GONE);
                    binding.ProgressCard.setVisibility(View.GONE);
                }else {
                    binding.Msg.setEnabled(true);
                    binding.ProgressCard.setVisibility(View.GONE);
                    binding.tabLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void GetMessages(Intent intent){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(intent.getStringExtra("uid"))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.ProgressCard.setVisibility(View.GONE);
                binding.Msg.setEnabled(true);
                data.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessagingModel model = dataSnapshot.getValue(MessagingModel.class);
                    data.add(model);


                }
                adapter.notifyDataSetChanged();
                binding.RecyclerViews.smoothScrollToPosition(binding.RecyclerViews.getAdapter().getItemCount());

                if (!snapshot.exists()) {
                    binding.defirstTextCard.setVisibility(View.VISIBLE);
                    binding.defirstText.setText(getString(R.string.be_first_to_send_msg) + intent.getStringExtra("username"));
                }else {
                    binding.defirstTextCard.setVisibility(View.GONE);
                    binding.defirstText.setText(getString(R.string.be_first_to_send_msg) + intent.getStringExtra("username"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void GetMessagingPoints(){
        String uid =Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference Coins = FirebaseDatabase.getInstance().getReference("MessagingEntries").child(uid);
        Coins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.LeftPoint.setText(snapshot.getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void AnimateSendButton(View view){
        view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                view.animate().setDuration(50).scaleX(1.0f).scaleY(1.0f).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if (!binding.Msg.getText().toString().isEmpty()){
                            binding.sendingProgress.setVisibility(View.VISIBLE);
                            ClickToSend();

                        }


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
        }).start();
    }
    private void BuyCoins(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("MessagingEntries").child(uid);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/ncp/payment/NGP843J43DRBE"));
        startActivity(intent);
    }


    private void ClickToSend(){
        ProgressDialog progressDialog = new ProgressDialog(this,R.style.CustomProgressDialogStyle);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        binding.sendingProgress.setVisibility(View.VISIBLE);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference Coins = FirebaseDatabase.getInstance().getReference("MessagingEntries").child(uid);
        Coins.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Messages.this,R.style.CustomProgressDialogStyle);
                    dialog.setTitle(getString(R.string.insufficient_local_credits));
                    dialog.setMessage(getString(R.string.each_message_sent_cost));
                    dialog.setPositiveButton(getString(R.string.buyNow), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            progressDialog.show();
                            DatabaseReference onCoins = FirebaseDatabase.getInstance().getReference("Entries").child(uid);
                            onCoins.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int availableOn = Integer.parseInt(snapshot.getValue(String.class));
                                    if (availableOn >0){
                                        String updateAvailableOn =String.valueOf(availableOn - 1);
                                        onCoins.setValue(updateAvailableOn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Coins.setValue("5").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        ClickToSend();
                                                        AdsServices services =  new AdsServices();
                                                        services.showMyAd(Messages.this, null,null);
                                                        progressDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialogInterface.dismiss();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialogInterface.dismiss();
                                                progressDialog.dismiss();
                                                Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    });
                    dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        binding.sendingProgress.setVisibility(View.GONE);
                    });
                    dialog.create();
                    dialog.show();

                } else if (Integer.parseInt(Objects.requireNonNull(snapshot.getValue(String.class))) <1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Messages.this,R.style.CustomProgressDialogStyle);
                    dialog.setTitle(getString(R.string.insufficient_local_credits));
                    dialog.setMessage(getString(R.string.each_message_sent_cost));
                    dialog.setPositiveButton(getString(R.string.buyNow), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            progressDialog.show();
                            DatabaseReference onCoins = FirebaseDatabase.getInstance().getReference("Entries").child(uid);
                            onCoins.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int availableOn = Integer.parseInt(snapshot.getValue(String.class));
                                    if (availableOn >0){
                                        String updateAvailableOn =String.valueOf(availableOn - 1);
                                        onCoins.setValue(updateAvailableOn).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Coins.setValue("5").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        ClickToSend();
                                                        progressDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialogInterface.dismiss();
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialogInterface.dismiss();
                                                progressDialog.dismiss();
                                                Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    });
                    dialog.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    dialog.create();
                    dialog.show();
                }else{
                    int currentCoins = Integer.parseInt(snapshot.getValue(String.class));
                    int finalAmount = currentCoins - 1;
                    DatabaseReference Coins = FirebaseDatabase.getInstance().getReference("MessagingEntries").child(uid);
                    Coins.setValue(String.valueOf(finalAmount)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            sendToMe();
                            SendTokenToAdmin sendTokenToAdmin = new SendTokenToAdmin();
                            sendTokenToAdmin.getUserToken(getIntent().getStringExtra("uid"),"message");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.Msg.setEnabled(true);
                            binding.sendingProgress.setVisibility(View.GONE);
                            Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendToMe(){
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DATE));
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        String time = String.valueOf(calendar.get(Calendar.MINUTE));
        String milis = String.valueOf(calendar.get(Calendar.MILLISECOND));
        String path = year+month+day+hour+time+milis;
        DatabaseReference sendToMe = FirebaseDatabase.getInstance().getReference("Chats")
                .child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(path);
        HashMap<Object,String> map = new HashMap<>();
        map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("receiver",getIntent().getStringExtra("uid"));
        map.put("message",binding.Msg.getText().toString().trim());
        map.put("time",path);
        map.put("status","sent");
        sendToMe.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendToOther(path);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.Msg.setEnabled(true);
                binding.sendingProgress.setVisibility(View.GONE);
                Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendToOther(String path){
        DatabaseReference sendToOther = FirebaseDatabase.getInstance().getReference("Chats")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Objects.requireNonNull(getIntent().getStringExtra("uid"))).child(path);
        HashMap<Object,String> map = new HashMap<>();
        map.put("sender",FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("receiver",getIntent().getStringExtra("uid"));
        map.put("message",binding.Msg.getText().toString().trim());
        map.put("time",path);
        map.put("status","sent");
        sendToOther.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                submitLastMessages(getIntent().getStringExtra("uid"),binding.Msg.getText().toString().trim(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                binding.Msg.setText("");
                binding.Msg.setEnabled(true);
                binding.sendingProgress.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.Msg.setEnabled(true);
                binding.sendingProgress.setVisibility(View.GONE);
                Toast.makeText(Messages.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void submitLastMessages(String uid, String message,String sender_uid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("LastMessages").child(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("message",message);
        map.put("sender",sender_uid);
        reference.setValue(map);

    }






}