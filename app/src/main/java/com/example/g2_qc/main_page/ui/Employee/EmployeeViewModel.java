package com.example.g2_qc.main_page.ui.Employee;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EmployeeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Employee fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}