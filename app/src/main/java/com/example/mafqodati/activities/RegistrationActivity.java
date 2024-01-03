package com.example.mafqodati.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mafqodati.databinding.ActivityRegistrationBinding;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Constants;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void toPickNewImage(View view) {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.erConfirmPassword.getText().toString();
        String firstName = binding.etFirstName.getText().toString().trim();
        String lastName = binding.etLastName.getText().toString().trim();


        // Validation
        if (firstName.isEmpty()) {
            binding.etFirstName.setError("Required");
            binding.etFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            binding.etLastName.setError("Required");
            binding.etLastName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Required");
            binding.etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Required");
            binding.etPassword.requestFocus();
            return;
        }
        if (!Constants.isValidEmail(email)){
            binding.etEmail.setError("Enter valid email");
            binding.etEmail.requestFocus();
            return;
        }
        if(!Constants.isValidPassword(password)){
            binding.etPassword.setError("Must be more that 8 characters");
            binding.etPassword.requestFocus();
            return;
        }
        if(!Constants.isPasswordMatches(password , confirmPassword)){
            binding.erConfirmPassword.setError("Must be match");
            binding.erConfirmPassword.requestFocus();
            return;
        }
        User.getInstance().setUserFirstName(firstName);
        User.getInstance().setUserLastName(lastName);
        User.getInstance().setUserEmail(email);
        User.getInstance().setAdmin(false);
        User.getInstance().setRegisterTime(System.currentTimeMillis());
        Intent intent = new Intent(this , SendOTPActivity.class) ;
        intent.putExtra("PASSWORD" , password);
        startActivity(intent);
    }
}