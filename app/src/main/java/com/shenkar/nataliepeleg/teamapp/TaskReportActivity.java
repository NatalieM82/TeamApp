package com.shenkar.nataliepeleg.teamapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class TaskReportActivity extends AppCompatActivity {
    private String task_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_report);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        task_id = intent.getStringExtra("TASK_ID");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TodoTask");
        query.whereEqualTo("objectId", task_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("Task:", objects.toString());
                    addTaskDetails(objects);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Teams retrieving failed", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public void addTaskDetails(List<ParseObject> objects){
        TextView taskTV = (TextView) findViewById(R.id.taskText);
        taskTV.setText(objects.get(0).getString("taskText"));

        TextView categoryTV = (TextView) findViewById(R.id.chosenCategory);
        categoryTV.setText(objects.get(0).getString("category"));

        TextView priorityTV = (TextView) findViewById(R.id.chosenPriority);
        priorityTV.setText(objects.get(0).getString("priority"));

        TextView locationTV = (TextView) findViewById(R.id.chosenLocation);
        locationTV.setText(objects.get(0).getString("location"));

        TextView dueTV = (TextView) findViewById(R.id.chosenDue);
        dueTV.setText(objects.get(0).getString("dueTo"));

        Button acceptBtn = (Button) findViewById(R.id.acceptBtn);
        Button rejectBtn = (Button) findViewById(R.id.rejectBtn);
        Button inProgBtn = (Button) findViewById(R.id.inProgressBtn);
        Button doneBtn = (Button) findViewById(R.id.doneBtn);

        final LinearLayout firstActions = (LinearLayout) findViewById(R.id.taskFirstActionBtns);
        final LinearLayout secondActions = (LinearLayout) findViewById(R.id.taskSecondActionsBtns);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Create a pointer to an object of class Point with id dlkj83d
                ParseObject point = ParseObject.createWithoutData("TodoTask", task_id);

                // Set a new value on quantity
                point.put("status", "ACCEPT");

                // Save
                point.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            Toast.makeText(getApplicationContext(),
                                    "Task Accepted successfully", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(),
                                    "Error saving, try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });


                secondActions.setVisibility(View.VISIBLE);

                firstActions.setVisibility(View.INVISIBLE);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Create a pointer to an object of class Point with id dlkj83d
                ParseObject point = ParseObject.createWithoutData("TodoTask", task_id);

                // Set a new value on quantity
                point.put("status", "REJECT");

                // Save
                point.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            Toast.makeText(getApplicationContext(),
                                    "Task Rejected", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(),
                                    "Error saving, try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });

        inProgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Create a pointer to an object of class Point with id dlkj83d
                ParseObject point = ParseObject.createWithoutData("TodoTask", task_id);

                // Set a new value on quantity
                point.put("status", "IN PROGRESS");

                // Save
                point.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            Toast.makeText(getApplicationContext(),
                                    "Task marked in progress", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(),
                                    "Error saving, try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Create a pointer to an object of class Point with id dlkj83d
                ParseObject point = ParseObject.createWithoutData("TodoTask", task_id);

                // Set a new value on quantity
                point.put("status", "DONE");

                // Save
                point.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            Toast.makeText(getApplicationContext(),
                                    "Task marked as done", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(),
                                    "Error saving, try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });



        String status = objects.get(0).getString("status");
        switch (status){
            case "WAITING":{

                secondActions.setVisibility(View.INVISIBLE);
                break;
            }
            case "ACCEPT":{
                firstActions.setVisibility(View.INVISIBLE);
                break;
            }
            case "DONE":{
                //show camera

                firstActions.setVisibility(View.INVISIBLE);
                secondActions.setVisibility(View.INVISIBLE);
                break;
            }
        }
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
            Intent intent = new Intent(TaskReportActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(TaskReportActivity.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(TaskReportActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
