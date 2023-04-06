package com.example.todo.friends;

import com.example.todo.util.FirebaseDBHelper;

public class FriendsManagement {


    /* ------------------------------------ 친구 목록 불러오기 ------------------------------------- */
    public void getFriendsList(){
        // firebase에서 가져오고 notifyFriendsList로 결과 받음.
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendsList();
    }

    /* ---------------------------------- 친구 신청 목록 불러오기 ----------------------------------- */
    public void getFriendsRequestList(){
        // firebase에서 가져오고 notifyFriendsList로 결과 받음.
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendsRequestList();
    }

    /* ----------------------------- 친구 ID를 받아서 존재하면 친구신청 ------------------------------- */
    public void requestFriend(String friendID) {
        // firebase에서 confirm하고 showResult로 결과 받음.
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.confirmFriendExist(friendID);
    }

    /* ------------------------------------ 친구 신청 수락하기 ------------------------------------- */
    public void acceptFriend(String friendID, String friendUID) {
        // firebase update
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.acceptFriend(friendID, friendUID);
    }

    /* ---------------------------------------- 친구 삭제 ----------------------------------------- */
    public void delFriend(String friendID, String friendUID) {
        // firebase update
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.delFriend(friendID, friendUID);
    }

    /* --------------------------------------- 친구 일정 조회 -------------------------------------- */
    public void getFriendToDoList(String friendID, int date) {
        FirebaseDBHelper firebaseDB = new FirebaseDBHelper();
        firebaseDB.loadFriendToDoList(friendID, date);
    }

}
