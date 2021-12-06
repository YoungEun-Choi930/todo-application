package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {

    Spinner sp_subject, sp_exam, sp_assignment, sp_lecture;
    ArrayAdapter subAdapter;
    String selected_sub;
    String selected_exam;
    String selected_assignment;
    String selected_lecture;
    List<SubjectInfo> subjectList;
    List<String> sub=new ArrayList<>();
    String[] dayList = {"1일 전", "3일 전","5일 전","7일 전"};
    String[] hourList = {"1시간 전", "2시간 전", "3시간 전", "5시간 전", "1일 전"};
    ArrayAdapter hourAdapter, dayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        SubjectManagement management =  new SubjectManagement();
        subjectList = management.getSubjectList();

        Button btn_no =  findViewById(R.id.no_subject); //취소
        Button btn_yes = findViewById(R.id.yes_subject); // 확인
        for(int i=0;i<subjectList.size();i++){
            sub.add(subjectList.get(i).getSubjectName());
        }

        sp_subject = findViewById(R.id.spinner_sub); //과목 스피너
        subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sub);
        sp_subject.setAdapter(subAdapter);
        if(subjectList.size()>0){
            selected_sub = subjectList.get(0).getSubjectName();
        }
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(sub.get(i));
                selected_sub = sub.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_assignment = findViewById(R.id.spinner_assignment); //과제 알림시간 스피너
        hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hourList);
        sp_assignment.setAdapter(hourAdapter);
        selected_assignment = hourList[0];
        sp_assignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(hourList[i]);
                selected_assignment = hourList[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_exam =  findViewById(R.id.spinner_exam); //시험 알림시간 스피너
        dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayList);
        sp_exam.setAdapter(dayAdapter);
        selected_exam = dayList[0];
        sp_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(dayList[i]);
                selected_exam = dayList[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        sp_lecture = findViewById(R.id.spinner_lecture); //강의 알림시간 스피너
        sp_lecture.setAdapter(hourAdapter);
        selected_lecture = hourList[0];
        sp_lecture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(hourList[i]);
                selected_lecture = hourList[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

                btn_yes.setOnClickListener((view) -> { //확인버튼 누르면


                    if(selected_sub == null){
                        Toast.makeText(this, "과목이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlarmManagement alarmManagement = new AlarmManagement();
                        boolean result = alarmManagement.addAlarm(selected_sub, selected_exam, selected_assignment, selected_lecture);

                        if (result) {
                            Toast.makeText(this, "알림추가 완료", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "알림추가 실패", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                });
        btn_no.setOnClickListener((view) -> { // 취소버튼 선택
            finish();
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

}