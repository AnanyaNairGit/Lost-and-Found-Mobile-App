package com.example.mafqodati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFirstName ;
    private EditText etLastName ;
    private EditText etEmail ;
    private EditText etPassword ;
    private EditText etPhone ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
    }

    public void toPickNewImage(View view) {
        String email = etEmail.getText().toString().trim() ;
        String password = etPassword.getText().toString().trim() ;
        User.getInstance().setUserFirstName(etFirstName.getText().toString().trim());
        User.getInstance().setUserLastName(etLastName.getText().toString().trim());
        User.getInstance().setUserEmail(email);
        User.getInstance().setUserPhone(etPhone.getText().toString().trim());
        User.getInstance().setAdmin(false);
        User.getInstance().setRegisterTime(System.currentTimeMillis());
        Auth.createUser(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FireStore.writeNewUser(authResult.getUser().getUid() , User.getInstance())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                              startActivity(new Intent(RegistrationActivity.this , PickProfilePicture.class));
                            }
                        }); 
            }
        });
    }
}