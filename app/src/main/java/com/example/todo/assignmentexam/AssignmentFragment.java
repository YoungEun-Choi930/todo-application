package com.example.todo.assignmentexam;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo.R;
import com.example.todo.todo.ToDoManagement;
import com.example.todo.todo.TodoManagementActivity;

import java.util.Calendar;


public class AssignmentFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText startdate;
    EditText starttime;
    EditText enddate;
    EditText endtime;
    Button yes, no;
    View view;

    public AssignmentFragment() {
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
        this.view = view;
        Calendar c = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.startdate_a: //시작날짜 선택 버튼
                DatePickerDialog startdate = new DatePickerDialog(getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                startdate.show();
                break;

            case R.id.starttime_a: //시작시간 선택 버튼
                TimePickerDialog starttime = new TimePickerDialog(getContext(),this,c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true);
                starttime.show();
                break;
            case R.id.enddate_a: //종료날짜 선택 버튼
                DatePickerDialog enddate = new DatePickerDialog(getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                enddate.show();
                break;
            case R.id.endtime_a://종료시간 선택 버튼
                TimePickerDialog endtime = new TimePickerDialog(getContext(),this,c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true);
                endtime.show();
                break;
            case R.id.btn_yes: //확인 버튼
                String subjectName = AddAssignmentExamActivity.context.subjectName;
                if(AddAssignmentExamActivity.context.et_todoname.getText().toString().equals("")|this.startdate.getText().toString().equals("")|this.enddate.getText().toString().equals("")|
                this.starttime.getText().toString().equals("")|this.enddate.getText().toString().equals("")){ //정보입력이 덜 된 경우
                    Toast.makeText(getActivity(),"모든 정보를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{//모든 정보가 입력된 경우
                    String todoName = AddAssignmentExamActivity.context.et_todoname.getText().toString();

                    String s_startdate = this.startdate.getText().toString();
                    String[] date = s_startdate.split("-");
                    if(date[1].length() == 1) date[1] = "0"+date[1];
                    if(date[2].length() == 1) date[2] = "0"+date[2];
                    s_startdate = date[0]+date[1]+date[2];     //시작날짜

                    String s_enddate = this.enddate.getText().toString();
                    date = s_enddate.split("-");
                    if(date[1].length() == 1) date[1] = "0"+date[1];
                    if(date[2].length() == 1) date[2] = "0"+date[2];
                    s_enddate = date[0]+date[1]+date[2];  //종료날짜

                    String s_starttime = this.starttime.getText().toString();
                    String[] time = s_starttime.split(":");
                    if(time[0].length() == 1) time[0] = "0"+time[0];
                    if(time[1].length() == 1) time[1] = "0"+time[1];
                    s_starttime = time[0]+time[1]; //시작시간

                    String s_endtime = this.endtime.getText().toString();
                    time = s_endtime.split(":");
                    if(time[0].length() == 1) time[0] = "0"+time[0];
                    if(time[1].length() == 1) time[1] = "0"+time[1];
                    s_endtime = time[0]+time[1];//종료시간
                    if(Integer.parseInt(s_enddate)<Integer.parseInt(s_startdate)){//시작날짜가 종료날짜보다 뒤인 경우
                        Toast.makeText(getActivity(),"시작날짜가 종료날짜보다 뒤일 수 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.parseInt(s_enddate)==Integer.parseInt(s_startdate)){//시작날짜가 종료날짜와 같은 경우
                        if(Integer.parseInt(s_endtime)<Integer.parseInt(s_starttime))//시작시간이 종료시간보다 작다면
                            Toast.makeText(getActivity(),"시작시간이 종료시간보다 뒤일 수 없습니다.",Toast.LENGTH_SHORT).show();
                        else{//정상적인 입력
                            ToDoManagement toDoManagement = new ToDoManagement();

                            boolean result = toDoManagement.addAssignment(subjectName,todoName,s_startdate,s_starttime,s_enddate,s_endtime);
                            if(result){
                                Toast.makeText(getActivity(),"과제추가 완료",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "과제추가 실패", Toast.LENGTH_SHORT).show();
                            }
                            TodoManagementActivity.mContext.toDoAdapter.notifyDataSetChanged();
                            AddAssignmentExamActivity.context.finish();
                        }
                    }
                    else{//정상적인 입력
                        ToDoManagement toDoManagement = new ToDoManagement();

                        boolean result = toDoManagement.addAssignment(subjectName,todoName,s_startdate,s_starttime,s_enddate,s_endtime);
                        if(result){
                            Toast.makeText(getActivity(),"과제추가 완료",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "과제추가 실패", Toast.LENGTH_SHORT).show();
                        }
                        TodoManagementActivity.mContext.toDoAdapter.notifyDataSetChanged();
                        AddAssignmentExamActivity.context.finish();
                    }


                }

                break;
            case R.id.btn_no:
                AddAssignmentExamActivity.context.finish();
                break;


        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int month = i1+1; //0부터 시작하기 때문에
        switch (view.getId()) {
            case R.id.startdate_a:
                startdate.setText(i + "-" + month + "-" + i2);
                break;
            case R.id.enddate_a:
                enddate.setText(i + "-" + month + "-" + i2);
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