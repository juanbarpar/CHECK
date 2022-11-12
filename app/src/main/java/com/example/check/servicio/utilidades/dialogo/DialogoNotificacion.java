package com.example.check.servicio.utilidades.dialogo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.check.R;

public class DialogoNotificacion {
    Context context;
    LottieAnimationView animationView;
    public DialogoNotificacion(Context context) {
        this.context = context;
    }
    public void dispararDialogo(View box,String titulo, String info, int animacion){

        Dialog dialog = new Dialog(context);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
        textView.setText(titulo);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textinfoView = box.findViewById(R.id.info);
        textinfoView.setText(info);
        animationView=box.findViewById(R.id.img);
        animationView.setAnimation(animacion);
        box.findViewById(R.id.boton_de_confirmacion).setOnClickListener(view2 -> {
            dialog.dismiss();
        });

        dialog.setContentView(box);
        dialog.show();
    }
}
