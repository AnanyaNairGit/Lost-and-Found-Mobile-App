package com.example.mafqodati.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.mafqodati.R;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecyclerPostAdapter extends FirestoreRecyclerAdapter<Post, RecyclerPostAdapter.ViewHolder> {

    public RecyclerPostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {

        holder.txtPostTitle.setText(model.getTitle());
        holder.txtPostContent.setText(model.getContent());
        holder.txtCreationDate.setText(Constants.convertTimeMillsToDateString(model.getCreationDate()));
        holder.txtCity.setText(model.getCity());

        if (model.getType() == 1) {
            holder.txtPostType.setText("Lost");
            holder.txtPostType.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
        } else {
            holder.txtPostType.setText("Found");
            holder.txtPostType.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        }
        Glide.with(holder.itemView.getContext())
                .load(model.getImagesUri().get(0))
                .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                .into(holder.imgPostImage);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPostTitle;
        TextView txtPostContent;
        TextView txtCity;
        TextView txtCreationDate;
        TextView txtPostType;
        ImageView imgPostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPostTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostContent = itemView.findViewById(R.id.txtPostContent);
            txtCreationDate = itemView.findViewById(R.id.txtCreationDate);
            txtPostType = itemView.findViewById(R.id.txtPostType);
            txtCity = itemView.findViewById(R.id.txtCity);
            imgPostImage = itemView.findViewById(R.id.imgPostMain);
        }
    }
}
