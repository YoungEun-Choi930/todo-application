package com.example.todo;

import static com.example.todo.FriendsManagementActivity.context;

import android.content.Context;
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

import java.util.HashMap;
import java.util.List;

public class FriendToDoAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    HashMap<String, Object> todoList;
    Context mcontext;
    LayoutInflater inflater;
    public static final int myType = 0;
    public static final int friendType = 1;
    int type;

    public FriendToDoAdapter(Context context, HashMap<String, Object> todoList, int type){
        this.todoList = todoList;
        this.mcontext=context;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == myType) {
            View itemview = inflater.from(parent.getContext()).inflate(R.layout.item_friend_todo, parent, false);
            return new AHolder(itemview);
        } else {
            View itemview = inflater.from(parent.getContext()).inflate(R.layout.item_friend_todo, parent, false);
            return new BHolder(itemview);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder , int position) {
        String subjectName = (String) todoList.keySet().toArray()[position];

        List<List> list = (List<List>) todoList.get(subjectName);


        if (holder instanceof AHolder) { //내 투두리스트
            ((AHolder) holder).todo_sub.setText(subjectName);
            ((AHolder) holder).todo_add.setVisibility(View.VISIBLE);
            ((AHolder) holder).recy_lecture.setAdapter(new FriendToDoLectureAdapter(mcontext, list.get(0),0));
            ((AHolder) holder).recy_assignment.setAdapter(new FriendToDoAssignmentAdapter(mcontext,list.get(1),0));
            ((AHolder) holder).recy_exam.setAdapter(new FriendToDoExamAdapter(mcontext,list.get(2),0));


        } else { //친구 투두리스트
            ((BHolder) holder).todo_sub.setText(subjectName);
            ((BHolder) holder).recy_lecture.setAdapter(new FriendToDoLectureAdapter(mcontext, list.get(0),1));
            ((BHolder) holder).recy_assignment.setAdapter(new FriendToDoAssignmentAdapter(mcontext,list.get(1),1));
            ((BHolder) holder).recy_exam.setAdapter(new FriendToDoExamAdapter(mcontext,list.get(2),1));

        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public class AHolder extends RecyclerView.ViewHolder {
        TextView todo_sub;
        Button todo_add;
        RecyclerView recy_lecture;
        RecyclerView recy_assignment;
        RecyclerView recy_exam;

        public AHolder(@NonNull View itemView) {
            super(itemView);
            todo_sub = itemView.findViewById(R.id.todo_subject);
            todo_add = itemView.findViewById(R.id.todo_add);
            recy_lecture = itemView.findViewById(R.id.recy_friend_lecture);
            recy_assignment = itemView.findViewById(R.id.recy_friend_assignment);
            recy_exam = itemView.findViewById(R.id.recy_friend_exam);

        }
    }

    public class BHolder extends RecyclerView.ViewHolder{
        TextView todo_sub;
        RecyclerView recy_lecture;
        RecyclerView recy_assignment;
        RecyclerView recy_exam;

        public BHolder(@NonNull View itemView) {
            super(itemView);
            todo_sub = itemView.findViewById(R.id.todo_subject);
            recy_lecture = itemView.findViewById(R.id.recy_friend_lecture);
            recy_assignment = itemView.findViewById(R.id.recy_friend_assignment);
            recy_exam = itemView.findViewById(R.id.recy_friend_exam);
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

    public void setList(HashMap<String, Object> todolist){
        todoList = todolist;
    }
}
