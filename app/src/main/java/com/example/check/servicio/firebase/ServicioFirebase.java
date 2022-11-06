package com.example.check.servicio.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.check.ActividadPrincipal;
import com.example.check.repositorio.dao.ImagenLocalDao;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.utilidades.Constantes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ServicioFirebase {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference reference;

    public ServicioFirebase() {

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

    }


    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public StorageReference getReference() {
        return reference;
    }

    public void setReference(StorageReference reference) {
        this.reference = reference;
    }

    public void subirFoto(Uri filePath, Context context, ImagenLocalDao imagenLocalDao){
        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Subiendo tu foto...");
        progressDialog.show();

        StorageReference ref
                = getReference()
                .child(
                        "images/"
                                + getmAuth().getUid() + "/" + Math.random());

        ref.putFile(filePath)
                .addOnSuccessListener(
                        taskSnapshot -> {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            //espera mientras se carga la imagen
                            Uri uriImage = uriTask.getResult();
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(getmAuth().getUid()));
                            docRef.get().addOnSuccessListener(documentSnapshot -> {
                                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                                Imagedb imdb = new Imagedb(Timestamp.now().toString(), usuario.getNombre(),uriImage.toString(), usuario.getExpedicion());
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = db.getReference("Images");
                                databaseReference.push().setValue(imdb);

                                imagenLocalDao.eliminar(imdb.getUrl());

                                progressDialog.dismiss();
                                Toast
                                        .makeText(context,
                                                "Imagen Actualizada!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            });
                        })

                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast
                            .makeText(context,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                })
                .addOnProgressListener(
                        taskSnapshot -> {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Subiendo... ("
                                            + (int) progress + "%)");
                        });
    }
}
