package com.rudy.go4lunch.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rudy.go4lunch.databinding.FragmentDialogPermissionBinding;

public class PermissionDialogFragment extends DialogFragment {

    FragmentDialogPermissionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogPermissionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
