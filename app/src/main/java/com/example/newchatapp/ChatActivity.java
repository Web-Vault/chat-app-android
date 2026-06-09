package com.example.newchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;

public class ChatActivity extends AppCompatActivity {

    TextView chatUserName;
    RecyclerView chatRecyclerView;
    EditText messageBox;
    Button sendBtn;

    ArrayList<Message> messageList;
    MessageAdapter adapter;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    String senderRoom;
    String receiverRoom;

    String receiverName;
    String receiverUid;

    ChildEventListener messageListener;
    ListenerRegistration statusListener;
    
    ImageView btnBack;
    ImageView profileImage;
    ImageButton btnAttachment;
    TextView chatUserStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatUserName = findViewById(R.id.chatUserName);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        sendBtn = findViewById(R.id.sendBtn);

        btnBack = findViewById(R.id.btnBack);
        profileImage = findViewById(R.id.profileImage);
        btnAttachment = findViewById(R.id.btnAttachment);
        chatUserStatus = findViewById(R.id.chatUserStatus);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        receiverName = getIntent().getStringExtra("userName");
        receiverUid = getIntent().getStringExtra("userUid");

        if (receiverUid == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chatUserName.setText(receiverName);

        // REAL-TIME STATUS LISTENER
        statusListener = firestore.collection("users")
                .document(receiverUid)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        chatUserStatus.setText("Offline");
                        return;
                    }

                    if (value == null) {
                        chatUserStatus.setText("Offline");
                        return;
                    }

                    if (!value.exists()) {
                        chatUserStatus.setText("Offline");
                        return;
                    }

