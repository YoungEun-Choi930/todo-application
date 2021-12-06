package com.example.todo;

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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendToDoActivity extends AppCompatActivity {
    MaterialCalendarView materialCalendarView;
    private HashMap<String, Object> friendToDoList;
    public ToDoAdapter toDoAdapter;
    public static FriendToDoActivity context;
    String sdate;
    private long start;     // 일정 목록 출력 시간 계산.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        start = System.currentTimeMillis();

        setContentView(R.layout.activity_friend_to_do);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name"); //선택한친구이름가져오기
        String uid = intent.getStringExtra("UID");


        Toolbar myToolbar = (Toolbar) findViewById(R.id.friendtodo_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(name);

        friendToDoList = new HashMap<>();

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                start = System.currentTimeMillis();
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


                FriendsManagement management = new FriendsManagement();
                management.getFriendToDoList(uid, Integer.parseInt(sdate));


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
        FriendsManagement management = new FriendsManagement();
        management.getFriendToDoList(uid, Integer.parseInt(sdate)); //들어가면 오늘날짜 선택된채로 리스트 띄우려고


        //리사이클러뷰
        RecyclerView recyclerView = findViewById(R.id.rcy_friend_todolist);
        toDoAdapter = new ToDoAdapter(context, friendToDoList,1);
        recyclerView.setAdapter(toDoAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        toDoAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);



    }

    public void notifyFriendToDoList(List<List> list) { //프렌드리스트업데이트
        HashMap<String, Object> map = new HashMap<>();

        List<LectureInfo> lectureList = list.get(0);
        for (LectureInfo lectureInfo : lectureList) {
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

        List<AssignmentInfo> assignmentlist = list.get(1);
        for(AssignmentInfo assignmentInfo: assignmentlist) {
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

        List<ExamInfo> examlist = list.get(2);
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
        toDoAdapter.setList(map);
        toDoAdapter.notifyDataSetChanged();

        long end = System.currentTimeMillis();

        System.out.println("----------------------------- 친구 일정 조회 화면 출력에 걸린 시간:" + (end - start)/1000.0 +"-------------------------------");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friendtodo_menu, menu); //툴바에 메뉴 설정
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_month:
                if(item.getTitle()=="주별"){
                    item.setTitle("월별");
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }
                else{
                    item.setTitle("주별");
                    materialCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                }
        }

        return super.onOptionsItemSelected(item);
    }
}