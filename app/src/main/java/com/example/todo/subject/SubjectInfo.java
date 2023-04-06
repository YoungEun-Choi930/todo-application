package com.example.todo.subject;
/*
과목정보를 sqlite에서 들고와서 화면에 보여주고자 할 때 정보를 List<SubjectInfo>와 같이 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재한다.
subjectName에 대하여 메소드를 getter만 두어 Constructor로 생성시에만 초기화할 수 있도록 하고,
checked는 getter와 setter를 만들어 과목 삭제 시 체크 값을 설정 및 받아올 수 있도록 한다.
 */
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
