package com.example.todo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AlarmManagement extends AppCompatActivity {

    public List<AlarmInfo> getAlarmList(){
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        List<AlarmInfo> list = adapter.loadAlarmList();
        return list;
    }

    public boolean addAlarm(String subjectName, String exam, String assingment, String video, String real){
        String query = "INSERT INTO AlarmList VALUES('"+
                subjectName+"','"+exam+"','"+assingment+"','"+video+"','"+real+"');";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
    public boolean delAlarm(String subjectName) {
        String query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBAdapter adapter = SQLiteDBAdapter.getInstance(getApplicationContext());
        boolean result = adapter.excuteQuery(query);
        return result;
    }
}
