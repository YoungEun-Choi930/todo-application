package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);


        Button btn_no = (Button) findViewById(R.id.no_subject); //취소
        Button btn_yes = (Button) findViewById(R.id.yes_subject); // 확인


        EditText alarmtime = findViewById(R.id.alarmtime);
        alarmtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        alarmtime.setText(i+":"+i1);
                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });

              btn_yes.setOnClickListener((view) -> { //확인버튼 누르면


                    if(alarmtime.getText().toString() == null){
                        Toast.makeText(this, "시간을 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean result = ((AlarmManagementActivity) AlarmManagementActivity.mContext).addAlarm(alarmtime.getText().toString(), ((AlarmManagementActivity) AlarmManagementActivity.mContext).alarmNumber++);

                        if (result) {
                            System.out.println("알림추가 성공");
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

    public void setAlarm(View view){
        Calendar subjectAlarm = Calendar.getInstance();
    //    subjectAlarm.set(Calendar.HOUR_OF_DAY, )
    }

}