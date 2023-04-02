package com.rudy.go4lunch.manager;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.UserRepository;

public class UserManager {

    private static volatile UserManager instance;
    private final UserRepository userRepository;

    public UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context) {
        return userRepository.signOut(context);
    }

    public void createUser() {
        userRepository.createUser();
    }

    public Task<User> getUserData() {
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> updateUsername(String username) {
        return userRepository.updateUsername(username);
    }

    public void updateBookedRestaurant(String bookedRestaurant, String placeId) {
        userRepository.UpdateBookedRestaurant(bookedRestaurant, placeId);
    }

    public void cancelBooking() {
        userRepository.cancelBooking();
    }

    public void addFavorite(String userID, String restaurantID) {
        userRepository.addFavorite(userID, restaurantID);
    }

    public void removeFavoriteRestaurant(String userID, String restaurantID) {
        userRepository.removeFavouriteRestaurant(userID, restaurantID);
    }

    public Task<Void> deleteUser(Context context) {
        return userRepository.deleteUser(context).addOnCompleteListener(task -> userRepository.deleteUserFromFirestore());
    }
}
