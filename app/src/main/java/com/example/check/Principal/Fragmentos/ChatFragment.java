package com.example.check.Principal.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.example.check.Utilities.PreferenceManager;
import com.example.check.databinding.FragmentChatBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private PreferenceManager preferenceManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        preferenceManager = new PreferenceManager(getContext());
        getToken();
        return binding.getRoot();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token) {
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constantes.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constantes.KEY_USER_ID)
                );
        documentReference.update(Constantes.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token creado"))
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }
}