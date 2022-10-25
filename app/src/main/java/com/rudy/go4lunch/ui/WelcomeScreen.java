package com.rudy.go4lunch.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityWelcomeScreenBinding;

import java.util.Collections;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupListeners();
    }

    private void setupListeners() {
        binding.googleButton.setOnClickListener(view -> {
            startSignInActivity();
        });
        binding.fcbButton.setOnClickListener(view -> {
            signWithFcb();
        });
    }

    private void getInstanceAuthUI(List<AuthUI.IdpConfig> provider) {
        AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .build();
    }

    //todo besoin d'etre optimisé
    private void signWithFcb() {
        List<AuthUI.IdpConfig> fcbProvider =
                Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(fcbProvider)
                        .build(),
                RC_SIGN_IN);
    }

    //todo besoin d'etre optimisé
    private void startSignInActivity() {
        List<AuthUI.IdpConfig> googleProvider =
                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
        // todo                     ou
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(googleProvider)
                        .setIsSmartLockEnabled(true, false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
//                userManager.createUser();
                showSnackBar(getString(R.string.connection_succeed));
                finish();
            } else {
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }

    public static void navigate(Activity activity) {
        Intent intent = new Intent(activity, WelcomeScreen.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}