package com.example.mafqodati;

import static com.example.mafqodati.util.Auth.getUserId;
import static com.example.mafqodati.util.Constants.POST_TYPE_FOUND;
import static com.example.mafqodati.util.Constants.POST_TYPE_LOST;
import static com.example.mafqodati.util.Constants.categories;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mafqodati.adapters.RecyclerImageFromGalleryAdapter;
import com.example.mafqodati.adapters.SelectCategoryAdapter;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {

    private EditText etTitle ;
    private EditText etContent ;
    private RecyclerView recyclerImagesFromGallery;
    private ImageView btnPickImages;
    private SwitchMaterial schType;
    ArrayList<Uri> chosenImagesUriList = new ArrayList<>();
    private RecyclerImageFromGalleryAdapter recyclerImageFromGalleryAdapter;

    private Spinner spnCategory;
    private Spinner spnCity;

    private MaterialButtonToggleGroup toggleButton;
    private static final int READ_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        btnPickImages = findViewById(R.id.btnPickImages);
        spnCategory = findViewById(R.id.spnCategory);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        initPickImagesRecyclerView();
        initPostTypeButton();
        initCitiesSpinner();


        SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(this);
        spnCategory.setAdapter(selectCategoryAdapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Post.getInstance().setCategoryId(categories.get(position).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Post.getInstance().setCategoryId(null);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }

        btnPickImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");

            pickMedia.launch(intent);
        });


    }

    private void initPickImagesRecyclerView() {
        recyclerImagesFromGallery = findViewById(R.id.recyclerImagesFromGallery);
        recyclerImageFromGalleryAdapter = new RecyclerImageFromGalleryAdapter(chosenImagesUriList);
        recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);
    }

    private void initCitiesSpinner() {
        spnCity = findViewById(R.id.spnCity);
        String[] municipalities = getResources().getStringArray(R.array.jordan_municipalities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, municipalities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCity.setAdapter(adapter);
        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Post.getInstance().setCity(municipalities[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Post.getInstance().setCity(null);
            }
        });
    }

    private void initPostTypeButton() {
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.btnLost:
                            Post.getInstance().setType(POST_TYPE_LOST);
                            break;
                        case R.id.btnFound:
                            Post.getInstance().setType(POST_TYPE_FOUND);
                            break;
                    }
                }
            }
        });
    }


    private void pickImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType(" image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
        Post.getInstance().setTitle(etTitle.getText().toString());
        Post.getInstance().setContent(etContent.getText().toString());
        Post.getInstance().setCreationDate(System.currentTimeMillis());
        Post.getInstance().setWriterId(getUserId());
        Post.getInstance().setFinished(false);
        for (Uri imageUri : chosenImagesUriList){
            Post.getInstance().getImagesUri().add(imageUri.toString());
        }
        startActivity(new Intent(this, PickLocationActivity.class));
    }
}