package com.example.todo;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDBHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    public FirebaseDBHelper(String ID) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userID = ID;
    }

    public boolean confirmFriendExist(String friendID){
        databaseReference.child("USERS");


        return true;

    }
//
//    public List<String> loadFriendsRequestList(String myID){
//
//    }
//
//    public List<String> loadFriendsList(String myID) {
//
//    }
//
//    public boolean requestFriend(String myID, String friendID) {
//
//    }
//
    public void uploadMyLecture(String subjectName, String lectureName, String startdate, String enddate){
        UploadInfo list = new UploadInfo(subjectName,Integer.parseInt(startdate),Integer.parseInt(enddate),false);

        HashMap<String,Object> info = new HashMap<>();
        info.put(lectureName, list.toMap());

        uploadInfo(info, "lecture");
    }

    public void uploadMyAssingment(String subjectName, String lectureName, String startdate, String enddate){
        UploadInfo list = new UploadInfo(subjectName,Integer.parseInt(startdate),Integer.parseInt(enddate),false);

        HashMap<String,Object> info = new HashMap<>();
        info.put(lectureName, list.toMap());

        uploadInfo(info, "assingment");
    }

    public void uploadMyExam(String subjectName, String ExamName, String date){
        UploadExamInfo list = new UploadExamInfo(subjectName,Integer.parseInt(date));

        HashMap<String,Object> info = new HashMap<>();
        info.put(ExamName, list.toMap());

        uploadInfo(info, "exam");
    }

    public void uploadInfo(HashMap info,String table) {
        databaseReference.child("USERS").child(userID).child(table).updateChildren(info);
    }




    public List<LectureInfo> loadFriendLectureList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child("USERS").child(friendID).child("lecture").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<LectureInfo> result = new ArrayList<>();

            for(DataSnapshot snapshot: list.getResult().getChildren()){
                UploadInfo info = snapshot.getValue(UploadInfo.class);

                if(info.startdate > date) //시작날짜가 선택날짜보다 뒤면 건너뛴다.
                    continue;
                if(info.enddate < date) //종료 날짜가 선택날짜보다 앞이면 건너뛴다.
                    continue;

                String lectureName = snapshot.getKey();
                String subjectName = info.subjectName;
                boolean isDone = info.isDone;

                LectureInfo lectureInfo = new LectureInfo(subjectName,lectureName,isDone);

                result.add(lectureInfo);
            }

            return result;
        }
        else
            return null;
    }

    public List<AssingmentInfo> loadFriendAssingmentList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child("USERS").child(friendID).child("lecture").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<AssingmentInfo> result = new ArrayList<>();

            for(DataSnapshot snapshot: list.getResult().getChildren()){
                UploadInfo info = snapshot.getValue(UploadInfo.class);

                if(info.startdate > date) //시작날짜가 선택날짜보다 뒤면 건너뛴다.
                    continue;
                if(info.enddate < date) //종료 날짜가 선택날짜보다 앞이면 건너뛴다.
                    continue;

                String lectureName = snapshot.getKey();
                String subjectName = info.subjectName;
                boolean isDone = info.isDone;

                AssingmentInfo assingmentInfo = new AssingmentInfo(subjectName,lectureName,isDone);

                result.add(assingmentInfo);
            }

            return result;
        }
        else
            return null;
    }

    public List<ExamInfo> loadFriendExamList(String friendID, int date){

        Task<DataSnapshot> list = databaseReference.child("USERS").child(friendID).child("lecture").get();

        if(list.getResult().exists()) { //데이터가 존재하면
            List<ExamInfo> result = null;

            for(DataSnapshot snapshot: list.getResult().getChildren()){
                UploadExamInfo info = snapshot.getValue(UploadExamInfo.class);

                if(info.date == date) { // 시험 날짜가 선택된 날짜랑 같으면 list에 추가

                    String examName = snapshot.getKey();
                    String subjectName = info.subjectName;

                    ExamInfo examInfo = new ExamInfo(subjectName, examName);

                    result.add(examInfo);
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
        List<AssingmentInfo> assingmentlist = loadFriendAssingmentList(friendID, date);
        List<ExamInfo> examlist = loadFriendExamList(friendID, date);

        todo.add(lecturelist);
        todo.add(assingmentlist);
        todo.add(examlist);

        return todo;
    }
}

class UploadInfo
{
    String subjectName;
    int startdate;
    int enddate;
    boolean isDone;

    public UploadInfo(String subjectName, int startdate, int enddate, boolean isDone) {
        this.subjectName = subjectName;
        this.startdate = startdate;
        this.enddate = enddate;
        this.isDone = isDone;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("subjectName", subjectName);
        info.put("startdate", startdate);
        info.put("enddate", enddate);
        info.put("isdone", isDone);
        return info;
    }
}

class UploadExamInfo
{
    String subjectName;
    int date;

    public UploadExamInfo(String subjectName, int date) {
        this.subjectName = subjectName;
        this.date = date;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("subjectName", subjectName);
        info.put("date", date);
        return info;
    }
}
