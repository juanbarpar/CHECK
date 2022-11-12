package com.example.check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.check.controlador.fragmento.FragmentoGaleria;
import com.example.check.controlador.fragmento.FragmentoInicio;
import com.example.check.controlador.fragmento.FragmentoPerfil;
import com.example.check.controlador.fragmento.Fragmento_Login;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class LogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) SmoothBottomBar smoothBottomBar = findViewById(R.id.log_sbar);
        remplazar(new Fragmento_Login());

        smoothBottomBar.setOnItemSelected((Function1<? super Integer, Unit>) o -> {
            switch (o) {
                case 0:
                    remplazar(new Fragmento_Login());
                    break;
                case 1:
                    remplazar(new FragmentoGaleria());
                    break;

            }
            return null;
        });
    }

    public void remplazar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }
}