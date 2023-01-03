package com.rudy.go4lunch.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.rudy.go4lunch.databinding.ActivityChatBinding;
import com.rudy.go4lunch.manager.ChatManager;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.Message;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.Listener {

    private ActivityChatBinding binding;
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_WORKMATE_ID = "key_workmate_id";
    public static final String WORKMATE_NAME = "workmate_name";
    private ChatAdapter chatAdapter;
    private String currentChatName;
    private UserManager userManager = UserManager.getInstance();
    //private ChatViewModel mChatViewModel = ChatViewModel.getInstance();
    private ChatManager chatManager = ChatManager.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureRecyclerView();
        setupListeners();
    }

    private void initUi() {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(WORKMATE_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListeners() {
        binding.sendButton.setOnClickListener(view -> sendMessage());
    }

    private void configureRecyclerView() {
        String currentUid = getIntent().getStringExtra(KEY_USER_ID);
        String workmateUid = getIntent().getStringExtra(KEY_WORKMATE_ID);
        //todo init avec un viewmodel
        if (currentUid.compareTo(workmateUid) < 0) {
            this.currentChatName = currentUid + workmateUid;
        } else {
            this.currentChatName = workmateUid + currentUid;
        }

        this.chatAdapter = new ChatAdapter(
                generateOptionsForAdapter(chatManager.getAllMessageForChat(this.currentChatName)), this);

        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerView.setAdapter(this.chatAdapter);
    }

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    public void onDataChanged() {
        // Show TextView in case RecyclerView is empty
        binding.emptyRecyclerView.setVisibility(this.chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void sendMessage() {
        boolean canSendMessage = !TextUtils.isEmpty(binding.chatEditText.getText()) && userManager.isCurrentUserLogged();
        if (canSendMessage) {
            chatManager.createMessageForChat(binding.chatEditText.getText().toString(), this.currentChatName);
            binding.chatEditText.setText("");
        }
    }

    public static void navigate(Activity activity, String currentUid, String workmateUid, String workmateName) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(KEY_USER_ID, currentUid);
        intent.putExtra(KEY_WORKMATE_ID, workmateUid);
        intent.putExtra(WORKMATE_NAME, workmateName);
        ActivityCompat.startActivity(activity, intent, null);
    }
}