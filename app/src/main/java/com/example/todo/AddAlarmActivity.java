package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {

    Spinner sp_subject, sp_exam, sp_assignment, sp_lecture;
    ArrayAdapter subAdapter;
    String selected_sub;
    String selected_exam;
    String selected_assignment;
    String selected_lecture;
    String lecturetype;
    List<SubjectInfo> subjectList;
    List<String> sub=new ArrayList<>();
    String[] timeList = {"10분 전","15분 전","30분 전","1시간 전"};
    String[] dayList = {"1일 전", "3일 전","5일 전","7일 전"};
    String[] hourList = {"1시간 전", "2시간 전", "3시간 전", "5시간 전", "1일 전"};
    ArrayAdapter hourAdapter, timeAdapter, dayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        subjectList = SubjectManagementActivity.getSubjectList();
        System.out.println(subjectList.size()+"리스트에몇개들었니?");

        Button btn_no = (Button) findViewById(R.id.no_subject); //취소
        Button btn_yes = (Button) findViewById(R.id.yes_subject); // 확인
        for(int i=0;i<subjectList.size();i++){
            sub.add(subjectList.get(i).getSubjectName());
        }

        sp_subject = (Spinner) findViewById(R.id.spinner_sub); //과목 불러와서 고르는 스피너
        subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sub);
        sp_subject.setAdapter(subAdapter);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(sub.get(i));
                //Toast.makeText(getApplicationContext(),arrayList.get(i),Toast.LENGTH_SHORT).show();
                selected_sub = sub.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        sp_assignment = (Spinner) findViewById(R.id.spinner_assignment); //과제알림시간설정
        hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hourList);
        sp_assignment.setAdapter(hourAdapter);
        sp_assignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(hourList[i]);
                //Toast.makeText(getApplicationContext(),arrayList.get(i),Toast.LENGTH_SHORT).show();
                selected_assignment = hourList[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_exam = (Spinner) findViewById(R.id.spinner_exam); //시험알림시간설정
        dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayList);
        sp_exam.setAdapter(dayAdapter);
        sp_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(dayList[i]);
                //Toast.makeText(getApplicationContext(),arrayList.get(i),Toast.LENGTH_SHORT).show();
                selected_exam = dayList[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        RadioGroup lecture = (RadioGroup) findViewById(R.id.lecture);
        sp_lecture = (Spinner) findViewById(R.id.spinner_lecture);
        lecture.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                               public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                                   int id = lecture.getCheckedRadioButtonId(); //선택되어있는 라디오버튼 가져옴
                                                   RadioButton start = (RadioButton) findViewById(id);
                                                   lecturetype = start.getResources().getResourceName(id);


                                                   String[] split = lecturetype.split("/");
                                                   lecturetype = split[1];
                                                   System.out.println(lecturetype+"강의유형뭐임?");


                                                   if (lecturetype.equals("realtime")) {//그게 실시간(대면)이면
                                                       sp_lecture.setAdapter(timeAdapter);
                                                       System.out.println("어디니?");
                                                       sp_lecture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                           @Override
                                                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                               ((TextView) view).setText(timeList[i]);
                                                               selected_lecture = timeList[i];
                                                           }

                                                           @Override
                                                           public void onNothingSelected(AdapterView<?> adapterView) {
                                                           }
                                                       });
                                                       //result = ((AlarmManagementActivity)AlarmManagementActivity.mContext).addAlarm(selected_sub,selected_exam,selected_assignment,"",selected_lecture);

                                                   } else {
                                                       sp_lecture.setAdapter(hourAdapter);
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
                                                       // result = ((AlarmManagementActivity)AlarmManagementActivity.mContext).addAlarm(selected_sub,selected_exam,selected_assignment,selected_lecture,"");
                                                   }
                                               }
                                           });


                btn_yes.setOnClickListener((view) -> { //확인버튼 누르면
                    boolean result;
                    if (lecturetype.equals("realtime")) {
                        result = ((AlarmManagementActivity) AlarmManagementActivity.mContext).addAlarm(selected_sub, selected_exam, selected_assignment, "", selected_lecture);
                    } else
                        result = ((AlarmManagementActivity) AlarmManagementActivity.mContext).addAlarm(selected_sub, selected_exam, selected_assignment, selected_lecture, "");

                    if (result) {
                        Toast.makeText(this, "알림추가 완료", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "알림추가 실패", Toast.LENGTH_SHORT).show();
                    finish();
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