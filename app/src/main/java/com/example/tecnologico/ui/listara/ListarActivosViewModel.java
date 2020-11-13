package com.example.tecnologico.ui.listara;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListarActivosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListarActivosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is listara fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}