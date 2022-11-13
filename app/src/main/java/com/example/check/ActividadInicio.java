package com.example.check;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.check.controlador.fragmento.Fragmento_Login;
import com.example.check.controlador.fragmento.RegistroFragmento;
import com.example.check.repositorio.dao.AutenticacionDao;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.firebase.ServicioFirebase;
import com.example.check.servicio.utilidades.dialogo.DialogoNotificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class ActividadInicio extends AppCompatActivity {

    private FirebaseAuth tokenAutenticacion;
    private ServicioFirebase servicioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        tokenAutenticacion = FirebaseAuth.getInstance();
        servicioFirebase = new ServicioFirebase();

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
                else {
                    DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
                    View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
                    dialogoNotificacion.dispararDialogo(box,"Error de credenciales","Revise su correo y contraseña",R.raw.fail);
                }
            });
        } else {
            DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            dialogoNotificacion.dispararDialogo(box,"Error de credenciales","Revise su correo y contraseña",R.raw.fail);
        }
    }

    private void autenticar() {
        FirebaseUser currentUser = tokenAutenticacion.getCurrentUser();
        if (currentUser != null) {
            Intent Log = new Intent(this, ActividadPrincipal.class);
            startActivity(Log);
        }
    }

    public void registrar(View view){

        ExpandableHintText editTextUser = findViewById(R.id.regNombre);
        ExpandableHintText editTextMail = findViewById(R.id.regCorreo);
        ExpandableHintText editTextPass = findViewById(R.id.regPass);
        ExpandableHintText editTextPassCon = findViewById(R.id.regPassConf);

        String user = editTextUser.getText();
        String mail = editTextMail.getText();
        String pass = editTextPass.getText();
        String passcon = editTextPassCon.getText();

        System.out.println(user+"------------");
        System.out.println(pass+"------------");

        if(!user.equals("") && !pass.equals("") && new AutenticacionDao().esValido(user,mail,pass,passcon)){
            tokenAutenticacion.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                autenticar();
                                Usuario user = new Usuario();
                                user.setNombre(editTextUser.getText());
                                user.setCorreo(editTextMail.getText());
                                user.setExpedicion("NA");
                                user.setImagen("");
                                servicioFirebase.registrarUsuario(user, task);
                            }else {
                                DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
                                View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
                                dialogoNotificacion.dispararDialogo(box,"Error de credenciales","Revise su correo y contraseña",R.raw.fail);
                            }
                        }
                    });

        }else {
            DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            dialogoNotificacion.dispararDialogo(box,"Error de credenciales","Revise su correo y contraseña",R.raw.fail);
        }
    }
}