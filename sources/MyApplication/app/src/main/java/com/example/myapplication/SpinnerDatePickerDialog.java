package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class SpinnerDatePickerDialog extends DatePickerDialog {

    public SpinnerDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);

        final Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        setView(inflateCustomDatePicker(c));
    }

    private View inflateCustomDatePicker(Calendar c) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.spinner_date_picker, null);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), this);

        return view;
    }
}