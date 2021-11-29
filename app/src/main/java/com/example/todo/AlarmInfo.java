package com.example.todo;
/*
강의 정보를 sqlite에서 들고와서 화면에 보여주고자 할 때 정보를 List<Alarm>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재하며, subjectName, examAlarmDate, assignmentAlarmDate, videoLectureAlarmDate, realTimeLectureAlarmDate에 대하여
메소드를 getter만 두어 생성시에만 초기화할 수 있도록 하고,
checked에 대하여 메소드를 getter와 setter를 두어 알림 삭제 시 체크값을 설정 및 받아올 수 있도록 한다.
 */
public class AlarmInfo {
    private boolean checked;
    private String subjectName;
    private String examAlarmDate;
    private String assignmentAlarmDate;
    private String videoLectureAlarmDate;


    public String getSubjectName() {
        return subjectName;
    }


    public String getExamAlarmDate() {
        return examAlarmDate;
    }

    public String getAssignmentAlarmDate() {
        return assignmentAlarmDate;
    }

    public String getVideoLectureAlarmDate() {
        return videoLectureAlarmDate;
    }


    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public AlarmInfo(boolean checked, String subjectName, String examAlarmDate, String assignmentAlarmDate, String videoLectureAlarmDate) {
        this.checked = checked;
        this.subjectName = subjectName;
        this.examAlarmDate = examAlarmDate;
        this.assignmentAlarmDate = assignmentAlarmDate;
        this.videoLectureAlarmDate = videoLectureAlarmDate;

    }
}
