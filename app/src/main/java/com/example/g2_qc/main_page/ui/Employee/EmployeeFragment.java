package com.example.g2_qc.main_page.ui.Employee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.g2_qc.databinding.FragmentEmployeeBinding;

public class EmployeeFragment extends Fragment {

    private FragmentEmployeeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmployeeViewModel employeeViewModel =
                new ViewModelProvider(this).get(EmployeeViewModel.class);

        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEmployee;
        employeeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}