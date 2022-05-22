package com.example.check.Gestion.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.databinding.ItemContainerUserBinding;
import com.example.check.listeners.UserListener;
import com.example.check.Entidad.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final List<String> uids;
    private UserListener userListener;

    public UsersAdapter(List<User> users,UserListener userListener,List<String> uids) {
        this.users = users;
        this.userListener = userListener;
        this.uids = uids;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position),uids.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }
        void setUserData(User user,String uid){
            binding.textName.setText(user.getName());
            binding.textEmail.setText(user.getEmail());

            System.out.println("UID: " + uid);
            binding.getRoot().setOnClickListener(view -> userListener.onUserClicked(uid));
        }
    }


}
