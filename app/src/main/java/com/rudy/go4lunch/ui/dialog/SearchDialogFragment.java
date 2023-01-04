package com.rudy.go4lunch.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.rudy.go4lunch.databinding.FragmentSearchDialogBinding;

public class SearchDialogFragment extends DialogFragment {

    private FragmentSearchDialogBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding = FragmentSearchDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        builder.setView(view);
        return builder.create();
    }
}