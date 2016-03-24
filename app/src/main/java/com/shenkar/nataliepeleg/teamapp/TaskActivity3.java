package com.shenkar.nataliepeleg.teamapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity3 extends AppCompatActivity {

    private Boolean isManager;
    private ParseUser currentUser;
    private String user_id;
    private String team_id;

    static final int GET_TASK_REQUEST = 1;


    // Construct the data source
    private ArrayList<Task> taskList;
    // Create the adapter to convert the array to views
    public static TasksAdapter adapter;
    private ListView listView;

    private Tracker mTracker;

    private static final String TAG = "MainActivity";

    public SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tasks");
        setSupportActionBar(toolbar);

        // Obtain the shared Tracker instance.
        ParseApplication application = (ParseApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TaskActivity3.this,
                        NewTaskActivity.class);
                intent.putExtra("TEAM_ID", team_id);
                startActivityForResult(intent, GET_TASK_REQUEST);
            }
        });

        //Tasks adapter and lists
        taskList = new ArrayList<Task>();
        adapter = new TasksAdapter(this, taskList);
        listView = (ListView) findViewById(R.id.tasksListView);
        listView.setAdapter(adapter);

        //Get team id to show specific tasks
        Intent intent = getIntent();
        team_id = intent.getStringExtra("TEAM_ID");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("objectId", team_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("Member Tasks", objects.toString());
                    toolbar.setTitle(objects.get(0).getString("TeamName"));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Team retrieving failed", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        //Get user id, in case user is not a member and need only his tasks
        currentUser = ParseUser.getCurrentUser();
        user_id = currentUser.getObjectId().toString();
        isManager = currentUser.getBoolean("manager");

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getTasks();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        getTasks();

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
            Intent intent = new Intent(TaskActivity3.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(TaskActivity3.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(TaskActivity3.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GET_TASK_REQUEST) {
            getTasks();
        }



    }

    public void updateUI() {
        adapter.notifyDataSetChanged();
        String name = "Tasks Activity";

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + name);
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Watch tasks")
                .build());
        // [END custom_event]
    }

    public void createTasksList(List<ParseObject> objects){
        if(objects.isEmpty()){
            TextView tv = (TextView) findViewById(R.id.noTasksText);
            tv.setText("No current tasks");
        }
        else {
            adapter.clear();
            for(int i=0 ; i<objects.size(); i++){
                Task task = new Task(objects.get(i).getString("taskText"), objects.get(i).getObjectId(),
                        objects.get(i).getBoolean("done"), objects.get(i).getString("status"), objects.get(i).getString("dueTo"));
                adapter.add(task);
                updateUI();
            }
        }

    }

    public void getTasks(){
        if(isManager == true){
            //get all task in team
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TodoTask");
            query.whereEqualTo("TeamId", team_id);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Manager Tasks", objects.toString());
                        createTasksList(objects);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Tasks retrieving failed", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        else {
            //get member tasks
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TodoTask");
            query.whereEqualTo("UserId", user_id);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Member Tasks", objects.toString());
                        createTasksList(objects);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Tasks retrieving failed", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        swipeContainer.setRefreshing(false);
    }

    public void showTask(View view){
        View v = (View) view.getParent();

        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        Task t = (Task) adapter.getItem(position);
        String task_id = t.getId();

        if(isManager){
            Intent intent = new Intent(
                    TaskActivity3.this,
                    NewTaskActivity.class);
            intent.putExtra("TASK_ID", task_id);
            intent.putExtra("TEAM_ID", team_id);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(
                    TaskActivity3.this,
                    TaskReportActivity.class);
            intent.putExtra("TASK_ID", task_id);
            startActivity(intent);
        }

    }

}
