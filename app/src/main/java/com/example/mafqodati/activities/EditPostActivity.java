package com.example.mafqodati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerUpdateImagesAdapter;
import com.example.mafqodati.databinding.ActivityEditPostBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class EditPostActivity extends AppCompatActivity {
    ActivityEditPostBinding binding;

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String postId = getIntent().getStringExtra("POST_ID");

        getPostData(postId);
        initTypesMenu();
        initCategoriesMenu();
        initCitiesMenu();
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(EditPostActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                post.setTitle(binding.etTitle.getText().toString());
                post.setDescription(binding.etContent.getText().toString());

                FireStore.updatePost(postId, post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
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
        this.post = post;
        binding.etTitle.setText(post.getTitle());
        binding.etContent.setText(post.getDescription());
        binding.etCity.setText(post.getCity());
        binding.etCategory.setText(post.getCategory());
        binding.etType.setText(post.getType());
        ArrayList<Uri> images = new ArrayList<>();
        for (String imgUri : post.getImagesUri()) {
            images.add(Uri.parse(imgUri));
        }
        RecyclerUpdateImagesAdapter recyclerImageFromGalleryAdapter = new RecyclerUpdateImagesAdapter(images);
        binding.recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);
    }

    private void initTypesMenu() {
        String[] types = getResources().getStringArray(R.array.type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, types);
        binding.etType.setAdapter(adapter);
        binding.etType.setEnabled(false);
    }

    private void initCitiesMenu() {
        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, cities);
        binding.etCity.setAdapter(adapter);
        binding.etCity.setEnabled(false);

    }

    private void initCategoriesMenu() {
        String[] categories = getResources().getStringArray(R.array.lost_found_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, categories);
        binding.etCategory.setAdapter(adapter);
        binding.etCategory.setEnabled(false);

    }


}