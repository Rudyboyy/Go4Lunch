package com.rudy.go4lunch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.ui.WelcomeScreen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WelcomeScreenTest {

    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private FirebaseUser firebaseUser;
    @Mock
    private UserManager userManager;
    @Mock
    private ActivityCompat.OnRequestPermissionsResultCallback callback;

    private WelcomeScreen welcomeScreen;

    List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Before
    public void setup() {
        welcomeScreen = new WelcomeScreen();
        AuthUI authUI = mock(AuthUI.class);
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        when(signInIntent).thenReturn(signInIntent);
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(userManager.getUserData()).thenReturn(null);

        welcomeScreen.userManager = userManager;
    }

//    @Test
//    public void onSignInResult_withSuccessResult_shouldCallCreateUser() {
//        FirebaseUser firebaseUser = mock(FirebaseUser.class);
//        IdpResponse response = new IdpResponse.Builder()
//                .setResultCode(Activity.RESULT_OK)
//                .build();
//        FirebaseAuthUIAuthenticationResult result = new FirebaseAuthUIAuthenticationResult(
//                firebaseUser, response);
//
//        welcomeScreen.onSignInResult(result);
//
//        verify(userManager).createUser();
//    }
}
