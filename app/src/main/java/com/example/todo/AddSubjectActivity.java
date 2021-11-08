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
            System.out.println(name_sub);
            System.out.println(number_sub);
            System.out.println(starttime_sub);
            System.out.println(endtime_sub);


            boolean result = addSubject(name_sub,number_sub,1,starttime_sub,2,endtime_sub);
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
    public boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime){
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }

}