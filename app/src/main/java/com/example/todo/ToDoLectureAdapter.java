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

public class ToDoLectureAdapter extends RecyclerView.Adapter <ToDoLectureAdapter.ItemViewHolder> {
    List<LectureInfo> todoLecture;
    Context mcontext;
    LayoutInflater inflater;

    public ToDoLectureAdapter(Context context, List<LectureInfo> list){

        this.todoLecture = list;
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
        CheckBox checkBox;
        ImageButton x_button;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_todo = itemView.findViewById(R.id.tv_todo);
            checkBox = itemView.findViewById(R.id.checkBox_todo);
            x_button = itemView.findViewById(R.id.x_button);
            checkBox.setEnabled(false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoLectureAdapter.ItemViewHolder holder, int position) {
        LectureInfo lectureInfo = todoLecture.get(position);
        holder.tv_todo.setText(lectureInfo.getLectureName());
        holder.checkBox.setChecked(lectureInfo.getIsDone());
        holder.checkBox.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return todoLecture.size();
    }
}
