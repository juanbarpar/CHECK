package com.example.check.servicio.utilidades.dialogo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.check.R;

import java.util.Formatter;


public class DialogoCarga {
    Context context;
    TextView textinfoView;
    Dialog dialog;


    public DialogoCarga(Context context) {
        this.context = context;
    }
    public void dispararDialogo(View box){
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
        textView.setText("Subiendo foto");
        textinfoView = box.findViewById(R.id.info);
        textinfoView.setText("Actualizando... ");
        // = box.findViewById(R.id.img);
        //imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
        dialog.setContentView(box);
        dialog.show();
    }
    public void porsentaje(Formatter progress){
        textinfoView.setText("("+progress+"%/100%) Actualizando... ");
    }
    public void finalizar(){
        dialog.dismiss();
    }
}
