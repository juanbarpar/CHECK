package com.example.check.controlador.fragmento;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.check.repositorio.entidad.Connection;
import com.example.check.repositorio.entidad.DestinosViaje;
import com.example.check.controlador.adaptador.TravelLocationAdapter;
import com.example.check.R;
import com.example.check.servicio.utilidades.excepciones.ExcepcionTareaFB;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentoInicio extends Fragment {



    public FragmentoInicio() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        Connection conexion = new Connection(getActivity());

        ViewPager2 locationViewPager = view.findViewById(R.id.locationViewPager);

        if(conexion.isConnected()){

            List<DestinosViaje> destinosViajes = new ArrayList<>();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = db.getReference("Expediciones");
            databaseReference.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    throw new ExcepcionTareaFB(Objects.requireNonNull(task.getException()).getMessage());
                }
                else {
                    for (DataSnapshot ds : task.getResult().getChildren()) {

                        DestinosViaje destinosViaje = ds.getValue(DestinosViaje.class);
                        destinosViajes.add(destinosViaje);
                    }
                    Objects.requireNonNull(locationViewPager.getAdapter()).notifyDataSetChanged();

                }
            });

            locationViewPager.setAdapter(new TravelLocationAdapter(destinosViajes));

            Objects.requireNonNull(locationViewPager.getAdapter()).notifyDataSetChanged();

        }else {

            List<DestinosViaje> destinosViajes = new ArrayList<>();
            DestinosViaje destinosViaje = new DestinosViaje();
            destinosViaje.url = "";
            destinosViaje.ubicación="No logramos conectar con el servidor";
            destinosViaje.Nombre="Offline";
            destinosViaje.fecha = "";
            Uri uri = Uri.parse("android.resource://com.example.check/" + R.drawable.signal);
            destinosViaje.imagen=uri.toString();
            destinosViajes.add(destinosViaje);
            locationViewPager.setAdapter(new TravelLocationAdapter(destinosViajes));

        }

        locationViewPager.setClipToPadding(false);
        locationViewPager.setClipChildren(false);
        locationViewPager.setOffscreenPageLimit(3);
        locationViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.90f + r * 0.04f);
        });
        locationViewPager.setPageTransformer(compositePageTransformer);

        return view;
    }

}