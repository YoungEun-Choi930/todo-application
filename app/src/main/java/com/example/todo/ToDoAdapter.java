package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ItemViewHolder> {

    List<SubjectInfo> todoSubject;
    Context mcontext;
    public ToDoAdapter(Context context, List<SubjectInfo> list){
        this.todoSubject=list;
        this.mcontext=context;
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        SubjectInfo subject=todoSubject.get(position);
        holder.todo_sub.setText(subject.getSubjectName());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView todo_sub;
        Button todo_add;
        RecyclerView recy_lecture;
        RecyclerView recy_assignment;
        RecyclerView recy_exam;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            todo_sub = itemView.findViewById(R.id.todo_subject);
            todo_add = itemView.findViewById(R.id.todo_add);
            recy_lecture = itemView.findViewById(R.id.recy_friend_lecture);
            recy_assignment = itemView.findViewById(R.id.recy_friend_assignment);
            recy_exam = itemView.findViewById(R.id.recy_friend_exam);
        }
    }
}
