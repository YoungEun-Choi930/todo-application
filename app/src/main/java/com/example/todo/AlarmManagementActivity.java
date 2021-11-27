package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmManagementActivity extends AppCompatActivity {
    private List<AlarmInfo> alarmInfoList;
    Button btn_del_alarm;
    private int ck=0;
    alarmAdapter alarmAdapter;
    public static Context mContext;

    //private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_management);
        mContext=this;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        alarmInfoList = getAlarmList();

        RecyclerView recyclerView = findViewById(R.id.recy_alarm);
        alarmAdapter = new alarmAdapter((ArrayList<AlarmInfo>) alarmInfoList);
        recyclerView.setAdapter(alarmAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        alarmAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        btn_del_alarm = (Button)findViewById(R.id.btn_del_alarm);
        btn_del_alarm.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<alarmAdapter.getcheckedList().size();i++){
                alarmInfoList.remove(alarmAdapter.getcheckedList().get(i)); //체크된목록 과목목록에서 제거
                delAlarm(alarmAdapter.getcheckedList().get(i).getSubjectName());
            }
            if(alarmAdapter.getcheckedList().size()>0){
                Toast.makeText(this, "알림 삭제 완료", Toast.LENGTH_SHORT).show();
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
            case R.id.del_alarm:
                if(ck==0){
                    btnCheck(1);
                    ck=1;
                    btn_del_alarm.setVisibility(View.VISIBLE);
                    break;
                }
                else if(ck==1){
                    btnCheck(0);
                    ck=0;
                    btn_del_alarm.setVisibility(View.GONE);
                    break;
                }

        }
        alarmAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void btnCheck(int i) {
        alarmAdapter.updateCheckBox(i);
        alarmAdapter.notifyDataSetChanged();;
    }


    public List<SubjectInfo> getSubjectList() {
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }

    public List<AlarmInfo> getAlarmList(){
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<AlarmInfo> list = adapter.loadAlarmList();
        return list;
    }

    // 리얼빼고 파라미터 4개인걸로 넘겼어용
    public boolean addAlarm(String subjectName, String exam, String assignment, String video, String real){
        String query = "INSERT INTO AlarmList VALUES('"+
                subjectName+"','"+exam+"','"+assignment+"','"+video+"','"+real+"');";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);
        AlarmInfo alarmInfo = new AlarmInfo(false,subjectName,exam,assignment,video,real);
        alarmInfoList.add(alarmInfo);
        alarmAdapter.notifyDataSetChanged();

        setSystemAlarm();
        return result;
    }
    private boolean delAlarm(String subjectName) {
        String query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.excuteQuery(query);

        delSystemAlarm();
        return result;
    }


    private void setSystemAlarm() {

    }

    private void delSystemAlarm() {

    }
}