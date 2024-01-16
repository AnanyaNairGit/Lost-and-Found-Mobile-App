package com.example.mafqodati.adapters;

import static com.example.mafqodati.util.Constants.convertTimeMillsToDateString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.example.mafqodati.R;
import com.example.mafqodati.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;

public class RecyclerMyPostAdapter extends FirestoreRecyclerAdapter<Post, RecyclerMyPostAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String postId);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String postId);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(String postId);

    }

    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     *
     * @param options
     */

    public RecyclerMyPostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        holder.txtPostTitle.setText(model.getTitle());
        holder.txtPostContent.setText(model.getDescription());
        holder.txtCreationDate.setText(convertTimeMillsToDateString(model.getCreationDate()));
        holder.chpCategory.setText(model.getCategory());
        holder.txtImgCount.setText(String.valueOf(model.getImagesUri().size()));
        holder.chpCity.setText(model.getCity());
        holder.chpType.setText(model.getType());
        if (model.getImagesUri().size() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(model.getImagesUri().get(0))
                    .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                    .into(holder.imgPostImage);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_post_card, parent, false);
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView txtPostTitle;
        TextView txtPostContent;

        TextView txtCreationDate;
        Chip chpType;
        Chip chpCity;
        Chip chpCategory;

        TextView txtImgCount;
        ImageView imgPostImage;
        Button btnDelete;
        Button btnEditPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPostTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostContent = itemView.findViewById(R.id.txtPostContent);
            txtCreationDate = itemView.findViewById(R.id.txtCreationDate);
            chpType = itemView.findViewById(R.id.chpType);
            container = itemView.findViewById(R.id.container);
            chpCity = itemView.findViewById(R.id.chpCity);
            chpCategory = itemView.findViewById(R.id.chpCategory);
            imgPostImage = itemView.findViewById(R.id.imgPostMain);
            txtImgCount = itemView.findViewById(R.id.txtImgCount);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEditPost = itemView.findViewById(R.id.btnEditPost);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onItemClick when an item is clicked
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });

            btnEditPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onUpdateClickListener != null){
                        onUpdateClickListener.onUpdateClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });

        }
    }
}
