package com.example.check.Gestion;

import androidx.annotation.NonNull;

import com.example.check.Entidad.Itineraries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GestionItinerario {

    private DatabaseReference mDatabase;


    public List<Itineraries> getItinerario(String expedition) {
        List<Itineraries> itinerarios = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();


            mDatabase.child("Itinerarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        if(ds.child("Nombre").getValue().toString().equals(expedition)){
                            for (int i = 1; i < 8; i++) {
                                if(ds.child("Día"+ i).exists()){
                                    Itineraries itinerario = new Itineraries();
                                    String eventos = null;
                                    itinerario.setDia("Día"+(i+1));
                                    itinerario.setFecha(ds.child("Día" + i).child(String.valueOf(0)).getValue().toString());
                                    for (DataSnapshot ds2: ds.child("Día" + i).getChildren()) {
                                        if(!ds2.getChildren().equals("0")){
                                            eventos += "•"+ ds2.getValue().toString() +"\n";
                                        }

                                    }
                                    itinerario.setEventos(eventos);

                                    itinerarios.add(itinerario);
                                }


                            }



                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        return itinerarios;
    }
}


