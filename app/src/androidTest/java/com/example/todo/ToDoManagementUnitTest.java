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
    public void 테스트0_02년도가1899과제추가() {
        boolean result = management.addAssignment("test","asstest2", "18991001", "0000", "18991101", "0000");
        assertFalse(result);

    }


    @Test
    public void 테스트0_03년도가2101과제추가() {
        boolean result = management.addAssignment("test","asstest3", "21011001", "0000", "21011101", "0000");
        assertFalse(result);

    }

    @Test
    public void 테스트0_04월이0과제추가() {
        boolean result = management.addAssignment("test","asstest4", "20210001", "0000", "20210002", "0000");
        assertFalse(result);

    }

    @Test
    public void 테스트0_05월이13과제추가() {
        boolean result = management.addAssignment("test","asstest5", "20211301", "0000", "20211302", "0000");
        assertFalse(result);

    }


    @Test
    public void 테스트0_06일이0과제추가() {
        boolean result = management.addAssignment("test","asstest6", "20211000", "0000", "2021100", "0000");
        assertFalse(result);

    }


    @Test
    public void 테스트0_07일이32과제추가() {
        boolean result = management.addAssignment("test","asstest7", "20211032", "0000", "2021132", "0000");
        assertFalse(result);

    }


    @Test
    public void 테스트0_08시가24과제추가() {
        boolean result = management.addAssignment("test","asstest8", "20211001", "2400", "20211101", "2400");
        assertFalse(result);

    }


    @Test
    public void 테스트0_09분이60과제추가() {
        boolean result = management.addAssignment("test","asstest9", "20211001", "0060", "20211101", "0060");
        assertFalse(result);

    }


    @Test
    public void 테스트1_01시험추가() {
        boolean result = management.addExam("test","examtest1","20211001","0000");
        assertTrue(result);

    }

    @Test
    public void 테스트1_02년도가1899시험추가() {
        boolean result = management.addExam("test","examtest2","18991001","0000");
        assertFalse(result);

    }

    @Test
    public void 테스트1_03년도가2101시험추가() {
        boolean result = management.addExam("test","examtest3","21011001","0000");
        assertFalse(result);

    }


    @Test
    public void 테스트1_04월이0시험추가() {
        boolean result = management.addExam("test","examtest4","20210001","0000");
        assertFalse(result);

    }


    @Test
    public void 테스트1_05월이13시험추가() {
        boolean result = management.addExam("test","examtest5","20211301","0000");
        assertFalse(result);

    }


    @Test
    public void 테스트1_06일이0시험추가() {
        boolean result = management.addExam("test","examtest6","202111000","0000");
        assertFalse(result);

    }


    @Test
    public void 테스트1_07일이32시험추가() {
        boolean result = management.addExam("test","examtest7","202111032","0000");
        assertFalse(result);

    }


    @Test
    public void 테스트1_08시가24시험추가() {
        boolean result = management.addExam("test","examtest8","20211001","2400");
        assertFalse(result);

    }


    @Test
    public void 테스트1_09분이60시험추가() {
        boolean result = management.addExam("test","examtest9","20211001","0060");
        assertFalse(result);

    }

    @Test
    public void 테스트2_01getToDo() {
        HashMap<String, Object> resulthashmap = management.getToDoList("20211001");

        assignmentList.add(new AssignmentInfo("test","asstest1",false));
        examList.add(new ExamInfo("test","examtest1"));
        assertTrue(assertHashmap(resulthashmap));

        assignmentList = new ArrayList<>();
        examList = new ArrayList<>();

    }


    @Test
    public void 테스트2_02getToDo() {
        HashMap<String, Object> resulthashmap = management.getToDoList("20211102");

        assertNull(resulthashmap.get(0));
        assertNull(resulthashmap.get(1));
        assertNull(resulthashmap.get(2));

    }

    private boolean assertHashmap(HashMap<String, Object> result) {
        List<List> resultlist = (List<List>) result.get("test");


        List<AssignmentInfo> resultassignment = resultlist.get(1);
        List<ExamInfo> resultexam = resultlist.get(2);

        if(assignmentList.size()!=resultassignment.size())
            return false;
        if(examList.size()!=resultexam.size())
            return false;
     //   assertEquals(assignmentList.size(), resultassignment.size());
     //   assertEquals(examList.size(), resultexam.size());

        for(int i = 0; i < assignmentList.size(); i++) {
            assertEquals(assignmentList.get(i).getSubjectName(), resultassignment.get(i).getSubjectName());
            assertEquals(assignmentList.get(i).getAssignmentName(), resultassignment.get(i).getAssignmentName());
            assertEquals(assignmentList.get(i).getIsDone(), resultassignment.get(i).getIsDone());
        }
        for(int i = 0; i < examList.size(); i++) {
            assertEquals(examList.get(i).getSubjectName(), resultexam.get(i).getSubjectName());
            assertEquals(examList.get(i).getExamName(), resultexam.get(i).getExamName());
        }
        return true;
    }

    @Test
    public void 테스트2_03ChangeisDone() {
        boolean result = management.changeIsDone("asstest1", "test", "assignment", 1);
        assertTrue(result);
    }

    @Test
    public void 테스트2_04getToDo() {
        HashMap<String, Object> resulthashmap = management.getToDoList("20211001");

        assignmentList.add(new AssignmentInfo("test","asstest1",true));
        examList.add(new ExamInfo("test","examtest1"));
        assertTrue(assertHashmap(resulthashmap));

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
        assertTrue(assertHashmap(resulthashmap));


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