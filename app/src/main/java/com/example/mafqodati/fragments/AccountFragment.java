package com.example.mafqodati.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mafqodati.R;
import com.example.mafqodati.adapters.RecyclerPostAdapter;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.models.User;
import com.example.mafqodati.util.Auth;
import com.example.mafqodati.util.FireStore;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    private CircleImageView imgUser;
    private TextView tvFullName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        imgUser = view.findViewById(R.id.imgUser);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        loadUserData(Auth.getUserId());
        return view;
    }

    private void loadUserData(String userId) {
        FireStore.getUserData(userId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    User.destruct();
                    User.getInstance().setUserEmail(documentSnapshot.getString("userEmail"));
                    User.getInstance().setUserFirstName(documentSnapshot.getString("userFirstName"));
                    User.getInstance().setUserLastName(documentSnapshot.getString("userLastName"));
                    User.getInstance().setUserProfileImgURL(documentSnapshot.getString("userProfileImgURL"));
                    User.getInstance().setUserPhone(documentSnapshot.getString("userPhone"));
                    User.getInstance().setAdmin(Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin")));
                    User.getInstance().setRegisterTime(documentSnapshot.getLong("registerTime"));
                    User.getInstance().setFcmToken(documentSnapshot.getString("fcmToken"));

                    tvFullName.setText(User.getInstance().getUserFirstName() + " " + User.getInstance().getUserLastName());
                    tvEmail.setText(User.getInstance().getUserEmail());
                    tvPhone.setText(User.getInstance().getUserPhone());
                    Glide.with(getContext())
                            .load(User.getInstance().getUserProfileImgURL())
                            .into(imgUser)
                            ;
                } else {
                    // User data doesn't exist or there was an error
                    // Handle the case accordingly
                }
            }
        });
    }

}