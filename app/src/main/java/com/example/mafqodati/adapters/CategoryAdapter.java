package com.example.mafqodati.adapters;


// CategoryAdapter.java

import android.net.Uri;
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
import com.example.mafqodati.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.ViewHolder> {

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Category model) {
        holder.txtCatName.setText(model.getNameEn());
        System.out.println(model.getImageUri());
        Glide.with(holder.itemView.getContext())
                .load(model.getImageUri())
                .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                .into(holder.imgCatImg);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCatName;
        ImageView imgCatImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCatName = itemView.findViewById(R.id.txtCatName);
            imgCatImg = itemView.findViewById(R.id.imgCatImg);
        }
    }
}
