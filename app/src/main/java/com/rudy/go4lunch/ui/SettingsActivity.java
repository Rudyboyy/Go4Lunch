package com.rudy.go4lunch.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivitySettingsBinding;
import com.rudy.go4lunch.manager.UserManager;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    private UserManager userManager = UserManager.getInstance();

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
        setDeleteButton();
        setUpdateButton();
        updateUIWithUserData();
    }

    private void setBackButton() {
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void setDeleteButton() {
        binding.deleteAccountButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.popup_message_confirmation_delete_account)
                    .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                            userManager.deleteUser(SettingsActivity.this)
                                    .addOnSuccessListener(aVoid -> {
                                                finish();
                                            }
                                    )
                    )
                    .setNegativeButton(R.string.popup_message_choice_no, null)
                    .show();
        });
    }

    private void setUpdateButton() {
        binding.updateButton.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            userManager.updateUsername(binding.editTextTextPersonName.getText().toString())
                    .addOnSuccessListener(aVoid -> {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    });
        });
    }

    private void updateUIWithUserData(){
        if(userManager.isCurrentUserLogged()){
            FirebaseUser user = userManager.getCurrentUser();
            setTextUserData(user);
            getUserData();
        }
    }

    private void setTextUserData(FirebaseUser user) {
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();
        binding.editTextTextPersonName.setText(username);
    }

    private void getUserData() {
        userManager.getUserData().addOnSuccessListener(user -> {
            String username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();
            binding.editTextTextPersonName.setText(username);
        });
    }
}
