package com.example.g2_qc.main_page.ui.Employer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import com.example.g2_qc.databinding.FragmentEmployerBinding;

public class EmployerFragment extends Fragment {

    private FragmentEmployerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmployerViewModel employerViewModel =
                new ViewModelProvider(this).get(EmployerViewModel.class);

        binding = FragmentEmployerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEmployer;
        employerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}