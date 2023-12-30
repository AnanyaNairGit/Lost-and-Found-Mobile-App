package com.example.mafqodati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mafqodati.databinding.ActivityMainBinding;
import com.example.mafqodati.fragments.AccountFragment;
import com.example.mafqodati.fragments.ChatsFragment;
import com.example.mafqodati.fragments.MainFragment;
import com.example.mafqodati.fragments.MyPostFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MainFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new MainFragment());
                    break;
                case R.id.shorts:
                    replaceFragment(new MyPostFragment());
                    break;
                case R.id.subscriptions:
                    replaceFragment(new ChatsFragment());
                    break;
                case R.id.library:
                    replaceFragment(new AccountFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void post(View view) {
        startActivity(new Intent(this , CreatePostActivity.class));
    }

}