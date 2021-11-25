package com.example.todo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB extends SQLiteOpenHelper {

    //Singleton 패턴
    private static SQLiteDB db;

    private SQLiteDB() {
        super(LoginActivity.ApplicationContext,"sqlite_file.db",null,9);
    }

    public static SQLiteDB getInstance(){
        if(db == null)
            db = new SQLiteDB();
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //sqlite에서는 foreign key를 추가할 수 없다.ㅠㅠ

        String subject = "CREATE TABLE IF NOT EXISTS SubjectList (" +
                "subjectName TEXT NOT NULL, number INTEGER, startWeekNumber INTEGER," +
                "startTime INTEGER, endWeekNumber INTEGER, endTime INTEGER, PRIMARY KEY(subjectName));";
        String lecture = "CREATE TABLE IF NOT EXISTS LectureList (" +
                "subjectName TEXT, lectureName TEXT, startDate INTEGER, startTime INTEGER, endDate INTEGER, endTime INTEGER, isDone INTEGER);";
        String assignment = "CREATE TABLE IF NOT EXISTS AssignmentList ( " +
                "subjectName TEXT NOT NULL, assignmentName TEXT NOT NULL, startDate INTEGER NOT NULL, startTIme INTEGER," +
                "endDate INTEGER NOT NULL, endTime INTEGER, isDone INTEGER);";
        String exam = "CREATE TABLE IF NOT EXISTS ExamList (" +
                "subjectName TEXT NOT NULL, examName TEXT NOT NULL, date INTEGER NOT NULL, time INTEGER);";
        String alarm = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "subjectName TEXT NOT NULL, examAlarmDate TEXT, assignmentAlarmDate TEXT," +
                "videoAlarmTime TEXT, realTimeAlarmDate TEXT, PRIMARY KEY(subjectName));";

        db.execSQL(subject);
        db.execSQL(lecture);
        db.execSQL(assignment);
        db.execSQL(exam);
        db.execSQL(alarm);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String subject = "DROP TABLE IF EXISTS SubjectList;";
        String lecture = "DROP TABLE IF EXISTS LectureList;";
        String assignment = "DROP TABLE IF EXISTS AssignmentList;";
        String exam = "DROP TABLE IF EXISTS ExamList;";
        String alarm = "DROP TABLE IF EXISTS AlarmList;";
        String friend = "DROP TABLE IF EXISTS Friends;";

        db.execSQL(subject);
        db.execSQL(lecture);
        db.execSQL(assignment);
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
