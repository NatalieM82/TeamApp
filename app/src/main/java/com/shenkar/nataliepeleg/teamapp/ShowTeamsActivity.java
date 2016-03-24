package com.shenkar.nataliepeleg.teamapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ShowTeamsActivity extends AppCompatActivity {

    ParseUser currentUser;

    // Construct the data source
    private ArrayList<Team> teamsList;
    // Create the adapter to convert the array to views
    private TeamsAdapter adapter;
    // Attach the adapter to a ListView
    private ListView listView;

    Button showTeamTasksBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teams);

        currentUser = ParseUser.getCurrentUser();

        String user_id = currentUser.getObjectId();

//        lvItems = (ListView) findViewById(R.id.teamsListView);
//        teamsList = new ArrayList<ParseObject>();
//        itemsAdapter = new ArrayAdapter<ParseObject>(this,
//                R.layout.teams_list_item, R.id.teamName, teamsList);
//        lvItems.setAdapter(itemsAdapter);


        teamsList = new ArrayList<Team>();
        adapter = new TeamsAdapter(this, teamsList);
        listView = (ListView) findViewById(R.id.teamsListView);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Team t = (Team) adapter.getItem(position);
//                String team_id = t.getId();
//                Log.d("show team: ", team_id);
//                //Go to show tasks activity
//                Intent intent = new Intent(
//                        ShowTeamsActivity.this,
//                        TasksActivity.class);
//                intent.putExtra("TEAM_ID", team_id);
//                startActivity(intent);
//            }
//        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Team t = (Team) adapter.getItem(position);
//                String team_id = t.getId();
//                Log.d("show team: ", team_id);
//                //Go to show tasks activity
//                Intent intent = new Intent(
//                        ShowTeamsActivity.this,
//                        TasksActivity.class);
//                intent.putExtra("TEAM_ID", team_id);
//                startActivity(intent);
//                return true;
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Team t = (Team) adapter.getItem(position);
                String team_id = t.getId();
                Log.d("show team: ", team_id);
                //Go to show tasks activity
                Intent intent = new Intent(
                        ShowTeamsActivity.this,
                        TasksActivity2.class);
                intent.putExtra("TEAM_ID", team_id);
                startActivity(intent);
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.whereEqualTo("managerId", user_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("Teams", objects.toString());
                    createTeamsList(objects);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Teams retrieving failed", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

//        showTeamTasksBtn = (Button) findViewById(R.id.showTeamsBtn);
//        showTeamTasksBtn.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Team t = (Team) adapter.getItem();
//                String team_id = t.getId();
//                Log.d("show team: ", team_id);
//                //Go to show tasks activity
//                Intent intent = new Intent(
//                        ShowTeamsActivity.this,
//                        TasksActivity.class);
//                intent.putExtra("TEAM_ID", team_id);
//                startActivity(intent);
//            }
//        });

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
            Intent intent = new Intent(ShowTeamsActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(ShowTeamsActivity.this,
                    AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(ShowTeamsActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }

    private void createTeamsList(List<ParseObject> objects){
        for(int i=0 ; i<objects.size(); i++){
            Team team = new Team(objects.get(i).getString("TeamName"), objects.get(i).getObjectId());
            adapter.add(team);
        }
        updateUI();
    }

    public void editTeamMembers(View view){
        View v = (View) view.getParent();

        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        Team t = (Team) adapter.getItem(position);
        String team_id = t.getId();

        Intent intent = new Intent(ShowTeamsActivity.this,
                CreateTeamActivity.class);
        intent.putExtra("TEAM_ID", team_id);
        startActivity(intent);
    }


}
