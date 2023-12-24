package com.example.mafqodati;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import com.example.mafqodati.models.Post;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStorage;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
            FireStorage.uploadProfilePicture(chosenImageUri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Use getDownloadUrl() instead of getUploadSessionUri()
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FireStore.updateUserPicture(Auth.getUserId(), uri.toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    finishAndGoBackToManin();
                                                }
                                            });
                                }
                            });
                        }
                    }
            );
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