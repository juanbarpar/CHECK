package com.example.check.controlador.fragmento;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.check.repositorio.entidad.Connection;
import com.example.check.repositorio.entidad.DestinosViaje;
import com.example.check.controlador.adaptador.TravelLocationAdapter;
import com.example.check.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoInicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoInicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoInicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoInicio newInstance(String param1, String param2) {
        FragmentoInicio fragment = new FragmentoInicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        System.out.println("fallo");
                    }
                    else {
                        for (DataSnapshot ds : task.getResult().getChildren()) {

                            DestinosViaje destinosViaje = ds.getValue(DestinosViaje.class);
                            destinosViajes.add(destinosViaje);
                        }
                        locationViewPager.getAdapter().notifyDataSetChanged();

                    }
                }
            });

            locationViewPager.setAdapter(new TravelLocationAdapter(destinosViajes));

            locationViewPager.getAdapter().notifyDataSetChanged();

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
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.90f + r * 0.04f);
            }
        });
        locationViewPager.setPageTransformer(compositePageTransformer);

        return view;
    }

}