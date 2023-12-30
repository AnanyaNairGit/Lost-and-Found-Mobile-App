package com.example.mafqodati;

import static com.example.mafqodati.util.Auth.getUserId;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mafqodati.adapters.RecyclerImageFromGalleryAdapter;
import com.example.mafqodati.databinding.ActivityCreatePostBinding;
import com.example.mafqodati.models.Post;


import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {
    ActivityCreatePostBinding binding;

    ArrayList<Uri> chosenImagesUriList = new ArrayList<>();
    private RecyclerImageFromGalleryAdapter recyclerImageFromGalleryAdapter;


    private static final int READ_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initPickImagesRecyclerView();
        initTypesMenu();
        initCategoriesMenu();
        initCitiesMenu();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }

        binding.btnPickImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");

            pickMedia.launch(intent);
        });


    }

    private void initPickImagesRecyclerView() {
        recyclerImageFromGalleryAdapter = new RecyclerImageFromGalleryAdapter(chosenImagesUriList);
        binding.recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);
    }

    private void initTypesMenu() {
        String[] types = getResources().getStringArray(R.array.type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, types);
        binding.etType.setAdapter(adapter);
    }

    private void initCitiesMenu() {
        String[] cities = getResources().getStringArray(R.array.jordan_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, cities);
        binding.etCity.setAdapter(adapter);
    }

    private void initCategoriesMenu() {
        String[] categories = getResources().getStringArray(R.array.lost_found_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, categories);
        binding.etCategory.setAdapter(adapter);
    }


    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            chosenImagesUriList.clear();
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            chosenImagesUriList.add(selectedImageUri);
                        } else if (data.getClipData() != null) {
                            // Multiple images selected
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() < 11) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                                    chosenImagesUriList.add(selectedImageUri);
                                }
                            } else {
                                Toast.makeText(this, "Not allowed to pick more than 10 images", Toast.LENGTH_LONG).show();
                            }
                        }
                        recyclerImageFromGalleryAdapter.notifyDataSetChanged();

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    public void toPickLocation(View view) {
        String title = binding.etTitle.getText().toString();
        String description = binding.etContent.getText().toString();
        String city = binding.etCity.getText().toString();
        String type = binding.etType.getText().toString();
        String category = binding.etCategory.getText().toString();

        if (title.isEmpty()) {
            binding.etTitle.setError("Required!");
            binding.etTitle.requestFocus();
            return;
        }
        if (description.isEmpty()) {
            binding.etContent.setError("Required!");
            binding.etContent.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            binding.etCity.setError("Required!");
            binding.etCity.requestFocus();
            return;
        }
        if (type.isEmpty()) {
            binding.etType.setError("Required!");
            binding.etType.requestFocus();
            return;
        }
        if (category.isEmpty()) {
            binding.etCategory.setError("Required!");
            binding.etCategory.requestFocus();
            return;
        }


        Post.getInstance().setTitle(title);
        Post.getInstance().setDescription(description);
        Post.getInstance().setCity(city);
        Post.getInstance().setCategory(category);
        Post.getInstance().setType(type);
        Post.getInstance().setCreationDate(System.currentTimeMillis());
        Post.getInstance().setWriterId(getUserId());
        Post.getInstance().setFinished(false);

        for (Uri imageUri : chosenImagesUriList) {
            Post.getInstance().getImagesUri().add(imageUri.toString());
        }
        startActivity(new Intent(this, PickLocationActivity.class));
    }
}