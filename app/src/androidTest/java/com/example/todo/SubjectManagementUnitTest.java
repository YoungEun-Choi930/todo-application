package com.example.todo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubjectManagementUnitTest {

    public static List<SubjectInfo> subjectlist;
    SubjectManagement management = new SubjectManagement();


    @BeforeClass
    public static void setUp()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();

        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();

        ActivityScenario.launch(SubjectManagementActivity.class);

        subjectlist = new ArrayList<>();

    }


    //getstartdate에 대해서는 defect testing을 하진 않음. 이게 맞나? testcase가 잘 생각안나서 일단 2개만 해놓음
    @Test
    public void 테스트0_01_20210901() {



        Date resultdate = management.getstartDate(2021,2,4);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse("2021-09-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(date, resultdate);
    }

    @Test
    public void 테스트0_02_20210302() {
        Date resultdate = management.getstartDate(2021,1,3);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse("2021-03-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(date, resultdate);
    }




    @Test
    public void 테스트1_01정상적인과목입력() {                        //추가성공

        int result = management.addSubject("test1",1,2,"0900",2,"1030",2021,2);
        assertEquals(1, result);

        subjectlist.add(new SubjectInfo("test1"));
    }


    @Test
    public void 테스트1_02강의개수가1개인경우() {

        int result = management.addSubject("test2",1,2,"0900",2,"1030",2021,2);
        assertEquals(1, result);

        subjectlist.add(new SubjectInfo("test2"));
    }

    @Test
    public void 테스트1_03강의개수가9개인경우() {

        int result = management.addSubject("test3",9,2,"0900",2,"1030",2021,2);
        assertEquals(1, result);

        subjectlist.add(new SubjectInfo("test3"));
    }

    @Test
    public void 테스트1_04강의개수가마이너스1개인경우() {

        int result = management.addSubject("test4",-1,2,"0900",2,"1030",2021,2);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_05강의개수가10개인경우() {

        int result = management.addSubject("test5",10,2,"0900",2,"1030",2021,2);
        assertEquals(-1, result);

    }

    // 여기선 소숫점 없앴어요

    @Test
    public void 테스트1_06시간이24시인경우() {

        int result = management.addSubject("test6",1,2,"2401",2,"2401",2021,2);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_07시간이60분인경우() {


        int result = management.addSubject("test7",1,2,"0960",2,"1060",2021,2);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_08년도가1899년인경우() {

        int result = management.addSubject("test8",1,2,"0900",2,"1030",1899,2);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_09년도가1900년인경우() {

        int result = management.addSubject("test9",1,2,"0900",2,"1030",1900,2);
        assertEquals(1, result);

        subjectlist.add(new SubjectInfo("test9"));
    }

    @Test
    public void 테스트1_10년도가2100년인경우() {

        int result = management.addSubject("test10",1,2,"0900",2,"1030",2100,2);
        assertEquals(1, result);

        subjectlist.add(1,new SubjectInfo("test10"));
    }

    @Test
    public void 테스트1_11년도가2101년인경우() {

        int result = management.addSubject("test11",1,2,"0900",2,"1030",2101,2);
        assertEquals(-1, result);

    }


    @Test
    public void 테스트1_12시작요일이2인경우() {

        int result = management.addSubject("test12",1,2,"0900",2,"1030",2021,2);
        assertEquals(1, result);

        subjectlist.add(2,new SubjectInfo("test12"));
    }

    @Test
    public void 테스트1_13시작요일이6인경우() {

        int result = management.addSubject("test13",1,6,"0900",6,"1030",2021,2);
        assertEquals(1, result);

        subjectlist.add(3,new SubjectInfo("test13"));
    }

    @Test
    public void 테스트1_14시작요일이1인경우() {

        int result = management.addSubject("test14",1,1,"0900",1,"1030",2021,2);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_15시작요일이7인경우() {

        int result = management.addSubject("test15",1,7,"0900",7,"1030",2021,2);
        assertEquals(-1, result);

    }


    @Test
    public void 테스트1_16학기가0인경우() {

        int result = management.addSubject("test16",1,2,"0900",2,"1030",2021,0);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_17학기가1인경우() {

        int result = management.addSubject("test17",1,2,"0900",2,"1030",2021,1);
        assertEquals(1, result);

        subjectlist.add(4,new SubjectInfo("test17"));
    }

    @Test
    public void 테스트1_18학기가3인경우() {

        int result = management.addSubject("test18",1,2,"0900",2,"1030",2021,3);
        assertEquals(-1, result);

    }

    @Test
    public void 테스트1_19이미존재하는과목입력() {

        int result = management.addSubject("test1",1,2,"0900",2,"1030",2021,2);
        assertEquals(0, result);

    }

    @Test
    public void 테스트2_getSubjectList() {
        List<SubjectInfo> resultList = management.getSubjectList();

        assertEquals(subjectlist.size(), resultList.size());
        for(int i = 0; i < resultList.size(); i++){
            assertEquals(subjectlist.get(i).getSubjectName(), resultList.get(i).getSubjectName());
        }
    }

    @Test
    public void 테스트3_01존재하는과목삭제() {

        management.delSubject("test1");

        subjectlist.remove(0);

        List<SubjectInfo> resultList = management.getSubjectList();

        assertEquals(subjectlist.size(), resultList.size());
        for(int i = 0; i < resultList.size(); i++){
            assertEquals(subjectlist.get(i).getSubjectName(), resultList.get(i).getSubjectName());
        }
    }

    @Test
    public void 테스트3_02존재하지않는과목삭제() {
        management.delSubject("test0");

        List<SubjectInfo> resultList = management.getSubjectList();

        assertEquals(subjectlist.size(), resultList.size());
        for(int i = 0; i < resultList.size(); i++){
            assertEquals(subjectlist.get(i).getSubjectName(), resultList.get(i).getSubjectName());
        }
    }


    @AfterClass
    public static void deleteSubject() {
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String sql = "DELETE FROM SubjectList";
        helper.executeQuery(sql);

        sql = "DELETE FROM LectureList";
        helper.executeQuery(sql);

    }

}