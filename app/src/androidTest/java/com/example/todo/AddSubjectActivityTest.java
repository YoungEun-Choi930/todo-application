package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AddSubjectActivityTest {


    class subject {
        String subjectName;
        String number;
        int startWeekNumber;
        String startTime;
        int endWeekNumber;
        String endTime;
        public subject(String subjectName, String number, int startWeekNumber, String startTime, int endWeekNumber, String endTime) {
            this.subjectName = subjectName;
            this.number = number;
            this.startWeekNumber = startWeekNumber;
            this.startTime = startTime;
            this.endWeekNumber = endWeekNumber;
            this.endTime = endTime;
        }
    }

    private List<subject> subjectList;

    @Before
    public void setUp()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();


        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();

        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT subjectName, number, startWeekNumber, startTime, endWeekNumber, endTime FROM SubjectList;";

        subjectList = new ArrayList<>();

        Cursor cursor = mDb.rawQuery(sql, null);
        if (cursor!=null)
        {
            // 칼럼의 마지막까지
            while( cursor.moveToNext() ) {

                subject s = new subject(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getString(3),cursor.getInt(4),cursor.getString(5));
                subjectList.add(s);
            }
        }
        cursor.close();
        db.close();



        ActivityScenario.launch(SubjectManagementActivity.class);

    }

    private void setSubjectInfo(subject s, String year, int semester)
    {


        onView(ViewMatchers.withId(R.id.name_subject)).perform(typeText(s.subjectName));     //과목이름
        onView(ViewMatchers.withId(R.id.number_subject)).perform(typeText(s.number));         //강의갯수

        switch(s.startWeekNumber) {   // 2:월, 3:화, 4:수, 5:목, 6:금      시작요일
            case 2:
                onView(ViewMatchers.withId(R.id.start_2)).perform(click()); break;
            case 3:
                onView(ViewMatchers.withId(R.id.start_3)).perform(click()); break;
            case 4:
                onView(ViewMatchers.withId(R.id.start_4)).perform(click()); break;
            case 5:
                onView(ViewMatchers.withId(R.id.start_5)).perform(click()); break;
            case 6:
                onView(ViewMatchers.withId(R.id.start_6)).perform(click()); break;
        }

        onView(ViewMatchers.withId(R.id.startTime_subject)).perform(typeText(s.startTime));         //시작시간

        switch(s.endWeekNumber) {   // 2:월, 3:화, 4:수, 5:목, 6:금        종료요일
            case 2:
                onView(ViewMatchers.withId(R.id.end_2)).perform(click()); break;
            case 3:
                onView(ViewMatchers.withId(R.id.end_3)).perform(click()); break;
            case 4:
                onView(ViewMatchers.withId(R.id.end_4)).perform(click()); break;
            case 5:
                onView(ViewMatchers.withId(R.id.end_5)).perform(click()); break;
            case 6:
                onView(ViewMatchers.withId(R.id.end_6)).perform(click()); break;
        }

        onView(ViewMatchers.withId(R.id.endTime_subject)).perform(typeText(s.endTime));           //종료시간

        switch (semester) {
            case 1:
                onView(ViewMatchers.withId(R.id.semester1)).perform(click()); break;
            case 2:
                onView(ViewMatchers.withId(R.id.semester2)).perform(click()); break;
        }

        onView(ViewMatchers.withId(R.id.year_subject)).perform(typeText(year));      //년도

    }

    public void insertSubject(subject s, String year, int semester)
    {
        setSubjectInfo(s, year,semester);
        onView(ViewMatchers.withId(R.id.yes_subject)).perform(click());

        SQLiteDBHelper dbHelper = new SQLiteDBHelper();


    }

    public void sqliteTrue(){
        //sqlite랑 subjectList랑 씽크가 맞는가
        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT subjectName, number, startWeekNumber, startTime, endWeekNumber, endTime FROM SubjectList;";

        Cursor cursor = mDb.rawQuery(sql, null);
        if (cursor!=null)
        {
            int i = 0;
            // 칼럼의 마지막까지
            while( cursor.moveToNext() ) {

                assertEquals(subjectList.get(i).subjectName, cursor.getString(0));
                assertEquals(subjectList.get(i).number, cursor.getString(1));
                assertEquals(subjectList.get(i).startWeekNumber, cursor.getInt(2));
                assertEquals(subjectList.get(i).startTime, cursor.getString(3));
                assertEquals(subjectList.get(i).endWeekNumber, cursor.getInt(4));
                assertEquals(subjectList.get(i).endTime, cursor.getString(5));
                i++;
            }

        }
        cursor.close();
        db.close();
    }

    @Test
    public void 정상적인과목입력() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test1","1",3,"1000",2,"1000");
        subjectList.add(s);
        insertSubject(s, "2021",2);
        sqliteTrue();
    }



    @Test
    public void 과목삭제() {
        ActivityScenario.launch(SubjectManagementActivity.class);
        onView(ViewMatchers.withId(R.id.del_subject)).perform(click());

        SubjectInfo subjectInfo = new SubjectInfo(subjectList.get(0).subjectName);
        SubjectManagementActivity.subjectAdapter.checkedList.add(subjectInfo);

        onView(ViewMatchers.withId(R.id.btn_del_sub)).perform(click());

        subjectList.remove(0);

        sqliteTrue();

    }



    //2번방법
//    private SubjectManagementActivity subjectManagementActivity;
//    private SQLiteDBHelper dbHelper;
//
//    @Before
//    public void setUp() {
//        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();
//        subjectManagementActivity = new SubjectManagementActivity();
//        dbHelper = new SQLiteDBHelper();
//    }
//
//    @Test
//    public void 정상적인과목추가() {
//        List<SubjectInfo> subjectList = dbHelper.loadSubjectList();
//
//        //public인데 메소드가 안뜨냐 왜 안됨?!?!?!??
//        subjectManagementActivity.addsub
//    }
}