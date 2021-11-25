package com.example.todo;

import static android.view.View.GONE;

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

public class ToDoExamAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    List<ExamInfo> todoExam;
    Context mcontext;
    LayoutInflater inflater;
    public static final int myType = 0;
    public static final int friendType = 1;
    int type;

    public ToDoExamAdapter(Context context, List<ExamInfo> list, int type){

        this.todoExam = list;
        this.mcontext=context;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == myType) {
            View itemview = inflater.inflate(R.layout.item_in_todo,parent,false);
            return new AHolder(itemview);
        } else {
            View itemview =inflater.inflate(R.layout.item_in_todo,parent,false);
            return new BHolder(itemview);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExamInfo examInfo = todoExam.get(position);

        if (holder instanceof AHolder) { //내 투두리스트
            ((AHolder) holder).tv_todo.setText(examInfo.getExamName());
            ((AHolder) holder).checkBox.setVisibility(GONE);
            ((AHolder) holder).xbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ///삭제하는거 만들어넣어야해
                }
            });

        } else { //친구 투두리스트
            ((BHolder) holder).tv_todo.setText(examInfo.getExamName());
            ((BHolder) holder).checkBox.setVisibility(GONE);
            ((BHolder) holder).xbutton.setVisibility(GONE);
        }
    }


    public class AHolder extends RecyclerView.ViewHolder {
        TextView tv_todo;
        CheckBox checkBox;
        ImageButton xbutton;
        public AHolder(@NonNull View itemView) {
            super(itemView);
            tv_todo = itemView.findViewById(R.id.tv_todo);
            checkBox = itemView.findViewById(R.id.checkBox_todo);
            xbutton = itemView.findViewById(R.id.x_button);
        }
    }

    public class BHolder extends RecyclerView.ViewHolder{
        TextView tv_todo;
        CheckBox checkBox;
        ImageButton xbutton;
        public BHolder(@NonNull View itemView) {
            super(itemView);
            tv_todo = itemView.findViewById(R.id.tv_todo);
            checkBox = itemView.findViewById(R.id.checkBox_todo);
            xbutton = itemView.findViewById(R.id.x_button);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (type== 0) {
            return myType;
        } else {
            return friendType;
        }
    }

    @Override
    public int getItemCount() {
        return todoExam.size();
    }
}
