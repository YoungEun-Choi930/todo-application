package com.example.todo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseDBHelper {
    private DatabaseReference databaseReference;
    private String userUID;

    public FirebaseDBHelper() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        userUID = LoginActivity.USERUID;
    }

    public void userRegistration(){
        databaseReference.child("USERS").child(LoginActivity.USERID).setValue(userUID);
    }


    /* ------------------------------------ 친구신청 목록 불러오기----------------------------------- */
    public void loadFriendsRequestList(){
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

    /* ------------------------------------ 서로친구 목록 불러오기----------------------------------- */
    public void loadFriendsList() {
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

    /* ------------------------ 친구 신청시 존재하는 id 인가, 이미 친구인가 ---------------------------- */
    public void confirmFriendExist(String friendID){

        // 해당 아이디가 사용자로 등록되어 있는가
        Task<DataSnapshot> userstask = databaseReference.child("USERS").child(friendID).get();

        OnCompleteListener existlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()) {
                    String friendUID = (String)(task.getResult().getValue());
                    requestFriend(friendUID);                                   // 사용자로 등록되어있으면 request Friend
                    FriendsManagementActivity.context.showResult(1);         // true 반환
                }
                else{
                    FriendsManagementActivity.context.showResult(-1);        // 사용자로 등록되어 있지 않으니 false 반환
                }
            }
        };

        // 이미 친구인 아이디 인가.
        Task<DataSnapshot> infotask = databaseReference.child("INFO").child(userUID).child("friend").child(friendID).get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()) {
                    FriendsManagementActivity.context.showResult(0);        // 이미 친구이면 false 반환
                }
                else{
                    userstask.addOnCompleteListener(existlistener);             // 친구가 아니면 등록되어있는 사용자인지 확인.
                }
            }
        };

        infotask.addOnCompleteListener(friendlistener);

    }

    /* ------------------------------------------- 친구 신청 -------------------------------------- */
    private void requestFriend(String friendUID) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("friendUID", userUID);
        map.put("value", 0);
        databaseReference.child("INFO").child(friendUID).child("friend").child(LoginActivity.USERID).setValue(map);
    }

    /* ---------------------------------------- 친구 신청 수락 ------------------------------------- */
    public void acceptFriend(String friendID, String friendUID) {
        databaseReference.child("INFO").child(userUID).child("friend").child(friendID).child("value").setValue(1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("friendUID", userUID);
        map.put("value", 1);
        databaseReference.child("INFO").child(friendUID).child("friend").child(LoginActivity.USERID).setValue(map);
    }

    /* -------------------------------------- 친구 삭제 ------------------------------------------- */
    public void delFriend(String friendID, String friendUID) {
        databaseReference.child("INFO").child(userUID).child("friend").child(friendID).child("value").setValue(null);
        databaseReference.child("INFO").child(userUID).child("friend").child(friendID).child("friendUID").setValue(null);
        databaseReference.child("INFO").child(friendUID).child("friend").child(LoginActivity.USERID).child("value").setValue(null);
        databaseReference.child("INFO").child(friendUID).child("friend").child(LoginActivity.USERID).child("friendUID").setValue(null);
    }

    /* ---------------------------------- 친구 to do list 가져오기---------------------------------- */
    public void loadFriendToDoList(String friendUID, int date) {
        List<List> result = new ArrayList();

        List<LectureInfo> lectureInfolist = new ArrayList<>();
        List<AssignmentInfo> assingmentInfolist = new ArrayList<>();
        List<ExamInfo> examInfolist = new ArrayList<>();


        Task<DataSnapshot> task = databaseReference.child("INFO").child(friendUID).get();

        OnCompleteListener friendlistener = new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for(DataSnapshot table: task.getResult().getChildren()) {

                            String tablename = table.getKey();

                            if(tablename.equals("lecture")) {//강의 테이블

                                for (DataSnapshot subjectlist : table.getChildren()) { //과목목록

                                    for (DataSnapshot lecturelist : subjectlist.getChildren()) { //강의목록
                                        UploadInfo lecture = lecturelist.getValue(UploadInfo.class);

                                        if (lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                                            continue;
                                        if (lecture.endDate < date)
                                            continue;

                                        String lectureName = lecturelist.getKey();
                                        String subjectName = subjectlist.getKey();
                                        boolean isDone;
                                        if(lecture.isDone == 1)
                                            isDone = true;
                                        else
                                            isDone = false;


                                        LectureInfo lectureInfo = new LectureInfo(subjectName, lectureName, isDone);
                                        lectureInfo.setViewTipe(1);
                                        lectureInfolist.add(lectureInfo);
                                    }

                                }
                            }
                            else if(tablename.equals("assignment")) {//과제테이블
                                for(DataSnapshot subjectlist: table.getChildren()){

                                    for(DataSnapshot assignmentlist: subjectlist.getChildren()) {
                                        UploadInfo lecture = assignmentlist.getValue(UploadInfo.class);

                                        if(lecture.startDate > date)        //date에 해당하지 않는 것은 넘어감.
                                            continue;
                                        if(lecture.endDate < date)
                                            continue;

                                        String lectureName = assignmentlist.getKey();
                                        String subjectName = subjectlist.getKey();
                                        boolean isDone;
                                        if(lecture.isDone == 1)
                                            isDone = true;
                                        else
                                            isDone = false;

                                        AssignmentInfo assignmentInfo = new AssignmentInfo(subjectName,lectureName,isDone);
                                        assignmentInfo.setViewTipe(1);
                                        assingmentInfolist.add(assignmentInfo);
                                    }

                                }
                            }
                            else if(tablename.equals("exam")) { //시험테이블
                                for(DataSnapshot subjectlist: table.getChildren()){

                                    for(DataSnapshot examlist: subjectlist.getChildren()) {
                                        UploadExamInfo lecture = examlist.getValue(UploadExamInfo.class);

                                        if(lecture.date == date)        //date에 해당하면 list에 넣음.
                                        {
                                            String lectureName = examlist.getKey();
                                            String subjectName = subjectlist.getKey();

                                            ExamInfo examInfo = new ExamInfo(subjectName,lectureName);
                                            examInfo.setViewTipe(1);
                                            examInfolist.add(examInfo);
                                        }
                                    }

                            }

                        }
                    }

                    result.add(lectureInfolist);
                    result.add(assingmentInfolist);
                    result.add(examInfolist);

                    FriendToDoActivity.context.notifyFriendToDoList(result);

                }
                else{
                    System.out.println("디비실패");     //여기로 올 수도 있음
                }
            }
        };

        task.addOnCompleteListener(friendlistener);

    }

    /* ---------------------------------- 강의 hashmap 변환 --------------------------------------- */
    public void uploadMyLecture(String subjectName, HashMap lecturelist){
        HashMap<String,Object> info = new HashMap<>();
        info.put(subjectName, lecturelist);

        uploadInfo(info, "lecture");
    }

    /* ---------------------------------- 과제 hashmap 변환 --------------------------------------- */
    public void uploadMyAssignment(String subjectName, String assignmentName, String startdate, String startTime, String enddate, String endTime){
        UploadInfo list = new UploadInfo(Integer.parseInt(startdate),Integer.parseInt(startTime),Integer.parseInt(enddate),Integer.parseInt(endTime),0);

        HashMap<String,Object> info = new HashMap<>();
        info.put(assignmentName, list.toMap());

        HashMap<String, Object> iinfo = new HashMap<>();
        iinfo.put(subjectName, info);

        uploadInfo(iinfo, "assignment");
    }

    /* ---------------------------------- 시험 hashmap 변환 --------------------------------------- */
    public void uploadMyExam(String subjectName, String examName, String date, String time){
        UploadExamInfo list = new UploadExamInfo(Integer.parseInt(date), Integer.parseInt(time));

        HashMap<String,Object> info = new HashMap<>();
        info.put(examName, list.toMap());

        HashMap<String, Object> iinfo = new HashMap<>();
        iinfo.put(subjectName, info);

        uploadInfo(iinfo, "exam");
    }

    /* ------------------------------- 강의, 과제, 시험 upload ------------------------------------- */
    private void uploadInfo(HashMap info,String table) {
        databaseReference.child("INFO").child(userUID).child(table).updateChildren(info);
    }

    /* -------------------------------------- 과목 삭제 ------------------------------------------- */
    public void delSubject(String subjectName) {
        databaseReference.child("INFO").child(userUID).child("lecture").child(subjectName).setValue(null);
        databaseReference.child("INFO").child(userUID).child("assignment").child(subjectName).setValue(null);
        databaseReference.child("INFO").child(userUID).child("exam").child(subjectName).setValue(null);
    }

    /* -------------------------------------- 과제 삭제 ------------------------------------------- */
    public void delMyAssignment(String assignmentName, String subjectName) {
        databaseReference.child("INFO").child(userUID).child("assignment").child(subjectName).child(assignmentName).setValue(null);

    }

    /* -------------------------------------- 시험 삭제 ------------------------------------------- */
    public void delMyExam(String examName, String subjectName) {
        databaseReference.child("INFO").child(userUID).child("exam").child(subjectName).child(examName).setValue(null);

    }

    /* -------------------------------- 강의, 과제 isDone 변경 ------------------------------------ */
    public void changeMyIsDone(String name, String subjectName, String table, int value) {
        databaseReference.child("INFO").child(userUID).child(table.toLowerCase()).child(subjectName).child(name).child("isDone").setValue(value);

    }
}


