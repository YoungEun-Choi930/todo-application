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
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
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


        /* ------------------------ 화면 생성시 오늘 날짜 선택으로 하기 위함 -------------------------- */
        String stringdate = CalendarDay.today().toString().replace("CalendarDay{","").replace("}","");  //오늘날짜

        String[] strdate = stringdate.split("-");               // 날짜 형식을 20210901 과 같이 변환
        if(strdate[1].length() == 1) strdate[1] = "0" + strdate[1];
        if(strdate[2].length() == 1) strdate[2] = "0" + strdate[2];
        sdate = strdate[0] + strdate[1] + strdate[2];

        todoList = getToDoList(sdate);      // to do list 가져오기


        /* -------------------- 달력에서 날짜를 선택하는 경우 해당 날짜의 to do 가져옴----------------------- */
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String stringdate = date.toString().replace("CalendarDay{","").replace("}",""); //선택된 날짜

                String[] strdate = stringdate.split("-");               // 날짜 형식을 20210901 과 같이 변환
                if(strdate[1].length() == 1) strdate[1] = "0" + strdate[1];
                if(strdate[2].length() == 1) strdate[2] = "0" + strdate[2];
                sdate = strdate[0] + strdate[1] + strdate[2];

                todoList = getToDoList(sdate);          // to do list 가져오기

                toDoAdapter.setList(todoList);
                toDoAdapter.notifyDataSetChanged();     // 화면 새로고침

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

    /* -------------------------- 선택한 날짜에 대한 to do 목록 불러오기 ------------------------------ */
    private HashMap<String, Object> getToDoList(String date) {
        // sqlite에서 정보 가져오기
        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<LectureInfo> lecturelist = helper.loadLectureList(date);
        List<AssignmentInfo> assignmentList = helper.loadAssignmentList(date);
        List<ExamInfo> examlist = helper.loadExamList(date);

        // HashMap으로 가공
        // Key: SubjectName, Value: List<List>(lectureList, assignmnetList, examList)
        HashMap<String, Object> map = new HashMap<>();

        //lecture
        for (LectureInfo lectureInfo : lecturelist) {
            if(map.containsKey(lectureInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(lectureInfo.getSubjectName());
                List<LectureInfo> lecture = todolist.get(0);
                lecture.add(lectureInfo);
                todolist.set(0,lecture);
                map.put(lectureInfo.getSubjectName(), todolist);
            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                lecture.add(lectureInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(lectureInfo.getSubjectName(),todolist);
            }
        }

        //assignment
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

        //exam
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

    /* --------------------------- 선택한 과목에 대하여 과제를 추가하기 -------------------------------- */
    public boolean addAssignment(String subjectName, String assignmentName, String startDate, String startTime, String endDate, String endTime) {
        // sqlite insert
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO AssignmentList VALUES('" +
                subjectName+"','"+assignmentName+"',"+startDate+","+startTime+","+endDate+","+endTime+",0);";
        boolean result = helper.executeQuery(query);

        // firebase insert
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyAssignment(subjectName,assignmentName, startDate, startTime, endDate, endTime);

        // 과목에 알림이 설정되어 있다면 추가한 시험에 대하여 알림 설정
        AlarmInfo alarmInfo = helper.loadAlarm(subjectName);
        if(alarmInfo == null)   return result;              // 알림이 설정되어 있지 않으면 종료

        int hourNum = 0;
        switch (alarmInfo.getAssignmentAlarmDate()) {
            case "1시간 전": hourNum = 1; break;
            case "2시간 전": hourNum = 2; break;
            case "3시간 전": hourNum = 3; break;
            case "5시간 전": hourNum = 4; break;
        }
        AlarmManagementActivity activity = new AlarmManagementActivity();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        activity.alarmManager=alarmManager;
        activity.addSystemAlarm(subjectName, assignmentName,endDate+endTime, hourNum,"Lecture");

        return result;
    }

    /* ------------------------------------ 선택한 과제 삭제하기 ------------------------------------ */
    public boolean delAssignment(String assignmentName, String subjectName) {
        // sqlite delete
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM AssignmentList WHERE assignmentName = '"+assignmentName+"';";
        boolean result = helper.executeQuery(query);

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyAssignment(assignmentName, subjectName);

        // 알림이 존재한다면 삭제
        int num = helper.loadAlarmNum(assignmentName);
        if(num != -1){
            //system delete
            AlarmManagementActivity activity = new AlarmManagementActivity();
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);     //여기 에러
            activity.notificationManager = notificationManager;
            activity.alarmManager=alarmManager;
            activity.delSystemAlarm(assignmentName);

            // sqlite delete
            query = "DELETE FROM AlarmList WHERE number = "+num+";";
            helper.executeQuery(query);
        }

        return result;
    }

    /* --------------------------- 선택한 과목에 대하여 시험을 추가하기 -------------------------------- */
    public boolean addExam(String subjectName, String examName, String date, String time) {
        // sqlite insert
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO ExamList VALUES('" +
                subjectName+"','"+examName+"',"+date+","+time+");";
        boolean result = helper.executeQuery(query);

        // firebase insert
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyExam(subjectName,examName, date, time);

        // 과목에 알림이 설정되어 있다면 추가한 시험에 대하여 알림 설정
        AlarmInfo alarmInfo = helper.loadAlarm(subjectName);
        if(alarmInfo == null)   return result;              // 알림이 설정되어 있지 않으면 종료

        int hourNum = 0;
        switch (alarmInfo.getExamAlarmDate()) {
            case "1일 전": hourNum = 24; break;
            case "3일 전": hourNum = 24*3; break;
            case "5일 전": hourNum = 24*5; break;
            case "7일 전": hourNum = 24*7; break;
        }
        System.out.println(time+"시험시간이 왜이러지");
        System.out.println(date+"시험날짜는 어케나오나");
        String formattime = null;
        if(time.length()==3) // 10시 이전은 시가 한글자라서 총 4글자가 되게 맞춰줌
            formattime="0"+time;
        AlarmManagementActivity activity = new AlarmManagementActivity();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        activity.alarmManager=alarmManager;
        activity.addSystemAlarm(subjectName, examName, date+formattime, hourNum, "Exam");

        return result;
    }

    /* ---------------------------------- 선택한 시험 삭제하기 -------------------------------------- */
    public boolean delExam(String examName, String subjectName) {
        // sqlite delete
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM ExamList WHERE examName = '"+examName+"';";
        boolean result = helper.executeQuery(query);

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyExam(examName, subjectName);

        // 알림이 존재한다면 삭제
        int num = helper.loadAlarmNum(examName);
        if(num != -1){
            //system delete
            AlarmManagementActivity activity = new AlarmManagementActivity();
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);     //여기 에러
            activity.notificationManager = notificationManager;
            activity.alarmManager=alarmManager;
            activity.delSystemAlarm(examName);

            // sqlite delete
            query = "DELETE FROM AlarmList WHERE number = "+num+";";
            helper.executeQuery(query);
        }

        return result;
    }

    /* --------------------------- 선택한 강의 또는 과제 완료 구분 변경하기 ---------------------------- */
    public boolean changeIsDone(String name, String subjectName, String table, int value) {
        // name: lectureName or assignmentName
        // subjectName: 해당 lecture 또는 assignment의 과목이름.
        // table: "Lecture" or "Assignment"
        // value: 바꿔야하는 isDone 값. 만약 지금 1이면 0을 보내고, 0이면 1을 보내주어야 함.

        // sqlite update
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "UPDATE "+table+"List SET isDone = "+value+" WHERE "+table.toLowerCase()+"Name = '"+name+"';";
        boolean result = helper.executeQuery(query);

        // firebase update
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.changeMyIsDone(name, subjectName, table, value);

        // 과목에 알림이 설정되어 있다면 알림 설정을 바꿔주어야함.
        AlarmInfo alarmInfo = helper.loadAlarm(subjectName);
        if(alarmInfo == null)   return result;     // 알림이 설정되어 있지 않으면 종료

        //알림이 설정되 있다면
        AlarmManagementActivity activity = new AlarmManagementActivity();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);     //여기 에러
        activity.notificationManager = notificationManager;
        activity.alarmManager=alarmManager;

        //isDone을 false로 설정해야하면
        if(value == 0) activity.delSystemAlarm(name);
        else {  //isDone을 true로 설정해야하면
            String alarmTime = helper.getAlarmTime(name, subjectName, table);

            String num = "";
            if(table.equals("Lecture")) num = alarmInfo.getVideoLectureAlarmDate();
            else if(table.equals("Assignment")) num = alarmInfo.getAssignmentAlarmDate();

            int hourNum = 0;
            switch (num) {
                case "1일 전": hourNum = 24; break;
                case "3일 전": hourNum = 24*3; break;
                case "5일 전": hourNum = 24*5; break;
                case "7일 전": hourNum = 24*7; break;
            }


            activity.addSystemAlarm(subjectName, name, alarmTime, hourNum, table);
        }

        return result;
    }

}
