package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDBAdapter
{

    private final Context mContext;
    private SQLiteDatabase mDb;
    private SQLiteDBHelper mDbHelper;

    public SQLiteDBAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new SQLiteDBHelper(mContext);
    }

    public SQLiteDBAdapter createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e("DataAdapter", mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public SQLiteDBAdapter open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Boolean excuteQuery(String sql)
    {
        try
        {
            mDb.execSQL(sql);
            return true;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "excuteQuery >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<LectureInfo> loadLectureList(String date)
    {
        try
        {
            String sql = "SELECT subjectName, lectureName, isDone FROM LectureList WHERE startDate <= "+date+" AND endDate >= "+date+";";

            List<LectureInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    LectureInfo info = new LectureInfo();

                    info.setSubjectName(cursor.getString(0));
                    info.setLectureName(cursor.getString(1));
                    info.setIsDone(Boolean.valueOf(cursor.getString(2)));

                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getLectureData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<AssingmentInfo> loadAssingmentList(String date)
    {
        try
        {
            String sql = "SELECT subjectName, assingmentName, isDone FROM AssingmentList WHERE startDate <= "+date+" AND endDate >= "+date+";";

            List<AssingmentInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    AssingmentInfo info = new AssingmentInfo();

                    info.setSubjectName(cursor.getString(0));
                    info.setAssingmentName(cursor.getString(1));
                    info.setIsDone(Boolean.valueOf(cursor.getString(2)));

                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getAssingmentData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<ExamInfo> loadExamList(String date)
    {
        try
        {
            String sql = "SELECT subjectName, examName FROM ExamList WHERE startDate <= "+date+" AND endDate >= "+date+";";

            List<ExamInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    ExamInfo info = new ExamInfo();

                    info.setSubjectName(cursor.getString(0));
                    info.setExamName(cursor.getString(1));

                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<SubjectInfo> loadSubjectList()
    {
        try
        {
            String sql = "SELECT subjectName FROM SubjectList;";

            List<SubjectInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    SubjectInfo info = new SubjectInfo();
                    info.setSubjectName(cursor.getString(0));
                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getSubjectData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<AlarmInfo> loadAlarmList()
    {
        try
        {
            String sql = "SELECT * FROM AlarmList;";

            List<AlarmInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    AlarmInfo info = new AlarmInfo();

                    info.setSubjectName(cursor.getString(0));
                    info.setExamAlarmDate(cursor.getString(1));
                    info.setAssingmentAlarmDate(cursor.getString(2));
                    info.setVideoLectureAlarmDate(cursor.getString(3));
                    info.setRealTimeLectureAlarmDate(cursor.getString(4));

                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getAlarmData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

}