package com.example.check.repositorio.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.check.repositorio.entidad.Itinerarios;
import com.example.check.controlador.adaptador.ItineraryAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItinerarioDao {
    private DatabaseReference mDatabase;
    private FirebaseAuth tokenAutenticacion;
    private FirebaseFirestore db;

    public void updateView(ViewPager2 viewPager, Context context, String image) {
        List<Itinerarios> itinerarios = new ArrayList<>();
        tokenAutenticacion = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db.collection("users").document(tokenAutenticacion.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String expedition = documentSnapshot.getString("expedicion");

                    mDatabase.child("Itinerarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                if (ds.child("Nombre").getValue().toString().equals(expedition)) {
                                    for (int i = 1; i < 8; i++) {
                                        if (ds.child("Día " + i).exists()) {
                                            Itinerarios itinerario = new Itinerarios();
                                            String eventos = "";
                                            itinerario.setDia("Día " + i);
                                            itinerario.setFecha(ds.child("Día " + i).child(String.valueOf(0)).getValue().toString());
                                            for (DataSnapshot ds2 : ds.child("Día " + i).getChildren()) {
                                                if (!ds2.getKey().equals("0")) {
                                                    eventos += "✓ " + ds2.getValue().toString() + "\n\n";
                                                }
                                            }
                                            itinerario.setEventos(eventos);
                                            itinerarios.add(itinerario);
                                        }
                                    }
                                }
                            }
                            viewPager.setAdapter(new ItineraryAdapter(context, itinerarios, image));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
        });
    }
}