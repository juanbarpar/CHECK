package com.example.check.repositorio.dao;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.utilidades.Constantes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ImagenLocalDao {
    private String ruta;
    private FirebaseStorage instanciaAlmacenamiento;
    private StorageReference referenciaAlmacenamiento;

    public ImagenLocalDao(File fi) {
        instanciaAlmacenamiento = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = instanciaAlmacenamiento.getReference();

        this.ruta = fi.getPath() + "/LocalImages.txt";
        this.verificarArchivo();
    }

    private void verificarArchivo() {
        try {
            File fi = new File(this.ruta);
            if (!fi.exists()) {
                fi.createNewFile();
            }
        } catch (IOException excep) {
            System.out.println("Error en la ruta");
        }
    }

    public void saveImage(Imagedb im) {
        this.saveOffline(im);
    }

    public void uploadOnline() {
        ArrayList<Imagedb> imagedbs = getTodos();

        for (Imagedb imagedb : imagedbs) {
            Uri filePath = Uri.parse(imagedb.getUrl());
            System.out.println("File off: " + filePath);

            if (filePath != null) {
                StorageReference ref = referenciaAlmacenamiento.child("images/" + imagedb.getUser() + "/" + Math.random());

                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                Uri uriImage = uriTask.getResult();

                                System.out.println("onSuccess");

                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(imagedb.getUser());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Usuario user = documentSnapshot.toObject(Usuario.class);
                                        System.out.println("name: " + user.getNombre());

                                        Imagedb imdb = new Imagedb(Timestamp.now().toString(), user.getNombre(), uriImage.toString(), user.getExpedicion());

                                        eliminar(imagedb.getUrl());

                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = db.getReference("Images");
                                        databaseReference.push().setValue(imdb);
                                    }
                                });
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error upl");
                            }
                        });
            }
        }
    }

    private void saveOffline(Imagedb im) {
        try {
            File fi = new File(this.ruta);
            FileWriter fr = new FileWriter(fi, true);
            PrintWriter pw = new PrintWriter(fr);
            pw.println(im);
            pw.close();
        } catch (IOException f) {
            System.out.println("Error");
        }
    }

    public ArrayList<Imagedb> getTodos() {
        FileReader file;
        BufferedReader br;
        String registro;

        Imagedb im = null;

        ArrayList<Imagedb> ims = new ArrayList();
        System.out.println(this.ruta);
        try {
            file = new FileReader(this.ruta);
            br = new BufferedReader(file);
            while ((registro = br.readLine()) != null) {
                String[] campos = registro.split(",");
                im = new Imagedb(campos[0], campos[1], campos[2], campos[3]);
                ims.add(im);
            }
        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Problemas con la ruta...");
        }
        return ims;
    }

    public void eliminar(String code) {
        File fi = new File(code);
        fi.delete();

        ArrayList<Imagedb> rooms = this.getTodos();
        for (Imagedb room : rooms) {
            if (room.getUrl().equals(code)) {
                rooms.remove(room);
                System.out.println("Eliminando: " + code);
                this.remplazarArchivo(rooms);
                break;
            }
        }
    }

    private void remplazarArchivo(ArrayList<Imagedb> rooms) {
        try {
            File fi = new File(this.ruta);
            FileWriter fr = new FileWriter(fi, false);
            PrintWriter pw = new PrintWriter(fr);

            for (Imagedb room : rooms) {
                pw.println(room);
            }
            pw.close();
        } catch (IOException f) {
            System.out.println("Error al remp");
        }
    }
}