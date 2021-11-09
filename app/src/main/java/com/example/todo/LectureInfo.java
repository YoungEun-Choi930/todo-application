package com.example.todo;

public class LectureInfo {
    private String subjectName;
    private String lectureName;
    private Boolean isDone;

    public LectureInfo(String subjectName, String lectureName, Boolean isDone) {
        this.subjectName = subjectName;
        this.lectureName = lectureName;
        this.isDone = isDone;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
