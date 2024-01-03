package com.example.mafqodati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.mafqodati.adapters.RecyclerViewPostImagesAdapter;
import com.example.mafqodati.databinding.ActivityViewPostBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ViewPostActivity extends AppCompatActivity {

    ActivityViewPostBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String postId = getIntent().getStringExtra("POST_ID");
        getPostData(postId);
    }

    private void getPostData(String postId) {
        FireStore.getPost(postId)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Post post = documentSnapshot.toObject(Post.class);
                        showPostData(post);
                    }
                });
    }

    private void showPostData(Post post) {

        binding.title.setText(post.getTitle());
        binding.description.setText(post.getDescription());
        binding.city.setText(post.getCity());
        binding.category.setText(post.getCategory());
        binding.type.setText(post.getType());
        RecyclerViewPostImagesAdapter.OnItemClickListener onItemClickListener = uri -> {
            Glide.with(this)
                    .load(uri)
                    .into(binding.imgMain);
        };
        RecyclerViewPostImagesAdapter recyclerViewPostImagesAdapter = new RecyclerViewPostImagesAdapter(post.getImagesUri(), onItemClickListener);
        binding.recyclerPostImages.setAdapter(recyclerViewPostImagesAdapter);
        binding.recyclerPostImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (post.getImagesUri().size() > 0){
            Glide.with(this)
                    .load(post.getImagesUri().get(0))
                    .into(binding.imgMain);
        }

        binding.btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + post.getLatitude()+"," + post.getLongitude()+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
        getUserData(post.getWriterId());
    }

    private void getUserData(String writerId) {
        FireStore.getUserData(writerId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                User user = documentSnapshot.toObject(User.class);
                showUserData(user);
            }
        });
    }

    private void showUserData(User user) {
        binding.btnCall.setText(user.getUserPhone());
        binding.tvUserName.setText(user.getUserFirstName() + " " + user.getUserLastName());
        Glide.with(this)
                        .load(user.getUserProfileImgURL())
                                .into(binding.imgUser);

    }
}