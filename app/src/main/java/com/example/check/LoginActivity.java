package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

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
                                Toast.makeText(LoginActivity.this, "Contraseña o correo incorrecto",
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
        p.setOn
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
    public void Register(View View) {
        Button Iniciar_sesion = findViewById(R.id.button3);
        Button Registrarse = findViewById(R.id.button4);
        TextView Requisitos = findViewById(R.id.Req_contra);
        Button Crear_Cuenta = findViewById(R.id.button2);
        TextView Textview2 = findViewById(R.id.textView2);
        Crear_Cuenta.setText("Crear Cuenta");
        Crear_Cuenta.setOnClickListener(this::toSign);
        ImageView Logo_Lugar = findViewById(R.id.ImageLogo);
        Requisitos.setText("-Números y Letras\n-Mínimo 8 caracteres");
        Textview2.setText("La Contraseña debe incluir lo siguiente:");
        Logo_Lugar.setBackgroundResource(R.drawable.logolugar2);
        Registrarse.setBackgroundColor(Color.parseColor("#055583"));
        Iniciar_sesion.setBackgroundColor(Color.parseColor("#033F61"));
        Iniciar_sesion.setTextColor(Color.parseColor("#A5A4A4"));
        Registrarse.setTextColor(Color.WHITE);
    }
    public void Loginbutton(View View) {
        Button Iniciar_sesion = findViewById(R.id.button3);
        Button Registrarse = findViewById(R.id.button4);
        Button Crear_Cuenta = findViewById(R.id.button2);
        TextView Requisitos = findViewById(R.id.Req_contra);
        TextView Textview2 = findViewById(R.id.textView2);
        ConstraintLayout Fondo = findViewById(R.id.Fondo);
        Crear_Cuenta.setOnClickListener(this::toLog);
        ImageView Logo_Lugar = findViewById(R.id.ImageLogo);
        Textview2.setText("");
        Logo_Lugar.setBackgroundResource(R.drawable.logolugar);
        Iniciar_sesion.setBackgroundColor(Color.parseColor("#055583"));
        Requisitos.setText("");
        Registrarse.setBackgroundColor(Color.parseColor("#033F61"));
        Registrarse.setTextColor(Color.parseColor("#A5A4A4"));
        Iniciar_sesion.setTextColor(Color.WHITE);
        Crear_Cuenta.setText("Entrar");
    }


}