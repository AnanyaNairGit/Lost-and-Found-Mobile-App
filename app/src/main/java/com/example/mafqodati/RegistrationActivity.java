package com.example.mafqodati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mafqodati.databinding.ActivityRegistrationBinding;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.Constants;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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