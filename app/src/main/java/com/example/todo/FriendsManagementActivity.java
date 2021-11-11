package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class FriendsManagementActivity extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);

        Intent getintent = getIntent();
        userID = getintent.getExtras().getString("userID");
    }
    public List<String> getFriendsList(){   //친구목록 불러오기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        List<String> list = firebaseDB.loadFriendsList();

        return list;
    }
    public List<String> getFriendsRequestList(){    //친구신청 목록 불러오기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        List<String> list = firebaseDB.loadFriendsRequestList();

        return list;
    }

    public boolean requestFriend(String friendID) { //친구 ID를 받아서 존재하면 친구신청, return ture, 존재하지 않으면 return false
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        if(firebaseDB.confirmFriendExist(friendID)) {
            firebaseDB.requestFriend(friendID);
            return true;
        }
        else
            return false;
    }

    public void acceptFriend(String friendID) { //친구 신청 수락하기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        firebaseDB.acceptFriend(friendID);
    }

    public List<List> getFriendToDoList(String friendID, int date) {    //친구 일정 조회
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        return firebaseDB.loadFriendToDoList(friendID, date);
    }


}