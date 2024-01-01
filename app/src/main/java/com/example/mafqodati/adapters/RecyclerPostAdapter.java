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
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerPostAdapter extends FirestoreRecyclerAdapter<Post, RecyclerPostAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String postId);
    }
    private OnItemClickListener onItemClickListener;

    public RecyclerPostAdapter(@NonNull FirestoreRecyclerOptions<Post> options ,OnItemClickListener onItemClickListener) {
        super(options);
        this.onItemClickListener = onItemClickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_card, parent, false);
        return new ViewHolder(view);
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
        if(model.getImagesUri().size()>0){
            Glide.with(holder.itemView.getContext())
                    .load(model.getImagesUri().get(0))
                    .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                    .into(holder.imgPostImage);
        }
        FireStore.getUserData(model.getWriterId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        holder.btnCall.setText(user.getUserPhone());
                    }
                }
            }
        });

    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        CardView container ;

        TextView txtPostTitle;
        TextView txtPostContent;

        TextView txtCreationDate;
        Chip chpType;
        Chip chpCity;
        Chip chpCategory;

        TextView txtImgCount;
        ImageView imgPostImage;
        Button btnCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPostTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostContent = itemView.findViewById(R.id.txtPostContent);
            txtCreationDate = itemView.findViewById(R.id.txtCreationDate);
            chpType = itemView.findViewById(R.id.chpType);
            chpCity = itemView.findViewById(R.id.chpCity);
            chpCategory = itemView.findViewById(R.id.chpCategory);
            container = itemView.findViewById(R.id.container);
            imgPostImage = itemView.findViewById(R.id.imgPostMain);
            txtImgCount = itemView.findViewById(R.id.txtImgCount);
            btnCall = itemView.findViewById(R.id.btnCall);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onItemClick when an item is clicked
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }
}
