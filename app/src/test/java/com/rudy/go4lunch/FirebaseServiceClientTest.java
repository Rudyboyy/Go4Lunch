package com.rudy.go4lunch;

import static com.rudy.go4lunch.FakeData.fakeCurrentUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.ui.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseServiceClientTest {

    final FirebaseAuth firebaseAuthMock = mock(FirebaseAuth.class);
    UserManager firebaseServicesClient = new UserManager();
    // Firestore
    final FirebaseUser firebaseUserMock = mock(FirebaseUser.class);

    MainActivity mActivity;


    @Before
    public void setUp() {
        mActivity = new MainActivity();
    }

    @Test
    public void shouldInitializeFirebaseServicesClient() {
        //    initFirebaseServicesClient(); ---> Called in setUp() method.
        assertNotNull(firebaseServicesClient.getCurrentUser());
    }

    @Test
    public void shouldPushUserDataToFirebase() {
        firebaseServicesClient.getUserData();

        final ArgumentCaptor<Map<String, Object>> userDataCaptor = ArgumentCaptor.forClass(HashMap.class);
//        verify(userDocumentMock).update(userDataCaptor.capture());

        assertTrue(userDataCaptor.getValue().containsValue(fakeCurrentUser.getBookedRestaurantPlaceId()));
        assertTrue(userDataCaptor.getValue().containsValue(fakeCurrentUser.getBookedRestaurant()));
        assertTrue(userDataCaptor.getValue().containsValue(fakeCurrentUser.getFavoriteRestaurants()));
    }

//    @Test
//    public void should_logout_current_user() {
//        firebaseServicesClient.signOut(firebaseServicesClient.getCurrentUser().zza().getApplicationContext());
//        verify(firebaseAuthMock.signOut());
//    }

    @Test
    public void shouldDeleteCurrentUserAccount() { //todo test            delete account
        final Task<Void> deleteTask = mock(Task.class);
        final ArgumentCaptor<OnSuccessListener> deleteCallbackCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        when(firebaseUserMock.delete()).thenReturn(deleteTask);
        when(deleteTask.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(deleteTask);

//        firebaseServicesClient.deleteUser();

//        verify(firebaseUserMock).delete();
//        verify(deleteTask).addOnSuccessListener(deleteCallbackCaptor.capture());
        deleteCallbackCaptor.getValue().onSuccess(null);
    }

    @Test
    public void should_get_current_user() {
        assertEquals(firebaseServicesClient.getCurrentUser().getUid(), fakeCurrentUser.getUid());
    }
}
