package com.example.todo;

public class SubjectInfo {
    private String subjectName;
    private boolean checked = false;

    public SubjectInfo(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
