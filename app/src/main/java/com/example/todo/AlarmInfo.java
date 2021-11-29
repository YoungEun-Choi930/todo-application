package com.example.todo;
/*
강의 정보를 sqlite에서 들고와서 화면에 보여주고자 할 때 정보를 List<Alarm>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재하며, subjectName, examAlarmDate, assignmentAlarmDate, videoLectureAlarmDate, realTimeLectureAlarmDate에 대하여
메소드를 getter만 두어 생성시에만 초기화할 수 있도록 하고,
checked에 대하여 메소드를 getter와 setter를 두어 알림 삭제 시 체크값을 설정 및 받아올 수 있도록 한다.
 */
public class AlarmInfo {
    private boolean checked;
    private String alarmTime;

    private int alarmNumber;
// 알람넘버 추가

   public String getAlarmTime() { return alarmTime; }
    public int getAlarmNumber() { return alarmNumber; }


    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public AlarmInfo(boolean checked, String alarmTime, int alarmNumber) {
        this.checked = checked;
        this.alarmTime = alarmTime;
        this.alarmNumber = alarmNumber;
    }
}
