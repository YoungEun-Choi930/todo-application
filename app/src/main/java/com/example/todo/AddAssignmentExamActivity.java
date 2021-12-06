package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddAssignmentExamActivity extends AppCompatActivity {
    ExamFragment examFragment;
    AssignmentFragment assignmentFragment;
    String typeName;
    String subjectName;
    EditText et_todoname;
    static AddAssignmentExamActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment_exam);
        context=this;
        Intent intent = getIntent();
        subjectName = intent.getStringExtra("subjectName");
        TextView subjectname = findViewById(R.id.subjectname);
        subjectname.setText(subjectName);

        examFragment = new ExamFragment();
        assignmentFragment = new AssignmentFragment();

        TextView tv_todoname = findViewById(R.id.tv_todoname);
        et_todoname = findViewById(R.id.et_todoname);

        RadioGroup select_type = findViewById(R.id.select_type);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment exam = new ExamFragment();
        transaction.replace(R.id.frame,exam); //처음 접속하면 examFragment 설정

        transaction.commit();

        select_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = select_type.getCheckedRadioButtonId(); //선택한버튼가져옴
                RadioButton type = findViewById(id);
                typeName = type.getResources().getResourceName(id);
                String[] split = typeName.split("/");
                typeName = split[1];
                if(typeName.equals("exam")){ //버튼이 시험 버튼이면
                    tv_todoname.setText("시험명");
                    et_todoname.setText(null);
                    et_todoname.setHint("시험명을 입력하세요.");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Fragment exam = new ExamFragment();
                    transaction.replace(R.id.frame,exam);

                    transaction.commit();

                }
                else{ // 버튼이 과제 버튼이면
                    tv_todoname.setText("과제명");
                    et_todoname.setText(null);
                    et_todoname.setHint("과제명을 입력하세요.");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    Fragment assignment = new AssignmentFragment();
                    transaction.replace(R.id.frame,assignment);

                    transaction.commit();

                }
            }
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