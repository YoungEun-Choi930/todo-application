package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class TodoManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(myToolbar);

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        Button btn_subject = (Button) findViewById(R.id.btn_subject);
        Button btn_friend = (Button) findViewById(R.id.btn_friend);
        Button btn_alarm = (Button) findViewById(R.id.btn_alarm);
        Button btn_month = (Button) findViewById(R.id.btn_month);


        btn_subject.setOnClickListener((view) -> { // 과목관리버튼 선택
            Intent intent = new Intent(getApplicationContext(), SubjectManagementActivity.class);
            startActivity(intent);

        });
        btn_friend.setOnClickListener((view) -> { // 친구관리버튼 선택
            Intent intent = new Intent(getApplicationContext(), FriendsManagementActivity.class);
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
        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<LectureInfo> lecturelist = helper.loadLectureList(date);
        List<AssignmentInfo> assignmentList = helper.loadAssignmentList(date);
        List<ExamInfo> examlist = helper.loadExamList(date);

        List<List> todolist = new ArrayList<>();
        todolist.add(lecturelist);
        todolist.add(assignmentList);
        todolist.add(examlist);
        return todolist;
    }

    private boolean addAssignment(String subjectName, String assignmentName, String startDate, String startTime, String endDate, String endTime) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO AssignmentList VALUES('" +
                subjectName+"','"+assignmentName+"',"+startDate+","+startTime+","+endDate+","+endTime+",0);";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyAssignment(subjectName,assignmentName, startDate, startTime, endDate, endTime);

        return result;
    }

    private boolean delAssignment(String assignmentName, String subjectName) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM AssignmentList WHERE assignmentName = '"+assignmentName+"';";

        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyAssignment(assignmentName, subjectName);

        return result;
    }

    private boolean addExam(String subjectName, String examName, String date, String time) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO ExamList VALUES('" +
                subjectName+"','"+examName+"',"+date+","+time+");";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyExam(subjectName,examName, date, time);

        return result;
    }

    private boolean delExam(String examName, String subjectName) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM ExamList WHERE examName = '"+examName+"';";

        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyExam(examName, subjectName);

        return result;
    }

    // name: lectureName or assignmentName
    // subjectName: 말그대로 해당 lecture 또는 assignment의 과목이름.
    // table: "Lecture" or "Assignment" 로 보내주세요
    // value: 바꿔야하는 isDone 값. 만약 지금 1이면 0을 보내고, 0이면 1을 보내주어야 함.
    private boolean changeIsDone(String name, String subjectName, String table, int value) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "UPDATE "+table+"List SET isDone = "+value+" WHERE "+table.toLowerCase()+"Name = '"+name+"';";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.changeMyIsDone(name, subjectName, table, value);

        return result;
    }

}
