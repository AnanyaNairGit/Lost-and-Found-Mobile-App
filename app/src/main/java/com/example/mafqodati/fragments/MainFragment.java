package com.example.mafqodati.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.CategoryAdapter;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.models.Category;
import com.example.mafqodati.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainFragment extends Fragment {

    private RecyclerView recyclerCategory, recyclerLatestPost;
    private CategoryAdapter categoryAdapter;
    private RecyclerPostAdapter recyclerPostAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerLatestPost = view.findViewById(R.id.recyclerLatestPost);

        FirestoreRecyclerOptions<Category> categoryOptions = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(db.collection("category"), Category.class)
                .build();

        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(db.collection("post"), Post.class)
                .build();
        categoryAdapter = new CategoryAdapter(categoryOptions);
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions);


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerCategory.setLayoutManager(horizontalLayoutManager);
        recyclerLatestPost.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerCategory.setAdapter(categoryAdapter);
        recyclerLatestPost.setAdapter(recyclerPostAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryAdapter.startListening();
        recyclerPostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryAdapter.stopListening();
        recyclerPostAdapter.stopListening();
    }

}