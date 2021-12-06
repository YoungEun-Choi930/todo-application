package com.example.todo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
        helper.executeQuery(query);

        query = "DELETE FROM LectureList;";
        helper.executeQuery(query);

        query = "DELETE FROM AssignmentList;";
        helper.executeQuery(query);

        query = "DELETE FROM ExamList;";
        helper.executeQuery(query);

        query = "DELETE FROM AlarmInfoList;";
        helper.executeQuery(query);

        query = "DROP TABLE AlarmList";
        helper.executeQuery(query);

        query = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "name TEXT NOT NULL, subjectName TEXT NOT NULL, number INTEGER PRIMARY KEY AUTOINCREMENT);";
        helper.executeQuery(query);
    }


    //순서대로 실행되도록 하기 위해서 함수 이름을 번호로 하였음.
    @Test
    public void test0_1AddSubject() {   //과목 추가
        String query = "INSERT INTO SubjectList VALUES('test1', 1, 2, 0000, 2, 0000);";
        boolean result = helper.executeQuery(query);
        assertTrue(result);


    }

    @Test
    public void test0_2loadSubjectList() { //과목 조회
        List<SubjectInfo> resultlist = helper.loadSubjectList();
        assertEquals(1, resultlist.size());

        SubjectInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
    }

    @Test
    public void test0_3AddSubjectEqual() {   //동일한과목 추가
        String query = "INSERT INTO SubjectList VALUES('test1', 1, 2, 0000, 2, 0000);";
        boolean result = helper.executeQuery(query);
        assertFalse(result);

    }

    @Test
    public void test0_4DelSubject() {   //과목 삭제
        String query = "DELETE FROM SubjectList WHERE subjectName = 'test1';";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

        List<SubjectInfo> resultlist = helper.loadSubjectList();
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test1_1AddLecture() {   //강의 추가
        String query = "INSERT INTO LectureList VALUES('test1','lname',20210901,0000,20211231,0000,0);";
        boolean result = helper.executeQuery(query);
        assertTrue(result);


    }

    @Test
    public void test1_2LoadLectureList() { //강의 목록 조회
        List<LectureInfo> resultlist = helper.loadLectureList("20210902");
        assertEquals(1, resultlist.size());

        LectureInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("lname", resultinfo.getLectureName());
        assertEquals(false, resultinfo.getIsDone());
    }

    @Test
    public void test1_3LoadLectureList() { //빈 강의 목록 조회
        List<LectureInfo> resultlist = helper.loadLectureList("20210801");
        assertEquals(0, resultlist.size());


    }

    @Test
    public void test1_4LoadLectureDateList() { // 강의날짜리스트가져오기
        List<String> resultlist = helper.loadLectureDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310000lname", result);
    }

    @Test
    public void test1_5DelLecture() {   //강의 삭제
        String query = "DELETE FROM LectureList WHERE subjectName = 'test1';";
        boolean result = helper.executeQuery(query);
        assertTrue(result);


    }

    @Test
    public void test1_6LoadLecture() { //위에서 삭제한 강의가 삭제되었는지 강의목록가져와서 확인
        List<LectureInfo> resultlist = helper.loadLectureList("20210902");
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test2_1AddAssignment() {   //과제 추가
        String query = "INSERT INTO AssignmentList VALUES('test1','aname',20210901,0000,20211231,0000,0);";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

    }

    @Test
    public void test2_2loadAssignment() { //위에서 추가한 과제를 확인

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210902");
        assertEquals(1, resultlist.size());

        AssignmentInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("aname", resultinfo.getAssignmentName());
        assertEquals(false, resultinfo.getIsDone());
    }

    @Test
    public void test2_3loadAssignment() { // 빈 목록 조회

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210801");
        assertEquals(0, resultlist.size());

    }


    @Test
    public void test2_4LoadAssignmentDateList() { // 가져온 날짜 정보가 2_1에서 추가한 정보랑 같은가
        List<String> resultlist = helper.loadAssignmentDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310000aname", result);
    }

    @Test
    public void test2_5DelAssignment() {   //과제 삭제
        String query = "DELETE FROM AssignmentList WHERE subjectName = 'test1';";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210902");
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test2_6LoadAssignment() { //삭제한 과제가 디비에서 사라졌는지 확인

        List<AssignmentInfo> resultlist = helper.loadAssignmentList("20210902");
        assertEquals(0, resultlist.size());

    }


    @Test
    public void test3_1AddExam() {   //시험 추가
        String query = "INSERT INTO ExamList VALUES('test1', 'ename', 20211231, 0900);";
        boolean result = helper.executeQuery(query);
        assertTrue(result);


    }

    @Test
    public void test3_2LoadExamList() { //위에서 추가된 시험이 목록으로 가져와지는지 확인
        List<ExamInfo> resultlist = helper.loadExamList("20211231");
        assertEquals(1, resultlist.size());

        ExamInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("ename", resultinfo.getExamName());
    }

    @Test
    public void test3_3LoadExamList() { //빈 시험 목록 가져오기
        List<ExamInfo> resultlist = helper.loadExamList("20210901");
        assertEquals(0, resultlist.size());

    }

    @Test
    public void test3_4LoadExamDateList() { //가져온 날짜 정보가 위에서 추가한 시험의 날짜정보와 같은지
        List<String> resultlist = helper.loadExamDateList("test1");
        String result = resultlist.get(0);
        assertEquals("202112310900ename", result);
    }

    @Test
    public void test3_5DelExam() {   //시험 삭제
        String query = "DELETE FROM ExamList WHERE subjectName = 'test1';";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

        List<ExamInfo> resultlist = helper.loadExamList("20211001");
        assertEquals(0, resultlist.size());
    }


    @Test
    public void test3_6LoadExamList() { //삭제되었는지 확인
        List<ExamInfo> resultlist = helper.loadExamList("20211231");
        assertEquals(0, resultlist.size());

    }

    @Test
    public void test4_1AddAlarm() {   //알림 정보 추가
        String query = "INSERT INTO AlarmInfoList VALUES('test1', '1일 전', '1시간 전', '1시간 전');";
        boolean result = helper.executeQuery(query);
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
    public void test4_2loadAlarmList() { //추가되었는지 확인
        List<AlarmInfo> resultlist = helper.loadAlarmList();
        assertEquals(1, resultlist.size());

         AlarmInfo resultinfo = resultlist.get(0);
        assertEquals("test1", resultinfo.getSubjectName());
        assertEquals("1시간 전", resultinfo.getAssignmentAlarmDate());
        assertEquals("1일 전", resultinfo.getExamAlarmDate());
        assertEquals("1시간 전", resultinfo.getVideoLectureAlarmDate());

    }

    @Test
    public void test4_3AddAlarmEquals() {   //동일한 알림 정보 추가
        String query = "INSERT INTO AlarmInfoList VALUES('test1', '1일 전', '1시간 전', '1시간 전');";
        boolean result = helper.executeQuery(query);
        assertFalse(result);
    }

    @Test
    public void test4_4DelAlarm() {   //알림 정보 삭제
        String query = "DELETE FROM AlarmInfoList WHERE subjectName = 'test1';";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

        List<AlarmInfo> resultlist = helper.loadAlarmList();
        assertEquals(0, resultlist.size());
    }

    @Test
    public void test4_5loadAlarmInfo() { //알림이 삭제되었는지
        List<AlarmInfo> resultlist = helper.loadAlarmList();
        assertEquals(0,resultlist.size());
    }

    @Test
    public void test5_1SetAlarmNum() {   //시스템 알림 추가
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
    public void test5_2LoadAlarmNum() { //저장해둔 시스템알림 번호들고오기
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
    public void test5_3LoadAlarmSubjectList() { // 불러온 과목의 알림번호들이 알맞은지
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
    public void test5_4DelSysAlarm() { //시스템알림삭제
        String query = "DELETE FROM AlarmList WHERE number = 1;";
        boolean result = helper.executeQuery(query);
        assertTrue(result);

    }

    @Test
    public void test5_5LoadAlarmNum() {//존재하지않는알림번호들고오기

        int a = helper.loadAlarmNum("a");
        assertEquals(-1,a);
    }

    @AfterClass
    public static void finish() {
        String sql = "DELETE FROM SubjectList";
        helper.executeQuery(sql);

        sql = "DELETE FROM LectureList";
        helper.executeQuery(sql);

        sql = "DELETE FROM AssignmentList";
        helper.executeQuery(sql);

        sql = "DELETE FROM ExamList";
        helper.executeQuery(sql);

        sql = "DELETE FROM AlarmInfoList";
        helper.executeQuery(sql);

        sql = "DROP TABLE AlarmList";
        helper.executeQuery(sql);

        sql = "CREATE TABLE IF NOT EXISTS AlarmList (" +
                "name TEXT NOT NULL, subjectName TEXT NOT NULL, number INTEGER PRIMARY KEY AUTOINCREMENT);";
        helper.executeQuery(sql);
    }


}
