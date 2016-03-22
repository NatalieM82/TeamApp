package com.shenkar.nataliepeleg.teamapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by NatalieMenahem on 20/03/2016.
 */
public class NewTaskActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Boolean isManager;
    private ParseUser currentUser;
    private String user_id;
    private String team_id;

    private Task currentTask;
    private EditText descEt;
    private int isDone = 0;   //0 no , 1 yes


    private static final String TAG = "NewTaskActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        descEt = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        currentUser = ParseUser.getCurrentUser();
        user_id = currentUser.getObjectId().toString();
        isManager = currentUser.getBoolean("manager");
        team_id = intent.getStringExtra("TEAM_ID");


        // Priorities Spinner element
        Spinner prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);

        // Spinner click listener
        prioritySpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        priorities.add("Normal");
        priorities.add("Urgent");
        priorities.add("Low");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        prioritySpinner.setAdapter(dataAdapter);



        // Categories Spinner element
        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        // Spinner click listener
        categorySpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Cleaning");
        categories.add("Electricity");
        categories.add("Computers");
        categories.add("General");
        categories.add("Other");


        // Creating adapter for spinner
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(categoriesAdapter);



        // Location Spinner element
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        // Spinner click listener
        locationSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> locations = new ArrayList<String>();
        locations.add("Kitchen");
        locations.add("Lobby");
        locations.add("Meeting room A");
        locations.add("Conference room");
        locations.add("Other");


        // Creating adapter for spinner
        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);

        // Drop down layout style - list view with radio button
        locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        locationSpinner.setAdapter(locationsAdapter);



        // Dueto Spinner element
        Spinner dueSpinner = (Spinner) findViewById(R.id.dueToSpinner);

        // Spinner click listener
        dueSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> duetos = new ArrayList<String>();
        duetos.add("Today");
        duetos.add("Tomorrow");

        // Creating adapter for spinner
        ArrayAdapter<String> dueToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, duetos);

        // Drop down layout style - list view with radio button
        dueToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        dueSpinner.setAdapter(dueToAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



    public void okClicked(View v) {
        if (descEt == null) return;
        String name = descEt.getText().toString();

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        Spinner prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        Spinner dueToSpinner = (Spinner) findViewById(R.id.dueToSpinner);

        String category = categorySpinner.getSelectedItem().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String location = locationSpinner.getSelectedItem().toString();
        String dueTo = dueToSpinner.getSelectedItem().toString();

//        if (dueTo.equals("Today"))

        // Prepare data intent
        Intent data = new Intent();

        final ParseObject taskObject = new ParseObject("TodoTask");

        taskObject.put("taskText", name);
        taskObject.put("done", false);
        taskObject.put("UserId", user_id);
        taskObject.put("TeamId", team_id);
        taskObject.put("status", "WAITING");
        taskObject.put("priority", priority);
        taskObject.put("category", category);
        taskObject.put("location", location);
        taskObject.put("dueTo", dueTo);


        taskObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Log.d("Task saved", taskObject.getObjectId());
                    Toast.makeText(getApplicationContext(),
                            "Tasks saved successfully", Toast.LENGTH_LONG)
                            .show();
                } else {
                    // The save failed.
                    Log.d("Task saved error", ": " + e);
                }
            }
        });


        // Activity finished ok, return the data
        setResult(RESULT_OK, data);

        finish();

    }


    public void cancel(View view)
    {
        finish();
    }

}
