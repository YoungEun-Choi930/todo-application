package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


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
                //날짜 선택되면 날짜정보 디비로 날려서 투두리스트 찾아오면 될 듯???
                String strdate = date.toString().replace("CalendarDay{","").replace("-","").replace("}","");
                List<List> todolist = getToDoList(strdate);
                System.out.println(todolist.get(0));
            }
        });
    }
    private List<List> getToDoList(String date) {
        SQLiteDBAdapter adapter = new SQLiteDBAdapter(getApplicationContext());
        List<LectureInfo> lecturelist = adapter.loadLectureList(date);
        List<AssingmentInfo> assingmentlist = adapter.loadAssingmentList(date);
        List<ExamInfo> examlist = adapter.loadExamList(date);

        List<List> todolist = new ArrayList<>();
        todolist.add(lecturelist);
        todolist.add(assingmentlist);
        todolist.add(examlist);
        return todolist;
    }

}
