// Import necessary packages and classes
package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

// Define the Adapter_Observation class, extending CursorAdapter
public class Adapter_Observation extends CursorAdapter {
    // Declare DBHelper object and activityId variable
    private DBHelper dbHelper;
    private final int activityId;

    // Constructor for the Adapter_Observation class
    public Adapter_Observation(Context context, Cursor c, int activityId) {
        // Call the superclass constructor with the provided context, cursor, and flags
        super(context, c, 0);
        // Initialize DBHelper object with the given context
        this.dbHelper = new DBHelper(context);
        // Initialize activityId with the provided value
        this.activityId = activityId;
    }

    // Override the newView method to inflate and return a new view for the observation item
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.observation_item, parent, false);
    }

    // Override the bindView method to bind data to the views within the observation item view
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find TextViews in the observation item view
        TextView observation_text = view.findViewById(R.id.observation_text);
        TextView observation_time_text = view.findViewById(R.id.observationTime_text);
        TextView comment_text = view.findViewById(R.id.comment_text);

        // Retrieve data from the cursor based on column names
        final int observationId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
        String comment = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

        // Set the text of TextViews with the retrieved data
        observation_text.setText(observation);
        observation_time_text.setText(time);
        comment_text.setText(comment);

        // Find the "delete_observation" button in the observation item view and set a click listener
        Button deleteButton = view.findViewById(R.id.delete_observation);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the observation from the database using the DBHelper
                dbHelper.deleteObservation(observationId);
                // Retrieve a new cursor with updated data after deletion
                Cursor newCursor = dbHelper.getObservationsByActivityId(activityId);
                // Swap the old cursor with the new one
                swapCursor(newCursor);
            }
        });

        // Find the "edit_observation" button in the observation item view and set a click listener
        Button editButton = view.findViewById(R.id.edit_observation);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the Update_Observation activity
                Intent intent = new Intent(context, Update_Observation.class);
                // Put the observationId and activityId as extras in the Intent
                intent.putExtra("observationId", observationId);
                intent.putExtra("activityId", activityId);
                // Start the Update_Observation activity with the created Intent
                context.startActivity(intent);
            }
        });
    }
}
