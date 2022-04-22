package com.example.check.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.check.databinding.ActivityTestSignUpBinding;

public class Test_Sign_Up_Activity extends AppCompatActivity {

    private String encodedImage;
    private ActivityTestSignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(binding.getRoot());
        setLiteners();
    }
    private void setLiteners(){
        binding.buttonSignUp.setOnClickListener(view -> {
            if(isValidSignUpDetails()) {
                signUp();
            }
        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }
    private  void signUp(){

    }
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
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
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
}