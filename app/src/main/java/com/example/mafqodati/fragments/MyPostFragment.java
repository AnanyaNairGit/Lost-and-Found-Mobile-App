package com.example.mafqodati.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MyPostFragment extends Fragment {
    private RecyclerView recyclerMyPost;
    private RecyclerPostAdapter recyclerPostAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);
        recyclerMyPost = view.findViewById(R.id.recyclerMyPost);
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(db.collection("posts").whereEqualTo("writerId",mAuth.getCurrentUser().getUid() ), Post.class)
                .build();
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions);
        recyclerMyPost.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMyPost.setAdapter(recyclerPostAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerPostAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        recyclerPostAdapter.stopListening();
    }

}