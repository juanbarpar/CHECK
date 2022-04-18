package com.example.check.Principal.Fragmentos;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.check.Entidad.Image;
import com.example.check.Gestion.ImageAdapter;
import com.example.check.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

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
    List<Image> imageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_social, container, false);

        recyclerView = view.findViewById(R.id.view_photo);

        StorageReference listRef = storage.getReference().child("images/"
                + mAuth.getUid());
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {
                    StorageReference storageRef = storage.getReference();
                    storageRef.child(item.getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            if(!imageList.contains(new Image(uri.toString()))){
                                System.out.println(uri);
                                imageList.add(new Image(uri.toString()));
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });

        System.out.println("Seg");

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        imageList.add(new Image("https://images.unsplash.com/photo-1610353087277-cb32686df0d0?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        imageList.add(new Image("https://images.unsplash.com/photo-1649740619716-2e18d4af6052?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        imageList.add(new Image("https://images.unsplash.com/photo-1649713462761-ac493a95381a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=388&q=80"));
        imageList.add(new Image("https://checknewplaces.com/wp-content/uploads/2022/03/1-1.jpg"));
        imageList.add(new Image("https://checknewplaces.com/wp-content/uploads/2022/03/1-2.jpg"));
        imageList.add(new Image("https://checknewplaces.com/wp-content/uploads/2022/03/1-3.jpg"));
        imageList.add(new Image("https://images.unsplash.com/photo-1649720247942-5be3ef21a86b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        imageList.add(new Image("https://images.unsplash.com/photo-1649649853880-05d01c3dc093?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));

        recyclerView.setAdapter(new ImageAdapter(imageList));

        return view;
    }

    public List<Image> getAllCloudImage(){
        List<Image> imageList = new ArrayList<>();

        return imageList;
    }
    private void actualizar(){

        System.out.println("Act");
        recyclerView.setAdapter(new ImageAdapter(imageList));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actualizar();
    }
}