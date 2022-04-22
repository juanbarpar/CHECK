package com.example.check.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.check.databinding.ActivityTestLoginBinding;

public class Test_login_Activity extends AppCompatActivity {
    private  ActivityTestLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

    }
    private void  setListeners() {
        binding.buttonSignUp.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),Test_Sign_Up_Activity.class)));

   }




}