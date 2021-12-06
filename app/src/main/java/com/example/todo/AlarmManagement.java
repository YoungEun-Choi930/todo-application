package com.example.todo;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmManagement {


    public static AlarmManager alarmManager;
    public static NotificationManager notificationManager;
    public static PendingIntent pendingIntent;
    public static int number;

    public List<AlarmInfo> getAlarmList(){ //알림리스트 리턴
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        List<AlarmInfo> list = adapter.loadAlarmList();
        return list;
    }


    public boolean addAlarm(String subjectName, String exam, String assignment, String video){ //알림추가
        String query = "INSERT INTO AlarmInfoList VALUES('"+
                subjectName+"','"+exam+"','"+assignment+"','"+video+"');";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.executeQuery(query); //쿼리문 날려서

        if(result == false) return false; //실패면 false

        AlarmInfo alarmInfo = new AlarmInfo(false,subjectName,exam,assignment,video);
        AlarmManagementActivity.alarmInfoList.add(alarmInfo); //화면의 리스트에 추가
        setSystemAlarm(subjectName, exam, assignment, video);//시스템알림 추가
        return true;
    }
    public boolean delAlarm(String subjectName) {//알림삭제

        SQLiteDBHelper helper = new SQLiteDBHelper();
        AlarmInfo alarmInfo = helper.loadAlarmInfo(subjectName);
        if(alarmInfo == null){
            return false;
        }

        String query = "DELETE FROM AlarmInfoList WHERE subjectName = '"+subjectName+"';";
        SQLiteDBHelper adapter = new SQLiteDBHelper();
        boolean result = adapter.executeQuery(query);
        delSubjectAlarm(subjectName);//과목에 설정된 알림 삭제
        return result;
    }


    private void setSystemAlarm(String subjectName, String exam, String assignment, String video) {//시스템알림 설정

        int examnum = Integer.parseInt(exam.substring(0,1));
        examnum = examnum * 24;

        int assignmentnum;
        if(assignment.equals("1일 전"))
            assignmentnum=24;
        else
            assignmentnum=Integer.parseInt(assignment.substring(0,1));

        int videonum;
        if(video.equals("1일 전"))
            videonum=24;
        else {
            videonum = Integer.parseInt(video.substring(0, 1));
        }
        //알림시간 계산

        SQLiteDBHelper helper = new SQLiteDBHelper();
        List<String> list = helper.loadLectureDateList(subjectName);            // sqlite에서 강의목록 들고오기

        //lecture
        for(String str: list) {
            String date = str.substring(0,12);
            String name = str.substring(12);

            addSystemAlarm(subjectName, name, date, videonum, "Lecture");

        }

        //assignment
        list = helper.loadAssignmentDateList(subjectName);
        for(String str: list) {
            String date = str.substring(0,12);
            String name = str.substring(12);

            addSystemAlarm(subjectName, name, date, assignmentnum, "Assignment");
        }

        //exam
        list = helper.loadExamDateList(subjectName);
        for(String str: list) {
            String date = str.substring(0,12);
            String name = str.substring(12);

            addSystemAlarm(subjectName, name, date, examnum, "Exam");

        }

    }

    /* ------------------- 과제, 시험을 추가하거나 / 강의, 과제의 isDone을 바꾸는 경우 -------------------- */
    public void addSystemAlarm(String subjectName, String alarmName, String alarmTime, int hourNum, String table){
        //tableNum: "Lecture" or "Assignment" or "Exam"
        //   alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


        //알림 날짜 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(alarmTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        calendar.add(Calendar.HOUR_OF_DAY, -hourNum);

        SQLiteDBHelper helper = new SQLiteDBHelper();
        int alarmNum = helper.setAlarmNum(alarmName,subjectName);
        number = alarmNum;
        Intent receiverIntent = new Intent(TodoManagementActivity.mContext, AlarmRecevier.class);
        pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, alarmNum, receiverIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    public void delSubjectAlarm(String subjectName){ //과목에 대한 알람을 한꺼번에 삭제
        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper();

        List<Integer> alarmNumber = sqLiteDBHelper.loadAlarmSubjectList(subjectName);
        for(int i=0;i<alarmNumber.size();i++){
            delSystemAlarm(alarmNumber.get(i));

        }
    }

    public void delSystemAlarm(int num){ //delSubjectAlarm에서 사용함. 번호로 삭제

        Intent intent = new Intent(LoginActivity.ApplicationContext, AlarmRecevier.class);
        pendingIntent = PendingIntent.getBroadcast(TodoManagementActivity.mContext, num, intent, 0);
        notificationManager.cancel(num);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }


}
