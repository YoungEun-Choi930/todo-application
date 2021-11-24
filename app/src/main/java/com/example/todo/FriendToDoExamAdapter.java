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

public class FriendToDoExamAdapter extends RecyclerView.Adapter <FriendToDoExamAdapter.ItemViewHolder> {
    List<LectureInfo> todoExam;
    Context mcontext;
    LayoutInflater inflater;

    public FriendToDoExamAdapter(Context context, List<LectureInfo> list){

        this.todoExam = list;
        this.mcontext=context;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.item_in_friend_todo,parent,false);

        return new ItemViewHolder(itemview);
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_todo;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_todo = itemView.findViewById(R.id.tv_todo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FriendToDoExamAdapter.ItemViewHolder holder, int position) {
        LectureInfo lectureInfo = todoExam.get(position);
        holder.tv_todo.setText(lectureInfo.getLectureName());

    }

    @Override
    public int getItemCount() {
        return todoExam.size();
    }
}
