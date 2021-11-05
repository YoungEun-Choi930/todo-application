package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    public SQLiteDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String subject = "CREATE TABLE IF NOT EXISTS SubjectList" +
                "(subjectName TEXT PRIMARY KEY, number INT, startWeekNumber INT, startTime INT, endWeekNumber INT, endTime INT);";
        String lecture = "CREATE TABLE IF NOT EXISTS LectureList" +
                "(subjectName TEXT, lectureName TEXT, startDate INT, endDate INT, isDone INT, " +
                "FOREIGN KEY(subjectName) REFERENCES subjet(subjectName));";
        String exam = "CREATE TABLE IF NOT EXISTS ExamList(subjectName TEXT, date INT, " +
                "FOREIGN KEY(subjectName) REFERENCES subjet(subjectName));";
        String assingment = "CREATE TABLE IF NOT EXISTS AssingmentList" +
                "(subjectName TEXT, assingmentName TEXT, startDate INT, endDate INT, isDone INT, " +
                "FOREIGN KEY(subjectName) REFERENCES subjet(subjectName));";
        String friend = "CREATE TABLE IF NOT EXISTS Friends(friendID);";
        String alarm = "CREATE TABLE IF NOT EXISTS AlarmList"+
                "(subjectName TEXT, examAlarmDate TEXT, assingmentAlarmDate TEXT, videoAlarmDate TEXT, realTimeAlarmDate TEXT);";

        DB.execSQL(subject);
        DB.execSQL(lecture);
        DB.execSQL(exam);
        DB.execSQL(assingment);
        DB.execSQL(friend);
        DB.execSQL(alarm);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS LectureList;");
        onCreate(DB);
    }

    public Boolean excuteQuery(String query){
        SQLiteDatabase DB = getWritableDatabase();
        DB.execSQL(query);                          //에러 exception추가하고 결과값 반환으로 수정하기
        return true;
    }

    public List<LectureInfo> loadLectureList(String date) {
        SQLiteDatabase DB = getWritableDatabase();
        String query = "SELECT subjectName, lectureName, isDone FROM LectureList WHERE startDate < "+date+" AND endDate > "+date+";";
        Cursor cursor = DB.rawQuery(query,null);

        List<LectureInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            LectureInfo info = new LectureInfo();
            info.setSubjectName(cursor.getString(0));
            info.setLectureName(cursor.getString(1));
            info.setIsDone(Boolean.valueOf(cursor.getString(2)));
            list.add(info);
        }
        cursor.close();
        return list;
    }
    public List<ExamInfo> loadExamList(String date) {
        SQLiteDatabase DB = getWritableDatabase();
        String query = "SELECT subjectName, examName FROM ExamList WHERE startDate < "+date+" AND endDate > "+date+";";
        Cursor cursor = DB.rawQuery(query,null);

        List<ExamInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            ExamInfo info = new ExamInfo();
            info.setSubjectName(cursor.getString(0));
            info.setExamName(cursor.getString(1));
            list.add(info);
        }
        cursor.close();
        return list;
    }
    public List<AssingmentInfo> loadAssingmentList(String date) {
        SQLiteDatabase DB = getWritableDatabase();
        String query = "SELECT subjectName, assingmentName, isDone FROM AssingmentList WHERE startDate < "+date+" AND endDate > "+date+";";
        Cursor cursor = DB.rawQuery(query,null);

        List<AssingmentInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            AssingmentInfo info = new AssingmentInfo();
            info.setSubjectName(cursor.getString(0));
            info.setAssingmentName(cursor.getString(1));
            info.setIsDone(Boolean.valueOf(cursor.getString(2)));
            list.add(info);
        }
        cursor.close();
        return list;
    }
    public List<SubjectInfo> loadSubjectList() {
        SQLiteDatabase DB = getWritableDatabase();
        String query = "SELECT subjectName FROM SubjectList;";
        Cursor cursor = DB.rawQuery(query,null);

        List<SubjectInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            SubjectInfo info = new SubjectInfo();
            info.setSubjectName(cursor.getString(0));
            list.add(info);
        }
        cursor.close();
        return list;
    }
    public List<AlarmInfo> loadAlarmList(){
        SQLiteDatabase DB = getWritableDatabase();
        String query = "SELECT * FROM AlarmList;";
        Cursor cursor = DB.rawQuery(query,null);

        List<AlarmInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            AlarmInfo info = new AlarmInfo();
            info.setSubjectName(cursor.getString(0));
            info.setExamAlarmDate(cursor.getString(1));
            info.setAssingmentAlarmDate(cursor.getString(2));
            info.setVideoLectureAlarmDate(cursor.getString(3));
            info.setRealTimeLectureAlarmDate(cursor.getString(4));
            list.add(info);
        }
        cursor.close();
        return list;
    }
}
