package com.example.todo;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlarmManagementUnitTest {

    public static List<AlarmInfo> alarmList;
    AlarmManagement alarmManagement = new AlarmManagement();

    @BeforeClass
    public static void setUp() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getEmail();
        userID = userID.substring(0, userID.indexOf('@'));
        LoginActivity.USERID = userID;
        LoginActivity.USERUID = auth.getCurrentUser().getUid();

        LoginActivity.ApplicationContext = ApplicationProvider.getApplicationContext();

        ActivityScenario.launch(TodoManagementActivity.class);
        ActivityScenario.launch(AlarmManagementActivity.class);

        SubjectManagement subjectManagement = new SubjectManagement();
        subjectManagement.addSubject("testAlarm",1,2,"0900",6,"2359",2021,2);

        alarmList = new ArrayList<>();
    }

   @Test
    public void 테스트1정상적인알림추가() {
        boolean result = alarmManagement.addAlarm("testAlarm","1일 전","1시간 전","1시간 전");
       assertEquals(true, result);
       alarmList.add(new AlarmInfo(false,"testAlarm","1일 전", "1시간 전", "1시간 전"));


   }

   @Test
   public void 테스트2알림목록조회 () {
       List<AlarmInfo> resultList = alarmManagement.getAlarmList();
       assertEquals(alarmList.size(),resultList.size());
   }

   @Test
   public void 테스트3이미존재하는알림추가() {
       boolean result = alarmManagement.addAlarm("testAlarm","1일 전","1시간 전","1시간 전");
       assertEquals(false, result);
   }

   @Test
   public void 테스트4정상적인알림삭제(){

        boolean result = alarmManagement.delAlarm("testAlarm");
        alarmList.remove(0);

       assertEquals(true, result);
   }

   @Test
   public void 테스트5알림목록조회() {
       List<AlarmInfo> resultList = alarmManagement.getAlarmList();

       assertEquals(0,resultList.size());
   }
   @Test
   public void 테스트6존재하지않는알림삭제(){
        boolean result = alarmManagement.delAlarm("test0");
        assertEquals(false, result);
   }




    @AfterClass
    public static void deleteAlarm() {
        SubjectManagement subjectManagement = new SubjectManagement();
        subjectManagement.delSubject("testAlarm");

    }

}
