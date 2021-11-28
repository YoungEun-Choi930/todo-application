package com.example.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyYearPickerDialog extends DialogFragment {
    private static final int MAX_YEAR = 2100;
    private static final int MIN_YEAR = 1900;

    private DatePickerDialog.OnDateSetListener listener;
    public Calendar c = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    Button cancel, confirm;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.year_picker, null);

        confirm = dialog.findViewById(R.id.btn_confirm);
        cancel = dialog.findViewById(R.id.btn_cancel);

        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);


        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, yearPicker.getValue(),0,0);
                MyYearPickerDialog.this.getDialog().cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyYearPickerDialog.this.getDialog().cancel();
            }
        });

        int year = c.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);

        builder.setView(dialog);

        return builder.create();

    }
}
