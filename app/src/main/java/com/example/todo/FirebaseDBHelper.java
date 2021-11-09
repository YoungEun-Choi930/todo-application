package com.example.todo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class FirebaseDBHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    public FirebaseDBHelper(String ID) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userID = ID;
    }

    public boolean confirmFriendExist(String friendID){
        databaseReference.child("USERS");
        return true;

    }
//
//    public List<String> loadFriendsRequestList(String myID){
//
//    }
//
//    public List<String> loadFriendsList(String myID) {
//
//    }
//
//    public boolean requestFriend(String myID, String friendID) {
//
//    }
//
    public boolean uploadMyLecture(String subjectName, String lectureName, String startdate, String enddate){
        HashMap<String, Object> info = new HashMap<>();
        info.put("subjectName", subjectName);
        info.put("startdate", Integer.parseInt(startdate));
        info.put("enddate", Integer.parseInt(enddate));
        info.put("isdone", false);

        HashMap<String,Object> iinfo = new HashMap<>();
        iinfo.put(lectureName, info);

        boolean result = uploadInfo(iinfo, "lecture");
        return result;
    }

    public boolean uploadInfo(HashMap info,String table) {
        final boolean[] result = new boolean[1];
        databaseReference.child("USERS").child(userID).child(table).updateChildren(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        result[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "uploadMyLecture >>"+ e.toString());
                        result[0] = false;
                    }
                });
//        databaseReference.removeEventListener();//???
        return result[0];
    }

    public List<LectureInfo> loadFriendLectureList(String friendID, String date){
        databaseReference.child("USERS").child(friendID).child("lecture").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful())
                    Log.e("Firebase","loadFriendLectureList >>"+ task.getException());
                else{
//                    HashMap<String, Object> list = new HashMap<>(task.getResult().getKey(),task.getResult().getValue());

                }
            }
        });
        return null;

    }
}
