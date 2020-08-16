package com.example.fitlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TriggerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    DBManager myDb;
    String userWeight;
    boolean firstLoad = true;
    private Chronometer stopWatch;
    private long pauseOffset;
    private boolean isRunning;
    int lapNumber = 0;
    int selectedMet = 0;
    TextView sessionView;
    TextView testView;
    EditText editText;
    Button btnNewSession;
    Button btnNewLap;
    Button btnDone;
    Spinner autoLap;
    Spinner metSelect;
    String[] selectedList = { "Select...", "Slow Walk ( 2 METs )", "Brisk Walk ( 3.5 METs )", "Casually Bicycling ( 4.5 METs )",
            "Strenuous Hiking ( 6.5 METs )", "20km/h Bicycling ( 14 METs )", "Testing Mode ( 50 METs )"};
    String[] selectedSeconds = { "Manual", "10 seconds", "30 seconds", "60 seconds", "1.5 minute/s", "3 minute/s"};
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        myDb = new DBManager(this);

        stopWatch = findViewById(R.id.stop_watch);
        stopWatch.setTypeface(ResourcesCompat.getFont(this, R.font.alba____));
        stopWatch.setFormat("%s");
        stopWatch.setBase(SystemClock.elapsedRealtime());

        Spinner sessionList = (Spinner) findViewById(R.id.speed_type);
        sessionList.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,selectedList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionList.setAdapter(arrayAdapter);

        btnNewSession = (Button)findViewById(R.id.button_save_session);
        editText = (EditText) findViewById(R.id.edit_text_box);

        Spinner autoSpinner = (Spinner) findViewById(R.id.auto_spinner);
        autoSpinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,selectedSeconds);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoSpinner.setAdapter(arrayAdapter2);

        btnDone = (Button)findViewById(R.id.button_done);
        btnDone.setEnabled(false);
        sessionView = (TextView)findViewById(R.id.selected_session_box);
        testView = (TextView)findViewById(R.id.selected_test_id);
        addSession();

        autoLap = (Spinner) findViewById(R.id.auto_spinner);
        metSelect = (Spinner) findViewById(R.id.speed_type);

        btnNewLap = (Button)findViewById(R.id.button_lap);
        btnNewLap.setEnabled(false);

        loadListView();

        metSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String selectionString = adapterView.getItemAtPosition(i).toString();
                if(selectionString.equals("Select..."))
                {

                }
                else if(selectionString.equals("Slow Walk ( 2 METs )"))
                {
                    selectedMet = 2;
                }
                else if(selectionString.equals("Brisk Walk ( 3.5 METs )"))
                {
                    selectedMet = 4;
                }
                else if(selectionString.equals("Casually Bicycling ( 4.5 METs )"))
                {
                    selectedMet = 5;
                }
                else if(selectionString.equals("Strenuous Hiking ( 6.5 METs )"))
                {
                    selectedMet = 7;
                }
                else if(selectionString.equals("20km/h Bicycling ( 14 METs )"))
                {
                    selectedMet = 14;
                }
                else if(selectionString.equals("Testing Mode ( 50 METs )"))
                {
                    selectedMet = 50;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autoLap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String selectionString = adapterView.getItemAtPosition(i).toString();
                if(selectionString.equals("Manual"))
                {
//                    Nothing
                }
                else if(selectionString.equals("10 seconds"))
                {
                    Toast.makeText(TriggerActivity.this,"Lapping every 10 seconds",Toast.LENGTH_SHORT).show();
                    Runnable nRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnNewLap.performClick();
                            handler.postDelayed(this, 10000L);
                        }
                    };
                    handler.post(nRunnable);
                }
                else if(selectionString.equals("30 seconds"))
                {
                    Toast.makeText(TriggerActivity.this,"Lapping every 30 seconds",Toast.LENGTH_SHORT).show();
                    Runnable nRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnNewLap.performClick();
                            handler.postDelayed(this, 30000L);
                        }
                    };
                    handler.post(nRunnable);
                }
                else if(selectionString.equals("60 seconds"))
                {
                    Toast.makeText(TriggerActivity.this,"Lapping every 60 seconds",Toast.LENGTH_SHORT).show();
                    Runnable nRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnNewLap.performClick();
                            handler.postDelayed(this, 60000L);
                        }
                    };
                    handler.post(nRunnable);
                }
                else if(selectionString.equals("1.5 minute/s"))
                {
                    Toast.makeText(TriggerActivity.this,"Lapping every 1.5 minute/s",Toast.LENGTH_SHORT).show();
                    Runnable nRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnNewLap.performClick();
                            handler.postDelayed(this, 90000L);
                        }
                    };
                    handler.post(nRunnable);
                }
                else if(selectionString.equals("3 minute/s"))
                {
                    Toast.makeText(TriggerActivity.this,"Lapping every 3 minute/s",Toast.LENGTH_SHORT).show();
                    Runnable nRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            btnNewLap.performClick();
                            handler.postDelayed(this, 180000L);
                        }
                    };
                    handler.post(nRunnable);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//              Nothing
            }
        });
    }

    public void startChronometer(View v)
    {
        if (!isRunning)
        {
            stopWatch.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            stopWatch.start();
            isRunning = true;
        }
    }
    public void pauseChronometer(View v)
    {
        if (isRunning)
        {
            stopWatch.stop();
            pauseOffset = SystemClock.elapsedRealtime() - stopWatch.getBase();
            String time = String.valueOf(pauseOffset);
            isRunning = false;
        }
    }
    public void resetChronometer(View v)
    {
        stopWatch.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void lapChronometer(View v)
    {
        if (isRunning)
        {
            btnNewLap.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String selectedSession = sessionView.getText().toString();
                    if(selectedSession.matches(""))
                    {
//                        Should never happen, 'Lap' button is disabled before a Session is created.
                    }
                    else
                    {
                        lapTick(selectedSession);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(TriggerActivity.this,"Timer has to be started",Toast.LENGTH_LONG).show();
        }
    }

    public void lapTick(String selectedSession)
    {
        lapNumber++;
        long lapTime = SystemClock.elapsedRealtime() - stopWatch.getBase();
        Lap lap = new Lap(selectedSession, lapNumber, lapTime);
        long lapRef = myDb.createLap(lap);
        firstLoad = false;
        btnNewSession.setEnabled(false);
        btnDone.setEnabled(true);
        loadListView();
    }

    public void doneButton(View v)
    {
        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finalizeSession();
                blankListView();
                btnNewSession.setEnabled(true);
                btnNewLap.setEnabled(false);
                btnDone.setEnabled(false);
                pauseChronometer(view);
                resetChronometer(view);
                editText.setEnabled(true);
                sessionView.setText("");
            }
        });
    }

    private void finalizeSession()
    {
        Session updateSession = (Session) sessionView.getTag();
        int sessionId = updateSession.getId();
        String listName = updateSession.getListName();
        int totalTime = (int) (SystemClock.elapsedRealtime() - stopWatch.getBase());
        int totalLaps = myDb.totalLapCount(listName);

        boolean isEmpty = checkProfiles();
        if(isEmpty)
        {
//            Nothing -- Could create Toast
        }
        else
        {
            Intent intent = getIntent();
            userWeight = intent.getStringExtra("keyTag");
            int weight = Integer.parseInt(userWeight);
            int weightKg = (int) ((weight) / 2.205);
            double timeMinutes = (totalTime) / 600000 ;

            double calorieCalc = ((timeMinutes)*(selectedMet*3.5*weightKg)/100);
            String calories = String.valueOf(calorieCalc);
            myDb.updateSession(sessionId, totalTime, totalLaps, calories);
        }
    }

    private void loadListView()
    {
        if (firstLoad)
        {
            Toast.makeText(TriggerActivity.this,"Select 'Activity Type' to track laps",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String selectedSession = sessionView.getText().toString();
            String sqlQuery = "SELECT * FROM LapList WHERE LIST_NAME = ?";
            String[] selectionArgs = {selectedSession};

            DBManager handler = new DBManager(this);
            SQLiteDatabase db = handler.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);

            ListView listLaps = (ListView) findViewById(R.id.list_view_laps);
            ListViewCursorAdapter cursorAdapter = new ListViewCursorAdapter(this, cursor);
            listLaps.setAdapter(cursorAdapter);
        }
    }
    private void blankListView()
    {
        String sqlQuery = "SELECT * FROM LapList WHERE LIST_NAME = null";

        DBManager handler = new DBManager(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        ListView listLaps = (ListView) findViewById(R.id.list_view_laps);
        ListViewCursorAdapter cursorAdapter = new ListViewCursorAdapter(this, cursor);
        listLaps.setAdapter(cursorAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        loadListView();
    }

    public boolean checkProfiles()
    {
        boolean empty = true;
        DBManager handler = new DBManager(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM ProfileList", null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    public void addSession()
    {
        btnNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String selectedSession = editText.getText().toString();
                if(selectedSession.matches(""))
                {
                    Toast.makeText(TriggerActivity.this,"Type in an Activity name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Session newSession = new Session(selectedSession);
                    long sessionRef = myDb.createSession(newSession);
                    int sessionId = (int) sessionRef;
                    Session tagSession = myDb.getSession(sessionId);

                    sessionView.setText(selectedSession);
                    sessionView.setTag(tagSession);
                    btnNewLap.setEnabled(true);
                    btnNewSession.setEnabled(false);
                    editText.setText("");
                    editText.setEnabled(false);
                    firstLoad = false;
                    lapNumber = 0;
                    loadListView();
                    resetChronometer(view);
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
//      Nothing
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
