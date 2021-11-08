package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import java.util.ArrayList;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    List<SubjectInfo> subjectlist;
    subjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        subjectlist = getSubjectList();

        RecyclerView recyclerView = findViewById(R.id.recy_sub);
        subjectAdapter = new subjectAdapter((ArrayList<SubjectInfo>) subjectlist);
        recyclerView.setAdapter(subjectAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.subject_menu, menu); //툴바에 ㅇ메뉴 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_subject: // 과목추가버튼 누른 경우
                Intent intent = new Intent(getApplicationContext(), AddSubjectActivity.class);
                startActivityForResult(intent,0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }


        }
    }

    public List<SubjectInfo> getSubjectList() {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }
    public boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime){
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+",'"+startTime+"',"+endWeekNumber+",'"+endTime+"');";
        System.out.print(query);
//        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
//        boolean result = adapter.excuteQuery(query);
        return true;
    }
    public boolean delSubject(String subjectName) {
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
}