package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.todo.assignmentexam.AddAssignmentExamActivity;
import com.example.todo.subject.AddSubjectActivity;
import com.example.todo.subject.SubjectInfo;
import com.example.todo.subject.SubjectManagementActivity;
import com.example.todo.todo.TodoManagementActivity;
import com.example.todo.util.SQLiteDB;
import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssignmentFragmentTest {

    class assignment {
        String assignmentName;
        int startDate;
        int startTime;
        int endDate;
        int endTime;

        int startYear;
        int startMonth;
        int startDay;

        int startHour;
        int startMinute;

        int endYear;
        int endMonth;
        int endDay;

        int endHour;
        int endMinute;

        public assignment(String assignmentName, String startDate, String startTime, String endDate, String endTime) {
            this.assignmentName = assignmentName;
            this.startDate = Integer.parseInt(startDate);
            this.startTime = Integer.parseInt(startTime);
            this.endDate = Integer.parseInt(endDate);
            this.endTime = Integer.parseInt(endTime);

            this.startYear = Integer.parseInt(startDate.substring(0,4));
            this.startMonth = Integer.parseInt(startDate.substring(4,6));
            this.startDay = Integer.parseInt(startDate.substring(6,8));

            this.startHour = Integer.parseInt(startTime.substring(0,2));
            this.startMinute = Integer.parseInt(startTime.substring(2,4));

            this.endYear = Integer.parseInt(endDate.substring(0,4));
            this.endMonth = Integer.parseInt(endDate.substring(4,6));
            this.endDay = Integer.parseInt(endDate.substring(6,8));

            this.endHour = Integer.parseInt(endTime.substring(0,2));
            this.endMinute = Integer.parseInt(endTime.substring(2,4));

        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();


        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();

        //subject를 하나 만들어서 마지막에 subject를 삭제하도록 하자.
        ActivityScenario.launch(SubjectManagementActivity.class);
        ActivityScenario.launch(AddSubjectActivity.class);

        onView(withId(R.id.name_subject)).perform(typeText("assignmentTest"));     //과목이름
        onView(withId(R.id.semester2)).perform(doubleClick());         // 학기
        onView(withId(R.id.number_subject)).perform(typeText("1"));       //강의갯수
        onView(withId(R.id.start_4)).perform(doubleClick());           //시작요일
        onView(withId(R.id.startTime_subject)).perform(click());       //시작시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 10));
        onView(ViewMatchers.withText("확인")).perform(click());
        onView(withId(R.id.end_3)).perform(doubleClick());             // 종료요일
        onView(withId(R.id.endTime_subject)).perform(click());         //종료시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 10));
        onView(ViewMatchers.withText("확인")).perform(click());
        onView(withId(R.id.year_subject)).perform(click());            //년도
        onView(withId(R.id.btn_confirm)).perform(click());      //년도


        onView(withId(R.id.yes_subject)).perform(click());             //확인버튼
        // 과목 test - 2021 2학기 - 강의1개 - 수~화 - 0000,0000 생성

    }

    private void insertAssignment(assignment a) {
        onView(withId(R.id.assignment)).perform(click());          //과제버튼
        onView(withId(R.id.et_todoname)).perform(typeText(a.assignmentName));  //과제이름

        onView(withId(R.id.startdate_a)).perform(click());         //시작날짜
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(a.startYear,a.startMonth,a.startDay));
        onView(ViewMatchers.withText("확인")).perform(click());

        onView(withId(R.id.starttime_a)).perform(click());         //시작시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(a.startHour,a.startMinute));
        onView(ViewMatchers.withText("확인")).perform(click());

        onView(withId(R.id.enddate_a)).perform(click());           //종료날짜
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(a.endYear,a.endMonth,a.endDay));
        onView(ViewMatchers.withText("확인")).perform(click());

        onView(withId(R.id.endtime_a)).perform(click());           //종료시간
        onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(a.endHour,a.endMinute));
        onView(ViewMatchers.withText("확인")).perform(click());

        onView(withId(R.id.btn_yes)).perform(click());             //확인버튼
    }

    public boolean sqliteAssignmentListTrue(assignment a){
        // 과제가 존재하고, 입력한 정보와 똑같이 들어갔으면 true, 과제가 존재하지 않으면 false, 정보가 다르면 error
        SQLiteDB db = SQLiteDB.getInstance();

        SQLiteDatabase mDb = db.getReadableDatabase();
        String sql = "SELECT assignmentName, startDate, startTime, endDate, endTime FROM AssignmentList WHERE subjectName = 'assignmentTest';";

        Cursor cursor = mDb.rawQuery(sql, null);

        if (cursor!=null) {

            // 칼럼의 마지막까지
            while( cursor.moveToNext() ) {
                if(cursor.getString(0).equals(a.assignmentName)) {
                    assertEquals(a.assignmentName, cursor.getString(0));
                    assertEquals(a.startDate, cursor.getInt(1));
                    assertEquals(a.startTime, cursor.getInt(2));
                    assertEquals(a.endDate, cursor.getInt(3));
                    assertEquals(a.endTime, cursor.getInt(4));
                    return true;
                }
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    @Test
    public void 테스트01정상적인과제추가() {                //추가성공
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test1","20210901","0000","20211001","0000");
        insertAssignment(a);
        assertTrue(sqliteAssignmentListTrue(a));
    }

    // 과목추가와는 다른 피커라서 년도에 대한 피커 테스트만 함
    @Test
    public void 테스트02년도가1899일경우() {              //1899가 1900으로 변경되어 입력
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test2","18990901","0000","18991001","0000");
        insertAssignment(a);

        a.startDate = 19000901;
        a.endDate = 19001001;
        assertTrue(sqliteAssignmentListTrue(a));
    }

    @Test
    public void 테스트03년도가1900일경우() {              //추가성공
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test3","19000901","0000","19001001","0000");
        insertAssignment(a);

        assertTrue(sqliteAssignmentListTrue(a));
    }

    @Test
    public void 테스트04년도가2100일경우() {              //추가성공
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test4","21000901","0000","21001001","0000");
        insertAssignment(a);

        assertTrue(sqliteAssignmentListTrue(a));
    }

    @Test
    public void 테스트05년도가2101일경우() {              //2101이 2100으로 입력
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test5","21000901","0000","21001001","0000");
        insertAssignment(a);

        a.startDate = 21000901;
        a.endDate = 21001001;
        assertTrue(sqliteAssignmentListTrue(a));
    }

    @Test
    public void 테스트06날짜가거꾸로() {             // 추가실패
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test6","20211001","0000","20210901","0000");
        insertAssignment(a);

        assertFalse(sqliteAssignmentListTrue(a));
    }

    @Test
    public void 테스트07시간이거꾸로() {             // 추가실패
        ActivityScenario.launch(TodoManagementActivity.class);
        Context mcontext = TodoManagementActivity.mContext;

        Intent intent = new Intent(mcontext, AddAssignmentExamActivity.class);
        intent.putExtra("subjectName", "assignmentTest");
        mcontext.startActivity(intent);

        ActivityScenario.launch(intent);

        assignment a = new assignment("test7","20210901","1200","20210901","1100");
        insertAssignment(a);

        assertFalse(sqliteAssignmentListTrue(a));
    }


    @Test
    public void 과제삭제(){

    }


    @AfterClass
    public static void deleteSubject() {        // Before Class에서 생성한 test과목 삭제
        ActivityScenario.launch(SubjectManagementActivity.class);
        onView(withId(R.id.del_subject)).perform(click());

        SubjectInfo subjectInfo = new SubjectInfo("assignmentTest");
        SubjectManagementActivity.subjectAdapter.checkedList.add(subjectInfo);

        onView(withId(R.id.btn_del_sub)).perform(click());
    }





}