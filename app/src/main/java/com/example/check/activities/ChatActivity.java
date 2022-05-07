package com.example.check.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.example.check.databinding.ActivityChatBinding;
import com.example.check.modelos.User;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constantes.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());

    }
}