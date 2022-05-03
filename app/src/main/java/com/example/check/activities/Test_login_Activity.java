package com.example.check.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.check.Principal.MainActivity;
import com.example.check.Utilities.Constantes;
import com.example.check.Utilities.PreferenceManager;
import com.example.check.databinding.ActivityTestLoginBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Test_login_Activity extends AppCompatActivity {
    private ActivityTestLoginBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivityTestLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

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

   private void signIn() {
        loading(true);
       FirebaseFirestore database = FirebaseFirestore.getInstance();
       database.collection(Constantes.KEY_COLLECTION_USERS)
               .whereEqualTo(Constantes.KEY_EMAIL,binding.inputEmail.getText().toString())
               .whereEqualTo(Constantes.KEY_PASSWORD, binding.inputPassword.getText().toString())
               .get()
               .addOnCompleteListener(task -> {
                   if(task.isSuccessful() && task.getResult() !=null
                            && task.getResult().getDocuments().size() > 0){
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManager.putBoolean(Constantes.KEY_IS_SIGNED_IN,true);
                       preferenceManager.putString(Constantes.KEY_USER_ID,documentSnapshot.getId());
                       preferenceManager.putString(Constantes.KEY_NAME, documentSnapshot.getString(Constantes.KEY_NAME));
                       preferenceManager.putString(Constantes.KEY_IMAGE, documentSnapshot.getString(Constantes.KEY_IMAGE));
                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
                   }else{
                       loading(false);
                       showToast("No es posible iniciar sesion");
                   }
               });
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