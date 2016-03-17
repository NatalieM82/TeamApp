package com.shenkar.nataliepeleg.teamapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
}
