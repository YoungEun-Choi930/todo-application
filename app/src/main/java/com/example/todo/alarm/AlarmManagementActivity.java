package com.example.todo.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.todo.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmManagementActivity extends AppCompatActivity {
    public static List<AlarmInfo> alarmInfoList;
    public static int number;
    Button btn_del_alarm;
    private int ck=0;
    com.example.todo.alarm.alarmAdapter alarmAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        AlarmManagement alarmManagement = new AlarmManagement();
        alarmInfoList = alarmManagement.getAlarmList();

        //리사이클러뷰
        RecyclerView recyclerView = findViewById(R.id.recy_alarm);
        alarmAdapter = new alarmAdapter((ArrayList<AlarmInfo>) alarmInfoList);
        recyclerView.setAdapter(alarmAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        alarmAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        //삭제버튼
        btn_del_alarm = (Button)findViewById(R.id.btn_del_alarm);
        btn_del_alarm.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<alarmAdapter.getcheckedList().size();i++){
                alarmInfoList.remove(alarmAdapter.getcheckedList().get(i)); //체크된 목록 과목목록에서 제거
                alarmManagement.delAlarm(alarmAdapter.getcheckedList().get(i).getSubjectName());
            }
            if(alarmAdapter.getcheckedList().size()>0){ //체크된 목록이 있었으면
                Toast.makeText(this, "알림삭제 완료", Toast.LENGTH_SHORT).show();
            }
            btn_del_alarm.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            alarmAdapter.notifyDataSetChanged();
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.alarm_menu, menu); //툴바에 메뉴 설정
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm: // 알림추가버튼 누른 경우
                Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.del_alarm: //알림삭제버튼 누른 경우
                if(ck==0){//삭제기능을 선택한 경우
                    btnCheck(1); //체크박스 보이게
                    ck=1;
                    btn_del_alarm.setVisibility(View.VISIBLE); //삭제버튼 보이게
                    break;
                }
                else if(ck==1){//다시 눌러서 취소하는 경우
                    btnCheck(0); //체크박스 안보이게
                    ck=0;
                    btn_del_alarm.setVisibility(View.GONE); // 삭제버튼 안보이게
                    break;
                }

        }
        alarmAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void btnCheck(int i) {//체크박스 보이게 함
        alarmAdapter.updateCheckBox(i);
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlarmManagement alarmManagement = new AlarmManagement();
        alarmInfoList = alarmManagement.getAlarmList();
        alarmAdapter.setList((ArrayList<AlarmInfo>) alarmInfoList);
        alarmAdapter.notifyDataSetChanged();

    }

}