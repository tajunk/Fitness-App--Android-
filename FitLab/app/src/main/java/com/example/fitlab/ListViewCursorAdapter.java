package com.example.fitlab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class ListViewCursorAdapter extends CursorAdapter
{
    private long mBase;
    private String mDefaultFormat = "%02d:%02d:%02d";

    public ListViewCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView lapNumber = (TextView) view.findViewById(R.id.lapNumber);
        TextView lapTime = (TextView) view.findViewById(R.id.lapTime);

        int dNumber = cursor.getInt(cursor.getColumnIndexOrThrow("LAP_NUMBER"));
        long dTime = cursor.getLong(cursor.getColumnIndexOrThrow("LAP_TIME"));
        String reformatTime = updateText(dTime);

        lapNumber.setText(String.valueOf(dNumber));
        lapTime.setText(reformatTime);
    }

    private String updateText(long now) {
        long seconds = (now - mBase) / 1000;
        int hh = (int)(seconds / 3600);
        int mm = (int)((seconds % 3600) / 60);
        int ss = (int)(seconds % 60);

        String text = String.format(Locale.US, mDefaultFormat, hh, mm, ss);
        return text;
    }
}
