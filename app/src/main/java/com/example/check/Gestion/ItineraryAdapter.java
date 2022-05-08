package com.example.check.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.Entidad.Itinerary;
import com.example.check.Entidad.TravelLocation;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.TravelLocationViewHolder>{
    private List<Itinerary> itineraries;
    public ItineraryAdapter(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }
    @NonNull
    @Override
    public TravelLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelLocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_day,
                        parent, false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull TravelLocationViewHolder holder, int position) {
        holder.setLocationData(itineraries.get(position));
    }
    @Override
    public int getItemCount() {
        return itineraries.size();
    }
    static class TravelLocationViewHolder extends RecyclerView.ViewHolder {

        private TextView textIts;

        TravelLocationViewHolder(@NonNull View itemView) {
            super(itemView);

            textIts = itemView.findViewById(R.id.textIts);


        }
        void setLocationData(Itinerary itinerary){





        }
    }
}