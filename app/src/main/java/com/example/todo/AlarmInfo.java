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


    public String getExamAlarmDate() {
        return examAlarmDate;
    }

    public String getAssignmentAlarmDate() {
        return assignmentAlarmDate;
    }

    public String getVideoLectureAlarmDate() {
        return videoLectureAlarmDate;
    }

    public String getRealTimeLectureAlarmDate() {
        return realTimeLectureAlarmDate;
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
