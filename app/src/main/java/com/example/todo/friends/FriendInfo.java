package com.example.todo.friends;
/*
친구 정보를 firebase에서 들고 와서 화면에 보여주고자 할 때 정보를 List<FriendInfo>형태로 넘겨주기 위하여 사용하는 클래스이다.
변수는 private로 존재한다.
friendName 과 friendUID에 대하여 메소드를 getter만 두어 Constructor로 생성시에만 초기화 할 수 있도록 하고,
cheked에 대한 메소드로는 getter와 setter를 두어 친구 삭제 시 체크 값을 설정 및 받아올 수 있도록 한다.
 */
public class FriendInfo {

    private String friendName;
    private String friendUID;
    private boolean checked = false;
    private int viewTipe=0;

    public FriendInfo(String friendName, String friendUID) {
        this.friendName = friendName;
        this.friendUID = friendUID;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendUID() {
        return friendUID;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getViewTipe() {
        return viewTipe;
    }

    public void setViewTipe(int viewTipe) {
        this.viewTipe = viewTipe;
    }
}
