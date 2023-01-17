package com.rudy.go4lunch.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivitySettingsBinding;
import com.rudy.go4lunch.manager.UserManager;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    private final UserManager userManager = UserManager.getInstance();

    public static final String YES = "YES";
    public static final String NO = "NO";
    public static final String SHARED_PREF_USER_INFO_NOTIFICATION = "SHARED_PREF_USER_INFO_NOTIFICATION";
    public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    String statusNotification = "";
    private String decision = "";

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
        setRadioButton();
    }

    private void setBackButton() {
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void setDeleteButton() {
        binding.deleteAccountButton.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                        userManager.deleteUser(SettingsActivity.this)
                                .addOnSuccessListener(aVoid -> finish()
                                )
                )
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show());
    }

    private void setUpdateButton() {
        binding.updateButton.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            userManager.updateUsername(binding.editTextTextPersonName.getText().toString())
                    .addOnSuccessListener(aVoid -> binding.progressBar.setVisibility(View.INVISIBLE));
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

    private void setRadioButton() {
        statusNotification = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NOTIFICATION, YES);

        if (statusNotification.equals(YES)) {
            binding.radioYes.setChecked(true);
        }
        if (statusNotification.equals(NO)) {
            binding.radioNo.setChecked(true);
        }

        binding.viewRadioGroup.setOnCheckedChangeListener((radioGroup, idChecked) -> {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            final int yes = R.id.radio_yes;
            final int no = R.id.radio_no;

            switch (idChecked) {
                case yes:
                    decision = YES;
                    editor.putString(SHARED_PREF_USER_INFO_NOTIFICATION, decision);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.receive_notification), Toast.LENGTH_SHORT).show();
                    break;
                case no:
                    decision = NO;
                    editor.putString(SHARED_PREF_USER_INFO_NOTIFICATION, decision);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.not_receive_notification), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
