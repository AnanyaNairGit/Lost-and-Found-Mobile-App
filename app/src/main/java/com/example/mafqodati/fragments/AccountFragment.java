package com.example.mafqodati.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.databinding.FragmentAccountBinding;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        loadUserData(Auth.getUserId());
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
                            .into(binding.imgUser)
                    ;

                } else {
                    // User data doesn't exist or there was an error
                    // Handle the case accordingly
                }
            }
        });
    }

}