package com.example.mafqodati.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mafqodati.R;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStorage;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.UploadTask;

public class PickProfilePicture extends AppCompatActivity {

    private ImageView imgProfilePicture ;
    private Uri chosenImageUri ;
    private Button btnUpdatePicture;
    private ProgressDialog progressDialog;

    private static final int READ_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_profile_picture);
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        btnUpdatePicture = findViewById(R.id.bynUpdatePicture);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }

        imgProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            pickMedia.launch(intent);
        });

        btnUpdatePicture.setOnClickListener(v-> {
            if (chosenImageUri == null){
                Toast.makeText(this, "Please Select A profile picture", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = new ProgressDialog(PickProfilePicture.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            Auth.createUser(User.getInstance().getUserEmail(), getIntent().getStringExtra("PASSWORD"))
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FireStorage.uploadProfilePicture(chosenImageUri).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Use getDownloadUrl() instead of getUploadSessionUri()
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            User.getInstance().setUserProfileImgURL(String.valueOf(uri));
                                            FireStore.writeNewUser(authResult.getUser().getUid())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }
                    );

                }
            });

        });

    }

    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            chosenImageUri = data.getData();
                            imgProfilePicture.setImageURI(chosenImageUri);
                        }
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    public void finishAndGoBackToManin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void toMain(View view) {
        finishAndGoBackToManin();
    }
}