package com.example.g2_qc.main_page.ui.Employer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EmployerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Employer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}