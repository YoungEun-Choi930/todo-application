package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class FriendToDoAssignmentAdapter extends RecyclerView.Adapter <FriendToDoAssignmentAdapter.ItemViewHolder> {
    List<AssignmentInfo> todoAssignment;
    Context mcontext;
    LayoutInflater inflater;

    public FriendToDoAssignmentAdapter(Context context, List<AssignmentInfo> list){

        this.todoAssignment = list;
        this.mcontext=context;
        this.inflater=LayoutInflater.from(context);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.item_in_friend_todo,parent,false);

        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AssignmentInfo assignmentInfo = todoAssignment.get(position);
        holder.tv_todo.setText(assignmentInfo.getAssignmentName());
        holder.checkBox.setChecked(assignmentInfo.getIsDone());
        holder.checkBox.setEnabled(false);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_todo;
        CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_todo = itemView.findViewById(R.id.tv_todo);
            checkBox = itemView.findViewById(R.id.checkBox_todo);
            checkBox.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return todoAssignment.size();
    }
}
