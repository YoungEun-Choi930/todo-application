package com.example.todo.util;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.todo.todo.LectureInfo;
import com.example.todo.alarm.AlarmInfo;
import com.example.todo.subject.SubjectInfo;
import com.example.todo.assignmentexam.AssignmentInfo;
import com.example.todo.assignmentexam.ExamInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
과목, 강의, 과제, 시험, 알림 정보를 sqliteDB에 저장하고, sqlite에 저장된 정보를 가져오기 위한 클래스이다.
 */
public class SQLiteDBHelper
{

    private SQLiteDB mDbHelper;

    public SQLiteDBHelper()
    {
        mDbHelper = SQLiteDB.getInstance();
    }

    public Boolean executeQuery(String sql)
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

            mDb.execSQL(sql);
            mDbHelper.close();
            return true;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "executeQuery >>"+ mSQLException.toString());
            return false;
        }
    }

    // sqliteDB에 저장된 과목정보를 불러와 List<SubjectInfo>형태로 가공하여 반환한다.
    public List<SubjectInfo> loadSubjectList()
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT subjectName FROM SubjectList;";

            List<SubjectInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    SubjectInfo info = new SubjectInfo(cursor.getString(0));
                    list.add(info);
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getSubjectData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public String getAlarmTime(String name, String subjectName, String table) {
        //lecture , assignment
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();

            String sql = "SELECT endDate, endTime FROM " + table + "List WHERE " + table.toLowerCase() + "Name = '" + name + "';";

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                cursor.moveToNext();

                String endDate = Integer.toString(cursor.getInt(0));
                String endTime = Integer.toString(cursor.getInt(1));
                if(endTime.length() == 1) endTime = "000" + endTime;
                if(endTime.length() == 2) endTime = "00" + endTime;
                if(endTime.length() == 3) endTime = "0" + endTime;

                return endDate + endTime;
            }
            cursor.close();
            mDbHelper.close();
            return "";
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "changIsDone >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


    // sqliteDB에 저장된 강의 정보에서 선택된 날짜에 해당하는 강의를 반환하는 메소드이다.
    // 강의의 시작날짜가 사용자가 선택한 날짜보다 더 전이고, 강의의 종료 날짜가 선택 날짜보다 이후인
    // 강의목록들을 가져오는 query문을 작성하여 sqliteDB에서 가져온다. 가져온 정보들을 List<LectureInfo>형태로 가공하여 반환한다.
    public List<LectureInfo> loadLectureList(String date)
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT subjectName, lectureName, isDone FROM LectureList WHERE startDate <= "+date+" AND endDate >= "+date+";";
            List<LectureInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {
                    int is = cursor.getInt(2);
                    boolean isd;
                    if(is == 1) isd = true;
                    else isd = false;
                    LectureInfo info = new LectureInfo(cursor.getString(0),cursor.getString(1),isd);

                    list.add(info);
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getLectureData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<AssignmentInfo> loadAssignmentList(String date)
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT subjectName, assignmentName, isDone FROM AssignmentList WHERE startDate <= "+date+" AND endDate >= "+date+";";

            List<AssignmentInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {
                    int is = cursor.getInt(2);
                    boolean isd;
                    if(is == 1) isd = true;
                    else isd = false;

                    AssignmentInfo info = new AssignmentInfo(cursor.getString(0),cursor.getString(1),isd);

                    list.add(info);
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getAssignmentData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public List<ExamInfo> loadExamList(String date)
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT subjectName, examName FROM ExamList WHERE date == "+date+";";

            List<ExamInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    ExamInfo info = new ExamInfo(cursor.getString(0),cursor.getString(1));

                    list.add(info);
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<AlarmInfo> loadAlarmList()
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT * FROM AlarmInfoList;";

            List<AlarmInfo> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    AlarmInfo info = new AlarmInfo(false,cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));

                    list.add(info);
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getAlarmData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


    public AlarmInfo loadAlarmInfo(String subjectName)      //loadAlarmInfo라고 바꿔야겠다
    {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT * FROM AlarmInfoList WHERE subjectName == '"+subjectName+"';";

            AlarmInfo info = null;

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    info = new AlarmInfo(false,cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));

                }
            }
            cursor.close();
            mDbHelper.close();
            return info;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getAlarmData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


    /*알림추가할 때 사용*/
    public List<String> loadLectureDateList(String subjectName)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int currentDate = Integer.parseInt(format.format(new Date(System.currentTimeMillis())));
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT lectureName, endDate, endTime, isDone FROM LectureList WHERE subjectName == '"+subjectName+"';";

            List<String> datelist = new ArrayList();
            List<String> list = new ArrayList<>();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    int is = cursor.getInt(3);
                    if(is == 1) continue;

                    int d = cursor.getInt(1);
                    if(d < currentDate) continue;

                    String name = cursor.getString(0);
                    String date = Integer.toString(cursor.getInt(1));
                    String time = Integer.toString(cursor.getInt(2));
                    if(time.length() == 1) time = "000" + time;
                    if(time.length() == 2) time = "00" + time;
                    if(time.length() == 3) time = "0" + time;

                    if(datelist.contains(date+time)) continue;
                    else {
                        datelist.add(date+time);
                        list.add(date+time+name);
                    }
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<String> loadAssignmentDateList(String subjectName)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int currentDate = Integer.parseInt(format.format(new Date(System.currentTimeMillis())));
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT assignmentName, endDate, endTime, isDone FROM AssignmentList WHERE subjectName == '"+subjectName+"';";

            List<String> datelist = new ArrayList<>();
            List<String> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    int is = cursor.getInt(3);
                    if(is == 1) continue;

                    int d = cursor.getInt(1);
                    if(d < currentDate) continue;

                    String name = cursor.getString(0);
                    String date = Integer.toString(cursor.getInt(1));
                    String time = Integer.toString(cursor.getInt(2));
                    if(time.length() == 1) time = "000" + time;
                    if(time.length() == 2) time = "00" + time;
                    if(time.length() == 3) time = "0" + time;

                    if(datelist.contains(date+time)) continue;
                    else {
                        datelist.add(date+time);
                        list.add(date+time+name);
                    }
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<String> loadExamDateList(String subjectName)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        int currentDate = Integer.parseInt(format.format(new Date(System.currentTimeMillis())));
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT examName, date, time FROM ExamList WHERE subjectName == '"+subjectName+"';";

            List<String> datelist = new ArrayList<>();
            List<String> list = new ArrayList();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                // 칼럼의 마지막까지
                while( cursor.moveToNext() ) {

                    int d = cursor.getInt(1);
                    if(d < currentDate) continue;

                    String name = cursor.getString(0);
                    String date = Integer.toString(cursor.getInt(1));
                    String time = Integer.toString(cursor.getInt(2));
                    if(time.length() == 1) time = "000" + time;
                    if(time.length() == 2) time = "00" + time;
                    if(time.length() == 3) time = "0" + time;

                    if(datelist.contains(date+time)) continue;
                    else {
                        datelist.add(date+time);
                        list.add(date+time+name);
                    }
                }
            }
            cursor.close();
            mDbHelper.close();
            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }


    /* -------------------------------- 시스템 알림을 sqlite에 저장 ----------------------------------*/
    public int setAlarmNum(String name, String subjectName) {
        String sql = "INSERT INTO AlarmList VALUES('"+name+"', '"+subjectName+"', null);";

        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL(sql);

        return loadAlarmNum(name);
    }

    /* -------------- chang is done 하거나 과제, 시험 삭제할때 알림 설정되어있는지 확인 용도 -------------- */
    public int loadAlarmNum(String name) {

        int number = -1;

        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT number FROM AlarmList WHERE name == '"+name+"';";

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                if(cursor.getCount() == 0) return -1;
                cursor.moveToNext();

                number = cursor.getInt(0);

            }
            cursor.close();
            mDbHelper.close();

            return number;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    /* ------------------------- 알림 삭제할 경우 시스템 알림 삭제하기 위해서 ---------------------------- */
    public List<Integer> loadAlarmSubjectList(String subjectName) {
        try
        {
            SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
            String sql = "SELECT number FROM AlarmList WHERE subjectName == '"+subjectName+"';";

            List<Integer> list = new ArrayList<>();

            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor!=null)
            {
                while( cursor.moveToNext() ) {
                    int num = cursor.getInt(0);
                    list.add(num);
                }
            }
            cursor.close();
            mDbHelper.close();

            return list;
        }
        catch (SQLException mSQLException)
        {
            Log.e("DataAdapter", "getExamData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }



}
