package com.rudy.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rudy.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static volatile UserRepository instance;

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String BOOKED_RESTAURANT = "bookedRestaurant";
    private static final String BOOKED_RESTAURANT_PLACE_ID = "bookedRestaurantPlaceId";
    private static final String FAVORITE_RESTAURANTS = "favoriteRestaurants";

    private FirebaseFirestore database;
    private final MutableLiveData<List<User>> allUsers = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            User userToCreate = new User(uid, username, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();

            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(BOOKED_RESTAURANT)) {
                    userToCreate.setBookedRestaurant((String) documentSnapshot.get(BOOKED_RESTAURANT));
                } else if (documentSnapshot.contains(BOOKED_RESTAURANT_PLACE_ID)) {
                    userToCreate.setBookedRestaurantPlaceId((String) documentSnapshot.get(BOOKED_RESTAURANT_PLACE_ID));
                    //                } else if (documentSnapshot.contains(FAVORITE_RESTAURANTS)) {//todo useless ?
//                    userToCreate.setFavoriteRestaurants(Collections.singletonList((String) documentSnapshot.get(FAVORITE_RESTAURANTS)));
//                    restaurantDto.setPlaceId((String) documentSnapshot.get(PLACE_ID));
//                    restaurantDto.setName((String) documentSnapshot.get(NAME));
//                    this.getFavoriteCollection().document(uid).set(restaurantDto);
//                    userToCreate.setFavoriteRestaurants((List<RestaurantDto>) documentSnapshot.get(FAVORITE_RESTAURANTS));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            return null;
        }
    }

    public void UpdateBookedRestaurant(String bookedRestaurant, String placeId) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(BOOKED_RESTAURANT, bookedRestaurant);
            this.getUsersCollection().document(uid).update(BOOKED_RESTAURANT_PLACE_ID, placeId);
        }
    }

    public void cancelBooking() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(BOOKED_RESTAURANT, null);
            this.getUsersCollection().document(uid).update(BOOKED_RESTAURANT_PLACE_ID, null);
        }
    }

    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }

    public void getDataBaseInstance() {
        database = FirebaseFirestore.getInstance();
    }

    public LiveData<List<User>> getAllUsers() {
        database.collection(COLLECTION_NAME)
                .whereNotEqualTo("uid", getCurrentUserUID())
                .get()
                .addOnCompleteListener(task -> {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot user : task.getResult()) {
                        users.add(user.toObject(User.class));
                        allUsers.setValue(users);
                    }
                });
        return allUsers;
    }

    public void addFavorite(String userId, String placeId, String restaurantName) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
        getUsersCollection().document(userId)
                .update(FAVORITE_RESTAURANTS, FieldValue.arrayUnion(placeId));
        }
    }

    public void removeFavouriteRestaurant(String userId, String placeId) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            getUsersCollection().document(userId)
                    .update(FAVORITE_RESTAURANTS, FieldValue.arrayRemove(placeId));
        }
    }
}
