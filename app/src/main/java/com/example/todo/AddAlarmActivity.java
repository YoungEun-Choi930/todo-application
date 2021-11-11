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
    ArrayAdapter arrayAdapter;
    String selected_sub;
    String selected_exam;
    String selected_assignment;
    String selected_lecture;
    String lecturetype;
    List<SubjectInfo> subjectList;
    String[] timeList = {"10분 전","15분 전","30분 전","한 시간 전"};
    String[] dayList = {"하루 전", "3일 전","5일 전","일주일 전"};
    ArrayAdapter timeAdapter, dayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        subjectList = ((SubjectManagementActivity) SubjectManagementActivity.mContext).getSubjectList();
        Button btn_no = (Button) findViewById(R.id.no_subject); //취소
        Button btn_yes = (Button) findViewById(R.id.yes_subject); // 확인

        sp_subject = (Spinner) findViewById(R.id.spinner_sub); //과목 불러와서 고르는 스피너
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjectList);
        sp_subject.setAdapter(arrayAdapter);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(subjectList.get(i).getSubjectName());
                //Toast.makeText(getApplicationContext(),arrayList.get(i),Toast.LENGTH_SHORT).show();
                selected_sub = subjectList.get(i).getSubjectName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_assignment = (Spinner) findViewById(R.id.spinner_assignment); //과제알림시간설정
        timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        sp_assignment.setAdapter(timeAdapter);
        sp_assignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setText(timeList[i]);
                //Toast.makeText(getApplicationContext(),arrayList.get(i),Toast.LENGTH_SHORT).show();
                selected_assignment = timeList[i];
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
                    sp_lecture.setAdapter(dayAdapter);
                    sp_lecture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ((TextView) view).setText(dayList[i]);
                            selected_lecture = dayList[i];
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