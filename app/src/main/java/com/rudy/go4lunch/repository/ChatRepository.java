package com.rudy.go4lunch.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.Message;


public class ChatRepository {

    private static final String CHAT_COLLECTION = "chats";
    private static final String MESSAGE_COLLECTION = "messages";
    private static volatile ChatRepository instance;

    private UserManager userManager;

    private ChatRepository() {
        this.userManager = UserManager.getInstance();
    }

    public static ChatRepository getInstance() {
        ChatRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ChatRepository.class) {
            if (instance == null) {
                instance = new ChatRepository();
            }
            return instance;
        }
    }

    public CollectionReference getChatCollection() {
        return FirebaseFirestore.getInstance().collection(CHAT_COLLECTION);
    }

    public Query getAllMessageForChat(String chat) {
        return this.getChatCollection()
                .document(chat)
                .collection(MESSAGE_COLLECTION)
                .orderBy("creationTimeStamp", Query.Direction.ASCENDING)
                .limit(50);
    }


    public void createMessageForChat(String textMessage, String chat) {

        userManager.getUserData().addOnSuccessListener(user -> {
            // Create the Message object
            Message message = new Message(textMessage, user, System.currentTimeMillis());

            // Store Message to Firestore
            this.getChatCollection()
                    .document(chat)
                    .collection(MESSAGE_COLLECTION)
                    .add(message);
        });
    }
}
