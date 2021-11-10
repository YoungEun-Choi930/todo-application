package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(myToolbar);

        Intent getintent = getIntent();
        userID = getintent.getExtras().getString("userID");


        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        Button btn_subject = (Button) findViewById(R.id.btn_subject);
        Button btn_friend = (Button) findViewById(R.id.btn_friend);
        Button btn_alarm = (Button) findViewById(R.id.btn_alarm);
        Button btn_month = (Button) findViewById(R.id.btn_month);


        btn_subject.setOnClickListener((view) -> { // 과목관리버튼 선택
            Intent intent = new Intent(getApplicationContext(), SubjectManagementActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);

        });
        btn_friend.setOnClickListener((view) -> { // 친구관리버튼 선택
            Intent intent = new Intent(getApplicationContext(), FriendsManagementActivity.class);
            intent.putExtra("userID",userID);
            startActivity(intent);

        });
        btn_alarm.setOnClickListener((view) -> { // 알림관리버튼 선택
            Intent intent = new Intent(getApplicationContext(), AlarmManagementActivity.class);
            startActivity(intent);

        });
        btn_month.setOnClickListener((view) -> { // 월별주별버튼 선택
            if(btn_month.getText()=="주별"){
                btn_month.setText("월별");
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            }
            else{
                btn_month.setText("주별");
                materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //CalendarDay -> String으로 변환
                String stringdate = date.toString().replace("CalendarDay{","").replace("}","");
                String[] strdate = stringdate.split("-");

                String sdate = strdate[0];
                if(strdate[1].length() == 1)
                    sdate += "0";
                sdate += strdate[1];

                if(strdate[2].length() == 1)
                    sdate += "0";
                sdate += strdate[2];

                List<List> todolist = getToDoList(sdate);
                List<LectureInfo> lectureInfos = todolist.get(0);
                for(LectureInfo info: lectureInfos)
                    System.out.println(info.getSubjectName());
            }
        });
    }
    private List<List> getToDoList(String date) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        List<LectureInfo> lecturelist = adapter.loadLectureList(date);
        List<AssingmentInfo> assingmentlist = adapter.loadAssingmentList(date);
        List<ExamInfo> examlist = adapter.loadExamList(date);

        List<List> todolist = new ArrayList<>();
        todolist.add(lecturelist);
        todolist.add(assingmentlist);
        todolist.add(examlist);
        return todolist;
    }

    private boolean addAssingment(String subjectName, String assingmentName, String startDate, String startTime, String endDate, String endTime) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        String query = "INSERT INTO AssingmentList VALUES('" +
                subjectName+"','"+assingmentName+"',"+startDate+","+startTime+","+endDate+","+endTime+",0);";
        boolean result = adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        firebaseDB.uploadMyAssingment(subjectName,assingmentName, startDate, startTime, endDate, endTime);

        return result;
    }

    private boolean delAssingment(String assingmentName) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        String query = "DELETE FROM AssingmentList WHERE assingmentName = '"+assingmentName+"';";

        boolean result = adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        //firebaseDB.delMyAssigment(String assingmentName);

        return result;
    }

    private boolean addExam(String subjectName, String examName, String date, String time) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        String query = "INSERT INTO ExamList VALUES('" +
                subjectName+"','"+examName+"',"+date+","+time+");";
        boolean result = adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        firebaseDB.uploadMyExam(subjectName,examName, date, time);

        return result;
    }

    private boolean delExam(String examName) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        String query = "DELETE FROM ExamList WHERE examName = '"+examName+"';";

        boolean result = adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        //firebaseDB.delMyExam(String examName);

        return result;
    }

    // name: lectureName or assingmentName
    // table: "Lecture" or "Assingment" 로 보내주세요
    // value: 바꿔야하는 isDone 값. 만약 지금 1이면 0을 보내고, 0이면 1을 보내주어야 함.
    private boolean changeIsDone(String name,String table, int value) {
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        String query = "UPDATE "+table+"List SET isDone = "+value+" WHERE "+table.toLowerCase()+"Name = '"+name+"';";
        boolean result = adapter.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper(userID);
        //firebaseDB.changMyIsDone(String name, String table, int value);

        return result;
    }

}
