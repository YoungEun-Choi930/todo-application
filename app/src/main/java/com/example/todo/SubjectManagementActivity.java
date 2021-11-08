package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    public static Context mContext;
    List<SubjectInfo> subjectlist;
    subjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;

        setContentView(R.layout.activity_subject_management);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.subject_toolbar);
        setSupportActionBar(myToolbar);//툴바달기

        subjectlist = getSubjectList();

        RecyclerView recyclerView = findViewById(R.id.recy_sub);
        subjectAdapter = new subjectAdapter((ArrayList<SubjectInfo>) subjectlist);
        recyclerView.setAdapter(subjectAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        subjectAdapter.notifyDataSetChanged();



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
                startActivity(intent);
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
            subjectAdapter.notifyDataSetChanged();

        }
    }

    public List<SubjectInfo> getSubjectList() {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }
    //+2021년(int) 2학기(int:1 or 2) 정보도 받아와야 할 듯
    //startWeekNumber 넘겨줄때 1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토 로 넘겨주세요
    public boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime, int year, int semester) {
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);

        if(result == false)
            return false;

        int startdate = getstartDate(year, semester, startWeekNumber);
        int enddate = startWeekNumber+7-endWeekNumber;

        //강의 추가
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < number; j++)
            {
                query = "INSERT INTO LectureList VALUES('" +
                        subjectName+"','"+subjectName+i+"주차"+j+"',"+startdate+","+startdate+enddate+",0);";
                result = adapter.excuteQuery(query);
                System.out.println(query);
                if(result == false) {
                    query = "DELETE FROM SubjectList WHERE subjectName = '" + subjectName + "';";
                    adapter.excuteQuery(query);
                    return false;
                }
                startdate += 7;
            }
        }
        SubjectInfo subjectInfo = new SubjectInfo();
        subjectInfo.setSubjectName(subjectName);
        subjectlist.add(subjectInfo);
        subjectAdapter.notifyDataSetChanged();

        return result;
    }
    private int getstartDate(int year, int semester, int startWeekNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd") ;
        String date = Integer.toString(year);
        if(semester == 1)
            date += "0301";
        else if(semester == 2)
            date += "0901";

        try {
            java.util.Date nDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            while (dayNum != startWeekNumber) {
                cal.add(Calendar.DATE, 1);
                dayNum = cal.get(Calendar.DAY_OF_WEEK);
            }

            SimpleDateFormat resultFormat = new SimpleDateFormat("yyyyMMdd");
            String result = resultFormat.format(cal.getTime());
            return Integer.parseInt(result);


        }catch(ParseException e){
            return 0;
        }
    }
    public boolean delSubject(String subjectName) {
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
}