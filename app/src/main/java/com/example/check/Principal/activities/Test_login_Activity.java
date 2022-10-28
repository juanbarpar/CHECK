package com.example.check.Principal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.check.Principal.MainActivity;
import com.example.check.databinding.ActivityTestLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Test_login_Activity extends AppCompatActivity {
    private ActivityTestLoginBinding binding;
    private FirebaseAuth mAuth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        setListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogAuthentication();
    }

    private void  setListeners() {
        binding.buttonSignUp.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),Test_Sign_Up_Activity.class)));
        binding.buttonSignIn.setOnClickListener(v ->{
            if(isValidSignInDetails()){
                signIn();
            }
        });

   }

    private void LogAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent Log = new Intent(this, MainActivity.class);
            startActivity(Log);
        }
    }

   private void signIn() {
        loading(true);


       String user = binding.inputEmail.getText().toString();

       String pass = binding.inputPassword.getText().toString();

       if(!user.equals("") && !pass.equals("")){
           mAuth.signInWithEmailAndPassword(user, pass)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {

                               LogAuthentication();

                           } else {

                           }
                       }
                   });
       }
       else{

           Toast.makeText(this, "Los campos estan vacios.",
                   Toast.LENGTH_SHORT).show();
       }
   }

   private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
   }

   private void showToast(String message){
       Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
   }

   private Boolean isValidSignInDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Introduce tu email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Introduce un email valido");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Introduce tu contraseña");
            return false;
        }else{
           return true;
        }
   }
}