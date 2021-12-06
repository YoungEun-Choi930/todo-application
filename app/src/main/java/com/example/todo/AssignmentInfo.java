package com.example.todo;
/*
과제 정보를 sqlite 또는 firebase에서 들고 와서 화면에 보여주고자 할 때 정보를 List<AssignmentInfo>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재한다.
subjectName과 assignmentName에 대하여 메소드를 getter만 두어 Constructor로 생성시에만 초기화 할 수 있도록 하고,
isDone에 대한 메소드로는 getter와 setter를 두어 이후에도 정보를 수정할 수 있도록 한다.
 */
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

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) { this.isDone = isDone;
    }

}
