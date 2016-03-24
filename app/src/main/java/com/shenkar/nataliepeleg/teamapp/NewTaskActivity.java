package com.shenkar.nataliepeleg.teamapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by NatalieMenahem on 20/03/2016.
 */
public class NewTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Boolean isManager;
    private ParseUser currentUser;
    private String user_id;
    private String team_id;
    private String task_id;

    private Task currentTask;
    private EditText descEt;
    private int isDone = 0;   //0 no , 1 yes


    private static final String TAG = "NewTaskActivity";

    LinkedHashMap<String, String> assignToMap;

    private Tracker mTracker;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // Obtain the shared Tracker instance.
        ParseApplication application = (ParseApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();

        descEt = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        currentUser = ParseUser.getCurrentUser();
        user_id = currentUser.getObjectId().toString();
        isManager = currentUser.getBoolean("manager");
        team_id = intent.getStringExtra("TEAM_ID");
        task_id = intent.getStringExtra("TASK_ID");


        // Priorities Spinner element
        final Spinner prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);

        // Spinner click listener
        prioritySpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> priorities = new ArrayList<String>();
        priorities.add("Normal");
        priorities.add("Urgent");
        priorities.add("Low");

        // Creating adapter for spinner
        final ArrayAdapter<String> prioritiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);

        // Drop down layout style - list view with radio button
        prioritiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        prioritySpinner.setAdapter(prioritiesAdapter);



        // Categories Spinner element
        final Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

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
        final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categorySpinner.setAdapter(categoriesAdapter);



        // Location Spinner element
        final Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

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
        final ArrayAdapter<String> locationsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locations);

        // Drop down layout style - list view with radio button
        locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        locationSpinner.setAdapter(locationsAdapter);



        // Dueto Spinner element
        final Spinner dueSpinner = (Spinner) findViewById(R.id.dueToSpinner);

        // Spinner click listener
        dueSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> duetos = new ArrayList<String>();
        duetos.add("Today");
        duetos.add("Tomorrow");

        // Creating adapter for spinner
        final ArrayAdapter<String> dueToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, duetos);

        // Drop down layout style - list view with radio button
        dueToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        dueSpinner.setAdapter(dueToAdapter);



        // Assign to Spinner element
        final Spinner assignToSpinner = (Spinner) findViewById(R.id.memberAssignSpin);

        // Spinner click listener
        assignToSpinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        assignToMap = new LinkedHashMap<String, String>();
        final List<String> assignToArray = new ArrayList<String>();
        assignToArray.add(currentUser.getString("username"));
        assignToMap.put(currentUser.getString("username"), user_id);



        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("TeamId", team_id);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.d("Team members", objects.toString());
                    for (int i = 0; i < objects.size(); i++) {
                        assignToMap.put(objects.get(i).getString("username"), objects.get(i).getObjectId());
                        assignToArray.add(objects.get(i).getString("username"));
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Teams retrieving failed", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        // Creating adapter for spinner
        final ArrayAdapter<String> assignToAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, assignToArray);

        // Drop down layout style - list view with radio button
        assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        assignToSpinner.setAdapter(assignToAdapter);



        if(task_id != null){
            Button saveBtn = (Button) findViewById(R.id.addToListBTN);
            saveBtn.setText("Update Task");

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("TodoTask");
            query2.whereEqualTo("objectId", task_id);
            query2.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Task:", objects.toString());
                        descEt.setText(objects.get(0).getString("taskText"));
                        int categorySpinnerPosition = categoriesAdapter.getPosition(objects.get(0).getString("category"));
                        categorySpinner.setSelection(categorySpinnerPosition);

                        int prioritySpinnerPosition = prioritiesAdapter.getPosition(objects.get(0).getString("priority"));
                        prioritySpinner.setSelection(prioritySpinnerPosition);

                        int locationSpinnerPosition = locationsAdapter.getPosition(objects.get(0).getString("location"));
                        locationSpinner.setSelection(locationSpinnerPosition);

                        int dueToSpinnerPosition = dueToAdapter.getPosition(objects.get(0).getString("dueTo"));
                        dueSpinner.setSelection(dueToSpinnerPosition);

//                        String assigned = assignToMap.get(objects.get(0).getString("UserId"));


//                        int pos = new ArrayList<String>(assignToMap.keySet()).indexOf(objects.get(0).getString("UserId"));
//                        Log.d("Assigned:", objects.get(0).getString("UserId"));
//                        Log.d("Assigned:", pos + " index");

                        int pos = 0;

                        for (String key : assignToMap.keySet()) {
                            String val = assignToMap.get(key);
                            if (val.equals(objects.get(0).getString("UserId"))){
                                pos = new ArrayList<String>(assignToMap.keySet()).indexOf(key);
                                Log.d("Assigned:", pos + " index");
                                break;
                            }
                        }
//                        int assignToSpinnerPosition = assignToAdapter.getPosition(assigned);
                        assignToSpinner.setSelection(pos);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Task retrieving failed", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
        Spinner assignToSpinner = (Spinner) findViewById(R.id.memberAssignSpin);

        String category = categorySpinner.getSelectedItem().toString();
        String priority = prioritySpinner.getSelectedItem().toString();
        String location = locationSpinner.getSelectedItem().toString();
        String dueTo = dueToSpinner.getSelectedItem().toString();
        String assignTo = assignToSpinner.getSelectedItem().toString();
        String id = assignToMap.get(assignTo);
        Log.d("assignTo: ", assignTo);
        Log.d("assignTo: ", id);


        // Prepare data intent
        Intent data = new Intent();
        final ParseObject taskObject;

        if(task_id != null) taskObject = ParseObject.createWithoutData("TodoTask", task_id);
        else taskObject = new ParseObject("TodoTask");

        taskObject.put("taskText", name);
        taskObject.put("done", false);
        taskObject.put("UserId", user_id);
        taskObject.put("TeamId", team_id);
        taskObject.put("status", "WAITING");
        taskObject.put("priority", priority);
        taskObject.put("category", category);
        taskObject.put("location", location);
        taskObject.put("dueTo", dueTo);
        taskObject.put("UserId", id);


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

        String trackername = "New Task Activity";

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + trackername);
        mTracker.setScreenName(trackername);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Add new task")
                .build());
        // [END custom_event]


        // Activity finished ok, return the data
        setResult(RESULT_OK, data);

        finish();

    }


    public void cancel(View view)
    {
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(NewTaskActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(NewTaskActivity.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(NewTaskActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
