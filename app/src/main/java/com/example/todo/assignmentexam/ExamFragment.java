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
import com.example.todo.assignmentexam.AddAssignmentExamActivity;
import com.example.todo.todo.ToDoManagement;

import java.util.Calendar;


public class ExamFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    EditText date;
    EditText time;
    Button yes, no;
    View view;

    public ExamFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);
        date = view.findViewById(R.id.date_e);
        date.setOnClickListener(this);
        time = view.findViewById(R.id.time_e);
        time.setOnClickListener(this);

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
            case R.id.date_e: //시험날짜
                DatePickerDialog date = new DatePickerDialog(getContext(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                date.show();
                break;

            case R.id.time_e: //시험시간
                TimePickerDialog time = new TimePickerDialog(getContext(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                time.show();
                break;

            case R.id.btn_yes: //확인 버튼
                if(AddAssignmentExamActivity.context.et_todoname.getText().toString().equals("") | this.date.getText().toString().equals("")|this.time.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"모든 정보를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{//모든 정보가 입력되었다면
                    String subjectName = AddAssignmentExamActivity.context.subjectName;//과목이름
                    String todoName = AddAssignmentExamActivity.context.et_todoname.getText().toString();//할일이름

                    String s_date = this.date.getText().toString();
                    String[] splitdate = s_date.split("-");
                    if(splitdate[1].length() == 1) splitdate[1] = "0"+splitdate[1];
                    if(splitdate[2].length() == 1) splitdate[2] = "0"+splitdate[2];
                    s_date = splitdate[0]+splitdate[1]+splitdate[2];     //시험날짜


                    String s_time = this.time.getText().toString();
                    String[] splittime = s_time.split(":");
                    if(splittime[0].length() == 1) splittime[0] = "0"+splittime[0];
                    if(splittime[1].length() == 1) splittime[1] = "0"+splittime[1];
                    s_time = splittime[0]+splittime[1]; //시험시간
                    ToDoManagement toDoManagement = new ToDoManagement();

                    boolean result = toDoManagement.addExam(subjectName,todoName,s_date,s_time); //시험추가
                    if(result){//시험추가에 성공했다면
                        Toast.makeText(getActivity(),"시험추가 완료",Toast.LENGTH_SHORT).show();

                    }
                    else {//실패했다면
                        Toast.makeText(getActivity(), "시험추가 실패", Toast.LENGTH_SHORT).show();
                    }
                    AddAssignmentExamActivity.context.finish();
                }

                break;
            case R.id.btn_no://취소버튼
                AddAssignmentExamActivity.context.finish();
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int month = i1+1; //0부터 시작하기 때문에
            date.setText(i + "-" + month + "-" + i2);

        }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        time.setText(i+":"+i1);
    }
}