/*
firebaseDB에서 데이터를 받아올 때 class의 형태로 받아올 수 있는데, 이를 위하여 생성한 클래스이다.
또한, firebase에 입력가능한 데이터 형태인 HashMap으로의 변환을 메소드로서 정의하였다.
 */
class UploadInfo
{
    public int startDate;
    public int startTime;
    public int endDate;
    public int endTime;
    public int isDone;

    public UploadInfo() {};

    public UploadInfo(int startdate, int startTime, int enddate, int endTime, int isDone) {
        this.startDate = startdate;
        this.startTime = startTime;
        this.endDate = enddate;
        this.endTime = endTime;
        this.isDone = isDone;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> info = new HashMap<>();
        info.put("startDate", startDate);
        info.put("startTime", startTime);
        info.put("endDate", endDate);
        info.put("endTime", endTime);
        info.put("isDone", isDone);
        return info;
    }
}

/*
firebaseDB에서 데이터를 받아올 때 class의 형태로 받아올 수 있는데, 이를 위하여 생성한 클래스이다.
강의와 과제는 데이터 형태가 동일하지만, 과제는 다른 정보들을 저장하기 때문에 강의과제와 분리하여 class를 정의하였다.
또한, firebase에 입력가능한 데이터 형태인 HashMap으로의 변환을 메소드로서 정의하였다.
 */
class UploadExamInfo
{
    public int date;
    public int time;

    public UploadExamInfo() {};

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