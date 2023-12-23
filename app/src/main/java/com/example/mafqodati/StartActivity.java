package com.example.mafqodati;

import static com.example.mafqodati.util.Constants.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mafqodati.models.Category;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        fetchDataAndPopulateCategories();
    }

    public void fetchDataAndPopulateCategories() {
        FireStore.getCategories().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear the existing list before adding new data
                    categories.clear();
                    // Loop through the result documents and convert them to Category objects
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String imageUri = document.getString("imageUri");
                        String nameEn = document.getString("nameEn");
                        String nameAr = document.getString("nameAr");
                        String code = document.getString("code");
                        Category category = new Category(imageUri, nameEn, nameAr, code);
                        categories.add(category);
                    }
                    startActivity(new Intent(StartActivity.this , LoginActivity.class));
                } else {
                    // Handle the error
                    Exception exception = task.getException();
                    if (exception != null) {
                        // Handle the exception
                    }
                }
            }
        });
    }
}