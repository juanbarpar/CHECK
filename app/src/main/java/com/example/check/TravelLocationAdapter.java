package com.example.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

            kbvLocation.setTag(travelLocation.imageUrl);


            Picasso.get().load(travelLocation.imageUrl).into(kbvLocation);
            textTitle.setText(travelLocation.title);
            textLocation.setText(travelLocation.location);
            textStartRating.setText(String.valueOf(travelLocation.startRating));

            textTitle.setTag(travelLocation.url);





        }
    }
}