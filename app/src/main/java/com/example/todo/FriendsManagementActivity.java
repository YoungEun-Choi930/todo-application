package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FriendsManagementActivity extends AppCompatActivity {
    List friendsList;
    Button btn_del_friend;
    friendAdapter friendAdapter;
    boolean result;
    private int ck=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.friend_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        friendsList = getFriendsList();


        RecyclerView recyclerView = findViewById(R.id.recy_friend);
        friendAdapter = new friendAdapter((ArrayList<String>) friendsList);
        recyclerView.setAdapter(friendAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        friendAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        btn_del_friend = (Button)findViewById(R.id.btn_del_friend);
        btn_del_friend.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<friendAdapter.getcheckedList().size();i++){
                friendsList.remove(friendAdapter.getcheckedList().get(i).getFriendName());  //친구리스트에서 삭제
              //  delF(friendAdapter.getcheckedList().get(i).getSubjectName()); 디비가 안만들어졌네용
            }

            Toast.makeText(this, "친구 삭제 완료", Toast.LENGTH_SHORT).show();
            btn_del_friend.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            friendAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friend_menu, menu); //툴바에 메뉴 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_friend:
                View dialogView = getLayoutInflater().inflate(R.layout.add_friend, null);
                EditText search = (EditText)findViewById(R.id.et_search);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        result = requestFriend(search.getText().toString());
                        if(result){
                            Toast.makeText(FriendsManagementActivity.this, "친구신청 완료", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }else {
                            Toast.makeText(FriendsManagementActivity.this, "대상을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case R.id.del_friend:
                if(ck==0){
                    btnCheck(1);
                    ck=1;
                    btn_del_friend.setVisibility(View.VISIBLE);
                    break;
                }
                else if(ck==1){
                    btnCheck(0);
                    ck=0;
                    btn_del_friend.setVisibility(View.GONE);
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void btnCheck(int i) {
        friendAdapter.updateCheckBox(i);
        friendAdapter.notifyDataSetChanged();;
    }
    public List<String> getFriendsList(){   //친구목록 불러오기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        List<String> list = firebaseDB.loadFriendsList();

        return list;
    }
    public List<String> getFriendsRequestList(){    //친구신청 목록 불러오기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        List<String> list = firebaseDB.loadFriendsRequestList();

        return list;
    }

    public boolean requestFriend(String friendID) { //친구 ID를 받아서 존재하면 친구신청, return ture, 존재하지 않으면 return false
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        if(firebaseDB.confirmFriendExist(friendID)) {
            firebaseDB.requestFriend(friendID);
            return true;
        }
        else
            return false;
    }

    public void acceptFriend(String friendID) { //친구 신청 수락하기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.acceptFriend(friendID);
    }

    public void delFriend(String friendID) { //친구 삭제
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delFriend(friendID);
    }

    public List<List> getFriendToDoList(String friendID, int date) {    //친구 일정 조회
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        return firebaseDB.loadFriendToDoList(friendID, date);
    }


}