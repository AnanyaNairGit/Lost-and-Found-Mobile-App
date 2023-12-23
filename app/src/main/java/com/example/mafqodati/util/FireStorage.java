package com.example.mafqodati.util;

import android.net.Uri;

import com.example.mafqodati.models.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireStorage {
    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    public static StorageReference postRef = getStorageInstance().getReference().child("posts");
    public static StorageReference userPostsRef = postRef.child(Auth.getUserId() + "-" + Auth.getCurrentUser().getEmail());

    public static Task<byte[]> getImageFromURL(String URL) {
        StorageReference profileReference = getStorageInstance().getReferenceFromUrl(URL);
        return profileReference.getBytes(1500000);
    }


    public static Task<UploadTask.TaskSnapshot> uploadPostImages(String postId) {
        Task<UploadTask.TaskSnapshot> tasks = null;
        for(int i = 0 ; i < Post.getInstance().getImagesUri().size() ; i++){
            StorageReference imageName = userPostsRef.child(postId).child(postId +System.currentTimeMillis() );
            int finalI = i;
            tasks = imageName.putFile(Uri.parse(Post.getInstance().getImagesUri().get(i))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Post.getInstance().getImagesUri().set(finalI, String.valueOf(uri));
                            FireStore.postRef(postId).update("imagesUri" , Post.getInstance().getImagesUri());
                        }
                    });
                }
            });

        }

        return tasks;
    }

}
