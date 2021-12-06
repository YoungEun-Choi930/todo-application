package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {
  //  private static final int REQUEST_CODE = 0;
    public static List<SubjectInfo> subjectlist;
    public static subjectAdapter subjectAdapter;
    Button btn_del_sub;
    int ck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        SubjectManagement management = new SubjectManagement();
        subjectlist = management.getSubjectList();

        RecyclerView recyclerView = findViewById(R.id.recy_sub);
        subjectAdapter = new subjectAdapter((ArrayList<SubjectInfo>) subjectlist);
        recyclerView.setAdapter(subjectAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        subjectAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        btn_del_sub= (Button)findViewById(R.id.btn_del_sub);
        btn_del_sub.setOnClickListener(view -> { //삭제버튼 선택되면

            for(int i=0;i<subjectAdapter.getcheckedList().size();i++){
                subjectlist.remove(subjectAdapter.getcheckedList().get(i)); //체크된목록 과목목록에서 제거

                management.delSubject(subjectAdapter.getcheckedList().get(i).getSubjectName());
            }
            if(subjectAdapter.getcheckedList().size()>0){
                Toast.makeText(this, "과목삭제 완료", Toast.LENGTH_SHORT).show();
            }
            btn_del_sub.setVisibility(View.GONE);
            btnCheck(0);
            ck=0;
            subjectAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subject_menu, menu); //툴바에 메뉴 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_subject: // 과목추가버튼 누른 경우
                Intent intent = new Intent(getApplicationContext(), AddSubjectActivity.class);
                startActivity(intent);

                break;
            case R.id.del_subject:
                if(ck==0){
                    btnCheck(1);
                    ck=1;
                    btn_del_sub.setVisibility(View.VISIBLE);
                    break;
                }
                else if(ck==1){
                   btnCheck(0);
                   ck=0;
                    btn_del_sub.setVisibility(View.GONE);
                    break;
                }

        }
        subjectAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public void btnCheck(int n){
        subjectAdapter.updateCheckBox(n);
        subjectAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SubjectManagement management = new SubjectManagement();
        subjectlist = management.getSubjectList();
        subjectAdapter.setList(subjectlist);
        subjectAdapter.notifyDataSetChanged();
    }


}