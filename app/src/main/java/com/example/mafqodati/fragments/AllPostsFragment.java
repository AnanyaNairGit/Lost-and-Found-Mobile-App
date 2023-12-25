package com.example.mafqodati.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class AllPostsFragment extends Fragment {

    private RecyclerView recyclerPost;
    private EditText etSearch;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerPostAdapter recyclerPostAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_posts, container, false);
        recyclerPost = view.findViewById(R.id.recyclerPost);
        etSearch = view.findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Query query = db.collection("posts").whereEqualTo("finished" , false);
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions);
        recyclerPost.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPost.setAdapter(recyclerPostAdapter);
        recyclerPostAdapter.startListening();
        return view ;
    }
    private void initList(Query query){

    }
}