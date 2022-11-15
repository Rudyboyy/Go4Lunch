package com.rudy.go4lunch.ui.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rudy.go4lunch.databinding.FragmentDialogPermissionBinding;

import java.util.Objects;

public class PermissionDialogFragment extends DialogFragment {

    FragmentDialogPermissionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogPermissionBinding.inflate(inflater, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setUpSettingsButton();
        return binding.getRoot();
    }

    private void setUpSettingsButton() {
        binding.settings.setOnClickListener(view -> {
            Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settings);
            dismiss();
        });
    }
}