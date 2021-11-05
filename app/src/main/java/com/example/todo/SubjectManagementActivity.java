package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {

    SQLiteDBHelper sqlitehelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);
    }
    public List<SubjectInfo> getSubjectList() {
        return sqlitehelper.loadSubjectList();
    }
    public void addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime){
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+","+number+","+startWeekNumber+",'"+startTime+","+endWeekNumber+",'"+endTime+"')";
        boolean result = sqlitehelper.excuteQuery(query); //성공 또는 실패 값 들고올랬는데 아직 구현 안함
    }
    public void delSubject(String subjectName) {
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        boolean result = sqlitehelper.excuteQuery(query);
    }
}