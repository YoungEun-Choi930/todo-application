package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AddSubjectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_subject);

        Button btn_no = (Button) findViewById(R.id.no_subject); //취소
        Button btn_yes = (Button) findViewById(R.id.yes_subject); // 확인

        EditText name_subject = (EditText) findViewById(R.id.name_subject);//과목명
        EditText number_subject = (EditText) findViewById(R.id.number_subject);//강의개수
        RadioGroup startweek_subject = (RadioGroup)findViewById(R.id.startWeekNumber_sujbect);//시작요일
        EditText startTime_subject = (EditText) findViewById(R.id.startTime_subject);//시작시간
        RadioGroup endweek_subject = (RadioGroup)findViewById(R.id.endWeekNumber_sujbect) ;//종료요일
        EditText endTime_subject = (EditText) findViewById(R.id.endTime_subject);//종료시간
        RadioGroup semester_subject = (RadioGroup) findViewById(R.id.semester);//학기
        EditText year_subject = (EditText) findViewById(R.id.year_subject);//년도


      //  1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토

        btn_yes.setOnClickListener((view) -> { // 확인버튼 선택
            int startid = startweek_subject.getCheckedRadioButtonId(); //뭐 선택됐는지 가져옴
            RadioButton start = (RadioButton)findViewById(startid);
            String strStart = start.getResources().getResourceName(startid);

            int endid = endweek_subject.getCheckedRadioButtonId();
            RadioButton end = (RadioButton)findViewById(endid);
            String strEnd = end.getResources().getResourceName(endid);

            int semesterid = semester_subject.getCheckedRadioButtonId();
            RadioButton semester = (RadioButton)findViewById(semesterid);



            String name_sub = name_subject.getText().toString();
            int number_sub = Integer.parseInt(number_subject.getText().toString());
            int startweeknumber_sub = Integer.parseInt(strStart.substring(strStart.length()-1)); //아이디가져와서 마지막글자만 뽑음. 왜냠 그게 요일번호니까
            String starttime_sub = startTime_subject.getText().toString();
            int endweeknumber_sub = Integer.parseInt(strEnd.substring(strEnd.length()-1));
            String endtime_sub = endTime_subject.getText().toString();
            int year_sub =Integer.parseInt(year_subject.getText().toString());
            int semester_sub = Integer.parseInt(semester.getText().toString());

            System.out.println(year_sub+"년도"+semester_sub);


            boolean result = ((SubjectManagementActivity)SubjectManagementActivity.mContext).addSubject(name_sub,number_sub,startweeknumber_sub,starttime_sub,endweeknumber_sub,endtime_sub,year_sub,semester_sub);

            if(result){
                Toast.makeText(this, "과목추가 완료", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "과목추가 실패", Toast.LENGTH_SHORT).show();

            finish();
        });
        btn_no.setOnClickListener((view) -> { // 취소버튼 선택
            finish();
        });

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) { //바깥 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }



}