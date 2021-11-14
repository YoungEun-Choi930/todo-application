package com.example.todo;

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
