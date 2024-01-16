package com.example.mafqodati.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class RecyclerUpdateImagesAdapter  extends RecyclerView.Adapter<RecyclerUpdateImagesAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;

    public RecyclerUpdateImagesAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public RecyclerUpdateImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View imageView = layoutInflater.inflate(R.layout.item_imge_update_post, parent, false);
        return new RecyclerUpdateImagesAdapter.ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerUpdateImagesAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(uriArrayList.get(position))
                .into(holder.imgSelected);

    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgSelected;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
