package com.example.check.Principal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.check.Entidad.Imagedb;
import com.example.check.Entidad.User;
import com.example.check.Gestion.GestionImage;
import com.example.check.Gestion.GestionOfflineImage;
import com.example.check.Gestion.adaptadores.ImageAdapter;
import com.example.check.Entidad.Connection;
import com.example.check.Principal.Fragmentos.HomeFragment;
import com.example.check.R;
import com.example.check.Principal.Fragmentos.SocialFragment;
import com.example.check.Principal.Fragmentos.UserFragment;
import com.example.check.Utilities.Constantes;
import com.example.check.Principal.activities.Test_login_Activity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference reference;
    private Connection connection;
    private GestionOfflineImage offlineImage;
    private File DIR_SAVE_IMAGES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DIR_SAVE_IMAGES = new File(getFilesDir(), "ImagePicker");
        offlineImage = new GestionOfflineImage(DIR_SAVE_IMAGES);
        System.out.println("File: "+DIR_SAVE_IMAGES.getPath());

        connection = new Connection(this);


        SmoothBottomBar smoothBottomBar = findViewById(R.id.bar_nav);
        System.out.println(connection.isConnected() + "------------");
        replace(new HomeFragment());


        smoothBottomBar.setOnItemSelected((Function1<? super Integer, kotlin.Unit>) o -> {

            System.out.println(o);

            switch (o) {
                case 0:
                    replace(new HomeFragment());
                    break;
                case 1:
                    replace(new SocialFragment());
                    break;
                case 2:
                    replace(new UserFragment());
                    break;
            }
            return null;
        });


    }

    public void replace(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }

    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            goLogin();
        }

    }
    private void goLogin() {

        Intent Log = new Intent(this, Test_login_Activity.class);
        startActivity(Log);

    }
    private void goWeb(String inURL) {

        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        startActivity(browse);

    }
    public void logout(View view) {
        mAuth.signOut();
        goLogin();
    }

    public void getView(View view) {
        if (!connection.isConnected()){

            View box = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.offline_box, findViewById(R.id.dialog_box_3)
            );

            Dialog dialog = new Dialog(this);

            box.findViewById(R.id.azul).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(box);
            dialog.show();

        }
        else {
            KenBurnsView kbvImage;
            TextView textTitle, textLocation, textStartRating;
            kbvImage = view.findViewById(R.id.kbvLocation);
            textTitle = view.findViewById(R.id.textTitle);
            textStartRating = view.findViewById(R.id.textStartRating);
            textLocation = view.findViewById(R.id.textLocation);

            System.out.println(textTitle.getText());
            System.out.println(textStartRating.getText());
            System.out.println(textLocation.getText());
            System.out.println(kbvImage.getTag());

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    MainActivity.this, R.style.bt_sheet_dialog

            );


            View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.bt_sheet, (LinearLayout) findViewById(R.id.bt_sheet_container)
            );
            bottonSheetView.findViewById(R.id.button_reserva).setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    goWeb((String) textTitle.getTag());

                    bottomSheetDialog.dismiss();
                }
            });

            KenBurnsView kenBurnsView = bottonSheetView.findViewById(R.id.imageRes);
            TextView textViewexp = bottonSheetView.findViewById(R.id.expedicion);
            TextView textViewlugar = bottonSheetView.findViewById(R.id.lugar);

            bottonSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewe) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Haz parte de nuestras " +
                            "expediciones programadas a distintos lugares de Colombia. " +
                            "Una diversidad de destinos, itinerarios llenos de momentos" +
                            " auténticos y experiencias de vida que recordarás para siempre " + textTitle.getTag());
                    try {
                        MainActivity.this.startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        System.out.println("Whatsapp have not been installed.");
                    }
                }
            });



            bottonSheetView.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewe) {
                    System.out.println("Set: expe");
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    database.collection(Constantes.KEY_COLLECTION_USERS).document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            bottomSheetDialog.dismiss();
                            User user = documentSnapshot.toObject(User.class);
                            System.out.println("tag: " + kbvImage.getTag().toString());
                            user.setExpedicion(kbvImage.getTag().toString());
                            database.collection(Constantes.KEY_COLLECTION_USERS).document(mAuth.getUid()).set(user);
                        }
                    });
                }
            });



            textViewexp.setText(textTitle.getText());
            textViewlugar.setText(textLocation.getText());
            Picasso.get().load(textLocation.getTag().toString()).into(kenBurnsView);
            bottomSheetDialog.setContentView(bottonSheetView);
            bottomSheetDialog.show();
        }
    }




    public void showImageOp(View view) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.bt_sheet_dialog);

        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bt_image, (LinearLayout) findViewById(R.id.bt_image_container)
        );

        bottonSheetView.findViewById(R.id.cameraOp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCamera();
                bottomSheetDialog.dismiss();
            }
        });
        bottonSheetView.findViewById(R.id.filesOp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromFiles();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();

    }

    public void showImage(View view){

        View box = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.image_layout, findViewById(R.id.image_container)
        );

        Dialog dialog = new Dialog(this);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RoundedImageView imageView = box.findViewById(R.id.imageFS);
        Glide.with(this)
                .load(view.getTag())
                .into(imageView);
        imageView.setBackground(null);

        dialog.setContentView(box);
        dialog.show();


        CharSequence text = view.getTag().toString();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

    }

    public void fromFiles() {
        ImagePicker.with(this).saveDir(DIR_SAVE_IMAGES)
                .galleryOnly()
                .crop()
                .start(REQUEST_IMAGE_CAPTURE);

        /*
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

         */
    }

    private void fromCamera() {

        ImagePicker.with(this).saveDir(DIR_SAVE_IMAGES)
                .cameraOnly()
                .start(REQUEST_IMAGE_CAPTURE);

    }


    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode){
            case 1:

                Uri currentUri = data.getData();
                System.out.println("Saved Uri: " + currentUri);
                View box = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.dialog_box, findViewById(R.id.dialog_box_con)
                );

                ImageView imageView = box.findViewById(R.id.selectedimage);
                Picasso.get().load(currentUri).into(imageView);
                Dialog dialog = new Dialog(this);


                box.findViewById(R.id.azul).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        uploadImage(currentUri);
                        dialog.dismiss();
                    }
                });
                box.findViewById(R.id.rojo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(box);
                dialog.show();
        }

    }

    private void uploadImage(Uri filePath) {


        if(!connection.isConnected()){


            Dialog dialog = new Dialog(this);
            View box = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.offline_image_box, findViewById(R.id.dialog_box_image_offline)
            );
            box.findViewById(R.id.azul).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });


            dialog.setContentView(box);
            dialog.show();



            Imagedb imdb2 = new Imagedb("wait",mAuth.getUid(),filePath.toString(),"placeholderw");
            offlineImage.saveImage(imdb2);


            return;

        }

        if (filePath != null) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo tu foto...");
            progressDialog.show();

            StorageReference ref
                    = reference
                    .child(
                            "images/"
                                    + mAuth.getUid() + "/" + Math.random());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful());
                                    Uri uriImage = uriTask.getResult();
                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(mAuth.getUid());
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user = documentSnapshot.toObject(User.class);
                                            System.out.println("Exp: "+user.getExpedicion());

                                            Imagedb imdb = new Imagedb(Timestamp.now().toString(),user.getName(),uriImage.toString(),user.getExpedicion());
                                            System.out.println("Exp: "+imdb.getExpedicion());

                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference = db.getReference("Images");
                                            databaseReference.push().setValue(imdb);

                                            offlineImage.eliminar(imdb.getUrl());

                                            progressDialog.dismiss();
                                            Toast
                                                    .makeText(MainActivity.this,
                                                            "Imagen Actualizada!!",
                                                            Toast.LENGTH_SHORT)
                                                    .show();



                                        }
                                    });




                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MainActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Subiendo... ("
                                                    + (int) progress + "%)");
                                }
                            });
        }
    }

    public void filter(View view){

        RecyclerView recyclerView = findViewById(R.id.view_photo);
        List<Imagedb> imagedbs = new ArrayList<>();
        imagedbs.addAll(GestionImage.imagedbs);

        System.out.println(GestionImage.imagedbs.get(1).getDate());
        imagedbs.clear();

        String tag = view.getTag().toString();
        if(tag.equals("Todas las expediciones")){
            imagedbs.addAll(GestionImage.imagedbs);

        }
        else {
            for(Imagedb item: GestionImage.imagedbs){
                if(item.getExpedicion().equals(tag)){
                    imagedbs.add(item);
                }
            }

        }

        recyclerView.setAdapter(new ImageAdapter(imagedbs, this));
        recyclerView.getAdapter().notifyDataSetChanged();

    }


}
