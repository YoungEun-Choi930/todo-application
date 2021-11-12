package com.example.todo;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseDBHelper {
    private DatabaseReference databaseReference;
    private String userID;

    public FirebaseDBHelper(String ID) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("USERS");
        userID = ID;
    }

    public boolean confirmFriendExist(String friendID){
        Task<DataSnapshot> friend = databaseReference.child(friendID).get();
        if(friend.getResult().exists()) //존재하면
            return true;
        else
            return false;

    }

    public List<String> loadFriendsRequestList(){       //친구신청 목록 불러오기
        Task<DataSnapshot> list = databaseReference.child(userID).child("friend").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<String> result = new ArrayList<>();
            for(DataSnapshot friend: list.getResult().getChildren()) {
                if((int) friend.getValue() == 0)   //친구신청 상태면 result에 넣는다.
                    result.add(friend.getKey());
            }
            return result;
        }
        else
            return null;
    }

    public List<String> loadFriendsList() {     //서로친구 목록 불러오기
        Task<DataSnapshot> list = databaseReference.child(userID).child("friend").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<String> result = new ArrayList<>();
            for(DataSnapshot friend: list.getResult().getChildren()) {
                if((int) friend.getValue() == 1)   //서로친구 상태면 result에 넣는다.
                    result.add(friend.getKey());
            }
            return result;
        }
        else
            return null;
    }

    public void requestFriend(String friendID) { //친구신청
        databaseReference.child(friendID).child("friend").child(userID).setValue(0);
    }

    public void acceptFriend(String friendID) { //친구신청수락
        databaseReference.child(userID).child("friend").child(friendID).setValue(1);
    }



    private List<LectureInfo> loadFriendLectureList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child(friendID).child("lecture").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<LectureInfo> result = new ArrayList<>();

            for(DataSnapshot subjectlist: list.getResult().getChildren()){

                for(DataSnapshot lecturelist: subjectlist.getChildren()) {
                    UploadInfo lecture = lecturelist.getValue(UploadInfo.class);

                    if(lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                        continue;
                    if(lecture.endDate < date)
                        continue;

                    String lectureName = lecturelist.getKey();
                    String subjectName = subjectlist.getKey();
                    boolean isDone = lecture.isDone;

                    LectureInfo lectureInfo = new LectureInfo(subjectName,lectureName,isDone);
                    result.add(lectureInfo);
                }

            }

            return result;
        }
        else
            return null;
    }

    private List<Assignment> loadFriendAssignmentList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child(friendID).child("lecture").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<Assignment> result = new ArrayList<>();

            for(DataSnapshot subjectlist: list.getResult().getChildren()){

                for(DataSnapshot assignmentlist: subjectlist.getChildren()) {
                    UploadInfo lecture = assignmentlist.getValue(UploadInfo.class);

                    if(lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                        continue;
                    if(lecture.endDate < date)
                        continue;

                    String lectureName = assignmentlist.getKey();
                    String subjectName = subjectlist.getKey();
                    boolean isDone = lecture.isDone;

                    Assignment assignmentInfo = new Assignment(subjectName,lectureName,isDone);
                    result.add(assignmentInfo);
                }

            }

            return result;
        }
        else
            return null;
    }

    private List<ExamInfo> loadFriendExamList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child(friendID).child("exam").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<ExamInfo> result = new ArrayList<>();

            for(DataSnapshot subjectlist: list.getResult().getChildren()){

                for(DataSnapshot examlist: subjectlist.getChildren()) {
                    UploadExamInfo lecture = examlist.getValue(UploadExamInfo.class);

                    if(lecture.date == date)        //date에 해당하면 list에 넣음.
                    {
                        String lectureName = examlist.getKey();
                        String subjectName = subjectlist.getKey();

                        ExamInfo examInfo = new ExamInfo(subjectName,lectureName);
                        result.add(examInfo);
                    }
                }
            }

            return result;
        }
        else
            return null;
    }

    public List<List> loadFriendToDoList(String friendID, int date) {
        List<List> todo = new ArrayList();
        List<LectureInfo> lecturelist = loadFriendLectureList(friendID, date);
        List<Assignment> assignmentlist = loadFriendAssignmentList(friendID, date);
        List<ExamInfo> examlist = loadFriendExamList(friendID, date);

        todo.add(lecturelist);
        todo.add(assignmentlist);
        todo.add(examlist);

        return todo;
    }

    public void uploadMyLecture(String subjectName, HashMap lecturelist){
        HashMap<String,Object> info = new HashMap<>();
        info.put(subjectName, lecturelist);

        uploadInfo(info, "lecture");
    }

    public void uploadMyAssignment(String subjectName, String assignmentName, String startdate, String startTime, String enddate, String endTime){
        UploadInfo list = new UploadInfo(Integer.parseInt(startdate),Integer.parseInt(startTime),Integer.parseInt(enddate),Integer.parseInt(endTime),false);

        HashMap<String,Object> info = new HashMap<>();
        info.put(assignmentName, list.toMap());

        HashMap<String, Object> iinfo = new HashMap<>();
        iinfo.put(subjectName, info);

        uploadInfo(iinfo, "assignment");
    }

    public void uploadMyExam(String subjectName, String examName, String date, String time){
        UploadExamInfo list = new UploadExamInfo(Integer.parseInt(date), Integer.parseInt(time));

        HashMap<String,Object> info = new HashMap<>();
        info.put(examName, list.toMap());

        HashMap<String, Object> iinfo = new HashMap<>();
        iinfo.put(subjectName, info);

        uploadInfo(iinfo, "exam");
    }

    private void uploadInfo(HashMap info,String table) {
        databaseReference.child(userID).child(table).updateChildren(info);
    }





    public void delSubject(String subjectName) {
        databaseReference.child(userID).child("lecture").child(subjectName).setValue(null);
        databaseReference.child(userID).child("assignment").child(subjectName).setValue(null);
        databaseReference.child(userID).child("exam").child(subjectName).setValue(null);
    }

    public void delMyAssignment(String assignmentName, String subjectName) {
        databaseReference.child(userID).child("assignment").child(subjectName).child(assignmentName).setValue(null);

    }

    public void delMyExam(String examName, String subjectName) {
        databaseReference.child(userID).child("exam").child(subjectName).child(examName).setValue(null);

    }

    public void changeMyIsDone(String name, String subjectName, String table, int value) {
        databaseReference.child(userID).child(table.toLowerCase()).child(subjectName).child(name).child("isdone").setValue(value);

    }
}



class UploadInfo
{
    int startDate;
    int startTime;
    int endDate;
    int endTime;
    boolean isDone;

    public UploadInfo(int startdate, int startTime, int enddate, int endTime, boolean isDone) {
        this.startDate = startdate;
        this.startTime = startTime;
        this.endDate = enddate;
        this.endTime = endTime;
        this.isDone = isDone;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("startdate", startDate);
        info.put("starttime", startTime);
        info.put("enddate", endDate);
        info.put("endtime", endTime);
        info.put("isdone", isDone);
        return info;
    }
}

class UploadExamInfo
{
    int date;
    int time;

    public UploadExamInfo(int date, int time) {
        this.date = date;
        this.time = time;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("date", date);
        info.put("time", time);
        return info;
    }
}