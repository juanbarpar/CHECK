package com.example.check.Principal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.check.Entidad.TravelLocation;
import com.example.check.Gestion.GestionItinerario;
import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.example.check.Utilities.PreferenceManager;
import com.example.check.Gestion.adaptadores.ChatAdapter;
import com.example.check.databinding.ActivityChatBinding;
import com.example.check.Entidad.ChatMessage;
import com.example.check.Entidad.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;

    private FirebaseFirestore database;
    private String conversionId = null;
    private String receiverId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        init();
        loadReceiverDetails();

        listenMessages();
    }
    private void init(){

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                mAuth.getUid()
        );

        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();

    }
    private void sendMessage(){

        HashMap<String,Object> message = new HashMap<>();
        message.put(Constantes.KEY_SENDER_ID,mAuth.getUid());
        message.put(Constantes.KEY_RECEIVER_ID,receiverId);
        message.put(Constantes.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constantes.KEY_TIMESTAMP, new Date());
        database.collection(Constantes.KEY_COLLECTION_CHAT).add(message);

        if(conversionId != null){
            updateConversion(binding.inputMessage.getText().toString());

        } else {
            HashMap<String, Object> conversion = new HashMap<>();

            DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(mAuth.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);

                    conversion.put(Constantes.KEY_SENDER_ID, mAuth.getUid());
                    conversion.put(Constantes.KEY_SENDER_NAME, user.getName());
                    conversion.put(Constantes.KEY_SENDER_IMAGE, user.getImage());
                    conversion.put(Constantes.KEY_RECEIVER_ID,receiverId);
                    conversion.put(Constantes.KEY_RECEIVER_NAME, receiverUser.getName());
                    conversion.put(Constantes.KEY_RECEIVER_IMAGE, receiverUser.getImage());
                    conversion.put(Constantes.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
                    conversion.put(Constantes.KEY_TIMESTAMP, new Date());
                    addConversion(conversion);
                    binding.inputMessage.setText(null);
                }
            });


        }

    }
    private void listenMessages(){
        database.collection(Constantes.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constantes.KEY_SENDER_ID,mAuth.getUid())
                .whereEqualTo(Constantes.KEY_RECEIVER_ID,receiverId)
                .addSnapshotListener(eventListener);
        database.collection(Constantes.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constantes.KEY_SENDER_ID, receiverUser)
                .whereEqualTo(Constantes.KEY_RECEIVER_ID,mAuth.getUid())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if(value != null){

            for (DocumentChange documentChange : value.getDocumentChanges()) {

                System.out.println("message: "+documentChange.getDocument().getString(Constantes.KEY_MESSAGE));

                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constantes.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constantes.KEY_MESSAGE);
                    chatMessage.datetime = getReadableDateTime(documentChange.getDocument().getDate(Constantes.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constantes.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                    chatAdapter.notifyDataSetChanged();
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            int count = chatMessages.size();
            if(count ==0){
                chatAdapter.notifyDataSetChanged();
            } else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversionId == null) {
            checkForConversion();
        }
    });

    private Bitmap getBitmapFromEncodedString(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }
    private void loadReceiverDetails() {
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            String j =(String) b.get(Constantes.KEY_USER);
            receiverId = j;
        }


        DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(receiverId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                receiverUser = documentSnapshot.toObject(User.class);
                System.out.println("name"+receiverUser.getName());
                binding.textName.setText(receiverUser.getName());
            }
        });


    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.layoutSend.setOnClickListener(view -> sendMessage());

    }
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constantes.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }
    private void updateConversion(String message) {
        DocumentReference documentReference =
                database.collection(Constantes.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constantes.KEY_LAST_MESSAGE, message,
                Constantes.KEY_TIMESTAMP, new Date()
        );
        binding.inputMessage.setText(null);
    }

    private void checkForConversion() {
        if(chatMessages.size() != 0){
            checkForConversionRemotely(
                    mAuth.getUid(),
                    receiverId
            );
            checkForConversionRemotely(
                    receiverId,
                    mAuth.getUid()
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constantes.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constantes.KEY_SENDER_ID,senderId)
                .whereEqualTo(Constantes.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
      if(task.isSuccessful() && task.getResult() !=null && task.getResult().getDocuments().size()>0){
          DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
          conversionId = documentSnapshot.getId();
      }
    };
}