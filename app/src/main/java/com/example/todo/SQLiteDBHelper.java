package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    public SQLiteDBHelper(Context context) {
        super(context,"sqlite_file.db",null,7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //sqlite에서는 foreign key를 추가할 수 없다.ㅠㅠ

        String subject = "CREATE TABLE IF NOT EXISTS SubjectList (" +
                "subjectName TEXT NOT NULL, number INTEGER, startWeekNumberINTEGER," +
                "startTime INTEGER, endWeekNumber INTEGER, endTime INTEGER, PRIMARY KEY(subjectName));";
        String lecture = "CREATE TABLE IF NOT EXISTS LectureList (" +
                "subjectName TEXT, lectureName TEXT, startDate INTEGER, startTime INTEGER, endDate INTEGER, endTime INTEGER, isDone INTEGER);";
        String assingment = "CREATE TABLE IF NOT EXISTS AssingmentList ( " +
                "subjectName TEXT NOT NULL, assingmentName TEXT NOT NULL, startDate INTEGER NOT NULL, startTIme INTEGER," +
                "endDate INTEGER NOT NULL, endTime INTEGER, isDone INTEGER);";
        String exam = "CREATE TABLE IF NOT EXISTS ExamList (" +
                "subjectName TEXT NOT NULL, examName TEXT NOT NULL, date INTEGER NOT NULL, time INTEGER);";
        String alarm = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "subjectName TEXT NOT NULL, examAlarmDate TEXT, AssingmentAlarmDate TEXT," +
                "videoAlarmTime TEXT, realTimeAlarmDate TEXT, PRIMARY KEY(subjectName));";
        String  friend = "CREATE TABLE IF NOT EXISTS Friends (friendID TEXT NOT NULL, PRIMARY KEY(friendID));";

        db.execSQL(subject);
        db.execSQL(lecture);
        db.execSQL(assingment);
        db.execSQL(exam);
        db.execSQL(alarm);
        db.execSQL(friend);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String subject = "DROP TABLE IF EXISTS SubjectList;";
        String lecture = "DROP TABLE IF EXISTS LectureList;";
        String assingment = "DROP TABLE IF EXISTS AssingmentList;";
        String exam = "DROP TABLE IF EXISTS ExamList;";
        String alarm = "DROP TABLE IF EXISTS AlarmList;";
        String friend = "DROP TABLE IF EXISTS Friends;";

        db.execSQL(subject);
        db.execSQL(lecture);
        db.execSQL(assingment);
        db.execSQL(exam);
        db.execSQL(alarm);
        db.execSQL(friend);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }


}
