package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {
    SQLiteDBHelper sqlitehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        Button btn_month = (Button) findViewById(R.id.btn_month);

        btn_month.setOnClickListener((view) -> {
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
                List<List> todolist = getToDoList(date.toString().replace("-",""));

            }
        });
    }
    private List<List> getToDoList(String date) {
        List<LectureInfo> lecturelist = sqlitehelper.loadLectureList(date);
        List<AssingmentInfo> assingmentlist = sqlitehelper.loadAssingmentList(date);
        List<ExamInfo> examlist = sqlitehelper.loadExamList(date);

        List<List> todolist = new ArrayList<>();
        todolist.add(lecturelist);
        todolist.add(assingmentlist);
        todolist.add(examlist);
        return todolist;
    }

    //어케하는거여?
}
