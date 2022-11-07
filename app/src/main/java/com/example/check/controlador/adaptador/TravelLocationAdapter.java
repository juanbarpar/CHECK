package com.example.check.controlador.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.repositorio.entidad.DestinosViaje;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TravelLocationAdapter extends RecyclerView.Adapter<TravelLocationAdapter.TravelLocationViewHolder>{
    private final List<DestinosViaje> destinosViajes;
    public TravelLocationAdapter(List<DestinosViaje> destinosViajes) {
        this.destinosViajes = destinosViajes;
    }


    @NonNull
    @Override
    public TravelLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelLocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_home,
                        parent, false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull TravelLocationViewHolder holder, int position) {
        holder.setLocationData(destinosViajes.get(position));
    }
    @Override
    public int getItemCount() {
        return destinosViajes.size();
    }
    static class TravelLocationViewHolder extends RecyclerView.ViewHolder {
        private final KenBurnsView kbvLocation;
        private final TextView textTitle;
        private final TextView textLocation;
        private final TextView textStartRating;

        TravelLocationViewHolder(@NonNull View itemView) {
            super(itemView);

            kbvLocation = itemView.findViewById(R.id.kbvLocation);
            textTitle = itemView.findViewById(R.id.textTitle);
            textStartRating = itemView.findViewById(R.id.textStartRating);
            textLocation = itemView.findViewById(R.id.textLocation);




        }
        void setLocationData(DestinosViaje destinosViaje){

            kbvLocation.setTag(destinosViaje.Nombre);

            itemView.setTag(destinosViaje.Nombre);


            Picasso.get().load(destinosViaje.imagen).into(kbvLocation);
            textTitle.setText(destinosViaje.Nombre);
            textLocation.setTag(destinosViaje.imagen);
            textLocation.setText(destinosViaje.ubicación);
            textStartRating.setText(String.valueOf(destinosViaje.fecha));

            textTitle.setTag(destinosViaje.url);

        }
    }
}