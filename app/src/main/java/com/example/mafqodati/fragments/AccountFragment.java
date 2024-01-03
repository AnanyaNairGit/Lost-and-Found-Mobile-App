package com.example.mafqodati.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.mafqodati.activities.LoginActivity;
import com.example.mafqodati.databinding.FragmentAccountBinding;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        loadUserData(Auth.getUserId());
        binding.btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Save")
                        .setMessage("Are you sure you want to sign out")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        return binding.getRoot();
    }

    private void loadUserData(String userId) {
        FireStore.getUserData(userId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    binding.tvFullName.setText(user.getUserFirstName() + " " + user.getUserLastName());
                    binding.tvEmail.setText(user.getUserEmail());
                    binding.tvPhone.setText(user.getUserPhone());
                    Glide.with(getActivity())
                            .load(user.getUserProfileImgURL())
                            .into(binding.imgUser);
                    setPostsCount();

                } else {
                    // User data doesn't exist or there was an error
                    // Handle the case accordingly
                }
            }
        });
    }


    private void setPostsCount() {
        FireStore.postRef().whereEqualTo("writerId", Auth.getUserId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.txtPostCount.setText("(" + task.getResult().size()+ ") Posts");                        }
                    }
                });
    }
}