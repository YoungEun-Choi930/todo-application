package com.example.todo;

public class ExamInfo {
    private String subjectName;
    private String examName;

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

}