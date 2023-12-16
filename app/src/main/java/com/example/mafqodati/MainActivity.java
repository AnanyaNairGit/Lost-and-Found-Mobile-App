package com.example.mafqodati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mafqodati.databinding.ActivityMainBinding;
import com.example.mafqodati.fragments.AccountFragment;
import com.example.mafqodati.fragments.ChatsFragment;
import com.example.mafqodati.fragments.MainFragment;
import com.example.mafqodati.fragments.MyPostFragment;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.util.Firestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

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
        ArrayList<String> images = new ArrayList<>();
        images.add("https://firebasestorage.googleapis.com/v0/b/mafqodati1.appspot.com/o/posts%2FCdFHpfUrAiWv4ynxxZ8falQybDI3%2FWhatsApp%20Image%202023-12-16%20at%206.59.23%20PM.jpeg?alt=media&token=97608d2a-37f1-4617-82eb-f6fd64ef4b57");
        images.add("https://firebasestorage.googleapis.com/v0/b/mafqodati1.appspot.com/o/posts%2FCdFHpfUrAiWv4ynxxZ8falQybDI3%2FWhatsApp%20Image%202023-12-16%20at%206.59.32%20PM.jpeg?alt=media&token=07637a6c-1a59-434f-b29a-2c4a9da263fa");
        images.add("https://firebasestorage.googleapis.com/v0/b/mafqodati1.appspot.com/o/posts%2FCdFHpfUrAiWv4ynxxZ8falQybDI3%2FWhatsApp%20Image%202023-12-16%20at%206.59.42%20PM.jpeg?alt=media&token=7193259c-369d-4cc4-8c71-3b1e78712e84");

        Post post = new Post();
        post.setCity("Amman");
        post.setContent("ILorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        post.setTitle("Lost Iphone");
        post.setType(1);
        post.setFinished(false);
        post.setCategoryId("aRYRbpOZX60274Kr24FP");
        post.setDistrict("Marka");
        post.setLatitude(31.8888);
        post.setLatitude(31.4444);
        post.setWriterId("CdFHpfUrAiWv4ynxxZ8falQybDI3");
        post.setImagesUri(images);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            post.setCreationDate(LocalDate.now().toString());
        }
        Firestore.writeNewPost(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });
    }
}