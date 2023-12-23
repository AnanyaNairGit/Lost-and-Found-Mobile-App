package com.example.mafqodati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mafqodati.models.User;

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
    }

    public void toPickNewImage(View view) {
        User.getInstance().setUserFirstName(etFirstName.getText().toString().trim());
        User.getInstance().setUserLastName(etLastName.getText().toString().trim());
        User.getInstance().setUserEmail(etEmail.getText().toString().trim());
        User.getInstance().setUserPhone(etPhone.getText().toString().trim());
    }
}