package com.example.check.Principal.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.check.Entidad.Connection;
import com.example.check.Entidad.Imagedb;
import com.example.check.Gestion.AlbumAdapter;
import com.example.check.Gestion.GestionExpediciones;
import com.example.check.Gestion.GestionImage;
import com.example.check.Gestion.ImageAdapter;
import com.example.check.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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
    public void onStart() {
        super.onStart();

    }

    private FirebaseStorage storage;
    private StorageReference reference;
    private FirebaseAuth mAuth;
    private View view;
    private RecyclerView recyclerView;
    List<Imagedb> imageList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        view =  inflater.inflate(R.layout.fragment_social, container, false);




        recyclerView = view.findViewById(R.id.view_photo);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        RecyclerView rvAlbum = view.findViewById(R.id.view_album);
        rvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        try {
            rvAlbum.setAdapter(new AlbumAdapter(new GestionExpediciones().getAllAlbum(new Connection().execute("").get())));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StorageReference listRef = storage.getReference().child("images/"
                + mAuth.getUid());

        recyclerView.setAdapter(new ImageAdapter(GestionImage.imagedbs,getActivity()));

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference("Images");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Imagedb imagedb = snapshot.getValue(Imagedb.class);
                GestionImage.addImage(imagedb);
                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }



}