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
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);

        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AlarmInfo alarmInfo=myAlarmList.get(position);

        holder.tv_name_alarm.setText(alarmInfo.getSubjectName());

        holder.checkBoxAlarm.setOnCheckedChangeListener(null);
        holder.checkBoxAlarm.setChecked(alarmInfo.getChecked());
        holder.checkBoxAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                alarmInfo.setChecked(isChecked);
                checkedList.add(alarmInfo);
            }
        });

        if(ck==1){
            holder.checkBoxAlarm.setVisibility(View.VISIBLE);
        }
        else
            holder.checkBoxAlarm.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return myAlarmList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name_alarm;
        CheckBox checkBoxAlarm;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_alarm = itemView.findViewById(R.id.tv_name_alarm);
            checkBoxAlarm = itemView.findViewById(R.id.checkBoxAlarm);

        }
    }

    public void updateCheckBox(int n){
        ck = n;
    }

    public ArrayList<AlarmInfo> getcheckedList() {
        return checkedList;
    }

}


