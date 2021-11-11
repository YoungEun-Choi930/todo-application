package com.example.todo;

public class AlarmInfo {
    public boolean checked;
    private String subjectName;
    private String examAlarmDate;
    private String assingmentAlarmDate;
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

    public String getAssingmentAlarmDate() {
        return assingmentAlarmDate;
    }

    public void setAssingmentAlarmDate(String assingmentAlarmDate) {
        this.assingmentAlarmDate = assingmentAlarmDate;
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

    public AlarmInfo(boolean checked, String subjectName, String examAlarmDate, String assingmentAlarmDate, String videoLectureAlarmDate, String realTimeLectureAlarmDate) {
        this.checked = checked;
        this.subjectName = subjectName;
        this.examAlarmDate = examAlarmDate;
        this.assingmentAlarmDate = assingmentAlarmDate;
        this.videoLectureAlarmDate = videoLectureAlarmDate;
        this.realTimeLectureAlarmDate = realTimeLectureAlarmDate;
    }
}
