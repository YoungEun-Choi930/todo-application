package com.example.todo.assignmentexam;
/*
시험 정보를 sqlite 또는 firebase에서 들고 와서 화면에 보여주고자 할 때 정보를 List<ExamInfo>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재한다.
subjectName과 examName에 대하여 메소드를 getter만 두어 Constructor로 생성시에만 초기화 할 수 있도록 한다.
 */
public class ExamInfo {
    private String subjectName;
    private String examName;
    //private int viewTipe=0;

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
   // public int getViewTipe() {
   //     return viewTipe;
   // }

  //  public void setViewTipe(int viewTipe) {
  //      this.viewTipe = viewTipe;
  //  }
}