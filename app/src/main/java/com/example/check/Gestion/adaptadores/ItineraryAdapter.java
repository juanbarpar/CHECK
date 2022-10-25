package com.example.check.Gestion.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.Entidad.Itineraries;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.TravelLocationViewHolder>{
    Context context;
    List<Itineraries> itinerarios;

    String image;
    public ItineraryAdapter(Context context, List<Itineraries> itinerarios, String image) {
        this.context = context;
        this.itinerarios = itinerarios;
        this.image=image;
    }


    @NonNull
    @Override
    public TravelLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelLocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_user_itinerary_2,
                        parent, false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull TravelLocationViewHolder holder, int position) {
        holder.setLocationData(itinerarios.get(position),context,image);
    }
    @Override
    public int getItemCount() {
        return itinerarios.size();
    }
    static class TravelLocationViewHolder extends RecyclerView.ViewHolder {
        private KenBurnsView kbvLocation;
        private TextView dayTextView , dateTextView, eventsTextView;
        private LinearLayout action;


        TravelLocationViewHolder(@NonNull View itemView) {
            super(itemView);

            action = itemView.findViewById(R.id.action_bar);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            eventsTextView = itemView.findViewById(R.id.eventsTextView);
            kbvLocation = itemView.findViewById(R.id.kbvLocation);


        }
        void setLocationData(Itineraries itinerarios, Context context, String image){

            dayTextView.setText(itinerarios.getDia());
            dateTextView.setText(itinerarios.getFecha());
            eventsTextView.setText(itinerarios.getEventos());
            Picasso.get().load(image).into(kbvLocation);


        }


    }

}