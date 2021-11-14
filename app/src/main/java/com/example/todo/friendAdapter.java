package com.example.todo;

import static com.example.todo.FriendsManagementActivity.context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class friendAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    ArrayList<FriendInfo> myFriendsList;
    ArrayList<FriendInfo> checkedList = new ArrayList();
    private int ck = 0;
    public static final int buttonType = 1;
    public static final int noButtonType = 0;

    public friendAdapter(ArrayList<FriendInfo> list){
        this.myFriendsList=list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == noButtonType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
            return new AHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_accept , parent, false);

            return new BHolder(v);
        }
       // View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
       // return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendInfo friendInfo= myFriendsList.get(position);
        if (holder instanceof AHolder) {
            ((AHolder) holder).tv_name_friend.setText(friendInfo.getFriendName());
            ((AHolder) holder).checkBox.setChecked(friendInfo.getChecked());
            if(ck==1){
                ((AHolder) holder).checkBox.setVisibility(View.VISIBLE);
            }
            else
                ((AHolder) holder).checkBox.setVisibility(View.GONE);

        } else { //수락버튼 있는 뷰
            ((BHolder) holder).tv_name_friend_ac.setText(friendInfo.getFriendName());
            ((BHolder) holder).checkBox_ac.setText(friendInfo.getFriendName());
            ((BHolder) holder).accept_ac.setOnClickListener(view -> {
                myFriendsList.remove(friendInfo);
                (FriendsManagementActivity.context).acceptFriend(friendInfo.getFriendName(), friendInfo.getFriendUID());
                notifyDataSetChanged();
                Toast.makeText(context, "친구 수락 완료", Toast.LENGTH_SHORT).show();
            });

            if(ck==1){
                ((BHolder) holder).checkBox_ac.setVisibility(View.VISIBLE);
            }
            else
                ((BHolder) holder).checkBox_ac.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (myFriendsList.get(position).getViewTipe() == 0) {
            return noButtonType;
        } else {
            return buttonType;
        }
    }
/*
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


        holder.accept.setOnClickListener(view  -> {
            myFriendsList.remove(friendInfo);
            FriendsManagementActivity.context.acceptFriend(friendInfo.getFriendName(), friendInfo.getFriendUID());
            notifyDataSetChanged();

        });
    }
*/
    @Override
    public int getItemCount() {
        return myFriendsList.size();
    }
/*
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name_friend;
        CheckBox checkBox;
        Button accept;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_friend = itemView.findViewById(R.id.tv_name_friend);
            checkBox = itemView.findViewById(R.id.checkFriend);
            accept = itemView.findViewById(R.id.accept_friend);
        }
    }
*/
    public void updateCheckBox(int n){
        ck = n;
    }

    public ArrayList<FriendInfo> getcheckedList() {
        return checkedList;
    }

    public void setData(ArrayList<FriendInfo> list) {
        this.myFriendsList = list;
    }

    public class AHolder extends RecyclerView.ViewHolder{
        TextView tv_name_friend;
        CheckBox checkBox;

        public AHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_friend = itemView.findViewById(R.id.tv_name_friend);
            checkBox = itemView.findViewById(R.id.checkFriend);
        }
    }

    public class BHolder extends RecyclerView.ViewHolder{
        TextView tv_name_friend_ac;
        CheckBox checkBox_ac;
        Button accept_ac;

        public BHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_friend_ac = itemView.findViewById(R.id.tv_name_friend_ac);
            checkBox_ac = itemView.findViewById(R.id.checkFriend_ac);
            accept_ac = itemView.findViewById(R.id.accept_friend_ac);
        }
    }
}


