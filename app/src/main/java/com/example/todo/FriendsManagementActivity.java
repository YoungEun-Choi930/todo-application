package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
     //firebase에서 정보 가져오면 넣어주고 notify 왜냐면 정보 가져오는데 시간이 걸려서
    private Button btn_del_friend, friends, friends_request, accept_friend;
    private static ArrayList<FriendInfo> friendsList = new ArrayList<>();
    public friendAdapter friendAdapter;
    public AlertDialog dialog;
    private int ck=0;

    public static FriendsManagementActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_friends_management);

        friendAdapter = new friendAdapter(friendsList);
        getFriendsList();   //friendlist에 정보 넣고 notify
        System.out.println("영은5");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.friend_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        RecyclerView recyclerView = findViewById(R.id.recy_friend);

        recyclerView.setAdapter(friendAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

//        friendAdapter.notifyDataSetChanged();


        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        EditText search = findViewById(R.id.et_search);

        friends = findViewById(R.id.friends);
        friends.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

        friends.setOnClickListener(view -> { //서로친구목록
            friends_request.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
            friends.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
            getFriendsList();
            friendAdapter.notifyDataSetChanged();
        });

        friends_request=findViewById(R.id.friends_request); //받은신청목록버튼
        friends_request.setOnClickListener(view -> {
            friends_request.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
            friends.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
            getFriendsRequestList();
            friendAdapter.notifyDataSetChanged();
        });




        btn_del_friend = (Button)findViewById(R.id.btn_del_friend);
        btn_del_friend.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<friendAdapter.getcheckedList().size();i++){
                FriendInfo friendInfo = friendAdapter.getcheckedList().get(i);
                friendsList.remove(friendInfo);  //친구리스트에서 삭제
                delFriend(friendInfo.getFriendName(), friendInfo.getFriendUID()); //디비가 안만들어졌네용
            }

            Toast.makeText(this, "친구 삭제 완료", Toast.LENGTH_SHORT).show();
            btn_del_friend.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            friendAdapter.setData(friendsList);
            friendAdapter.notifyDataSetChanged();
        });
        System.out.println("영은6");

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
               // View dialogView = getLayoutInflater().inflate(R.layout.add_friend, null);
                final EditText et = new EditText(this);
                et.setHint("신청을 보낼 아이디를 입력하세요.");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(et);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestFriend(et.getText().toString());

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
        friendAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void btnCheck(int i) {
        friendAdapter.updateCheckBox(i);
        friendAdapter.notifyDataSetChanged();
    }

    private void getFriendsList(){   //친구목록 불러오기
        System.out.println("-------------getFreindsList-------------");
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendsList();

        System.out.println("영은4");
    }

    public void notifyfriendslist(ArrayList<FriendInfo> list) {
        friendAdapter.setData(list);
        friendsList = list;
        friendAdapter.notifyDataSetChanged();       //왜 화면이 안뜰까
        System.out.println("----notify");


    }

    private void getFriendsRequestList(){    //친구신청 목록 불러오기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendsRequestList();

    }

    public void requestFriend(String friendID) { //친구 ID를 받아서 존재하면 친구신청
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.confirmFriendExist(friendID);
    }

    public void showresult(boolean result) {
        if(result){
            Toast.makeText(FriendsManagementActivity.this, "친구신청 완료", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        }else {
            Toast.makeText(FriendsManagementActivity.this, "대상을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void acceptFriend(String friendID, String friendUID) { //친구 신청 수락하기
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.acceptFriend(friendID, friendUID);
    }

    public void delFriend(String friendID, String friendUID) { //친구 삭제
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delFriend(friendID, friendUID);
    }

    public void getFriendToDoList(String friendID, int date) {    //친구 일정 조회
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendToDoList(friendID, date);
    }


}