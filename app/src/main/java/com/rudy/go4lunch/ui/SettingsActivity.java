package com.rudy.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.rudy.go4lunch.databinding.ActivityMainBinding;
import com.rudy.go4lunch.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setBackButton();
    }

    private void setBackButton() {
        binding.backButton.setOnClickListener(view -> finish());
    }
}