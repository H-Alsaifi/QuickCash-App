package com.example.g2_qc.paypal_integration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.g2_qc.R;
import com.example.g2_qc.databinding.FragmentEmployerBinding;
import com.example.g2_qc.databinding.FragmentPaypalBinding;
import com.example.g2_qc.databinding.FragmentProfileBinding;


public class paypal extends Fragment {

    View view;
    private FragmentPaypalBinding binding;
    private Button complete_payment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPaypalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}