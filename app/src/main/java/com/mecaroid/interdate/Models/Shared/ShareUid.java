package com.mecaroid.interdate.Models.Shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareUid extends ViewModel {
    private final MutableLiveData<String> Uid = new MutableLiveData<>();
    public void setText(String text){
        Uid.setValue(text);
    }
    public LiveData<String> getTextLiveData(){
        return Uid;
    }
}
