package com.example.todo.friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.todo.R;

import java.util.ArrayList;

public class FriendsManagementActivity extends AppCompatActivity {
     //firebase에서 정보 가져오면 넣어주고 notify 왜냐면 정보 가져오는데 시간이 걸려서
    private Button btn_del_friend, friends, friends_request;
    private static ArrayList<FriendInfo> friendsList;
    public com.example.todo.friends.friendAdapter friendAdapter;
    public AlertDialog dialog;
    private int ck=0;
    private long start;     //목록 출력 시간 계산.


    public static FriendsManagementActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start = System.currentTimeMillis();
        context = this;
        setContentView(R.layout.activity_friends_management);
        friendsList = new ArrayList<>();
        friendAdapter = new friendAdapter(friendsList);

        FriendsManagement management = new FriendsManagement();
        management.getFriendsList();   //friendlist에 정보 넣고 notify

        Toolbar myToolbar = (Toolbar) findViewById(R.id.friend_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        //리사이클러뷰
        RecyclerView recyclerView = findViewById(R.id.recy_friend);

        recyclerView.setAdapter(friendAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        friendAdapter.setOnItemClickListener(new friendAdapter.OnItemClickListener() { //어댑터아이템 클릭되면
            @Override
            public void onItemClick(View v, int pos) {
                String name =friendAdapter.getName(pos);
                String uid = friendAdapter.getUid(pos);

                if(ck==0){ //친구삭제 기능을 사용중이지 않은 경우
                    Intent intent = new Intent(getApplicationContext(), FriendToDoActivity.class);  //친구 일정 조회
                    intent.putExtra("name",name);
                    intent.putExtra("UID",uid);
                    startActivity(intent);
                }
            }
        });
        friends = findViewById(R.id.friends);
        friends.setBackgroundColor(context.getResources().getColor(R.color.purple_200));

        friends.setOnClickListener(view -> { //서로친구목록 버튼
            start = System.currentTimeMillis();
            friends_request.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
            friends.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
            management.getFriendsList();
            friendAdapter.notifyDataSetChanged();
        });

        friends_request=findViewById(R.id.friends_request); //받은친구신청 버튼
        friends_request.setOnClickListener(view -> {
            start = System.currentTimeMillis();
            friends_request.setBackgroundColor(context.getResources().getColor(R.color.purple_200));
            friends.setBackgroundColor(context.getResources().getColor(R.color.purple_500));
            management.getFriendsRequestList();
            friendAdapter.setData(friendsList);
            friendAdapter.notifyDataSetChanged();
        });


        btn_del_friend = (Button)findViewById(R.id.btn_del_friend);
        btn_del_friend.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<friendAdapter.getcheckedList().size();i++){
                FriendInfo friendInfo = friendAdapter.getcheckedList().get(i);
                friendsList.remove(friendInfo);  //친구리스트에서 삭제
                management.delFriend(friendInfo.getFriendName(), friendInfo.getFriendUID());
            }
            if(friendAdapter.getcheckedList().size()>0){
                Toast.makeText(this, "친구삭제 완료", Toast.LENGTH_SHORT).show();
            }
            btn_del_friend.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            friendAdapter.setData(friendsList);
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
            case R.id.add_friend://친구신청버튼
                final EditText et = new EditText(this);
                et.setHint("신청을 보낼 아이디를 입력하세요.");
                AlertDialog.Builder builder = new AlertDialog.Builder(this); //다이얼로그
                builder.setView(et);// 다이얼로그에 editText 넣기
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
                        if(et.getText().toString().equals("")){ //아이디를 입력하지 않은 경우
                            Toast.makeText(FriendsManagementActivity.this,"아이디를 입력하세요",Toast.LENGTH_SHORT).show();
                        }
                        else{ //입력 되었으면 친구요청
                           FriendsManagement management = new FriendsManagement();
                            management.requestFriend(et.getText().toString());
                        }

                    }
                });

                break;

            case R.id.del_friend://친구삭제버튼
                if(ck==0){//삭제기능을 사용중이지 않으면
                    btnCheck(1);//체크박스 보이게
                    ck=1;
                    btn_del_friend.setVisibility(View.VISIBLE);//삭제버튼 보이게
                    break;
                }
                else if(ck==1){//이미 삭제기능 사용중이면
                    btnCheck(0);//체크박스 안보이게
                    ck=0;
                    btn_del_friend.setVisibility(View.GONE);//삭제버튼 안보이게
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

    /* ------------------------------- 친구 목록, 친구 신청 목록 결과 -------------------------------- */
    public void notifyFriendsList(ArrayList<FriendInfo> list) {     //firebase에서 호출하는 메소드
        friendAdapter.setData(list);
        friendsList = list;
        friendAdapter.notifyDataSetChanged();           // 목록 화면 업데이트

        long end = System.currentTimeMillis();

        System.out.println("----------------------------- 친구 목록 조회 화면 출력에 걸린 시간:" + (end - start)/1000.0 +"----------------------------------");


    }

    /* -------------------------------------- 친구 신청 결과 -------------------------------------- */
    public void showResult(int result) {                        //firebase에서 호출하는 메소드
        if(result == 1){
            Toast.makeText(FriendsManagementActivity.this, "친구신청 완료", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        }else if(result == -1){
            Toast.makeText(FriendsManagementActivity.this, "존재하지 않는 사용자 입니다.", Toast.LENGTH_SHORT).show();
        }else if(result == 0){
            Toast.makeText(FriendsManagementActivity.this, "이미 친구인 사용자 입니다.", Toast.LENGTH_SHORT).show();
        }
    }


}