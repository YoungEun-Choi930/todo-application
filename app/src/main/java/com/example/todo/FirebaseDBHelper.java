package com.example.todo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseDBHelper {
    private DatabaseReference databaseReference;
    private String userUID;

    public FirebaseDBHelper() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userUID = MainActivity.USERUID;
    }

    public void login(){
        databaseReference.child("USERS").child(MainActivity.USERID).setValue(userUID);
    }



    public void loadFriendsRequestList(){       //친구신청 목록 불러오기
        ArrayList<FriendInfo> result = new ArrayList<>();

        Task<DataSnapshot> task = databaseReference.child("INFO").child(userUID).child("friend").get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for(DataSnapshot friends: task.getResult().getChildren()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) friends.getValue();
                        long value = (long) map.get("value");
                        if (value == 0) {  //친구신청 상태면 result에 넣는다.

                            String id = friends.getKey();
                            String uid = (String) map.get("friendUID");
                            FriendInfo info = new FriendInfo(id, uid);
                            info.setViewTipe(1);
                            result.add(info);
                        }
                    }
                    FriendsManagementActivity.context.notifyFriendsList(result);

                }
                else{
                    System.out.println("디비실패");
                }
            }
        };

        task.addOnCompleteListener(friendlistener);

    }


    public void loadFriendsList() {     //서로친구 목록 불러오기
        ArrayList<FriendInfo> result = new ArrayList<>();

        Task<DataSnapshot> task = databaseReference.child("INFO").child(userUID).child("friend").get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for(DataSnapshot friends: task.getResult().getChildren()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) friends.getValue();
                        long value = (long) map.get("value");
                        if (value == 1) {  //서로친구 상태면 result에 넣는다.

                            String id = friends.getKey();
                            String uid = (String) map.get("friendUID");
                            FriendInfo info = new FriendInfo(id, uid);
                            result.add(info);

                            System.out.println("--------------"+id);
                        }
                    }
                    FriendsManagementActivity.context.notifyFriendsList(result);

                }
                else{
                    System.out.println("디비실패");
                }
            }
        };

        task.addOnCompleteListener(friendlistener);


    }


    public void confirmFriendExist(String friendID){    //친구 존재하는지 확인
        Task<DataSnapshot> task = databaseReference.child("USERS").child(friendID).get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()) {
                    String friendUID = (String)(task.getResult().getValue());
                    requestFriend(friendUID);
                    FriendsManagementActivity.context.showresult(true);
                }
                else{
                    FriendsManagementActivity.context.showresult(false);
                }
            }
        };

        task.addOnCompleteListener(friendlistener);

    }

    private void requestFriend(String friendUID) { //친구신청
        HashMap<String, Object> map = new HashMap<>();
        map.put("friendUID", userUID);
        map.put("value", 0);
        databaseReference.child("INFO").child(friendUID).child("friend").child(MainActivity.USERID).setValue(map);
    }

    public void acceptFriend(String friendID, String friendUID) { //친구신청수락
        databaseReference.child("INFO").child(userUID).child("friend").child(friendID).child("value").setValue(1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("friendUID", userUID);
        map.put("value", 1);
        databaseReference.child("INFO").child(friendUID).child("friend").child(MainActivity.USERID).setValue(map);
    }

    public void delFriend(String friendID, String friendUID) { //친구삭제
        databaseReference.child("INFO").child(userUID).child("friend").child(friendID).child("value").setValue(null);
        databaseReference.child("INFO").child(friendUID).child("friend").child(MainActivity.USERID).child("value").setValue(null);
    }



    private List<LectureInfo> loadFriendLectureList(String friendUID, int date){
        List<LectureInfo> result = new ArrayList<>();
        Task<DataSnapshot> task = databaseReference.child("INFO").child(friendUID).child("lecture").get();
        if(task.getResult().exists()) { //데이터가 존재하면

            for (DataSnapshot subjectlist : task.getResult().getChildren()) { //과목목록

                for (DataSnapshot lecturelist : subjectlist.getChildren()) { //강의목록
                    UploadInfo lecture = lecturelist.getValue(UploadInfo.class);

                    if (lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                        continue;
                    if (lecture.endDate < date)
                        continue;

                    String lectureName = lecturelist.getKey();
                    String subjectName = subjectlist.getKey();
                    boolean isDone = lecture.isDone;

                    LectureInfo lectureInfo = new LectureInfo(subjectName, lectureName, isDone);
                    result.add(lectureInfo);
                }

            }
        }
        return result;
    }

    private List<Assignment> loadFriendAssignmentList(String friendID, int date){
        List<Assignment> result = new ArrayList<>();
        Task<DataSnapshot> task = databaseReference.child("INFO").child(friendID).child("lecture").get();
        if(task.getResult().exists()) { //데이터가 존재하면

            for(DataSnapshot subjectlist: task.getResult().getChildren()){

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

        }
        return result;
    }

    private List<ExamInfo> loadFriendExamList(String friendID, int date){
        List<ExamInfo> result = new ArrayList<>();
        Task<DataSnapshot> task = databaseReference.child("INFO").child(friendID).child("exam").get();
        if(task.getResult().exists()) { //데이터가 존재하면

            for(DataSnapshot subjectlist: task.getResult().getChildren()){

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
        }
        return result;
    }

    public void loadFriendToDoList(String friendUID, int date) {
        List<List> result = new ArrayList();

        List<LectureInfo> lectureInfolist = new ArrayList<>();
        List<Assignment> assingmentInfolist = new ArrayList<>();
        List<ExamInfo> examInfolist = new ArrayList<>();


        Task<DataSnapshot> task = databaseReference.child(friendUID).get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for(DataSnapshot friend: task.getResult().getChildren()) {

                        for(DataSnapshot table: friend.getChildren()) {
                            String tablename = table.getKey();
                            if(tablename.equals("lecture")) {//강의 테이블
                                for (DataSnapshot subjectlist : task.getResult().getChildren()) { //과목목록

                                    for (DataSnapshot lecturelist : subjectlist.getChildren()) { //강의목록
                                        UploadInfo lecture = lecturelist.getValue(UploadInfo.class);

                                        if (lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                                            continue;
                                        if (lecture.endDate < date)
                                            continue;

                                        String lectureName = lecturelist.getKey();
                                        String subjectName = subjectlist.getKey();
                                        boolean isDone = lecture.isDone;

                                        LectureInfo lectureInfo = new LectureInfo(subjectName, lectureName, isDone);
                                        lectureInfolist.add(lectureInfo);
                                    }

                                }
                            }
                            else if(tablename.equals("assignment")) {//과제테이블
                                for(DataSnapshot subjectlist: task.getResult().getChildren()){

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
                                        assingmentInfolist.add(assignmentInfo);
                                    }

                                }
                            }
                            else if(tablename.equals("exam")) { //시험테이블
                                for(DataSnapshot subjectlist: task.getResult().getChildren()){

                                    for(DataSnapshot examlist: subjectlist.getChildren()) {
                                        UploadExamInfo lecture = examlist.getValue(UploadExamInfo.class);

                                        if(lecture.date == date)        //date에 해당하면 list에 넣음.
                                        {
                                            String lectureName = examlist.getKey();
                                            String subjectName = subjectlist.getKey();

                                            ExamInfo examInfo = new ExamInfo(subjectName,lectureName);
                                            examInfolist.add(examInfo);
                                        }
                                    }
                                }
                            }

                        }
                    }

                    result.add(lectureInfolist);
                    result.add(assingmentInfolist);
                    result.add(examInfolist);
//                    FriendsManagementActivity.context.notifyFriendToDoList(result);

                }
                else{
                    System.out.println("디비실패");     //여기로 올 수도 있음
                }
            }
        };

        task.addOnCompleteListener(friendlistener);

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
        databaseReference.child("INFO").child(userUID).child(table).updateChildren(info);
    }





    public void delSubject(String subjectName) {
        databaseReference.child("INFO").child(userUID).child("lecture").child(subjectName).setValue(null);
        databaseReference.child("INFO").child(userUID).child("assignment").child(subjectName).setValue(null);
        databaseReference.child("INFO").child(userUID).child("exam").child(subjectName).setValue(null);
    }

    public void delMyAssignment(String assignmentName, String subjectName) {
        databaseReference.child("INFO").child(userUID).child("assignment").child(subjectName).child(assignmentName).setValue(null);

    }

    public void delMyExam(String examName, String subjectName) {
        databaseReference.child("INFO").child(userUID).child("exam").child(subjectName).child(examName).setValue(null);

    }

    public void changeMyIsDone(String name, String subjectName, String table, int value) {
        databaseReference.child("INFO").child(userUID).child(table.toLowerCase()).child(subjectName).child(name).child("isdone").setValue(value);

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