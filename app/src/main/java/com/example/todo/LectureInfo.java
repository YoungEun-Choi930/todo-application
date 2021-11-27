package com.example.todo;
/*
강의 정보를 sqlite 또는 firebase에서 들고 와서 화면에 보여주고자 할 때 정보를 List<LectureInfo>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재한다.
subjectName과 letureName에 대하여 메소드를 getter만 두어 Constructor로 생성시에만 초기화 할 수 있도록 하고,
isDone에 대한 메소드로는 getter와 setter를 두어 이후에도 정보를 수정할 수 있도록 한다.
 */

public class LectureInfo {
    private String subjectName;
    private String lectureName;
    private Boolean isDone;
    private int viewTipe=0;

    public LectureInfo(String subjectName, String lectureName, Boolean isDone) {
        this.subjectName = subjectName;
        this.lectureName = lectureName;
        this.isDone = isDone;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getLectureName() {
        return lectureName;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
    public int getViewTipe() {
        return viewTipe;
    }

    public void setViewTipe(int viewTipe) {
        this.viewTipe = viewTipe;
    }
}
