package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExamFragmentTest {

    class exam {
        String examName, strdate, strtime;
        int year, month, date;
        int hour, minute;

        public exam(String examName, String date, String time){

            this.examName = examName;
            this.strdate = date;
            this.strtime = time;
            this.year = Integer.parseInt(date.substring(0,4));
            this.month = Integer.parseInt(date.substring(4,6));
            this.date = Integer.parseInt(date.substring(6,8));

            this.hour = Integer.parseInt(time.substring(0,2));
            this.minute = Integer.parseInt(time.substring(2,4));
        }
    }

    private static List<exam> examList;

    @BeforeClass
    public static void setUp() throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();
        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();


        ActivityScenario.launch(AddSubjectActivity.class);

        onView(ViewMatchers.withId(R.id.name_subject)).perform(typeText("examTest"));     //과목이름
        onView(ViewMatchers.withId(R.id.semester2)).perform(doubleClick());         // 학기
        onView(ViewMatchers.withId(R.id.number_subject)).perform(typeText("1"));         //강의갯수
        onView(ViewMatchers.withId(R.id.start_4)).perform(doubleClick());       //시작요일
        onView(ViewMatchers.withId(R.id.startTime_subject)).perform(click());         //시작시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(00, 00));
        onView(ViewMatchers.withText("확인")).perform(click());
        onView(ViewMatchers.withId(R.id.end_3)).perform(doubleClick());               // 종료요일
        onView(ViewMatchers.withId(R.id.endTime_subject)).perform(click());         //종료시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(00, 00));
        onView(ViewMatchers.withText("확인")).perform(click());
        onView(ViewMatchers.withId(R.id.year_subject)).perform(click());            //년도
        onView(ViewMatchers.withId(R.id.btn_confirm)).perform(click());      //년도

        onView(ViewMatchers.withId(R.id.yes_subject)).perform(click());         //확인버튼
        // 과목 test - 2021 2학기 - 강의1개 - 수~화 - 0000,0000 생성

    }

    public boolean sqliteExamListTrue(exam e){    //examList에서의 번호
        // 시험이 존재하고, 입력한 정보와 똑같이 들어갔으면 true, 시험이 존재하지 않으면 false, 정보가 다르면 error
        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT subjectName, examName, date, time FROM ExamList;";

        Cursor cursor = mDb.rawQuery(sql, null);
        if (cursor!=null) {

            // 칼럼의 마지막까지
            while( cursor.moveToNext() ) {
                if(cursor.getString(0).equals(e.examName)) {
                    assertEquals(e.examName, cursor.getString(0));
                    assertEquals(Integer.parseInt(e.strdate), cursor.getInt(2));
                    assertEquals(Integer.parseInt(e.strtime), cursor.getInt(3));
                    return true;
                }
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public void insertExam(exam e) {
        onView(ViewMatchers.withId(R.id.exam)).perform(click());          //시험버튼
        // 시험명
        onView(ViewMatchers.withId(R.id.et_todoname)).perform(typeText(e.examName));
        //날짜
        onView(ViewMatchers.withId(R.id.date_e)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(e.year, e.month, e.date));
        onView(ViewMatchers.withText("확인")).perform(click());
        //시간
        onView(ViewMatchers.withId(R.id.time_e)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(e.hour, e.minute));
        onView(ViewMatchers.withText("확인")).perform(click());
        //확인
        onView(ViewMatchers.withId(R.id.btn_yes)).perform(click());             //확인버튼
    }

    @Test
    public void 정상적인시험추가() {

        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "examTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        exam e = new exam("test1","20210901","1800");
        insertExam(e);
        assertTrue(sqliteExamListTrue(e));

    }
    @Test
    public void 년도가1899일경우() {              //1899가 1900으로 변경되어 입력
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "examTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        ExamFragmentTest.exam e = new ExamFragmentTest.exam("test2","18990901","0000");
        insertExam(e);

        assertTrue(sqliteExamListTrue(e));
    }

    @Test
    public void 년도가1900일경우() {              //추가성공
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "examTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        ExamFragmentTest.exam e = new ExamFragmentTest.exam("test3","19900901","0000");
        insertExam(e);

        assertTrue(sqliteExamListTrue(e));
    }

    @Test
    public void 년도가2100일경우() {              //추가성공
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "examTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        ExamFragmentTest.exam e = new ExamFragmentTest.exam("test4","21000901","0000");
        insertExam(e);

        assertTrue(sqliteExamListTrue(e));
    }

    @Test
    public void 년도가2101일경우() {              //2101이 2100으로 입력
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "examTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        ExamFragmentTest.exam e = new ExamFragmentTest.exam("test5","21010901","0000");
        insertExam(e);

        assertTrue(sqliteExamListTrue(e));
    }




    @AfterClass
    public static void deleteSubject() {
        ActivityScenario.launch(SubjectManagementActivity.class);
        onView(ViewMatchers.withId(R.id.del_subject)).perform(click());

        SubjectInfo subjectInfo = new SubjectInfo("test");
        SubjectManagementActivity.subjectAdapter.checkedList.add(subjectInfo);

        onView(ViewMatchers.withId(R.id.btn_del_sub)).perform(click());
    }
}