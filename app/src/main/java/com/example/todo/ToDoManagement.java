package com.example.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToDoManagement {


    /* -------------------------- 선택한 날짜에 대한 to do 목록 불러오기 ------------------------------ */
    public HashMap<String, Object> getToDoList(String date) {
        // sqlite에서 정보 가져오기
        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<LectureInfo> lecturelist = helper.loadLectureList(date);
        List<AssignmentInfo> assignmentList = helper.loadAssignmentList(date);
        List<ExamInfo> examlist = helper.loadExamList(date);

        // HashMap으로 가공
        // Key: SubjectName, Value: List<List>(lectureList, assignmnetList, examList)
        HashMap<String, Object> map = new HashMap<>();

        //lecture
        for (LectureInfo lectureInfo : lecturelist) {
            if(map.containsKey(lectureInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(lectureInfo.getSubjectName());
                List<LectureInfo> lecture = todolist.get(0);
                lecture.add(lectureInfo);
                todolist.set(0,lecture);
                map.put(lectureInfo.getSubjectName(), todolist);
            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                lecture.add(lectureInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(lectureInfo.getSubjectName(),todolist);
            }
        }

        //assignment
        for(AssignmentInfo assignmentInfo: assignmentList) {
            if(map.containsKey(assignmentInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(assignmentInfo.getSubjectName());
                List<AssignmentInfo> lecture = todolist.get(1);
                lecture.add(assignmentInfo);
                todolist.set(1,lecture);
                map.put(assignmentInfo.getSubjectName(), todolist);

            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                assignment.add(assignmentInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(assignmentInfo.getSubjectName(),todolist);
            }
        }

        //exam
        for(ExamInfo examInfo: examlist) {
            if(map.containsKey(examInfo.getSubjectName())) {
                List<List> todolist = (List<List>) map.get(examInfo.getSubjectName());
                List<ExamInfo> lecture = todolist.get(2);
                lecture.add(examInfo);
                todolist.set(2,lecture);
                map.put(examInfo.getSubjectName(), todolist);

            }
            else {
                List<List> todolist = new ArrayList<>();
                List<LectureInfo> lecture = new ArrayList<>();
                List<AssignmentInfo> assignment = new ArrayList<>();
                List<ExamInfo> exam = new ArrayList<>();

                exam.add(examInfo);

                todolist.add(lecture);
                todolist.add(assignment);
                todolist.add(exam);
                map.put(examInfo.getSubjectName(),todolist);
            }
        }

        return map;
    }

    /* --------------------------- 선택한 과목에 대하여 과제를 추가하기 -------------------------------- */
    public boolean addAssignment(String subjectName, String assignmentName, String startDate, String startTime, String endDate, String endTime) {
        // 예외처리
        // 화면에서 올 수 없는 경우
        int istartd = Integer.parseInt(startDate);
        int istartt = Integer.parseInt(startTime);
        int iendd = Integer.parseInt(endDate);
        int iendt = Integer.parseInt(endTime);

        if((istartd/10000) > 2100 || (istartd/10000) < 1900) return false;
        if((istartd%10000)/ 100 > 12 || (istartd%10000) / 100 < 1) return false;
        if((istartd%100) > 31 || (istartd%100) < 1) return false;
        if(istartt >= 2400) return false;
        if((istartt % 100) >= 60) return false;
        if((iendd/10000) > 2100 || (iendd/10000) < 1900) return false;
        if((iendd%10000)/ 100 > 12 || (iendd%10000) / 100 < 1) return false;
        if((iendd%100) > 31 || (iendd%100) < 1) return false;
        if(iendt >= 2400) return false;
        if((iendt % 100) >= 60) return false;

        // sqlite insert
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO AssignmentList VALUES('" +
                subjectName+"','"+assignmentName+"',"+startDate+","+startTime+","+endDate+","+endTime+",0);";
        boolean result = helper.executeQuery(query);

        // firebase insert
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyAssignment(subjectName,assignmentName, startDate, startTime, endDate, endTime);

        // 과목에 알림이 설정되어 있다면 추가한 시험에 대하여 알림 설정
        AlarmInfo alarmInfo = helper.loadAlarmInfo(subjectName);
        if(alarmInfo == null)   return result;              // 알림이 설정되어 있지 않으면 종료

        int hourNum = 0;
        switch (alarmInfo.getAssignmentAlarmDate()) {
            case "1시간 전": hourNum = 1; break;
            case "2시간 전": hourNum = 2; break;
            case "3시간 전": hourNum = 3; break;
            case "5시간 전": hourNum = 4; break;
        }

        // system alarm add
        AlarmManagement alarmManagement = new AlarmManagement();
        alarmManagement.addSystemAlarm(subjectName, assignmentName,endDate+endTime, hourNum,"Lecture");

        return result;
    }

    /* ------------------------------------ 선택한 과제 삭제하기 ------------------------------------ */
    public boolean delAssignment(String assignmentName, String subjectName) {
        // sqlite delete
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM AssignmentList WHERE assignmentName = '"+assignmentName+"';";
        boolean result = helper.executeQuery(query);

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyAssignment(assignmentName, subjectName);

        // 알림이 존재한다면 삭제
        int num = helper.loadAlarmNum(assignmentName);
        if(num != -1){
            //system alarm delete
            AlarmManagement alarmManagement = new AlarmManagement();
            alarmManagement.delSystemAlarm(num);

            // sqlite delete
            query = "DELETE FROM AlarmList WHERE number = "+num+";";
            helper.executeQuery(query);
        }

        return result;
    }

    /* --------------------------- 선택한 과목에 대하여 시험을 추가하기 -------------------------------- */
    public boolean addExam(String subjectName, String examName, String date, String time) {
        // 예외처리
        // 화면에서 올 수 없는 경우
        int idate = Integer.parseInt(date);
        int itime = Integer.parseInt(time);

        if((idate/10000) > 2100 || (idate/10000) < 1900) return false;
        if((idate%10000)/ 100 > 12 || (idate%10000) / 100 < 1) return false;
        if((idate%100) > 31 || (idate%100) < 1) return false;
        if(itime >= 2400) return false;
        if((itime % 100) >= 60) return false;

        // sqlite insert
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "INSERT INTO ExamList VALUES('" +
                subjectName+"','"+examName+"',"+date+","+time+");";
        boolean result = helper.executeQuery(query);

        // firebase insert
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.uploadMyExam(subjectName,examName, date, time);

        // 과목에 알림이 설정되어 있다면 추가한 시험에 대하여 알림 설정
        AlarmInfo alarmInfo = helper.loadAlarmInfo(subjectName);
        if(alarmInfo == null)   return result;              // 알림이 설정되어 있지 않으면 종료

        int hourNum = 0;
        switch (alarmInfo.getExamAlarmDate()) {
            case "1일 전": hourNum = 24; break;
            case "3일 전": hourNum = 24*3; break;
            case "5일 전": hourNum = 24*5; break;
            case "7일 전": hourNum = 24*7; break;
        }

        // system alarm add
        AlarmManagement alarmManagement = new AlarmManagement();
        alarmManagement.addSystemAlarm(subjectName, examName, date+time, hourNum, "Exam");

        return result;
    }

    /* ---------------------------------- 선택한 시험 삭제하기 -------------------------------------- */
    public boolean delExam(String examName, String subjectName) {
        // sqlite delete
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "DELETE FROM ExamList WHERE examName = '"+examName+"';";
        boolean result = helper.executeQuery(query);

        // firebase delete
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delMyExam(examName, subjectName);

        // 알림이 존재한다면 삭제
        int num = helper.loadAlarmNum(examName);
        if(num != -1){
            //system alarm delete
            AlarmManagement alarmManagement = new AlarmManagement();
            alarmManagement.delSystemAlarm(num);
            // sqlite delete
            query = "DELETE FROM AlarmList WHERE number = "+num+";";
            helper.executeQuery(query);
        }

        return result;
    }

    /* --------------------------- 선택한 강의 또는 과제 완료 구분 변경하기 ---------------------------- */
    public boolean changeIsDone(String name, String subjectName, String table, int value) {
        // name: lectureName or assignmentName
        // subjectName: 해당 lecture 또는 assignment의 과목이름.
        // table: "Lecture" or "Assignment"
        // value: 바꿔야하는 isDone 값. 만약 지금 1이면 0을 보내고, 0이면 1을 보내주어야 함.

        // sqlite update
        SQLiteDBHelper helper = new SQLiteDBHelper();
        String query = "UPDATE "+table+"List SET isDone = "+value+" WHERE "+table.toLowerCase()+"Name = '"+name+"';";
        boolean result = helper.executeQuery(query);

        if(result == false) return false;

        // firebase update
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.changeMyIsDone(name, subjectName, table, value);

        // 과목에 알림이 설정되어 있다면 알림 설정을 바꿔주어야함.
        AlarmInfo alarmInfo = helper.loadAlarmInfo(subjectName);
        if(alarmInfo == null)   return result;     // 알림이 설정되어 있지 않으면 종료

        //알림이 설정되 있다면
        AlarmManagement alarmManagement = new AlarmManagement();
        //isDone을 false로 설정해야하면
        if(value == 0) {
            int num = helper.loadAlarmNum(name);
            alarmManagement.delSystemAlarm(num);
        }
        else {  //isDone을 true로 설정해야하면
            String alarmTime = helper.getAlarmTime(name, subjectName, table);

            int hourNum = 0;

            if(table.equals("Lecture")){
                if(alarmInfo.getVideoLectureAlarmDate().equals("1일 전"))
                    hourNum=24;
                else
                    hourNum=Integer.parseInt(alarmInfo.getVideoLectureAlarmDate().substring(0,1));
            }
            else if(table.equals("Assignment")){
                if(alarmInfo.getAssignmentAlarmDate().equals("1일 전"))
                    hourNum=24;
                else
                    hourNum=Integer.parseInt(alarmInfo.getVideoLectureAlarmDate().substring(0,1));
            }
            //system alarm add
            alarmManagement.addSystemAlarm(subjectName, name, alarmTime, hourNum, table);
        }

        return result;
    }

}
