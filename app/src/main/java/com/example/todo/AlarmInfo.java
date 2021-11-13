package com.example.todo;

public class AlarmInfo {
    private boolean checked;
    private String subjectName;
    private String examAlarmDate;
    private String assignmentAlarmDate;
    private String videoLectureAlarmDate;
    private String realTimeLectureAlarmDate;


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamAlarmDate() {
        return examAlarmDate;
    }

    public void setExamAlarmDate(String examAlarmDate) {
        this.examAlarmDate = examAlarmDate;
    }

    public String getAssignmentAlarmDate() {
        return assignmentAlarmDate;
    }

    public void setAssignmentAlarmDate(String assignmentAlarmDate) {
        this.assignmentAlarmDate = assignmentAlarmDate;
    }

    public String getVideoLectureAlarmDate() {
        return videoLectureAlarmDate;
    }

    public void setVideoLectureAlarmDate(String videoLectureAlarmDate) {
        this.videoLectureAlarmDate = videoLectureAlarmDate;
    }

    public String getRealTimeLectureAlarmDate() {
        return realTimeLectureAlarmDate;
    }

    public void setRealTimeLectureAlarmDate(String realTimeLectureAlarmDate) {
        this.realTimeLectureAlarmDate = realTimeLectureAlarmDate;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public AlarmInfo(boolean checked, String subjectName, String examAlarmDate, String assignmentAlarmDate, String videoLectureAlarmDate, String realTimeLectureAlarmDate) {
        this.checked = checked;
        this.subjectName = subjectName;
        this.examAlarmDate = examAlarmDate;
        this.assignmentAlarmDate = assignmentAlarmDate;
        this.videoLectureAlarmDate = videoLectureAlarmDate;
        this.realTimeLectureAlarmDate = realTimeLectureAlarmDate;
    }
}
