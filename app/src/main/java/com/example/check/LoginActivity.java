package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        LogAuthentication();
    }
    private void LogAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent Log = new Intent(this, MainActivity.class);
            startActivity(Log);
        }
    }

    public void toLog(View view){

        EditText u = findViewById(R.id.User);
        String user = u.getText().toString();

        EditText p = findViewById(R.id.Pass);
        String pass = p.getText().toString();

        if(!user.equals("") && !pass.equals("")){
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                LogAuthentication();

                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{

            Toast.makeText(LoginActivity.this, "Los campos estan vacios.",
                    Toast.LENGTH_SHORT).show();

        }


    }

    public void toSign(View view){

        EditText u = findViewById(R.id.User);
        String user = u.getText().toString();

        EditText p = findViewById(R.id.Pass);
        String pass = p.getText().toString();

        if(!user.equals("") && !pass.equals("")){
            mAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                LogAuthentication();

                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{

            Toast.makeText(LoginActivity.this, "Los campos estan vacios.",
                    Toast.LENGTH_SHORT).show();

        }

    }


}