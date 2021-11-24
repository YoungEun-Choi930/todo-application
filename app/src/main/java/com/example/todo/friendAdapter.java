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
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

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

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendInfo friendInfo= myFriendsList.get(position);
        if (holder instanceof AHolder) {
            ((AHolder) holder).tv_name_friend.setText(friendInfo.getFriendName());
            ((AHolder) holder).checkBox.setChecked(friendInfo.getChecked());
            ((AHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    friendInfo.setChecked(isChecked);
                    if(friendInfo.getChecked()){
                        checkedList.add(friendInfo);

                    }else{
                        checkedList.remove(friendInfo);
                    }
                }
            });
            if(ck==1){
                ((AHolder) holder).checkBox.setVisibility(View.VISIBLE);
            }
            else
                ((AHolder) holder).checkBox.setVisibility(View.GONE);

        } else { //수락버튼 있는 뷰
            ((BHolder) holder).tv_name_friend_ac.setText(friendInfo.getFriendName());
            ((BHolder) holder).checkBox_ac.setChecked(friendInfo.getChecked());
            ((BHolder) holder).checkBox_ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    friendInfo.setChecked(isChecked);
                    if(friendInfo.getChecked()){
                        checkedList.add(friendInfo);

                    }else{
                        checkedList.remove(friendInfo);
                    }
                }
            });

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

    @Override
    public int getItemCount() {
        return myFriendsList.size();
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

    public class AHolder extends RecyclerView.ViewHolder{
        TextView tv_name_friend;
        CheckBox checkBox;

        public AHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(view,pos);
                        }
                    }
                }
            });
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

    public interface OnItemClickListener{ //리스너인터페이스
        void onItemClick(View v, int pos);
    }
    public String getName(int position){
        return myFriendsList.get(position).getFriendName();
    }
    public String getUid(int position){
        return myFriendsList.get(position).getFriendUID();
    }
}


