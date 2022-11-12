package com.example.check.servicio.utilidades.dialogo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.check.ActividadPrincipal;
import com.example.check.R;

import java.security.AccessController;

public class Dialogo {
    Context context;
    public Dialogo(Context context) {
        this.context = context;
    }
    public void dispararDialogo(View box,String titulo, String info){

        Dialog dialog = new Dialog(context);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
        textView.setText(titulo);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textinfoView = box.findViewById(R.id.info);
        textinfoView.setText(info);
        //ImageView imageView = box.findViewById(R.id.img);
        //imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
        box.findViewById(R.id.boton_de_confirmacion).setOnClickListener(view2 -> {
            dialog.dismiss();
        });

        dialog.setContentView(box);
        dialog.show();
    }
}
