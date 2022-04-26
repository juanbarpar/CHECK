package com.example.check.Gestion;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.check.Entidad.Imagedb;
import com.example.check.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Collections;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private List<Imagedb> imageList;
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

            Glide.with(activity)
                    .load(image.getUrl())
                    .into(imageView);

        }
    }

}
