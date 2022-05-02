package com.example.check.Principal;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.check.Entidad.Imagedb;
import com.example.check.Principal.Fragmentos.ChatFragment;
import com.example.check.Entidad.Connection;
import com.example.check.Gestion.GestionTravelLocation;
import com.example.check.Principal.Fragmentos.HomeFragment;
import com.example.check.R;
import com.example.check.Principal.Fragmentos.SocialFragment;
import com.example.check.Principal.Fragmentos.UserFragment;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.Intent;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GestionTravelLocation gesExp;
    private FirebaseStorage storage;
    private StorageReference reference;
    private Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        gesExp = new GestionTravelLocation();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        connection = new Connection();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                    replace(new ChatFragment());
                    break;
                case 3:
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


        Intent Log = new Intent(this, LoginActivity.class);
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

            textViewexp.setText(textTitle.getText());
            textViewlugar.setText(textLocation.getText());
            Picasso.get().load((String) kbvImage.getTag()).into(kenBurnsView);
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

    public void fromFiles() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void fromCamera() {

        ImagePicker.with(this)
                .cameraOnly()
                .start(REQUEST_IMAGE_CAPTURE);

    }

    public static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Code: "+ requestCode);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode){
            case 1:

                Uri currentUri = data.getData();
                System.out.println(currentUri);
                System.out.println(requestCode);

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

        if (filePath != null) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
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



                                    Imagedb imdb = new Imagedb();
                                    imdb.setDate("wait");
                                    imdb.setUser(mAuth.getUid());
                                    imdb.setUrl(uriImage.toString());

                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = db.getReference("Images");
                                    databaseReference.push().setValue(imdb);

                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MainActivity.this,
                                                    "Imagen Actualizada!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
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
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }







}