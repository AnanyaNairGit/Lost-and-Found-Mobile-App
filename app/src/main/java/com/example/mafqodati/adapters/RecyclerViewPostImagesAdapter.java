package com.example.mafqodati.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class RecyclerViewPostImagesAdapter extends RecyclerView.Adapter<RecyclerViewPostImagesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String uri);
    }
    private  OnItemClickListener onItemClickListener;
     List<String> images ;

    public RecyclerViewPostImagesAdapter(List<String> images ,  OnItemClickListener onItemClickListener) {
        this.images = images;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_images_list , parent , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uri = images.get(position);
        if(images.size() > 0){
            Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .into(holder.imgPostItem);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout container ;
        RoundedImageView imgPostItem ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPostItem = itemView.findViewById(R.id.imgPostItem);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(images.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
