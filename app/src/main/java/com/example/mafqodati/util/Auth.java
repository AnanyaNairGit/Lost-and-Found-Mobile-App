package com.example.mafqodati.util;

import android.app.Activity;
import android.content.Intent;

import com.example.mafqodati.activities.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }


    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }
    public static String getCurrentUserId = getCurrentUser().getUid();
    public static void signOut(Activity activity) {
        if (getCurrentUser() != null) {
            getFirebaseAuthInstance().signOut();
        }
        moveToLogin(activity);
    }
    public static String getUserId(){
        return getCurrentUser().getUid();
    }

    public static void moveToLogin(Activity activity) {
        Intent loginIntent = new Intent(activity, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(loginIntent);
        activity.finishAndRemoveTask();
    }
    public static Task<AuthResult> createUser(String email , String password){
        return getFirebaseAuthInstance().createUserWithEmailAndPassword(email , password);
    }
}
