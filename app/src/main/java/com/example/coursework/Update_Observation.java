package com.example.coursework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Update_Observation extends AppCompatActivity {

    // Declare UI elements
    private EditText observationInp, commentsInp, observationTimeInp, activityIdEditText;
    private Button updateButton;
    private DBHelper dbHelper;
    private int observationId, activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        // Initialize UI elements and DBHelper
        observationInp = findViewById(R.id.observation_Update);
        observationTimeInp = findViewById(R.id.observationTime_Update);
        commentsInp = findViewById(R.id.comments_Update);
        activityIdEditText = findViewById(R.id.activity_id);
        updateButton = findViewById(R.id.updateButton);
        dbHelper = new DBHelper(this);

        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("observationId") && intent.hasExtra("activityId")) {
            observationId = intent.getIntExtra("observationId", -1);
            activityId = intent.getIntExtra("activityId", -1);
            activityIdEditText.setText(String.valueOf(activityId));

            // Retrieve the existing observation data and populate the UI elements
            Observation observation = dbHelper.getObservationById(observationId);
            if (observation != null) {
                observationInp.setText(observation.getObservationText());
                observationTimeInp.setText(observation.getObservationTime());
                commentsInp.setText(observation.getComments());
            } else {
                Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid observation data", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set onClickListener for the observationTime EditText to show a DatePickerDialog
        observationTimeInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Calendar object to get the current date
                Calendar calendar = Calendar.getInstance();

                // Get the current year, month, and day
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to allow users to select dates
                DatePickerDialog datePickerDialog = new DatePickerDialog(Update_Observation.this,
                        // Listen for events when the user selects a date
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Create the selected date string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                            // Display the selected date in the observationTime EditText
                            observationTimeInp.setText(selectedDate);
                        },
                        // Set default year, month, and day
                        year, month, day);

                // Show the DatePickerDialog to the user
                datePickerDialog.show();
            }
        });

        // Set onClickListener for the updateButton to update the observation
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observationText = observationInp.getText().toString();
                String observationTime = observationTimeInp.getText().toString();
                String comments = commentsInp.getText().toString();

                // Check if essential information is entered
                if (observationText.isEmpty() || observationTime.isEmpty()) {
                    Toast.makeText(Update_Observation.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt to update the observation in the database
                    boolean isUpdated = dbHelper.updateObservation(observationId, activityId, observationText, observationTime, comments);
                    if (isUpdated) {
                        // Display a success message and navigate to View_Observation activity
                        Toast.makeText(Update_Observation.this, "Observation updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Update_Observation.this, View_Observation.class);
                        intent.putExtra("activityId", activityId);
                        startActivity(intent);
                        finish();
                    } else {
                        // Display a failure message
                        Toast.makeText(Update_Observation.this, "Failed to update observation", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
