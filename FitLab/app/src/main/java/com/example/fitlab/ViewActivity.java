package com.example.fitlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    DBManager myDb;
    TextView selectedSession;
    ListView displayedSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        myDb = new DBManager(this);

        loadSessionListView();
        selectedSession = (TextView)findViewById(R.id.view_session_box);
        displayedSessions = (ListView)findViewById(R.id.overall_session_view);
        displayedSessions.setClickable(true);
        displayedSessions.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Cursor cursor = (Cursor) adapterView.getAdapter().getItem(i);
                int sessionId = cursor.getInt(0);
                Session session = myDb.getSession(sessionId);
                String sessionName = session.getListName();
                selectedSession.setText(sessionName);
                loadLapsListView(sessionName);
            }
        });
    }
    public boolean checkSessions()
    {
        boolean empty = true;
        DBManager handler = new DBManager(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM SessionList", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    private void loadSessionListView()
    {
        boolean isEmpty = checkSessions();
        if(isEmpty == true)
        {
            Toast.makeText(ViewActivity.this,"No Sessions exist",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String sqlQuery = "SELECT * FROM SessionList";

            DBManager handler = new DBManager(this);
            SQLiteDatabase db = handler.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlQuery, null);

            ListView listLaps = (ListView) findViewById(R.id.overall_session_view);
            SessionCursorAdapter cursorAdapter = new SessionCursorAdapter(this, cursor);
            listLaps.setAdapter(cursorAdapter);
        }
    }

    private void loadLapsListView(String listName)
    {
        String sqlQuery = "SELECT * FROM LapList WHERE LIST_NAME = ?";
        String[] selectionArgs = {listName};

        DBManager handler = new DBManager(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);

        ListView listLaps = (ListView) findViewById(R.id.overall_lap_view);
        ListViewCursorAdapter cursorAdapter = new ListViewCursorAdapter(this, cursor);
        listLaps.setAdapter(cursorAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.menu_item_home:
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_trigger:
            {
                Intent intent = new Intent(this, TriggerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_view:
            {
                Intent intent = new Intent(this, ViewActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}
