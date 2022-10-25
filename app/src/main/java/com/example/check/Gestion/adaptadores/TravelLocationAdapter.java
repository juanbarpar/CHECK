package com.example.check.Gestion.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.Entidad.TravelLocation;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TravelLocationAdapter extends RecyclerView.Adapter<TravelLocationAdapter.TravelLocationViewHolder>{
    private List<TravelLocation> travelLocations;
    public TravelLocationAdapter(List<TravelLocation> travelLocations) {
        this.travelLocations = travelLocations;
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
        holder.setLocationData(travelLocations.get(position));
    }
    @Override
    public int getItemCount() {
        return travelLocations.size();
    }
    static class TravelLocationViewHolder extends RecyclerView.ViewHolder {
        private KenBurnsView kbvLocation;
        private TextView textTitle, textLocation, textStartRating;
        private ConstraintLayout constraintLayout;

        TravelLocationViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.waitingforid);
            kbvLocation = itemView.findViewById(R.id.kbvLocation);
            textTitle = itemView.findViewById(R.id.textTitle);
            textStartRating = itemView.findViewById(R.id.textStartRating);
            textLocation = itemView.findViewById(R.id.textLocation);




        }
        void setLocationData(TravelLocation travelLocation){

            kbvLocation.setTag(travelLocation.Nombre);

            itemView.setTag(travelLocation.Nombre);


            Picasso.get().load(travelLocation.imagen).into(kbvLocation);
            textTitle.setText(travelLocation.Nombre);
            textLocation.setTag(travelLocation.imagen);
            textLocation.setText(travelLocation.ubicación);
            textStartRating.setText(String.valueOf(travelLocation.fecha));

            textTitle.setTag(travelLocation.url);

        }
    }
}