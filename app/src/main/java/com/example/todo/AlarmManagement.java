package com.example.todo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AlarmManagement extends AppCompatActivity {
    SQLiteDBHelper sqlitehelper;


    public List<AlarmInfo> getAlarmList(){
        return sqlitehelper.loadAlarmList();
    }

    public void addAlarm(String subjectName, String exam, String assingment, String video, String real){
        String query = "INSERT INTO AlarmList VALUES('"+
                subjectName+"','"+exam+"','"+assingment+"','"+video+"','"+real+"');";
        boolean result = sqlitehelper.excuteQuery(query); //성공 또는 실패 값 들고올랬는데 아직 구현 안함
    }
    public void delAlarm(String subjectName) {
        String query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
        boolean result = sqlitehelper.excuteQuery(query);
    }
}
