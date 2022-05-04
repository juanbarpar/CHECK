package com.example.check.Principal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.check.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();

    }



    public void sendData(View view) {

        EditText textName = findViewById(R.id.editTextName);
        String nombre = textName.getText().toString();

        EditText textNick = findViewById(R.id.editTextNick);
        String nick = textNick.getText().toString();

        Spinner spinner = findViewById(R.id.spinner);
        String expedicion = spinner.getSelectedItem().toString();


        Map<String, Object> user = new HashMap<>();
        user.put("Nombre", nombre);
        user.put("Nick", nick);
        user.put("Expedicion", expedicion);

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "Datos Guardados correctamente!", Toast.LENGTH_SHORT).show();
                        toMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Fallo al guardar datos!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void toMain(){
        Intent Log = new Intent(this, MainActivity.class);
        startActivity(Log);

    }

}