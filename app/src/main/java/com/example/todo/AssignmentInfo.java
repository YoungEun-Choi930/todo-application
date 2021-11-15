package com.example.todo;

public class AssignmentInfo {
    private String subjectName;
    private String assignmentName;
    private Boolean isDone;

    public AssignmentInfo(String subjectName, String assignmentName, Boolean isDone) {
        this.subjectName = subjectName;
        this.assignmentName = assignmentName;
        this.isDone = isDone;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        isDone = isDone;
    }
}
