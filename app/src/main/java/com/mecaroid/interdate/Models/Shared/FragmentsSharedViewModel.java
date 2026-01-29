package com.mecaroid.interdate.Models.Shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

public class FragmentsSharedViewModel extends ViewModel {
    private final MutableLiveData<Map<String,String>> dataMap = new MutableLiveData<>();
    private void setData(Map<String,String> data){
        dataMap.setValue(data);
    }
    public LiveData<Map<String,String>> getData(){
        return dataMap;
    }
}
