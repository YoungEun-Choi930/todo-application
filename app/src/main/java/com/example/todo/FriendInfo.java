package com.example.todo;

public class FriendInfo {

    private String friendName;
    private boolean checked = false;

    public FriendInfo(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String subjectName) {
        this.friendName = friendName;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
