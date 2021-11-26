package com.example.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class AssignmentFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText startdate;
    EditText starttime;
    EditText enddate;
    EditText endtime;
    Button yes, no;
    View view;

    public AssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        startdate = view.findViewById(R.id.startdate_a);
        startdate.setOnClickListener(this);
        starttime = view.findViewById(R.id.starttime_a);
        starttime.setOnClickListener(this);
        enddate = view.findViewById(R.id.enddate_a);
        enddate.setOnClickListener(this);
        endtime = view.findViewById(R.id.endtime_a);
        endtime.setOnClickListener(this);
        yes = view.findViewById(R.id.btn_yes);
        yes.setOnClickListener(this);
        no = view.findViewById(R.id.btn_no);
        no.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        System.out.println("아니왜클릭이안되냐");
        this.view = view;
        Calendar c = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.startdate_a:
                DatePickerDialog startdate = new DatePickerDialog(getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                startdate.show();
                break;

            case R.id.starttime_a:
                TimePickerDialog starttime = new TimePickerDialog(getContext(),this,c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true);
                starttime.show();
                break;
            case R.id.enddate_a:
                DatePickerDialog enddate = new DatePickerDialog(getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                enddate.show();
                break;
            case R.id.endtime_a:
                TimePickerDialog endtime = new TimePickerDialog(getContext(),this,c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true);
                endtime.show();
                break;
            case R.id.btn_yes:
                String subjectName = AddAssignmentExamActivity.context.subjectName;
                String todoName = AddAssignmentExamActivity.context.et_todoname.getText().toString();

                String s_startdate = this.startdate.getText().toString();
                String[] date = s_startdate.split("-");
                s_startdate = date[0]+date[1]+date[2];     //시작날짜

                String s_enddate = this.enddate.getText().toString();
                date = s_enddate.split("-");
                s_enddate = date[0]+date[1]+date[2];  //종료날짜

                String s_starttime = this.starttime.getText().toString();
                String[] time = s_starttime.split(":");
                s_starttime = time[0]+time[1];

                String s_endtime = this.endtime.getText().toString();
                time = s_endtime.split(":");
                s_endtime = time[0]+time[1];

                boolean result = TodoManagementActivity.mContext.addAssignment(subjectName,todoName,s_startdate,s_starttime,s_enddate,s_endtime);
                if(result){
                    Toast.makeText(getContext(),"과제추가 완료",Toast.LENGTH_SHORT);
                    System.out.println("추가완료");
                }
                else {
                    Toast.makeText(getContext(), "과제추가 실패", Toast.LENGTH_SHORT);
                    System.out.println("추가실패");
                }
                AddAssignmentExamActivity.context.finish();
                break;
            case R.id.btn_no:
                AddAssignmentExamActivity.context.finish();
                break;


        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        switch (view.getId()) {
            case R.id.startdate_a:
                startdate.setText(i + "-" + i1 + "-" + i2);
                break;
            case R.id.enddate_a:
                enddate.setText(i + "-" + i1 + "-" + i2);
                break;

        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        switch (view.getId()) {
            case R.id.starttime_a:
                starttime.setText(i + ":" + i1);
                break;

            case R.id.endtime_a:
                endtime.setText(i + ":" + i1);
                break;

        }
    }
}