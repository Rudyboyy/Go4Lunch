package com.rudy.go4lunch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ActivityWelcomeScreenBinding;
import com.rudy.go4lunch.manager.UserManager;

import java.util.Collections;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;
    private UserManager userManager = UserManager.getInstance();

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
        startSignWithActivity(binding.googleButton);
        startSignWithActivity(binding.fcbButton);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );


    private void startSignWithActivity(View view) {
        view.setOnClickListener(view1 -> {
            List<AuthUI.IdpConfig> providers = null;
            if (view == binding.googleButton) {
                providers = Collections.singletonList(
                        new AuthUI.IdpConfig.GoogleBuilder().build());
            } else if (view == binding.fcbButton) {
                providers = Collections.singletonList(
                        new AuthUI.IdpConfig.FacebookBuilder().build());
            }

            assert providers != null;
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });
    }

    private void onSignInResult(@NonNull FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            showSnackBar(getString(R.string.connection_succeed));
            userManager.createUser();
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            this.startActivity(mainActivityIntent);
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

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    public static void navigate(Activity activity) {
        Intent intent = new Intent(activity, WelcomeScreen.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}