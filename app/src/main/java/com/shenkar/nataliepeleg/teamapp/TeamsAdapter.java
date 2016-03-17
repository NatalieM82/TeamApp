package com.shenkar.nataliepeleg.teamapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NatalieMenahem on 17/03/2016.
 */
public class TeamsAdapter extends ArrayAdapter<Team> {

    public TeamsAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Team team = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teams_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.teamName);
        // Populate the data into the template view using the data object
        tvName.setText(team.name);
        // Return the completed view to render on screen
        return convertView;
    }
}
