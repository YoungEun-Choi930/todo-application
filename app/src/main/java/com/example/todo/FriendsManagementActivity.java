package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class FriendsManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);

        Intent getintent = getIntent();
    }
    public List<String> getFriendsList_sqlite(){
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        List<String> list = adapter.loadFriendsList();
        return list;
    }
//    public List<String> getFriendsList_firebase(String userID){
//
//    }
//    public List<String> getFriendsRequestList_firebase(){
//
//    }


}