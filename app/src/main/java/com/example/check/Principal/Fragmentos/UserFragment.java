package com.example.check.Principal.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.check.Entidad.TravelLocation;
import com.example.check.Entidad.User;
import com.example.check.Gestion.GestionItinerario;
import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.example.check.Principal.activities.Test_login_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FirebaseAuth mAuth;
    private User user;
    private ViewPager viewPager;


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

        View view = inflater.inflate(R.layout.fragment_user, container, false);


        user = new User();

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        //HashMap<String,Object> user = new HashMap<>();



        List<TravelLocation> travelLocations = new ArrayList<>();
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

                        TravelLocation travelLocation = ds.getValue(TravelLocation.class);
                        System.out.println(travelLocation.toString());
                        travelLocations.add(travelLocation);
                    }
                    System.out.println("AQUI???? "+String.valueOf(task.getResult().getValue()));
                }
            }
        });
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        final String[] image = new String[1];
        DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(mAuth.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                System.out.println(user.getName());
                TextView textView = view.findViewById(R.id.nombreUsuario);
                textView.setText(user.getName());
                TextView textView2 = view.findViewById(R.id.expedicion);
                textView2.setText(user.getExpedicion());
                for (TravelLocation t : travelLocations){
                    if(t.Nombre.equals(user.getExpedicion())){

                        ImageView imageView = view.findViewById(R.id.banner);
                        Picasso.get().load(t.imagen).into(imageView);

                        GestionItinerario itinerario = new GestionItinerario();
                        itinerario.updateView(viewPager,getContext(), t.imagen);
                        break;

                    }
                }
            }
        });

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(5);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@android.support.annotation.NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.90f + r * 0.04f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);





        return view;
    }


    private void signOut(){
        mAuth.signOut();
        goLogin();

    }

    private void goLogin() {

        Intent Log = new Intent(getActivity(), Test_login_Activity.class);
        startActivity(Log);

    }


}
