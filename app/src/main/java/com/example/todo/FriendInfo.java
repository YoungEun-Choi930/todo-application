package com.example.todo;

public class FriendInfo {

    private String friendName;
    private String friendUID;
    private boolean checked = false;

    public FriendInfo(String friendName, String friendUID) {
        this.friendName = friendName;
        this.friendUID = friendUID;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String subjectName) {
        this.friendName = friendName;
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
}
