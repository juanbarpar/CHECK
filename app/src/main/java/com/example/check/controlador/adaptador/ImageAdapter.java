package com.example.check.controlador.adaptador;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private final List<Imagedb> imageList;
    Activity activity;

    public ImageAdapter(List<Imagedb> imageList, Activity activity) {


        this.imageList = imageList;

        this.activity = activity;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setImageView(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_post);

        }
        void setImageView(Imagedb image){

            imageView.setTag(image.getUrl());

            imageView.setOnClickListener(view -> {

                View box = LayoutInflater.from(activity.getApplicationContext()).inflate(
                        R.layout.image_layout, activity.findViewById(R.id.image_container)
                );

                Dialog dialog = new Dialog(activity);

                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                box.setOnClickListener(view1 -> dialog.dismiss());

                RoundedImageView imageView = box.findViewById(R.id.imageFS);
                Glide.with(activity)
                        .load(view.getTag())
                        .into(imageView);
                imageView.setBackground(null);

                dialog.setContentView(box);
                dialog.show();


                CharSequence text = image.getUser() +" - "+image.getExpedicion();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(activity, text, duration);
                toast.show();

            });

            Glide.with(activity)
                    .load(image.getUrl())
                    .into(imageView);
            imageView.setBackground(null);

        }
    }

}