                    try {

                        Boolean isOnline =
                                value.getBoolean("isOnline");

                        if (Boolean.TRUE.equals(isOnline)) {

                            chatUserStatus.setText("Online");

                        } else {

                            Long lastSeen =
                                    value.getLong("lastSeen");

                            if (lastSeen != null) {

                                String time =
                                        android.text.format.DateFormat
                                                .format("hh:mm a", lastSeen)
                                                .toString();

                                chatUserStatus.setText(
                                        "Last seen " + time
                                );

                            } else {

                                chatUserStatus.setText("Offline");
                            }
                        }

                    } catch (Exception e) {

                        chatUserStatus.setText("Offline");

                    }
                });

        String senderUid = mAuth.getUid();
        if (senderUid == null) {
            finish();
            return;
        }

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(
                this,
                messageList,
                this::showMessageOptions
        );

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnAttachment.setOnClickListener(v -> {
            Toast.makeText(ChatActivity.this, "Media sharing coming soon", Toast.LENGTH_SHORT).show();
        });


        // 🔥 SEND MESSAGE
        sendBtn.setOnClickListener(v -> {
            String messageText = messageBox.getText().toString().trim();
            if (messageText.isEmpty()) return;

            String messageId = databaseReference.push().getKey();
            if (messageId == null) return;

            Message message = new Message(
                    messageText,
                    senderUid,
                    System.currentTimeMillis()
            );

            // sender
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(messageId)
                    .setValue(message);

            // receiver
            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(messageId)
                    .setValue(message);

            // last message info for chat list
            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(senderRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessage")
                    .setValue(messageText);

            databaseReference.child("chats")
                    .child(receiverRoom)
                    .child("lastMessageTime")
                    .setValue(System.currentTimeMillis());

            messageBox.setText("");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set self as online
        if (mAuth.getUid() != null) {
            firestore.collection("users").document(mAuth.getUid())
                    .update("isOnline", true);
        }
        attachListener();
    }

    @Override
    protected void onStop() {
        super.onStop();

        String uid = mAuth.getUid();

        if (uid != null) {

            firestore.collection("users")
                    .document(uid)
                    .update(
                            "isOnline", false,
                            "lastSeen", System.currentTimeMillis()
                    );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (statusListener != null) {
            statusListener.remove();
        }
    }

    private void attachListener() {
        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {

                Message message = snapshot.getValue(Message.class);

                // Safety checks
                if (message == null) {
                    return;
                }

                if (message.getSenderId() == null) {
                    return;
                }

                message.setMessageId(snapshot.getKey());

                messageList.add(message);
                adapter.notifyItemInserted(messageList.size() - 1);
                chatRecyclerView.scrollToPosition(messageList.size() - 1);

                String messageKey = snapshot.getKey();
                String senderId = message.getSenderId();

                if (!senderId.equals(mAuth.getUid())
                        && messageKey != null) {

                    long currentTime = System.currentTimeMillis();

                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("delivered")
                            .setValue(true);

                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("deliveredTime")
                            .setValue(currentTime);

                    databaseReference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("delivered")
                            .setValue(true);

                    databaseReference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("deliveredTime")
                            .setValue(currentTime);


                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("seen")
                            .setValue(true);

                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("seenTime")
                            .setValue(currentTime);

                    databaseReference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("seen")
                            .setValue(true);

                    databaseReference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(messageKey)
                            .child("seenTime")
                            .setValue(currentTime);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message updatedMessage = snapshot.getValue(Message.class);
                if (updatedMessage != null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        Message local = messageList.get(i);
                        if (local.getTimestamp() == updatedMessage.getTimestamp()
                                && local.getSenderId().equals(updatedMessage.getSenderId())) {
                            messageList.set(i, updatedMessage);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                String deletedId = snapshot.getKey();

                for (int i = 0; i < messageList.size(); i++) {

                    Message message = messageList.get(i);

                    if (message.getMessageId() != null &&
                            message.getMessageId().equals(deletedId)) {

                        messageList.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        };

        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addChildEventListener(messageListener);
    }

    private void showMessageOptions(Message message) {

        if (message.isDeleted()) {

            Toast.makeText(
                    this,
                    "Message already deleted",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String[] options;

        if (message.getSenderId().equals(mAuth.getUid())) {

            options = new String[]{
                    "Copy",
                    "Info",
                    "Delete for Me",
                    "Delete for Everyone"
            };

        } else {

            options = new String[]{
                    "Copy",
                    "Delete for Me"
            };
        }

        new AlertDialog.Builder(this)
                .setTitle("Message Options")
                .setItems(options, (dialog, which) -> {

                    if (message.getSenderId().equals(mAuth.getUid())) {

                        switch (which) {

                            case 0:
                                copyMessage(message);
                                break;

                            case 1:
                                Intent intent = getIntent(message);

                                startActivity(intent);
                                break;

                            case 2:
                                deleteForMe(message);
                                break;

                            case 3:
                                deleteForEveryone(message);
                                break;
                        }

                    } else {

                        switch (which) {

                            case 0:
                                copyMessage(message);
                                break;

                            case 1:
                                deleteForMe(message);
                                break;
                        }
                    }
                })
                .show();
    }

    private Intent getIntent(Message message) {
        Intent intent =
                new Intent(
                        ChatActivity.this,
                        MessageInfoActivity.class
                );

        intent.putExtra(
                "message",
                message.getMessage()
        );

        intent.putExtra(
                "sentTime",
                message.getTimestamp()
        );

        intent.putExtra(
                "deliveredTime",
                message.getDeliveredTime()
        );

        intent.putExtra(
                "seenTime",
                message.getSeenTime()
        );
        return intent;
    }

    private void copyMessage(Message message) {

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(CLIPBOARD_SERVICE);

        ClipData clip =
                ClipData.newPlainText(
                        "message",
                        message.getMessage()
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "Message copied",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showMessageInfo(Message message) {

        String sentTime =
                android.text.format.DateFormat.format(
                        "dd/MM/yyyy hh:mm a",
                        message.getTimestamp()
                ).toString();

        String deliveredTime = "Not Delivered";

        if (message.getDeliveredTime() > 0) {

            deliveredTime =
                    android.text.format.DateFormat.format(
                            "dd/MM/yyyy hh:mm a",
                            message.getDeliveredTime()
                    ).toString();
        }

        String seenTime = "Not Seen";

        if (message.getSeenTime() > 0) {

            seenTime =
                    android.text.format.DateFormat.format(
                            "dd/MM/yyyy hh:mm a",
                            message.getSeenTime()
                    ).toString();
        }

        String info =
                "Message: " + message.getMessage()
                        + "\n\nSent: " + sentTime
                        + "\n\nDelivered: " + deliveredTime
                        + "\n\nSeen: " + seenTime;

        new AlertDialog.Builder(this)
                .setTitle("Message Info")
                .setMessage(info)
                .setPositiveButton("OK", null)
                .show();
    }

    private void deleteForMe(Message message) {

        if (message.getMessageId() == null) {

            Toast.makeText(
                    this,
                    "Unable to delete message",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        databaseReference.child("chats")
                .child(senderRoom)
                .child("lastMessage")
                .get()
                .addOnSuccessListener(snapshot -> {

                    String currentLastMessage =
                            snapshot.getValue(String.class);

                    boolean shouldUpdatePreview = false;

                    if (currentLastMessage != null) {

                        if (currentLastMessage.equals(message.getMessage())) {
                            shouldUpdatePreview = true;
                        }

                        if (message.isDeleted()
                                && currentLastMessage.equals("This message was deleted")) {
                            shouldUpdatePreview = true;
                        }
                    }

                    boolean finalShouldUpdatePreview = shouldUpdatePreview;

                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .removeValue()
                            .addOnSuccessListener(unused -> {

                                if (finalShouldUpdatePreview) {
                                    updateLastMessagePreview(false);
                                }

                                Toast.makeText(
                                        ChatActivity.this,
                                        "Message deleted",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            ChatActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void deleteForEveryone(Message message) {

        if (message.getMessageId() == null) {

            Toast.makeText(
                    this,
                    "Unable to delete message",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Mark message as deleted in sender room
        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .child(message.getMessageId())
                .child("deleted")
                .setValue(true);

        // Mark message as deleted in receiver room
        databaseReference.child("chats")
                .child(receiverRoom)
                .child("messages")
                .child(message.getMessageId())
                .child("deleted")
                .setValue(true);

        // Refresh chat list preview
        updateLastMessagePreview(true);

        Toast.makeText(
                this,
                "Message deleted for everyone",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void updateLastMessagePreview(boolean updateReceiverToo) {

        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .get()
                .addOnSuccessListener(snapshot -> {

                    Message latestMessage = null;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Message msg =
                                dataSnapshot.getValue(Message.class);

                        if (msg == null) continue;

                        if (latestMessage == null ||
                                msg.getTimestamp() >
                                        latestMessage.getTimestamp()) {

                            latestMessage = msg;
                        }
                    }

                    String newLastMessage = "";
                    long newLastMessageTime = 0;

                    if (latestMessage != null) {

                        if (latestMessage.isDeleted()) {

                            newLastMessage =
                                    "This message was deleted";

                        } else {

                            newLastMessage =
                                    latestMessage.getMessage();
                        }

                        newLastMessageTime =
                                latestMessage.getTimestamp();
                    }

                    // Update sender room
                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("lastMessage")
                            .setValue(newLastMessage);

                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("lastMessageTime")
                            .setValue(newLastMessageTime);

                    // Update receiver room only if needed
                    if (updateReceiverToo) {

                        databaseReference.child("chats")
                                .child(receiverRoom)
                                .child("lastMessage")
                                .setValue(newLastMessage);

                        databaseReference.child("chats")
                                .child(receiverRoom)
                                .child("lastMessageTime")
                                .setValue(newLastMessageTime);
                    }
                });
    }
}