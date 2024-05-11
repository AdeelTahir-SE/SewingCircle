package com.adeeltahir.sewingcircle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel  extends ViewModel {
    private MutableLiveData<String> sharedString = new MutableLiveData<>();

    public void setSharedString(String text) {
        sharedString.setValue(text);
    }

    public LiveData<String> getSharedString() {
        return sharedString;
    }
}