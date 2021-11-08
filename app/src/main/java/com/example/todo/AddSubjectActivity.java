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

        Button btn_no = (Button) findViewById(R.id.no_subject);
        Button btn_yes = (Button) findViewById(R.id.yes_subject);

        EditText name_subject = (EditText) findViewById(R.id.name_subject);
        EditText number_subject = (EditText) findViewById(R.id.number_subject);
        RadioGroup startweek_subject = (RadioGroup)findViewById(R.id.week);
        EditText startTime_subject = (EditText) findViewById(R.id.startTime_subject);
      //  RadioGroup endweek_subject
        EditText endTime_subject = (EditText) findViewById(R.id.endTime_subject);


        btn_yes.setOnClickListener((view) -> { // 확인버튼 선택
            String name_sub = name_subject.getText().toString();
            int number_sub = Integer.parseInt(number_subject.getText().toString());
            //int weeknumber_sub = Integer.parseInt(number_subject.getText().toString());
            String starttime_sub = startTime_subject.getText().toString();
            //int weeknumber_sub = Integer.parseInt(number_subject.getText().toString());
            String endtime_sub = endTime_subject.getText().toString();


            boolean result = addSubject(name_sub,number_sub,1,starttime_sub,2,endtime_sub,2021,2);

            System.out.print(result+"9999");
        });
        btn_no.setOnClickListener((view) -> { // 취소버튼 선택
            finish();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //바깥 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    //+2021년(int) 2학기(int:1 or 2) 정보도 받아와야 할 듯
    //startWeekNumber 넘겨줄때 1:일, 2:월, 3:화, 4:수, 5:목, 6:금, 7:토 로 넘겨주세요
    public boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime, int year, int semester) {
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);

        if(result == false)
            return false;

        int startdate = getstartDate(year, semester, startWeekNumber);
        int enddate = startWeekNumber+7-endWeekNumber;

        //강의 추가
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < number; j++)
            {
                query = "INSERT INTO LectureList VALUES('" +
                        subjectName+"','"+subjectName+i+"주차"+j+"',"+startdate+","+startdate+enddate+",0);";
                result = adapter.excuteQuery(query);
                if(result == false) {
                    query = "DELETE FROM SubjectList WHERE subjectName = '" + subjectName + "';";
                    adapter.excuteQuery(query);
                    return false;
                }
            }
        }

        return result;
    }
    private int getstartDate(int year, int semester, int startWeekNumber) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        String date = Integer.toString(year);
        if(semester == 1)
            date += "0301";
        else if(semester == 2)
            date += "0901";

        try {
            java.util.Date nDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);

            while (dayNum != startWeekNumber) {
                cal.add(Calendar.DATE, 1);
                dayNum = cal.get(Calendar.DAY_OF_WEEK);
            }

            return Integer.parseInt(cal.getTime().toString());
        }catch(ParseException e){
            return 0;
        }
    }

}