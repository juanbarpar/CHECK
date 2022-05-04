package com.example.check.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.Entidad.Itineraries;
import com.example.check.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder>{
    private List<Itineraries> itineraries;
    public ItineraryAdapter(List<Itineraries> itineraries){
        this.itineraries = itineraries;
    }
    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ItineraryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_user_itinerary,
                        parent, false
                )
                );




    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryAdapter.ItineraryViewHolder holder, int position) {
        holder.setLocationData(itineraries.get(position));
    }

    @Override
    public int getItemCount() {return itineraries.size();}

    static class ItineraryViewHolder extends RecyclerView.ViewHolder{
        private TextView textDay, textDate, textEvents;


            ItineraryViewHolder(@NonNull View itemView){
                super(itemView);

                textDay = itemView.findViewById(R.id.dayTextView);
                textDate = itemView.findViewById(R.id.dateTextView);
                textEvents = itemView.findViewById(R.id.eventsTextView);


            }

            void setLocationData(Itineraries itineraries){
                textDay.setText(itineraries.getDia());
                textDate.setText(itineraries.getFecha());
                textEvents.setText(itineraries.getEventos());


            }


    }









}