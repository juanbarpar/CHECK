package com.example.check.Principal.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.check.repositorio.entidad.User;
import com.example.check.ActividadPrincipal;
import com.example.check.servicio.utilidades.Constantes;
import com.example.check.databinding.ActivityTestSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Test_Sign_Up_Activity extends AppCompatActivity {

    private String encodedImage;
    private ActivityTestSignUpBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        binding = ActivityTestSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    private void setListeners(){

        binding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

    }

    private void showToast(String message){

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() *previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if(encodedImage==null) {
            showToast("Selecciona una imagen de perfil");
            return false;
        }else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Ingresa el nombre");
            return false;
        }else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Ingresa un email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString().trim()).matches()){
            showToast("Ingresa un email valido");
            return  false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Ingresa una contraseña");
            return false;
        }else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Confirma tu contraseña");
            return false;
        }else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("La contraseña y la confirmacion de contraseña deben ser iguales");
            return false;
        }else{
            return true;
        }

    }

    private void loading(Boolean isLoading){
        if(isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }

    public void Sign(View view){

        String user = binding.inputEmail.getText().toString().trim();
        String pass = binding.inputPassword.getText().toString().trim();

        if(!user.equals("") && !pass.equals("") && isValidSignUpDetails()){
            mAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loading(true);
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                //HashMap<String,Object> user = new HashMap<>();

                                User user = new User();
                                user.setName(binding.inputName.getText().toString());
                                user.setEmail(binding.inputEmail.getText().toString().trim());
                                user.setExpedicion(("En Busca de la Identidad"));
                                user.setImage(encodedImage);


                                database.collection(Constantes.KEY_COLLECTION_USERS).document(task.getResult().getUser().getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                System.out.println(task.getResult().getUser().getUid());
                                                LogAuthentication();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("Error: "+e);
                                            }
                                        });
                            } else {

                            }
                        }
                    })
            .addOnFailureListener(exception -> {
                showToast("El email ya se encuentra registrado");
            });
        }

    }

    private void LogAuthentication() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent Log = new Intent(this, ActividadPrincipal.class);
            startActivity(Log);
        }
    }

}
