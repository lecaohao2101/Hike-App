package com.example.coursework;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class View_Observation extends AppCompatActivity {
    private ListView listView;
    private Adapter_Observation adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_view);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("activityId")) {
            int activityId = intent.getIntExtra("activityId", -1);
            listView = findViewById(R.id.listObservation);
            dbHelper = new DBHelper(this);

            // Modify the database query to include the primary key column as "_id"
            Cursor cursor = dbHelper.getObservationsByActivityId(activityId);

            MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "activity_id", "observation", "observation_time", "comments"});

            // Populate the matrixCursor with data from the original cursor
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    int activityIds = cursor.getInt(cursor.getColumnIndexOrThrow("activity_id"));
                    String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
                    String observationTime = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
                    String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

                    matrixCursor.addRow(new Object[]{id, activityIds, observation, observationTime, comments});
                } while (cursor.moveToNext());
            }

            // Create an Adapter_Observation and set it to the ListView
            adapter = new Adapter_Observation(this, matrixCursor, activityId);
            listView.setAdapter(adapter);
        } else {
            // Display a toast message and finish the activity if there is no "activityId" extra
            Toast.makeText(this, "There are no observed data to display.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set onClickListener for the backButton to finish the activity
        Button backButton = findViewById(R.id.backButton_view);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set onClickListener for the deleteAllButton to delete all observations related to the hiking activity
        Button deleteAllButton = findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int activityId = getIntent().getIntExtra("activityId", -1);

                // Delete all observations related to the hiking activity
                dbHelper.deleteallObservationsByActivityId(activityId);

                // Get a new cursor after deletion and update the adapter
                Cursor newCursor = dbHelper.getObservationsByActivityId(activityId);
                adapter.swapCursor(newCursor);
            }
        });
    }
}
