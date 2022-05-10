package com.example.check.Gestion;

import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.check.Entidad.TravelLocation;
import com.example.check.Entidad.User;
import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class GestionUser {

    private static FirebaseAuth mAuth;
    private static User user;



    public static User getUser(){

        mAuth = FirebaseAuth.getInstance();


        return user;
    }
}
