package com.example.mafqodati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.mafqodati.adapters.RecyclerImageFromGalleryAdapter;
import com.example.mafqodati.databinding.ActivityFilterBinding;

public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initTypesMenu();
        initCitiesMenu();
        initCategoriesMenu();
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etCity.setText("");
                binding.etType.setText("");
                binding.etCategory.setText("");
            }
        });
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = binding.etCity.getText().toString();
                String type = binding.etType.getText().toString();
                String category = binding.etCategory.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("city", city);
                resultIntent.putExtra("type", type);
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
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




}