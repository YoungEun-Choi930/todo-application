package com.example.todo;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ToDoAssignmentAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    List<AssignmentInfo> todoAssignment;
    Context mcontext;
    LayoutInflater inflater;
    public static final int myType = 0;
    public static final int friendType = 1;
    int type;

    public ToDoAssignmentAdapter(Context context, List<AssignmentInfo> list, int type){

        this.todoAssignment = list;
        this.mcontext=context;
        this.inflater=LayoutInflater.from(context);
        this.type = type;
    }




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
        AssignmentInfo assignmentInfo = todoAssignment.get(position);

        if (holder instanceof AHolder) { //내 투두리스트
            ((AHolder) holder).tv_todo.setText(assignmentInfo.getAssignmentName());
            ((AHolder) holder).checkBox.setChecked(assignmentInfo.getIsDone());
            ((AHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int ck=0;
                    if(isChecked){
                        ck=1;
                        //알람삭제
                        //AlarmManagementActivity.mContext.delSystemAlarm(assignmentInfo.getAssignmentName());
                        TodoManagementActivity.mContext.changeIsDone(assignmentInfo.getAssignmentName(),assignmentInfo.getSubjectName(),"Assignment",ck);
                        assignmentInfo.setIsDone(isChecked);
                    }
                    else{
                        //알람추가
                        //AlarmManagementActivity.mContext.addSystemAlarm(assignmentInfo.getSubjectName(),assignmentInfo.getAssignmentName());
                        TodoManagementActivity.mContext.changeIsDone(assignmentInfo.getAssignmentName(),assignmentInfo.getSubjectName(),"Assignment",ck);
                        assignmentInfo.setIsDone(isChecked);

                    }


                }
            });
            ((AHolder) holder).xbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean result = TodoManagementActivity.mContext.delAssignment(assignmentInfo.getAssignmentName(),assignmentInfo.getSubjectName());
                    if(result){
                        todoAssignment.remove(assignmentInfo);
                        Toast.makeText(view.getContext(),"과제삭제 성공",Toast.LENGTH_SHORT).show();
                        System.out.println("과제삭제성공");
                        TodoManagementActivity.mContext.toDoAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(view.getContext(),"과제삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else { //친구 투두리스트
            ((BHolder) holder).tv_todo.setText(assignmentInfo.getAssignmentName());
            ((BHolder) holder).checkBox.setChecked(assignmentInfo.getIsDone());
            ((BHolder) holder).checkBox.setEnabled(false);
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
        return todoAssignment.size();
    }
}
