package com.example.todo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubjectManagement {


    /* --------------------------- sqlite에서 과목정보 가져오기 ------------------------------------- */
    public List<SubjectInfo> getSubjectList() {
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<SubjectInfo> list = adapter.loadSubjectList();
        return list;
    }

    /* ------------------------------- 과목 추가, 강의추가 ----------------------------------------- */
    public int addSubject(String subjectName, int number, int startWeekNumber, String startTime, int endWeekNumber, String endTime, int year, int semester) {
        //startWeekNumber  2:월, 3:화, 4:수, 5:목, 6:금

        // 예외처리
        // -1은 화면에서 올 수 없는 경우
        if(number < 0 || number > 9) return -1;
        if(startWeekNumber < 2 || startWeekNumber > 6) return -1;
        if(Integer.parseInt(startTime) >= 2400) return -1;
        if((Integer.parseInt(startTime) % 100) >= 60) return -1;
        if(endWeekNumber < 2 || endWeekNumber > 6) return -1;
        if(Integer.parseInt(endTime) >= 2400) return -1;
        if((Integer.parseInt(endTime) % 100) >= 60) return -1;
        if(year < 1900 || year > 2100) return -1;
        if(semester != 1 && semester != 2) return -1;


        // sqlite에 과목 추가
        String query = "INSERT INTO SubjectList VALUES('"+
                subjectName+"',"+number+","+startWeekNumber+","+startTime+","+endWeekNumber+","+endTime+");";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.executeQuery(query);

        if(result == false) return 0;


        Date startdate = getstartDate(year, semester, startWeekNumber);         // 1주차 강의 시작 날짜 구하기
        int enddate = endWeekNumber - startWeekNumber;          // 시작날짜와 종료날짜 간의 차이 구하기
        if(enddate < 0)
            enddate += 7;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strstartdate;
        String strenddate;

        // firebase에 추가할 때 Hashmap을 전달
        // key: 강의이름, value: Hashmap<String, Integer> (startDate, startTime, endDate, endTime, isDone) 정보
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        HashMap<String, Object> lecturelist = new HashMap<>();


        Calendar cal = Calendar.getInstance();
        cal.setTime(startdate);
        String lectureName;

        // 1주차부터 16주차까지 강의 생성
        for(int i = 1; i <= 16; i++)
        {
            strstartdate = dateFormat.format(cal.getTime());        // 시작날짜
            cal.add(Calendar.DATE, enddate);
            strenddate = dateFormat.format(cal.getTime());          // 종료날짜

            for(int j = 1; j <= number; j++)    //사용자가 입력한 한 주의 강의 갯수 만큼
            {
                //sqlite insert
                lectureName = subjectName+" "+i+"주차"+j;
                query = "INSERT INTO LectureList VALUES('" +
                        subjectName+"','"+lectureName+"',"+strstartdate+","+startTime+","+strenddate+","+endTime+",0);";
                result = adapter.executeQuery(query);

                //firebase insert
                HashMap<String, Object> map = new HashMap<>();
                map.put("startDate", Integer.parseInt(strstartdate));
                map.put("startTime", Integer.parseInt(startTime));
                map.put("endDate", Integer.parseInt(strenddate));
                map.put("endTime", Integer.parseInt(endTime));
                map.put("isDone", 0);

                lecturelist.put(lectureName, map);

                if(result == false) {
                    query = "DELETE FROM SubjectList WHERE subjectName = '" + subjectName + "';";
                    adapter.executeQuery(query);
                    return -1;
                }
            }

            cal.add(Calendar.DATE, 7-enddate);  // startdate에 1주일 추가.
        }

        firebaseDB.uploadMyLecture(subjectName, lecturelist);   // firebase에 강의정보 insert


        return 1;
    }

    /* --------------------------- 1주차 강의 시작날짜를 구하는 메소드 -------------------------------- */
    public Date getstartDate(int year, int semester, int startWeekNumber) {  //test때문에 public
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = Integer.toString(year);
        if(semester == 1)               // 1학기라면 3월 2일 부터
            date += "0302";
        else if(semester == 2)          // 2학기라면 9월 1일 부터
            date += "0901";

        Calendar cal = Calendar.getInstance();
        try {
            java.util.Date nDate = dateFormat.parse(date);

            cal.setTime(nDate);
            int dayNum = cal.get(Calendar.DAY_OF_WEEK);     // 3/1, 9/1 의 요일 구하기

            while (dayNum != startWeekNumber) {             // 사용자가 입력한 시작 요일이 될 때까지 +1일
                cal.add(Calendar.DATE, 1);
                dayNum = cal.get(Calendar.DAY_OF_WEEK);
            }

        }catch(ParseException e){
            Log.e("AddSubject->AddLecure", "getstartDate >>"+ e.toString());
        }
        return cal.getTime();
    }

    /* ------------------------------------- 과목 삭제 --------------------------------------- */
    public void delSubject(String subjectName) {        //test때문에 일단 public
        // sqlite delete
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        String query = "DELETE FROM SubjectList WHERE subjectName = '"+subjectName+"';";
        adapter.executeQuery(query);

        query = "DELETE FROM LectureList WHERE subjectName = '"+subjectName+"';";
        adapter.executeQuery(query);

        query = "DELETE FROM AssignmentList WHERE subjectName = '"+subjectName+"';";
        adapter.executeQuery(query);

        query = "DELETE FROM ExamList WHERE subjectName = '"+subjectName+"';";
        adapter.executeQuery(query);

        // 과목에 알림이 설정되어 있다면 시스템에 알림을 지운다.
        AlarmInfo alarmInfo = adapter.loadAlarmInfo(subjectName);
        if(alarmInfo != null)
        {
            AlarmManagement alarmManagement = new AlarmManagement();
            alarmManagement.delSubjectAlarm(subjectName);

            query = "DELETE FROM AlarmInfoList WHERE subjectName = '"+subjectName+"';";
            adapter.executeQuery(query);

            query = "DELETE FROM AlarmList WHERE subjectName = '"+subjectName+"';";
            adapter.executeQuery(query);
        }

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delSubject(subjectName);

    }
}
