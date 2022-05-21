package com.example.check.Gestion.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.check.Entidad.Itineraries;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class ItinerayAdapter extends PagerAdapter {
    Context context;
    List<Itineraries> itinerarios;
    LayoutInflater layoutInflater;
    String image;

    public ItinerayAdapter(Context context, List<Itineraries> itinerarios, String image) {
        this.context = context;
        this.itinerarios = itinerarios;
        this.image=image;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return itinerarios.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_user_itinerary_2, container, false);
        TextView dayTextView = view.findViewById(R.id.dayTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView eventsTextView = view.findViewById(R.id.eventsTextView);
        KenBurnsView kbvLocation = view.findViewById(R.id.kbvLocation);

        dayTextView.setText(itinerarios.get(position).getDia());
        dateTextView.setText(itinerarios.get(position).getFecha());
        eventsTextView.setText(itinerarios.get(position).getEventos());
        Glide.with(context).load(image).into(kbvLocation);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
