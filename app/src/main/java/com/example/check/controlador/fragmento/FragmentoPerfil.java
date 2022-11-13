package com.example.check.controlador.fragmento;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.check.ActividadInicio;
import com.example.check.repositorio.entidad.Usuario;

import com.example.check.repositorio.entidad.DestinosViaje;

import com.example.check.repositorio.dao.ItinerarioDao;
import com.example.check.R;
import com.example.check.servicio.firebase.ServicioFirebase;
import com.example.check.servicio.utilidades.Constantes;
import com.example.check.servicio.utilidades.excepciones.ExcepcionTareaFB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentoPerfil extends Fragment {

    public FragmentoPerfil() {
        // Required empty public constructor
    }


    private Usuario usuario;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        usuario = new Usuario();
        ServicioFirebase servicioFirebase = new ServicioFirebase();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        view.findViewById(R.id.logout).setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent Log = new Intent(getActivity(), ActividadInicio.class);
            startActivity(Log);
        });

        List<DestinosViaje> destinosViajes = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference("Expediciones");
        databaseReference.get().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                throw new ExcepcionTareaFB(Objects.requireNonNull(task.getException()).getMessage());
            } else {
                for (DataSnapshot ds : task.getResult().getChildren()) {

                    DestinosViaje destinosViaje = ds.getValue(DestinosViaje.class);
                    destinosViajes.add(destinosViaje);

                }
            }
        });
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);


        DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(servicioFirebase.getTokenAutenticacion().getUid()));
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            usuario = documentSnapshot.toObject(Usuario.class);
            TextView textView = view.findViewById(R.id.nombreUsuario);
            textView.setText(usuario.getNombre());
            TextView textView2 = view.findViewById(R.id.expedicion);

            textView2.setText(usuario.getExpedicion());
            for (DestinosViaje t : destinosViajes) {
                if (t.Nombre.equals(usuario.getExpedicion())) {
                    ImageView imageView = view.findViewById(R.id.imagenPerfil);
                    Picasso.get().load(t.imagen).into(imageView);
                    ItinerarioDao itinerario = new ItinerarioDao();
                    itinerario.updateView(viewPager, getContext(), t.imagen);
                    break;

                }
            }
        });

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(5);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.90f + r * 0.04f);
        });
        viewPager.setPageTransformer(compositePageTransformer);
        return view;
    }


}
