package com.example.mafqodati.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafqodati.activities.FilterActivity;
import com.example.mafqodati.R;
import com.example.mafqodati.activities.ViewPostActivity;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.databinding.FragmentMainBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.Query;


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

    RecyclerPostAdapter.OnItemClickListener onItemClickListener = postId -> {
        Intent intent = new Intent(getActivity(), ViewPostActivity.class);

        // Put extra data (replace "yourKey" and "yourData" with appropriate key and data)
        intent.putExtra("POST_ID", postId);

        // Start the activity
        startActivity(intent);
    };

    private void fillRecyclerList() {
        Query query = buildFilterQuery();
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions , onItemClickListener);
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

            if (!city.isEmpty()) {
                Chip chip = new Chip(getActivity());
                chip.setText(city);
                chip.setClickable(true);
                chip.setCheckable(false); // Set to true if you want the chips to behave like checkable buttons
                binding.chipGroup.addView(chip);
                query = query.whereEqualTo("city", city);
            }
            if (!type.isEmpty()) {
                Chip chip = new Chip(getActivity());
                chip.setText(type);
                chip.setClickable(true);
                chip.setCheckable(false); // Set to true if you want the chips to behave like checkable buttons
                binding.chipGroup.addView(chip);
                query = query.whereEqualTo("type", type);
            }
            if (!category.isEmpty()) {
                Chip chip = new Chip(getActivity());
                chip.setText(category);
                chip.setClickable(true);
                chip.setCheckable(false); // Set to true if you want the chips to behave like checkable buttons
                binding.chipGroup.addView(chip);
                query = query.whereEqualTo("category", category);
            }

            query = query.orderBy("orderBy", Query.Direction.DESCENDING);
            Chip chip = new Chip(getActivity());
            chip.setText("Clear");
            chip.setClickable(true);
            chip.setCheckable(false); // Set to true if you want the chips to behave like checkable buttons
            chip.setChipIcon(getResources().getDrawable(R.drawable.baseline_cancel_24));
            binding.chipGroup.addView(chip);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterData = null;
                    fillRecyclerList();
                    binding.chipGroup.removeAllViews();
                }
            });
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