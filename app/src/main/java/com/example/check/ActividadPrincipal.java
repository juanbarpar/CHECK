package com.example.check;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.repositorio.dao.ImagenDao;
import com.example.check.controlador.adaptador.ImageAdapter;
import com.example.check.repositorio.entidad.Connection;
import com.example.check.controlador.fragmento.FragmentoInicio;
import com.example.check.controlador.fragmento.FragmentoGaleria;
import com.example.check.controlador.fragmento.FragmentoPerfil;
import com.example.check.servicio.firebase.ServicioFirebase;
import com.example.check.servicio.utilidades.Constantes;
import com.example.check.servicio.utilidades.excepciones.ExcepcionConexion;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class ActividadPrincipal extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ServicioFirebase servicioFirebase;
    private Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new Connection(this);

        SmoothBottomBar smoothBottomBar = findViewById(R.id.bar_nav);
        remplazar(new FragmentoInicio());

        ImageView imageTopView = findViewById(R.id.topImg);
        TextView textTopView = findViewById(R.id.topText);

        smoothBottomBar.setOnItemSelected((Function1<? super Integer, kotlin.Unit>) o -> {
            switch (o) {
                case 0:
                    remplazar(new FragmentoInicio());
                    imageTopView.setImageResource(R.drawable.ic_location);
                    textTopView.setText("Nuestras expediciones");
                    break;
                case 1:
                    remplazar(new FragmentoGaleria());
                    imageTopView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    textTopView.setText("Comparte con nosotros");
                    break;
                case 2:
                    remplazar(new FragmentoPerfil());
                    imageTopView.setImageResource(R.drawable.ic_user);
                    textTopView.setText("Busca tu identidad");
                    break;
            }
            return null;
        });
    }


    public void remplazar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();
    }

    public void onStart() {
        super.onStart();

        servicioFirebase = new ServicioFirebase();
        FirebaseUser currentUser = servicioFirebase.getTokenAutenticacion().getCurrentUser();

        if (currentUser == null) {
            cambiarActividad();
        }

    }

    private void cambiarActividad() {
        Intent Log = new Intent(this, LogActivity.class);
        startActivity(Log);
    }

    private void cambiarIntento(String accion, String contexto) {
        Intent Log = new Intent(accion, Uri.parse(contexto));
        startActivity(Log);
    }

    public void desplegarInformacion(View view) {
        if (!connection.isConnected()) {
            throw new ExcepcionConexion(connection.toString());
        } else {
            KenBurnsView kbvImage;
            TextView textTitle, textLocation;
            kbvImage = view.findViewById(R.id.kbvLocation);
            textTitle = view.findViewById(R.id.textTitle);
            textLocation = view.findViewById(R.id.textLocation);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActividadPrincipal.this, R.style.bt_sheet_dialog);

            View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bt_sheet, findViewById(R.id.bt_sheet_container));
            bottonSheetView.findViewById(R.id.button_reserva).setOnClickListener(view1 -> {
                cambiarIntento(Intent.ACTION_VIEW, (String) textTitle.getTag());
                bottomSheetDialog.dismiss();
            });

            KenBurnsView kenBurnsView = bottonSheetView.findViewById(R.id.imageRes);
            TextView textViewexp = bottonSheetView.findViewById(R.id.expedicion);
            TextView textViewlugar = bottonSheetView.findViewById(R.id.lugar);

            bottonSheetView.findViewById(R.id.share).setOnClickListener(viewe -> {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Haz parte de nuestras " + "expediciones programadas a distintos lugares de Colombia. " + "Una diversidad de destinos, itinerarios llenos de momentos" + " auténticos y experiencias de vida que recordarás para siempre " + textTitle.getTag());
                try {
                    ActividadPrincipal.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    System.out.println("Whatsapp have not been installed.");
                }
            });

            bottonSheetView.findViewById(R.id.set).setOnClickListener(viewe -> {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(servicioFirebase.getTokenAutenticacion().getUid())).get().addOnSuccessListener(documentSnapshot -> {
                    bottomSheetDialog.dismiss();
                    Usuario user = documentSnapshot.toObject(Usuario.class);
                    assert user != null;
                    user.setExpedicion(kbvImage.getTag().toString());
                    database.collection(Constantes.KEY_COLLECTION_USERS).document(servicioFirebase.getTokenAutenticacion().getUid()).set(user);
                });
            });

            textViewexp.setText(textTitle.getText());
            textViewlugar.setText(textLocation.getText());
            Picasso.get().load(textLocation.getTag().toString()).into(kenBurnsView);
            bottomSheetDialog.setContentView(bottonSheetView);
            bottomSheetDialog.show();
        }
    }

    public void desplegarOpcionesdeCaptura(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActividadPrincipal.this, R.style.bt_sheet_dialog);
        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bt_image, findViewById(R.id.bt_image_container));

        bottonSheetView.findViewById(R.id.cameraOp).setOnClickListener(view1 -> {
            seleccionarDeCamara();
            bottomSheetDialog.dismiss();
        });
        bottonSheetView.findViewById(R.id.filesOp).setOnClickListener(view12 -> {
            seleccionarDeArchivos();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }

    public void seleccionarDeArchivos() {
        ImagePicker.with(this).galleryOnly().crop().start(REQUEST_IMAGE_CAPTURE);

    }

    private void seleccionarDeCamara() {
        ImagePicker.with(this).cameraOnly().start(REQUEST_IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 1) {
            //error
            assert data != null;
            Uri currentUri = data.getData();

            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_confirmacion, findViewById(R.id.dialog_box));
            Dialog dialog = new Dialog(this);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
            textView.setText("Confirmacion");
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textinfoView = box.findViewById(R.id.info);
            textinfoView.setText("Esta seguro que quiere subir esta imagen");
            LottieAnimationView imageView = box.findViewById(R.id.img);
            imageView.setAnimation(R.raw.request);

            box.findViewById(R.id.boton_de_confirmacion).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    subirImagen(currentUri);
                }
            });
            box.findViewById(R.id.boton_de_cancelar).setOnClickListener(view2 -> {
                dialog.dismiss();
            });

            dialog.setContentView(box);
            dialog.show();
        }
    }
    private void subirImagen(Uri filePath) {
        if (!connection.isConnected()) {

            return;
        }
        if (filePath != null) {
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View box2 = LayoutInflater.from(this).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View box = LayoutInflater.from(this).inflate(R.layout.box_carga, findViewById(R.id.load_box));
            servicioFirebase.subirFoto(filePath, this,box,box2);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtrar(View view) {
        RecyclerView recyclerView = findViewById(R.id.view_photo);
        List<Imagedb> imagedbs = new ArrayList<>(ImagenDao.imagedbs);
        imagedbs.clear();

        String tag = view.getTag().toString();
        if (tag.equals("Todas las expediciones")) {
            imagedbs.addAll(ImagenDao.imagedbs);
        } else {
            for (Imagedb item : ImagenDao.imagedbs) {
                if (item.getExpedicion().equals(tag)) {
                    imagedbs.add(item);
                }
            }
        }
        recyclerView.setAdapter(new ImageAdapter(imagedbs, this));
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }
    
}