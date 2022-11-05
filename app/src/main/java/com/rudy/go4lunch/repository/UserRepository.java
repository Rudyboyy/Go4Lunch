package com.rudy.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rudy.go4lunch.model.User;

public class UserRepository {

    private static volatile UserRepository instance;

    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String CHOSE_FIELD = "chose";

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }
    @Nullable
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }

        private CollectionReference getUsersCollection(){
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        }

        public void createUser() {
            FirebaseUser user = getCurrentUser();
            if(user != null){
                String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
                String username = user.getDisplayName();
                String uid = user.getUid();

                User userToCreate = new User(uid, username, urlPicture);

                Task<DocumentSnapshot> userData = getUserData();

                userData.addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.contains(CHOSE_FIELD)){
                        userToCreate.setChoice((Boolean) documentSnapshot.get(CHOSE_FIELD));
                    }
                    this.getUsersCollection().document(uid).set(userToCreate);
                });
            }
        }

        public Task<DocumentSnapshot> getUserData(){
            String uid = this.getCurrentUserUID();
            if(uid != null){
                return this.getUsersCollection().document(uid).get();
            }else{
                return null;
            }
        }

        public Task<Void> updateUsername(String username) {
            String uid = this.getCurrentUserUID();
            if(uid != null){
                return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
            }else{
                return null;
            }
        }

        public void updateChoice(Boolean chose) {
            String uid = this.getCurrentUserUID();
            if(uid != null){
                this.getUsersCollection().document(uid).update(CHOSE_FIELD, chose);
            }
        }

        public void deleteUserFromFirestore() {
            String uid = this.getCurrentUserUID();
            if(uid != null){
                this.getUsersCollection().document(uid).delete();
            }
        }
}
