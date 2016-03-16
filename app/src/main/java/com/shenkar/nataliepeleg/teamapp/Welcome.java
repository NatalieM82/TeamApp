package com.shenkar.nataliepeleg.teamapp;

/**
 * Created by NatalieMenahem on 07/03/2016.
 */

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends Activity {
    // Declare Variable
    Button logout;
    Button createTeam;
    boolean isManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        isManager = currentUser.getBoolean("manager");

        if(isManager == false){

        }

        // Locate Button in welcome.xml
        logout = (Button) findViewById(R.id.logout);

        createTeam = (Button) findViewById(R.id.createTeamBtn);

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });

        createTeam.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Welcome.this,
                        CreateTeamActivity.class);
                startActivity(intent);
            }
        });
    }
}
