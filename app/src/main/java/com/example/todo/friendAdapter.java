package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class friendAdapter extends RecyclerView.Adapter <friendAdapter.ItemViewHolder> {
    ArrayList<FriendInfo> myFriendsList;
    ArrayList<FriendInfo> checkedList = new ArrayList();
    private int ck = 0;

    public friendAdapter(ArrayList<FriendInfo> list){
        this.myFriendsList=list;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FriendInfo friendInfo=myFriendsList.get(position);

        holder.tv_name_friend.setText(friendInfo.getFriendName());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(friendInfo.getChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                friendInfo.setChecked(isChecked);
                checkedList.add(friendInfo);
            }
        });

        if(ck==1){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else
            holder.checkBox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return myFriendsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name_friend;
        CheckBox checkBox;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_friend = itemView.findViewById(R.id.tv_name_friend);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

    public void updateCheckBox(int n){
        ck = n;
    }

    public ArrayList<FriendInfo> getcheckedList() {
        return checkedList;
    }

    public void setData(ArrayList<FriendInfo> list) {
        this.myFriendsList = list;
    }

}


