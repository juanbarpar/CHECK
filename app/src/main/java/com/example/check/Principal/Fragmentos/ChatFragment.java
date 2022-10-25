package com.example.check.Principal.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.check.Entidad.User;
import com.example.check.Utilities.Constantes;
import com.example.check.Principal.activities.ChatActivity;
import com.example.check.Principal.activities.UsersActivity;
import com.example.check.Gestion.adaptadores.RecentConversationsAdapter;
import com.example.check.databinding.FragmentChatBinding;
import com.example.check.listeners.ConversionListener;
import com.example.check.Entidad.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements ConversionListener {

    private FragmentChatBinding binding;

    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private void setListeners() {
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), UsersActivity.class)));
    }
    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void init() {
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        binding = FragmentChatBinding.inflate(getLayoutInflater());
        init();
        //getToken();
        setListeners();
        listenConversations();
        return binding.getRoot();
    }
    private void showToast(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

    private void listenConversations(){
        database.collection(Constantes.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constantes.KEY_SENDER_ID, mAuth.getUid())
                .addSnapshotListener(eventListener);
        database.collection(Constantes.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constantes.KEY_RECEIVER_ID, mAuth.getUid())
                .addSnapshotListener(eventListener);
    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }if (value!=null){
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constantes.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_ID);

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if(mAuth.getUid().equals(senderId)){
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_ID);
                    }else{
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constantes.KEY_SENDER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constantes.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constantes.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constantes.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constantes.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                }else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for(int i=0; i<conversations.size(); i++){
                        String senderId = documentChange.getDocument().getString(Constantes.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constantes.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                            conversations.get(i).message = documentChange.getDocument().getString(Constantes.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constantes.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1,obj2)-> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onConversionClicked(String user) {
      Intent intent = new Intent(getActivity(), ChatActivity.class);
      intent.putExtra(Constantes.KEY_USER, user);
      startActivity(intent);
    }
}