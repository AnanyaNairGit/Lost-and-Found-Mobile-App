package com.example.mafqodati.fragments;


import static com.example.mafqodati.util.Auth.getUserId;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.mafqodati.FilterActivity;
import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.databinding.FragmentMainBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Random;


public class MainFragment extends Fragment {

    Intent filterData;
    FragmentMainBinding binding;
    private RecyclerPostAdapter recyclerPostAdapter;

    private static final int REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        fillRecyclerList();
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  fillToDb();
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return binding.getRoot();
    }

    private void fillToDb() {
        String[] categories = getResources().getStringArray(R.array.lost_found_categories);
        String[] types = getResources().getStringArray(R.array.type);
        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        for (String category : categories) {
            for (String type : types) {
                for (String city : cities) {
                    Post.destruct();
                    Post post = Post.getInstance();
                    post.setType(type);
                    post.setCategory(category);
                    post.setCity(city);
                    post.setTitle(generateRandomString());
                    post.setDescription(generateRandomString());
                    post.setLatitude(0.6);
                    post.setLongitude(31.7);
                    post.setWriterId(getUserId());
                    post.setFinished(false);
                    post.setCreationDate(System.currentTimeMillis());
                    FireStore.writeNewPost(post);
                }
            }
        }
    }
    public  String generateRandomString() {
        // Define the characters allowed in the random string
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Create an instance of Random
        Random random = new Random();

        // Generate a random length between 3 and 20
        int length = random.nextInt(18) + 3; // Generates a random integer between 3 and 20

        // Generate the random string
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedCharacters.length());
            randomString.append(allowedCharacters.charAt(index));
        }

        return randomString.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            // Handle the result here
            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    filterData = data;
                    fillRecyclerList();
                }

            } else {
                // The activity might have been canceled
            }
        }
    }

    private void fillRecyclerList() {
        Query query = buildFilterQuery();
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions);
        binding.recyclerPost.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPost.setAdapter(recyclerPostAdapter);
        recyclerPostAdapter.startListening();
    }

    private Query buildFilterQuery() {
        Query query = FireStore.postRef();
        if (filterData != null) {
            // Retrieve data from the Intent's extras
            String city = filterData.getStringExtra("city");
            String type = filterData.getStringExtra("type");
            String category = filterData.getStringExtra("category");
            String orderBy = filterData.getStringExtra("orderBy");
            String orderDirection = filterData.getStringExtra("orderDirection");
            if (!city.isEmpty()) {
                query = query.whereEqualTo("city", city);
            }
            if (!type.isEmpty()) {
                query = query.whereEqualTo("type", type);
            }
            if (!category.isEmpty()) {
                query = query.whereEqualTo("category", category);
            }
            if (!orderBy.isEmpty()) {
                if (!orderDirection.isEmpty()) {
                    if (orderBy.equals("Descending")) {
                        query = query.orderBy("orderBy", Query.Direction.DESCENDING);
                    } else {
                        query = query.orderBy("orderBy", Query.Direction.ASCENDING);
                    }
                } else {
                    query = query.orderBy("orderBy", Query.Direction.ASCENDING);
                }
            }
        }
        return query;
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