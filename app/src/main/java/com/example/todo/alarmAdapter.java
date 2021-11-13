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

public class alarmAdapter extends RecyclerView.Adapter <alarmAdapter.ItemViewHolder> {
    ArrayList<AlarmInfo> myAlarmList;
    ArrayList<AlarmInfo> checkedList = new ArrayList();
    private int ck = 0;

    public alarmAdapter(ArrayList<AlarmInfo> list){
        this.myAlarmList=list;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);

        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AlarmInfo alarmInfo=myAlarmList.get(position);

        holder.tv_name_sub.setText(alarmInfo.getSubjectName());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(alarmInfo.getChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                alarmInfo.setChecked(isChecked);
                checkedList.add(alarmInfo);
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
        return myAlarmList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name_sub;
        CheckBox checkBox;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_sub = itemView.findViewById(R.id.tv_name_sub);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

    public void updateCheckBox(int n){
        ck = n;
    }

    public ArrayList<AlarmInfo> getcheckedList() {
        return checkedList;
    }

}


