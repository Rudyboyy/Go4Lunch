package com.rudy.go4lunch.ui.chat;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.rudy.go4lunch.R;
import com.rudy.go4lunch.databinding.ItemChatBinding;
import com.rudy.go4lunch.manager.UserManager;
import com.rudy.go4lunch.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatAdapter extends FirestoreRecyclerAdapter<Message, ChatAdapter.MessageViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    // VIEW TYPES
    private static final int SENDER_TYPE = 1;
    private static final int RECEIVER_TYPE = 2;

    private Listener callback;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Listener callback) {
        super(options);
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        String currentUserId = UserManager.getInstance().getCurrentUser().getUid();
        boolean isSender = getItem(position).getUserSender().getUid().equals(currentUserId);

        return (isSender) ? SENDER_TYPE : RECEIVER_TYPE;
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.itemView.invalidate();
        holder.updateWithMessage(model);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false), viewType == 1);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ItemChatBinding binding;

        private final int colorCurrentUser;
        private final int colorRemoteUser;

        private boolean isSender;

        public MessageViewHolder(@NonNull View itemView, boolean isSender) {
            super(itemView);
            this.isSender = isSender;
            binding = ItemChatBinding.bind(itemView);

            colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.fcb_blue);
            colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.orange);
        }

        public void updateWithMessage(Message message) {

            // Update message
            binding.messageTextView.setText(message.getMessage());
            binding.messageTextView.setTextAlignment(isSender ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = dateFormat.format(message.getCreationTimeStamp());
            binding.dateTextView.setText(time);

            if (message.getUserSender().getUrlPicture() != null)
                Glide.with(binding.profileImage.getContext())
                        .load(message.getUserSender().getUrlPicture())
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImage);

            if (message.getUrlImage() != null) {
                Glide.with(binding.profileImage.getContext())
                        .load(message.getUrlImage())
                        .into(binding.senderImageView);
                binding.senderImageView.setVisibility(View.VISIBLE);
            } else {
                binding.senderImageView.setVisibility(View.GONE);
            }

            updateLayoutFromSenderType();
        }

        private void updateLayoutFromSenderType() {
            Drawable drawable = binding.messageTextContainer.getBackground();
            if (drawable instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) drawable;
                // Fais quelque chose avec le GradientDrawable ici
                gradientDrawable.setColor(isSender ? colorCurrentUser : colorRemoteUser);
            } else if (drawable instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) drawable;
                // Fais quelque chose avec le ColorDrawable ici
                colorDrawable.setColor(isSender ? colorCurrentUser : colorRemoteUser);
            }
            binding.messageTextContainer.requestLayout();
            if (!isSender) {
                updateProfileContainer();
                updateMessageContainer();
            }
        }

        private void updateProfileContainer() {
            // Update the constraint for the profile container (Push it to the left for receiver message)
            ConstraintLayout.LayoutParams profileContainerLayoutParams = (ConstraintLayout.LayoutParams) binding.profileContainer.getLayoutParams();
            profileContainerLayoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            profileContainerLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            binding.profileContainer.requestLayout();
        }

        private void updateMessageContainer() {
            // Update the constraint for the message container (Push it to the right of the profile container for receiver message)
            ConstraintLayout.LayoutParams messageContainerLayoutParams = (ConstraintLayout.LayoutParams) binding.messageContainer.getLayoutParams();
            messageContainerLayoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            messageContainerLayoutParams.endToStart = ConstraintLayout.LayoutParams.UNSET;
            messageContainerLayoutParams.startToEnd = binding.profileContainer.getId();
            messageContainerLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            messageContainerLayoutParams.horizontalBias = 0.0f;
            binding.messageContainer.requestLayout();

            // Update the constraint (gravity) for the text of the message (content + date) (Align it to the left for receiver message)
            LinearLayout.LayoutParams messageTextLayoutParams = (LinearLayout.LayoutParams) binding.messageTextContainer.getLayoutParams();
            messageTextLayoutParams.gravity = Gravity.START;
            binding.messageTextContainer.requestLayout();

            LinearLayout.LayoutParams dateLayoutParams = (LinearLayout.LayoutParams) binding.dateTextView.getLayoutParams();
            dateLayoutParams.gravity = Gravity.BOTTOM | Gravity.START;
            binding.dateTextView.requestLayout();

        }
    }
}
