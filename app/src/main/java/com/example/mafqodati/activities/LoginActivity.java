package com.example.mafqodati.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mafqodati.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginOnClick(View view) {
        String email = String.valueOf(((EditText) findViewById(R.id.etEmail)).getText());
        String password = String.valueOf(((EditText) findViewById(R.id.etPassword)).getText());

        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // SUCCESS login
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finishAndRemoveTask();
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed login
                            Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void toRegistration(View view) {
        startActivity(new Intent(this , RegistrationActivity.class));
    }
}