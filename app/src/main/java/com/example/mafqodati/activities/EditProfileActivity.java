package com.example.mafqodati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.example.mafqodati.databinding.ActivityEditProfileBinding;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        fillUserData();
        binding.etEmail.setEnabled(false);
        binding.etPhone.setEnabled(false);
    }

    private void fillUserData() {
        FireStore.getUserData(Auth.getUserId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    binding.btnSave.setEnabled(true);
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User user = documentSnapshot.toObject(User.class);
                    binding.etFirstName.setText(user.getUserFirstName());
                    binding.etLastName.setText(user.getUserLastName());
                    Glide.with(EditProfileActivity.this)
                            .load(user.getUserProfileImgURL())
                            .into(binding.userImage);
                    binding.etEmail.setText(user.getUserEmail());
                    binding.etPhone.setText(user.getUserPhone());
                }
            }
        });
    }
}