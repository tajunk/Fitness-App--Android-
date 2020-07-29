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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    DBManager myDb;
    TextView initialMessage;
    TextView userWeight;
    TextView infoMessage;
    EditText nameEntry;
    EditText weightEntry;
    Spinner listsDropDown;
    Button saveProfile;
    Button moveTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DBManager(this);

        initialMessage = (TextView) findViewById(R.id.initial_message);
        userWeight = (TextView) findViewById(R.id.user_weight_box);
        infoMessage = (TextView) findViewById(R.id.info_message);
        nameEntry = (EditText) findViewById(R.id.display_name_entry);
        weightEntry = (EditText) findViewById(R.id.weight_measurement_entry);
        moveTimer = (Button) findViewById(R.id.button_timer_main);

        listsDropDown = (Spinner)findViewById(R.id.profile_list);
        listsDropDown.setOnItemSelectedListener(this);
        loadSpinnerData();

        moveTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String intentText = userWeight.getText().toString();
                Intent intent = new Intent(MainActivity.this, TriggerActivity.class);
                intent.putExtra("keyTag", intentText);
                startActivity(intent);
            }
        });

    }

    public void saveButton(View v)
    {
        String profileName = nameEntry.getText().toString();
        String stringConversion = weightEntry.getText().toString();
        int profileWeight = Integer.parseInt(stringConversion);

        Profile newProfile = new Profile(profileName, profileWeight);
        long profileRef = myDb.createProfile(newProfile);
        Toast.makeText(MainActivity.this,"Profile " + nameEntry.getText().toString() + " created.",Toast.LENGTH_SHORT).show();
        loadSpinnerData();

        nameEntry.setText("");
        weightEntry.setText("");
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

    private void loadSpinnerData()
    {
        boolean isEmpty = checkProfiles();
        if(isEmpty == true)
        {
            Toast.makeText(MainActivity.this,"Create a profile for all Timer features",Toast.LENGTH_SHORT).show();
        }
        else
        {
            List<Profile> spinnerLists = myDb.getProfiles();
            ArrayAdapter<Profile> dataAdapter = new ArrayAdapter<Profile>(this, android.R.layout.simple_spinner_item, spinnerLists);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listsDropDown.setAdapter(dataAdapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        boolean isEmpty = checkProfiles();
        if(isEmpty == true)
        {
            Toast.makeText(MainActivity.this,"Create a profile for all Timer features",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Profile profileSelection = (Profile) adapterView.getItemAtPosition(i);
            userWeight.setTag(profileSelection);
            int weightInt = profileSelection.getUserWeight();
            String displayWeight = String.valueOf(weightInt);
            userWeight.setText(displayWeight);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                String intentText = userWeight.getText().toString();
                Intent intent = new Intent(this, TriggerActivity.class);
                intent.putExtra("keyTag", intentText);
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
