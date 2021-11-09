package com.example.todo;

public class AssingmentInfo {
    private String subjectName;
    private String assingmentName;
    private Boolean isDone;

    public AssingmentInfo(String subjectName, String assingmentName, Boolean isDone) {
        this.subjectName = subjectName;
        this.assingmentName = assingmentName;
        this.isDone = isDone;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAssingmentName() {
        return assingmentName;
    }

    public void setAssingmentName(String assingmentName) {
        this.assingmentName = assingmentName;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isdone) {
        isDone = isdone;
    }
}
