package com.example.todo;

public class SubjectInfo {
    private String subjectName;
    private boolean checked = false;

    public String getSubjectName() {
        return subjectName;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
