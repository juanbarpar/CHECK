package com.example.check.Gestion;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.check.Entidad.Itineraries;
import com.example.check.R;
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


public class GestionItinerario {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    public void updatelocationViewPager(ViewPager2 locationViewPager) {
        List<Itineraries> itinerarios = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String expedition = documentSnapshot.getString("Expedicion");

                    mDatabase.child("Itinerarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                if(ds.child("Nombre").getValue().toString().equals(expedition)){
                                    for (int i = 1; i < 8; i++) {
                                        if(ds.child("Día "+ i).exists()){
                                            Itineraries itinerario = new Itineraries();
                                            String eventos = "";
                                            itinerario.setDia("Día "+i);
                                            itinerario.setFecha(ds.child("Día " + i).child(String.valueOf(0)).getValue().toString());
                                            for (DataSnapshot ds2: ds.child("Día " + i).getChildren()) {
                                                if(!ds2.getKey().equals("0")){
                                                    eventos += "•"+ ds2.getValue().toString() +"\n";
                                                }

                                            }
                                            itinerario.setEventos(eventos);
                                            System.out.println(itinerario.getDia()+", "
                                                    +itinerario.getFecha()+ ", "
                                                    +itinerario.getEventos());


                                            itinerarios.add(itinerario);
                                        }
                                    }
                                    locationViewPager.setAdapter(new ItineraryAdapter(itinerarios));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}


