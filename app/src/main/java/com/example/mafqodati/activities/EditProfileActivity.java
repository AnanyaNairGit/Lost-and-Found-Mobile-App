package com.example.mafqodati.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.example.mafqodati.databinding.ActivityEditProfileBinding;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStorage;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    String userImageUri;
    Uri newUserImageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        fillUserData();
        binding.etEmail.setEnabled(false);
        binding.etPhone.setEnabled(false);
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Save")
                        .setMessage("Are you sure you want to update")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateUserInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        binding.tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickMedia.launch(intent);
            }
        });
    }

    private void updateUserInfo() {
        progressDialog.show();
        String firstName = binding.etFirstName.getText().toString() ;
        String lastName = binding.etLastName.getText().toString();
        String email = binding.etEmail.getText().toString() ;
        String phone = binding.etPhone.getText().toString();
        if(firstName.isEmpty()){
            binding.etFirstName.setError("Required");
            binding.etFirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            binding.etLastName.setError("Required");
            binding.etLastName.requestFocus();
            return;
        }
        User user = User.getInstance();
        user.setUserFirstName(firstName);
        user.setUserLastName(lastName);
        user.setUserEmail(email);
        user.setUserPhone(phone);

        if (newUserImageUri != null) {
            FireStorage.uploadImage(newUserImageUri , Auth.getUserId()).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    user.setUserProfileImgURL(s);
                    FireStore.updateUser(Auth.getUserId(), user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });
                }
            });
        } else {
            user.setUserProfileImgURL(userImageUri);
            FireStore.updateUser(Auth.getUserId(), user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
        }


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
                    userImageUri = user.getUserProfileImgURL();
                    progressDialog.dismiss();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            newUserImageUri = selectedImageUri;
                            binding.userImage.setImageURI(newUserImageUri);
                        }

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

}