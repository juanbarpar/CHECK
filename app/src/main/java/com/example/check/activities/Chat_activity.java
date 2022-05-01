package com.example.check.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.example.check.Utilities.Constantes;
import com.example.check.Utilities.PreferenceManager;
import com.example.check.databinding.FragmentChatBinding;
import com.example.check.databinding.FragmentUserBinding;

public class Chat_activity extends AppCompatActivity {
    private FragmentUserBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding= FragmentUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();

    }
    private void loadUserDetails(){
        binding.nombreUsuario.setText(preferenceManager.getString(Constantes.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constantes.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        binding.imagenPerfil.setImageBitmap(bitmap);
    }
}
