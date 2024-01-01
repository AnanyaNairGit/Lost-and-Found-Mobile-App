package com.example.mafqodati.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerMyPostAdapter;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.databinding.FragmentMyPostBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MyPostFragment extends Fragment {
    FragmentMyPostBinding binding;
    private RecyclerMyPostAdapter recyclerMyPostAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPostBinding.inflate(inflater, container, false);

        fillMyPostRecycler();


        return binding.getRoot();
    }


    RecyclerMyPostAdapter.OnDeleteClickListener onDeleteClickListener = postId -> {
        Log.v("LOG_ON_ITEM" , postId);
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Deleted Item")
                .setCancelable(false)
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FireStore.postRef(postId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fillMyPostRecycler();
                            }
                        });
                    }
                })
                .setNegativeButton("No" , null)
                .show();
    };

    RecyclerMyPostAdapter.OnItemClickListener onItemClickListener = postId -> {
        Log.v("LOG_ON_ITEM" , postId);
    };

    private void fillMyPostRecycler() {
        FirestoreRecyclerOptions<Post> postOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(db.collection("posts").whereEqualTo("writerId", mAuth.getCurrentUser().getUid()), Post.class)
                .build();
        recyclerMyPostAdapter = new RecyclerMyPostAdapter(postOptions, onItemClickListener , onDeleteClickListener);
        binding.recyclerMyPost.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerMyPost.setAdapter(recyclerMyPostAdapter);
        recyclerMyPostAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerMyPostAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerMyPostAdapter.stopListening();
    }

}