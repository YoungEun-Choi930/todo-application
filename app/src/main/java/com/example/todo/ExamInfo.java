package com.example.todo;

public class ExamInfo {
    private String subjectName;
    private String examName;
    private int viewTipe=0;

    public ExamInfo(String subjectName, String examName) {

        this.subjectName = subjectName;
        this.examName = examName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getExamName() {
        return examName;
    }
    public int getViewTipe() {
        return viewTipe;
    }

    public void setViewTipe(int viewTipe) {
        this.viewTipe = viewTipe;
    }
}