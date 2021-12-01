package com.example.todo;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToDoManagementUnitTest {

    public static List<AssignmentInfo> assignmentList;
    public static List<ExamInfo> examList;
    ToDoManagement management = new ToDoManagement();


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

        assignmentList = new ArrayList<>();
        examList = new ArrayList<>();


    }


    @Test
    public void 테스트0_01과제추가() {
        boolean result = management.addAssignment("test","asstest1", "20211001", "0000", "20211101", "0000");
        assertTrue(result);

    }

    @Test
    public void 테스트1_01시험추가() {
        boolean result = management.addExam("test","examtest1","20211001","0000");
        assertTrue(result);

    }


    @Test
    public void 테스트2_01getToDo() {
        HashMap<String, Object> resulthashmap = management.getToDoList("20211001");

        assignmentList.add(new AssignmentInfo("test","asstest1",false));
        examList.add(new ExamInfo("test","examtest1"));
        assertHashmap(resulthashmap);

        assignmentList = new ArrayList<>();
        examList = new ArrayList<>();

    }

    private void assertHashmap(HashMap<String, Object> result) {
        List<List> resultlist = (List<List>) result.get("test");
        List<AssignmentInfo> resultassignment = resultlist.get(1);
        List<ExamInfo> resultexam = resultlist.get(2);

        assertEquals(assignmentList.size(), resultassignment.size());
        assertEquals(examList.size(), resultexam.size());

        for(int i = 0; i < assignmentList.size(); i++) {
            assertEquals(assignmentList.get(i).getSubjectName(), resultassignment.get(i).getSubjectName());
            assertEquals(assignmentList.get(i).getAssignmentName(), resultassignment.get(i).getAssignmentName());
            assertEquals(assignmentList.get(i).getIsDone(), resultassignment.get(i).getIsDone());
        }
        for(int i = 0; i < examList.size(); i++) {
            assertEquals(examList.get(i).getSubjectName(), resultexam.get(i).getSubjectName());
            assertEquals(examList.get(i).getExamName(), resultexam.get(i).getExamName());
        }
    }

    @Test
    public void 테스트2_02ChangeisDone() {
        boolean result = management.changeIsDone("asstest1", "test", "assignment", 1);
        assertTrue(result);
    }

    @Test
    public void 테스트2_03getToDo() {
        HashMap<String, Object> resulthashmap = management.getToDoList("20211001");

        assignmentList.add(new AssignmentInfo("test","asstest1",true));
        examList.add(new ExamInfo("test","examtest1"));
        assertHashmap(resulthashmap);

        assignmentList = new ArrayList<>();
        examList = new ArrayList<>();
    }

    @Test
    public void 테스트3_01과제삭제() {
        management.delAssignment("asstest1", "test");


    }

    @Test
    public void 테스트3_02getToDo() {

        HashMap<String, Object> resulthashmap = management.getToDoList("20211001");

        examList.add(new ExamInfo("test","examtest1"));
        assertHashmap(resulthashmap);

        assignmentList = new ArrayList<>();
        examList = new ArrayList<>();
    }

    @Test
    public void 테스트4_01시험삭제() {
        management.delExam("examtest1", "test");

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