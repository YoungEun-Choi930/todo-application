package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);
    }
    public List<SubjectInfo> getSubjectList() {
        SQLiteDBAdapter adapter = new SQLiteDBAdapter(getApplicationContext());
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }
    public boolean addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime){
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+",'"+startTime+"',"+endWeekNumber+",'"+endTime+"');";
        SQLiteDBAdapter adapter = new SQLiteDBAdapter(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
    public boolean delSubject(String subjectName) {
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBAdapter adapter = new SQLiteDBAdapter(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
}