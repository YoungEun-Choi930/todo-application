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

        AlarmManagement.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManagement.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


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

        ToDoManagement toDoManagement = new ToDoManagement(mContext);
        todoList = toDoManagement.getToDoList(sdate);
       // todoList = getToDoList(sdate);      // to do list 가져오기


        /* -------------------- 달력에서 날짜를 선택하는 경우 해당 날짜의 to do 가져옴----------------------- */
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String stringdate = date.toString().replace("CalendarDay{","").replace("}",""); //선택된 날짜

                String[] strdate = stringdate.split("-");               // 날짜 형식을 20210901 과 같이 변환
                if(strdate[1].length() == 1) strdate[1] = "0" + strdate[1];
                if(strdate[2].length() == 1) strdate[2] = "0" + strdate[2];
                sdate = strdate[0] + strdate[1] + strdate[2];

                ToDoManagement toDoManagement = new ToDoManagement(mContext);
                todoList = toDoManagement.getToDoList(sdate);
                //todoList = getToDoList(sdate);          // to do list 가져오기
                System.out.println(todoList.size()+"현재과목몇개니");
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
        ToDoManagement toDoManagement = new ToDoManagement(mContext);
        todoList = toDoManagement.getToDoList(sdate);
        toDoAdapter.setList(todoList);
        toDoAdapter.notifyDataSetChanged();
    }


}
