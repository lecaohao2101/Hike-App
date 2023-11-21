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

public class ObservationActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText observationInp, commentsInp, observationTimeInp, activity_id;
    private Button saveButton, backButton, viewButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        // Initialize UI elements
        activity_id = findViewById(R.id.activity_id);
        observationInp = findViewById(R.id.observationInp);
        observationTimeInp = findViewById(R.id.observationTimeInp);
        commentsInp = findViewById(R.id.commentsInp);
        saveButton = findViewById(R.id.save_button_observation);
        backButton = findViewById(R.id.back_button_observation);
        viewButton = findViewById(R.id.view_button_observation);
        dbHelper = new DBHelper(this);

        // Get data from the intent
        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            int userId = intent.getIntExtra("userId", -1);
            activity_id.setText(String.valueOf(userId));
        } else {
            activity_id.setText("N/A");
        }

        // Set onClickListener for the "Back" button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ObservationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the "Observation Time" EditText to show a DatePickerDialog
        observationTimeInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a Calendar object to get the current date
                Calendar calendar = Calendar.getInstance();

                //Get the current year
                int year = calendar.get(Calendar.YEAR);

                //Get the current month
                int month = calendar.get(Calendar.MONTH);

                //Get the current year
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                //Create a calendar dialog box to allow users to select dates
                DatePickerDialog datePickerDialog = new DatePickerDialog(ObservationActivity.this,

                        //Listen for events when the user selects a date
                        (view, year1, monthOfYear, dayOfMonth) -> {

                            //When the user selects, create the selected date, month, and year string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                            //Displays the selected date string in the date of birth input field
                            observationTimeInp.setText(selectedDate);
                        },

                        //Set default day, month, and year
                        year, month, day);

                //Show the calendar dialog to the user
                datePickerDialog.show();
            }
        });

        // Set onClickListener for the "Save" button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from input fields
                String avt_id = activity_id.getText().toString();
                String observation = observationInp.getText().toString();
                String timeof = observationTimeInp.getText().toString();
                String comment = commentsInp.getText().toString();

                // Validate input fields
                if (avt_id.isEmpty() || observation.isEmpty() || timeof.isEmpty()) {
                    Toast.makeText(ObservationActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert observation into the database
                    boolean isInserted = dbHelper.insertObservation(Integer.parseInt(avt_id), observation, timeof, comment);
                    if (isInserted) {
                        Toast.makeText(ObservationActivity.this, "The data has been saved to the database", Toast.LENGTH_SHORT).show();
                        // Clear input fields after successful data insertion
                        clearInputFields();
                    } else {
                        Toast.makeText(ObservationActivity.this, "Saving data to the database failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Helper method to clear input fields
            private void clearInputFields() {
                observationInp.setText("");
                observationTimeInp.setText("");
                observationTimeInp.setText("");
                commentsInp.setText("");
            }
        });

        // Set onClickListener for the "View" button to navigate to View_Observation activity
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityId = activity_id.getText().toString();
                if (!activityId.isEmpty()) {
                    Intent intent = new Intent(ObservationActivity.this, View_Observation.class);
                    intent.putExtra("activityId", Integer.parseInt(activityId));
                    startActivity(intent);
                } else {
                    Toast.makeText(ObservationActivity.this, "Please enter activity_id before viewing observations.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
