package com.example.check;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.check.controlador.fragmento.Fragmento_Login;
import com.example.check.controlador.fragmento.RegistroFragmento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class LogActivity extends AppCompatActivity {

    private FirebaseAuth tokenAutenticacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        tokenAutenticacion = FirebaseAuth.getInstance();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) SmoothBottomBar smoothBottomBar = findViewById(R.id.log_sbar);
        remplazar(new Fragmento_Login());

        smoothBottomBar.setOnItemSelected((Function1<? super Integer, Unit>) o -> {
            switch (o) {
                case 0:
                    remplazar(new Fragmento_Login());
                    break;
                case 1:
                    remplazar(new RegistroFragmento());
                    break;

            }
            return null;
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        autenticar();
    }

    public void remplazar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }

    public void ingresar(View view) {

        ExpandableHintText editTextUser = findViewById(R.id.user);
        ExpandableHintText editTextPass = findViewById(R.id.pass);
        String user = editTextUser.getText().toString();
        String pass = editTextPass.getText().toString();

        if (!user.equals("") && !pass.equals("")) {
            tokenAutenticacion.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    autenticar();
                }
            });
        } else {
            Toast.makeText(this, "Los campos estan vacios.", Toast.LENGTH_SHORT).show();
        }
    }

    private void autenticar() {
        FirebaseUser currentUser = tokenAutenticacion.getCurrentUser();
        if (currentUser != null) {
            Intent Log = new Intent(this, ActividadPrincipal.class);
            startActivity(Log);
        }
    }
}