package com.example.fitlab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

public class SessionCursorAdapter extends CursorAdapter
{
    private long mBase;
    private String mDefaultFormat = "%02d:%02d:%02d";

    public SessionCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return LayoutInflater.from(context).inflate(R.layout.session_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView listName = (TextView) view.findViewById(R.id.listName);
        TextView createdDate = (TextView) view.findViewById(R.id.createdDate);
        TextView elapsedTime = (TextView) view.findViewById(R.id.elapsedTime);
        TextView caloriesCalc = (TextView) view.findViewById(R.id.caloriesCalc);
        TextView totalLaps = (TextView) view.findViewById(R.id.totalLaps);

        String dName = cursor.getString(cursor.getColumnIndexOrThrow("LIST_NAME"));
        String dDate = cursor.getString(cursor.getColumnIndexOrThrow("CREATED_DATE"));
        long dTime = cursor.getLong(cursor.getColumnIndexOrThrow("ELAPSED_TIME"));
        String dCalories = cursor.getString(cursor.getColumnIndexOrThrow("CALORIES_CALC"));
        int dTotal = cursor.getInt(cursor.getColumnIndexOrThrow("TOTAL_LAPS"));
        String reformatTime = updateText(dTime);

        listName.setText(dName);
        createdDate.setText(dDate);
        elapsedTime.setText(reformatTime);
        caloriesCalc.setText(dCalories);
        totalLaps.setText(String.valueOf(dTotal));
    }

    private String updateText(long now)
    {
        long seconds = (now - mBase) / 1000;
        int hh = (int)(seconds / 3600);
        int mm = (int)((seconds % 3600) / 60);
        int ss = (int)(seconds % 60);

        String text = String.format(Locale.US, mDefaultFormat, hh, mm, ss);
        return text;
    }
}
