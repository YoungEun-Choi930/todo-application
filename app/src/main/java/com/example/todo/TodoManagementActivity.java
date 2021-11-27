package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TodoManagementActivity extends AppCompatActivity {
    private HashMap<String, Object> todoList;
    public ToDoAdapter toDoAdapter;
    public static TodoManagementActivity mContext;
    String sdate;
    MaterialCalendarView materialCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        mContext=this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(myToolbar);

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.setDynamicHeightEnabled(true);
        Button btn_subject = (Button) findViewById(R.id.btn_subject);
        Button btn_friend = (Button) findViewById(R.id.btn_friend);
        Button btn_alarm = (Button) findViewById(R.id.btn_alarm);
        Button btn_month = (Button) findViewById(R.id.btn_month);

        todoList = new HashMap<>();


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

        String stringdate = CalendarDay.today().toString().replace("CalendarDay{","").replace("}","");
        String[] strdate = stringdate.split("-");
        sdate = strdate[0];      //20210901 이런식으로 바꿈
        if(strdate[1].length() == 1)
            sdate += "0";
        sdate += strdate[1];
        if(strdate[2].length() == 1)
            sdate += "0";
        sdate += strdate[2];
        todoList = getToDoList(sdate);
         //들어가면 오늘날짜 선택된채로 리스트 띄울라고

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //CalendarDay -> String으로 변환
                String stringdate = date.toString().replace("CalendarDay{","").replace("}","");
                String[] strdate = stringdate.split("-");

                sdate = strdate[0];      //20210901 이런식으로 바꿈
                if(strdate[1].length() == 1)
                    sdate += "0";
                sdate += strdate[1];

                if(strdate[2].length() == 1)
                    sdate += "0";
                sdate += strdate[2];

                todoList = getToDoList(sdate);

                toDoAdapter.setList(todoList);
                toDoAdapter.notifyDataSetChanged();


            }
        });

        RecyclerView recyclerView = findViewById(R.id.rcy_todolist);
        toDoAdapter = new ToDoAdapter(this, todoList,0);
        recyclerView.setAdapter(toDoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        toDoAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    @Override
    protected void onResume() {
        super.onResume();
        todoList = getToDoList(sdate);
        toDoAdapter.setList(todoList);
        toDoAdapter.notifyDataSetChanged();
    }

    private HashMap<String, Object> getToDoList(String date) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<LectureInfo> lecturelist = helper.loadLectureList(date);
        List<AssignmentInfo> assignmentList = helper.loadAssignmentList(date);
        List<ExamInfo> examlist = helper.loadExamList(date);

        HashMap<String, Object> map = new HashMap<>();


        for (LectureInfo lectureInfo : lecturelist) {
            if(map.containsKey(lectureInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(lectureInfo.getSubjectName());
                List<LectureInfo> lecture = todolist.get(0);
                lecture.add(lectureInfo);
                System.out.println(lectureInfo.getLectureName()+"강의이름뭐임");
                todolist.set(0,lecture);
                map.put(lectureInfo.getSubjectName(), todolist);
            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();
                System.out.println(lectureInfo.getLectureName()+"강의이름");

                lecture.add(lectureInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(lectureInfo.getSubjectName(),todolist);
            }
        }


        for(AssignmentInfo assignmentInfo: assignmentList) {
            if(map.containsKey(assignmentInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(assignmentInfo.getSubjectName());
                List<AssignmentInfo> lecture = todolist.get(1);
                lecture.add(assignmentInfo);
                todolist.set(1,lecture);
                map.put(assignmentInfo.getSubjectName(), todolist);

            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                assignment.add(assignmentInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(assignmentInfo.getSubjectName(),todolist);
            }
        }

        for(ExamInfo examInfo: examlist) {
            if(map.containsKey(examInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(examInfo.getSubjectName());
                List<ExamInfo> lecture = todolist.get(2);
                lecture.add(examInfo);
                todolist.set(2,lecture);
                map.put(examInfo.getSubjectName(), todolist);

            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                exam.add(examInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(examInfo.getSubjectName(),todolist);
            }
        }

        return map;
    }

    public boolean addAssignment(String subjectName, String assignmentName, String startDate, String startTime, String endDate, String endTime) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO AssignmentList VALUES('" +
                subjectName+"','"+assignmentName+"',"+startDate+","+startTime+","+endDate+","+endTime+",0);";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyAssignment(subjectName,assignmentName, startDate, startTime, endDate, endTime);
        System.out.println("과제추가하러들어왔음");
        return result;
    }

    public boolean delAssignment(String assignmentName, String subjectName) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM AssignmentList WHERE assignmentName = '"+assignmentName+"';";

        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyAssignment(assignmentName, subjectName);

        return result;
    }

    public boolean addExam(String subjectName, String examName, String date, String time) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO ExamList VALUES('" +
                subjectName+"','"+examName+"',"+date+","+time+");";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyExam(subjectName,examName, date, time);

        return result;
    }

    public boolean delExam(String examName, String subjectName) {
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
    public boolean changeIsDone(String name, String subjectName, String table, int value) {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "UPDATE "+table+"List SET isDone = "+value+" WHERE "+table.toLowerCase()+"Name = '"+name+"';";
        boolean result = helper.excuteQuery(query);

        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.changeMyIsDone(name, subjectName, table, value);
        System.out.println("체크업데이트");
        return result;
    }

}
