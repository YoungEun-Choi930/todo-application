package com.example.todo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.internal.MethodSorter;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SQLiteDBHelperTest {                       // Unit Test


    public static SQLiteDBHelper helper;

    @BeforeClass
    public static void setUp() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0,userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();


        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();


        //sqlite 초기화
        helper = new SQLiteDBHelper();
        String query = "DELETE FROM SubjectList;";
        helper.excuteQuery(query);

        query = "DELETE FROM LectureList;";
        helper.excuteQuery(query);

        query = "DELETE FROM AssignmentList;";
        helper.excuteQuery(query);

        query = "DELETE FROM ExamList;";
        helper.excuteQuery(query);

        query = "DELETE FROM AlarmInfoList;";
        helper.excuteQuery(query);

        query = "DROP TABLE AlarmList";
        helper.excuteQuery(query);

        query = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "name TEXT NOT NULL, subjectName TEXT NOT NULL, number INTEGER PRIMARY KEY AUTOINCREMENT);";
        helper.excuteQuery(query);
    }


    //순서대로 실행되도록 하기 위해서 함수 이름을 번호로 하였음.
    @Test
    public void test01AddSubject() {   //과목 추가
        String query = "INSERT INTO SubjectList VALUES('test1', 1, 2, 0000, 2, 0000);";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<SubjectInfo> resultlist = helper.loadSubjectList();
        assertEquals(1, resultlist.size());

        SubjectInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
    }

    @Test
    public void test02DelSubject() {   //과목 삭제
        String query = "DELETE FROM SubjectList WHERE subjectName = 'test1';";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<SubjectInfo> resultlist = helper.loadSubjectList();
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test03AddLecture() {   //강의 추가
        String query = "INSERT INTO LectureList VALUES('test1','lname',20210901,0000,20211231,0000,0);";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<LectureInfo> resultlist = helper.loadLectureList("20210902");
        assertEquals(1, resultlist.size());

        LectureInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("lname", resultinfo.getLectureName());
        assertEquals(false, resultinfo.getIsDone());
    }

    @Test
    public void test04LoadLectureDateList() {
        List<String> resultlist = helper.loadLectureDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310000lname", result);
    }

    @Test
    public void test05DelLecture() {   //강의 삭제
        String query = "DELETE FROM LectureList WHERE subjectName = 'test1';";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<LectureInfo> resultlist = helper.loadLectureList("20210902");
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test06AddAssignment() {   //과제 추가
        String query = "INSERT INTO AssignmentList VALUES('test1','aname',20210901,0000,20211231,0000,0);";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210902");
        assertEquals(1, resultlist.size());

        AssignmentInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("aname", resultinfo.getAssignmentName());
        assertEquals(false, resultinfo.getIsDone());
    }

    @Test
    public void test07LoadAssignmentDateList() {
        List<String> resultlist = helper.loadAssignmentDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310000aname", result);
    }

    @Test
    public void test08DelAssignment() {   //과제 삭제
        String query = "DELETE FROM AssignmentList WHERE subjectName = 'test1';";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210902");
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test09AddExam() {   //시험 추가
        String query = "INSERT INTO ExamList VALUES('test1', 'ename', 20211231, 0900);";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<ExamInfo> resultlist = helper.loadExamList("20211231");
        assertEquals(1, resultlist.size());

        ExamInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("ename", resultinfo.getExamName());
    }

    @Test
    public void test10LoadExamDateList() {
        List<String> resultlist = helper.loadExamDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310900ename", result);
    }

    @Test
    public void test11DelExam() {   //시험 삭제
        String query = "DELETE FROM ExamList WHERE subjectName = 'test1';";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<ExamInfo> resultlist = helper.loadExamList("20211001");
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test12AddAlarm() {   //알림 정보 추가
        String query = "INSERT INTO AlarmInfoList VALUES('test1', '1일 전', '1시간 전', '1시간 전');";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<AlarmInfo> resultlist = helper.loadAlarmList();
        assertEquals(1, resultlist.size());

        AlarmInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("1일 전", resultinfo.getExamAlarmDate());
        assertEquals("1시간 전", resultinfo.getAssignmentAlarmDate());
        assertEquals("1시간 전", resultinfo.getVideoLectureAlarmDate());
    }

    @Test
    public void test13DelAlarm() {   //알림 정보 삭제
        String query = "DELETE FROM AlarmInfoList WHERE subjectName = 'test1';";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        List<AlarmInfo> resultlist = helper.loadAlarmList();
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test14SetAlarmNum() {   //시스템 알림 추가
        int a = helper.setAlarmNum("a","test");
        assertEquals(1,a);

        a = helper.setAlarmNum("b","test");
        assertEquals(2,a);

        a = helper.setAlarmNum("c","test");
        assertEquals(3,a);

        a = helper.setAlarmNum("d","test");
        assertEquals(4,a);
    }

    @Test
    public void test15LoadAlarmNum() { //저장해둔 시스템알림 번호들고오기
        int a = helper.loadAlarmNum("a");
        assertEquals(1,a);

        a = helper.loadAlarmNum("b");
        assertEquals(2,a);

        a = helper.loadAlarmNum("c");
        assertEquals(3,a);

        a = helper.loadAlarmNum("d");
        assertEquals(4,a);
    }

    @Test
    public void test16LoadAlarmSubjectList() {
        List<Integer> resultlist = helper.loadAlarmSubjectList("test");
        int a = resultlist.get(0);
        int b = resultlist.get(1);
        int c = resultlist.get(2);
        int d = resultlist.get(3);
        assertEquals(1, a);
        assertEquals(2, b);
        assertEquals(3, c);
        assertEquals(4, d);
    }

    @Test
    public void test17DelSysAlarm() {
        String query = "DELETE FROM AlarmList WHERE number = 1;";
        boolean result = helper.excuteQuery(query);
        assertTrue(result);

        int a = helper.loadAlarmNum("a");
        assertEquals(-1,a);
    }

    @AfterClass
    public static void finish() {
        String sql = "DELETE FROM SubjectList";
        helper.excuteQuery(sql);

        sql = "DELETE FROM LectureList";
        helper.excuteQuery(sql);

        sql = "DELETE FROM AssignmentList";
        helper.excuteQuery(sql);

        sql = "DELETE FROM ExamList";
        helper.excuteQuery(sql);

        sql = "DELETE FROM AlarmInfoList";
        helper.excuteQuery(sql);

        sql = "DROP TABLE AlarmList";
        helper.excuteQuery(sql);

        sql = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "name TEXT NOT NULL, subjectName TEXT NOT NULL, number INTEGER PRIMARY KEY AUTOINCREMENT);";
        helper.excuteQuery(sql);
    }


}
