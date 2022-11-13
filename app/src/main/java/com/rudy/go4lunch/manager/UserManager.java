package com.rudy.go4lunch.manager;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.rudy.go4lunch.model.User;
import com.rudy.go4lunch.repository.UserRepository;

public class UserManager {

    private static volatile UserManager instance;
    private  UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public void createUser(){
        userRepository.createUser();
    }

    public Task<User> getUserData(){
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public Task<Void> updateUsername(String username){
        return userRepository.updateUsername(username);
    }

    public void updateChoice(Boolean chose){
        userRepository.updateChoice(chose);
    }

    public Task<Void> deleteUser(Context context){
        return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            userRepository.deleteUserFromFirestore();
        });
    }
}
