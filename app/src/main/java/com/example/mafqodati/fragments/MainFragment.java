package com.example.mafqodati.fragments;

import static com.example.mafqodati.util.Constants.POST_TYPE_ANY;
import static com.example.mafqodati.util.Constants.POST_TYPE_FOUND;
import static com.example.mafqodati.util.Constants.POST_TYPE_LOST;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.CategoryAdapter;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.models.Category;
import com.example.mafqodati.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MainFragment extends Fragment {

    private RecyclerView recyclerCategory, recyclerLatestPost;
    private CategoryAdapter categoryAdapter;
    private Button btnShowAllPosts;
    private RecyclerPostAdapter recyclerPostAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MaterialButtonToggleGroup toggleButtonType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerLatestPost = view.findViewById(R.id.recyclerLatestPost);

        initPostTypeButton(view);
        intiShowAllPostsButton(view);
        FirestoreRecyclerOptions<Category> categoryOptions = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(db.collection("category"), Category.class)
                .build();

        filterRecentPost(POST_TYPE_ANY);

        categoryAdapter = new CategoryAdapter(categoryOptions);


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerCategory.setLayoutManager(horizontalLayoutManager);

        recyclerCategory.setAdapter(categoryAdapter);

        return view;
    }

    private void intiShowAllPostsButton(View view) {
        btnShowAllPosts = view.findViewById(R.id.btnShowAllPosts);
        btnShowAllPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllPostsFragment fragment = new AllPostsFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null); // Add to back stack to handle back navigation
                fragmentTransaction.commit();
            }
        });
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

    private void initPostTypeButton(View view) {
        toggleButtonType = view.findViewById(R.id.toggleButton);
        toggleButtonType.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnLost:
                            filterRecentPost(POST_TYPE_LOST);
                            break;
                        case R.id.btnFound:
                            filterRecentPost(POST_TYPE_FOUND);
                            break;
                        case R.id.btnBoth:
                            filterRecentPost(POST_TYPE_ANY);
                            break;
                    }
                }
            }
        });
    }

    private void filterRecentPost(int postType) {
        long twoDaysAgoTimestamp = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000); // Timestamp for 2 days ago
        Query query = db.collection("posts")
                .whereGreaterThanOrEqualTo("creationDate", twoDaysAgoTimestamp)
                .whereEqualTo("finished" , false);
        if (postType == POST_TYPE_LOST) {
            query = query.whereEqualTo("type", POST_TYPE_LOST);

        } else if (postType == POST_TYPE_FOUND) {
            query=  query.whereEqualTo("type", POST_TYPE_FOUND);
        }
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        recyclerPostAdapter = new RecyclerPostAdapter(postOptions);
        recyclerLatestPost.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerLatestPost.setAdapter(recyclerPostAdapter);
        recyclerPostAdapter.startListening();

    }


}