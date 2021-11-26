package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
        public subject(String subjectName, String number, String startWeekNumber, String startTime, String endWeekNumber, String endTime) {
            this.subjectName = subjectName;
            this.number = number;
            switch(startWeekNumber){
                case "월": this.startWeekNumber = 2; break;
                case "화": this.startWeekNumber = 3; break;
                case "수": this.startWeekNumber = 4; break;
                case "목": this.startWeekNumber = 5; break;
                case "금": this.startWeekNumber = 6; break;
            }
            this.startTime = startTime;
            switch(endWeekNumber){
                case "월": this.endWeekNumber = 2; break;
                case "화": this.endWeekNumber = 3; break;
                case "수": this.endWeekNumber = 4; break;
                case "목": this.endWeekNumber = 5; break;
                case "금": this.endWeekNumber = 6; break;
            }
            this.endTime = endTime;
        }
    }

    private static List<subject> subjectList;

    @BeforeClass
    public static void setUp()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();


        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();

        subjectList = new ArrayList<>();
        ActivityScenario.launch(SubjectManagementActivity.class);

    }

    private void setSubjectInfo(subject s, String year, int semester)
    {


        onView(ViewMatchers.withId(R.id.name_subject)).perform(typeText(s.subjectName));     //과목이름

        switch (semester) {     //학기
            case 1:
                onView(ViewMatchers.withId(R.id.semester1)).perform(doubleClick()); break;          //클릭이 씹히는 경우가 있어서 double 클릭
            case 2:
                onView(ViewMatchers.withId(R.id.semester2)).perform(doubleClick()); break;
        }

        onView(ViewMatchers.withId(R.id.number_subject)).perform(typeText(s.number));         //강의갯수

        switch(s.startWeekNumber) {   // 2:월, 3:화, 4:수, 5:목, 6:금      시작요일
            case 2:
                onView(ViewMatchers.withId(R.id.start_2)).perform(doubleClick()); break;
            case 3:
                onView(ViewMatchers.withId(R.id.start_3)).perform(doubleClick()); break;
            case 4:
                onView(ViewMatchers.withId(R.id.start_4)).perform(doubleClick()); break;
            case 5:
                onView(ViewMatchers.withId(R.id.start_5)).perform(doubleClick()); break;
            case 6:
                onView(ViewMatchers.withId(R.id.start_6)).perform(doubleClick()); break;
        }

        onView(ViewMatchers.withId(R.id.startTime_subject)).perform(typeText(s.startTime));         //시작시간

        switch(s.endWeekNumber) {   // 2:월, 3:화, 4:수, 5:목, 6:금        종료요일
            case 2:
                onView(ViewMatchers.withId(R.id.end_2)).perform(doubleClick()); break;
            case 3:
                onView(ViewMatchers.withId(R.id.end_3)).perform(doubleClick()); break;
            case 4:
                onView(ViewMatchers.withId(R.id.end_4)).perform(doubleClick()); break;
            case 5:
                onView(ViewMatchers.withId(R.id.end_5)).perform(doubleClick()); break;
            case 6:
                onView(ViewMatchers.withId(R.id.end_6)).perform(doubleClick()); break;
        }

        onView(ViewMatchers.withId(R.id.endTime_subject)).perform(typeText(s.endTime));           //종료시간



        onView(ViewMatchers.withId(R.id.year_subject)).perform(typeText(year));      //년도

    }

    public void insertSubject(subject s, String year, int semester)
    {
        setSubjectInfo(s, year,semester);
        onView(ViewMatchers.withId(R.id.yes_subject)).perform(click());

        SQLiteDBHelper dbHelper = new SQLiteDBHelper();


    }

    public boolean sqliteSubjectListTrue(int i){    //subjectList에서의 번호
        //sqlite랑 subjectList랑 똑같으면 true반환. 두개에 들어있는 갯수가 다르면 false반환. 내용이 다르면 error
        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT subjectName, number, startWeekNumber, startTime, endWeekNumber, endTime FROM SubjectList;";

        Cursor cursor = mDb.rawQuery(sql, null);
        if (cursor!=null)
        {
            if(subjectList.size() != cursor.getCount()){
                cursor.close();
                db.close();
                return false;
            }

            // 칼럼의 마지막까지
            while( cursor.moveToNext() ) {
                if(cursor.getString(0).equals(subjectList.get(i).subjectName)) {
                    assertEquals(subjectList.get(i).subjectName, cursor.getString(0));
                    assertEquals(Integer.parseInt(subjectList.get(i).number), cursor.getInt(1));           //여기서 걸리는것같은데
                    assertEquals(subjectList.get(i).startWeekNumber, cursor.getInt(2));
                    assertEquals(Integer.parseInt(subjectList.get(i).startTime), cursor.getInt(3));
                    assertEquals(subjectList.get(i).endWeekNumber, cursor.getInt(4));
                    assertEquals(Integer.parseInt(subjectList.get(i).endTime), cursor.getInt(5));
                    break;
                }
            }

        }
        cursor.close();
        db.close();
        return true;
    }

    public boolean sqliteLectureListTrue(String subjectName, int count, int startDate){
        //강의갯수가 count만큼 들어갔는가. 1주차 강의날짜가 올바르게 들어갔는가.
        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT subjectName, lectureName, startDate FROM LectureList WHERE subjectName = '"+subjectName+"';";

        Cursor cursor = mDb.rawQuery(sql, null);


        if (cursor!=null) {

            if (cursor.getCount() != count) {
                cursor.close();
                db.close();
                return false;
            }
            //1주차 시작날짜가 startDate와 같은가.
            if(count > 0) {
                cursor.moveToNext();
                assertEquals(startDate, cursor.getInt(2));
            }
        }
        cursor.close();
        db.close();
        return true;
    }

    @Test
    public void 정상적인과목입력() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test1","1","월","0900","월","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
    }

    @Test
    public void 강의개수가1개인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test2","1","월","0900","월","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test2",16,20210906));
    }

    @Test
    public void 강의개수가9개인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test3","9","월","0900","월","1030");
        insertSubject(s, "2021",2);


        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test3",144,20210906));
    }

    @Test
    public void 강의개수가0개인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test4","0","월","0900","월","1030");
        insertSubject(s, "2021",2);


        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test4",0,20210906));
    }

    @Test
    public void 강의개수가10개인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test5","10","월","0900","월","1030");
        insertSubject(s, "2021",2);

        s.number = "1";
        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test5",16,20210906));
    }

    @Test
    public void 강의개수가소수점인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test6","1.1","월","0900","월","1030");
        insertSubject(s, "2021",2);

        s.number = "1";
        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test6",16,20210906));
    }

    @Test
    public void 시간이24시를넘는경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test7","1","월","2401","월","2401");
        insertSubject(s, "2021",2);

        //입력이 안되어야함. subjectList에 추가안하고 sqlite랑 비교.
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));

    }

    @Test
    public void 시간이소수점인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test8","1","월","09.00","월","10.30");
        insertSubject(s, "2021",2);

        s.startTime = "0900";
        s.endTime = "1030";
        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test6",16,20210906));
    }

    @Test
    public void 년도가9999년인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test9","1","월","0900","월","1030");
        insertSubject(s, "9999",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
    }

    @Test
    public void 년도가0년인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test10","1","월","0900","월","1030");
        insertSubject(s, "0",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
    }

    @Test
    public void 년도가소수점인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test11","1","월","0900","월","1030");
        insertSubject(s, "20.21",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
    }

    @Test
    public void 시작요일이수요일인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test12","1","수","0900","수","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test12",16,20210901));
    }

    @Test
    public void 시작요일이목요일인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test13","1","목","0900","목","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test13",16,20210902));
    }

    @Test
    public void 시작요일이금요일인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test14","1","금","0900","금","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test14",16,20210903));
    }

    @Test
    public void 시작요일이월요일인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test15","1","월","0900","월","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test15",16,20210906));
    }

    @Test
    public void 시작요일이화요일인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test16","1","화","0900","화","1030");
        insertSubject(s, "2021",2);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test16",16,20210907));
    }

    @Test
    public void 학기가1인경우() {
        ActivityScenario.launch(AddSubjectActivity.class);
        subject s = new subject("test17","1","월","0900","월","1030");
        insertSubject(s, "2021",1);

        subjectList.add(s);
        assertTrue(sqliteSubjectListTrue(subjectList.size()-1));
        assertTrue(sqliteLectureListTrue("test17",16,20210301));
    }





    public static void deleteSubject(int number) {
        ActivityScenario.launch(SubjectManagementActivity.class);
        onView(ViewMatchers.withId(R.id.del_subject)).perform(click());

        SubjectInfo subjectInfo = new SubjectInfo(subjectList.get(number).subjectName);
        SubjectManagementActivity.subjectAdapter.checkedList.add(subjectInfo);

        onView(ViewMatchers.withId(R.id.btn_del_sub)).perform(click());

        subjectList.remove(number);


    }

    @AfterClass         //왜안해????
    public static void del() {
        for(int i = 0; i < subjectList.size(); i ++){
            deleteSubject(0);
        }

    }


}