package com.example.check.Principal.Fragmentos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.check.Entidad.TravelLocation;
import com.example.check.Entidad.User;
import com.example.check.Principal.LoginActivity;
import com.example.check.R;
import com.example.check.Utilities.Constantes;
import com.example.check.Utilities.PreferenceManager;
import com.example.check.activities.Test_login_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
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
                        RoundedImageView imageView = view.findViewById(R.id.imageUserExp);
                        Glide.with(getActivity())
                                .load(t.imagen)
                                .into(imageView);
                        imageView.setBackground(null);
                    }
                }
            }
        });


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