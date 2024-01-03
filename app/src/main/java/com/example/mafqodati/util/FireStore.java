package com.example.mafqodati.util;

import com.example.mafqodati.models.ChatData;
import com.example.mafqodati.models.Post;
import com.example.mafqodati.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FireStore {
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference postRef() {
      return  getFirestoreInstance().collection("posts");
    }
    public static CollectionReference usersRef() {
      return  getFirestoreInstance().collection("users");
    }

    public static Task<Void> updateUser(String userId , User user) {
        return usersRef().document(userId).set(user);
    }

    public static Task<Void> writeNewUser(String userId, User newUser) {
        return getFirestoreInstance().collection("users").document(userId).set(newUser);
    }

    public static Task<Void> updateUserPicture(String userId, String newUri) {
        return getFirestoreInstance().collection("users").document(userId).update("userProfileImgURL", newUri);
    }


    public static Task<DocumentReference> writeNewPost(Post newPost) {
        return getFirestoreInstance().collection("posts").add(newPost);
    }

    public static Task<Void> updatePost(String postId, Post post) {
        return getFirestoreInstance().collection("posts").document(postId).set(post);
    }

    public static DocumentReference postRef(String postId) {
        return getFirestoreInstance().collection("posts").document(postId);
    }

    public static Task<Void> removeUser(String userId) {
        return getFirestoreInstance().collection("user").document(userId).delete();
    }

    public static Task<Void> removePost(String postId) {
        return getFirestoreInstance().collection("posts").document(postId).delete();
    }

    public static Task<DocumentSnapshot> getUserData(String userId) {
        return getFirestoreInstance().collection("users").document(userId).get();
    }

    public static Task<DocumentReference> addPost(List<String> imageUrls) {
        Post.getInstance().setImagesUri(imageUrls);
        // Save the post to Firestore
        return getFirestoreInstance().collection("posts").add(Post.getInstance());
    }

    public static Task<DocumentSnapshot> getPost(String postId){
        return postRef().document(postId).get();
    }
    public static Query getPostData(int type, DocumentSnapshot startAfter) {
        if (startAfter != null) {
            if (type == 0)
                return getFirestoreInstance().collection("posts").orderBy("writeTime", Query.Direction.DESCENDING).limit(20).startAfter(startAfter);
            else
                return getFirestoreInstance().collection("posts").whereEqualTo("type", type)
                        .orderBy("writeTime", Query.Direction.DESCENDING).limit(20).startAfter(startAfter);
        } else {
            if (type == 0)
                return getFirestoreInstance().collection("posts").orderBy("writeTime", Query.Direction.DESCENDING).limit(20);
            else
                return getFirestoreInstance().collection("posts").whereEqualTo("type", type)
                        .orderBy("writeTime", Query.Direction.DESCENDING).limit(20);
        }
    }

    public static Task<QuerySnapshot> getMyPostData(String userId, DocumentSnapshot startAfter) {
        if (startAfter != null)
            return getFirestoreInstance().collection("posts").whereEqualTo("writerId", userId)
                    .orderBy("writeTime", Query.Direction.DESCENDING).limit(20).startAfter(startAfter).get();
        else
            return getFirestoreInstance().collection("posts").whereEqualTo("writerId", userId)
                    .orderBy("writeTime", Query.Direction.DESCENDING).limit(20).get();
    }


    public static Task<DocumentSnapshot> getPostDataFromId(String docId) {
        return getFirestoreInstance().collection("posts").document(docId).get();
    }


    public static Task<Void> updateProfileNickName(String userId, String nickName) {
        return getFirestoreInstance().collection("users").document(userId).update("userNickName", nickName);
    }

    public static Task<Void> updateProfileImage(String userId, String userProfileImgURL) {
        return getFirestoreInstance().collection("users").document(userId).update("userProfileImgURL", userProfileImgURL);
    }

//    public static Task<DocumentReference> createChatRoom(String myId, String targetId){
//        ArrayList<String> userList= new ArrayList<>(Arrays.asList(myId,targetId));
//        ChatRoom chatRoom = new ChatRoom(userList);
//        return getFirestoreInstance().collection("chatRoom").add(chatRoom);
//    }

    public static Task<DocumentReference> sendChat(String chatId, ChatData chatData) {
        return getFirestoreInstance().collection("chatRoom").document(chatId).collection("chatData").add(chatData);
    }

    public static Task<QuerySnapshot> searchChatRoom(String myId, String targetId) {
        ArrayList<String> userList1 = new ArrayList<>(Arrays.asList(myId, targetId));
        ArrayList<String> userList2 = new ArrayList<>(Arrays.asList(targetId, myId));
        return getFirestoreInstance().collection("chatRoom").whereIn("chatUserId", Arrays.asList(userList1, userList2)).get();
    }

    public static Query getChatDataQuery(String chatRoomId) {
        return getFirestoreInstance().collection("chatRoom").document(chatRoomId).collection("chatData").orderBy("date", Query.Direction.ASCENDING);
    }


    public static Task<QuerySnapshot> getUnfinishedPost(int postType) {
        if (postType == 0) {
            return getFirestoreInstance().collection("posts")
                    .whereEqualTo("finished", false).get();
        } else {
            return getFirestoreInstance().collection("posts")
                    .whereEqualTo("type", postType)
                    .whereEqualTo("finished", false).get();
        }
    }

    public static Task<QuerySnapshot> getUnfinishedBuildingPost(int postType, int buildingNum) {
        if (postType == 0) {
            return getFirestoreInstance().collection("posts")
                    .whereEqualTo("summaryBuildingType", buildingNum)
                    .whereEqualTo("finished", false).get();
        } else {
            return getFirestoreInstance().collection("posts")
                    .whereEqualTo("summaryBuildingType", buildingNum)
                    .whereEqualTo("type", postType)
                    .whereEqualTo("finished", false).get();
        }
    }

    public static Query getMyChatRoom(String userId) {
        return getFirestoreInstance().collection("chatRoom").whereArrayContains("chatUserId", userId);
    }

    public static Task<Void> setUserFcmToken(String userId, String fcmToken) {
        return getFirestoreInstance().collection("user").document(userId).update("fcmToken", fcmToken);
    }

    public static Task<QuerySnapshot> getBuildingPost(int buildingId) {
        return getFirestoreInstance().collection("posts").whereEqualTo("summaryBuildingType", buildingId).get();
    }

    public static Task<QuerySnapshot> getCategories() {
        return getFirestoreInstance().collection("category").get();
    }


}
