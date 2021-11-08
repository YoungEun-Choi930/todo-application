package com.example.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class subjectAdapter extends RecyclerView.Adapter <subjectAdapter.ItemViewHolder> {
    ArrayList<SubjectInfo> mySubjectList;
    public subjectAdapter(ArrayList<SubjectInfo> list){
        this.mySubjectList=list;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);

        return new ItemViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull subjectAdapter.ItemViewHolder holder, int position) {
        SubjectInfo subjectInfo=mySubjectList.get(position);

        holder.tv_name_sub.setText(subjectInfo.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return mySubjectList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name_sub;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name_sub = itemView.findViewById(R.id.tv_name_sub);
        }
    }
}
