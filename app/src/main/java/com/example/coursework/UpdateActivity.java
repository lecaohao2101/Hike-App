package com.example.coursework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    // Declare UI elements
    private DBHelper dbHelper;
    private int userId;
    private EditText name_Update, location_Update, date_Update, length_Update, description_Update;
    private RadioGroup radioGroup, radioGroupLevel_Update;
    private Button update;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize UI elements and DBHelper
        dbHelper = new DBHelper(this);

        name_Update = findViewById(R.id.name_Update);
        location_Update = findViewById(R.id.location_Update);
        date_Update = findViewById(R.id.date_Update);
        length_Update = findViewById(R.id.length_Update);
        description_Update = findViewById(R.id.description_Update);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroupLevel_Update = findViewById(R.id.radioGroup_level_Update);
        update = findViewById(R.id.update);
        userId = getIntent().getIntExtra("userId", -1);

        // Load existing data of the hiking activity from the database
        loadDataFromDatabase(userId);

        // Set onClickListener for the date_Update EditText to show a DatePickerDialog
        date_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Calendar object to get the current date
                Calendar calendar = Calendar.getInstance();

                // Get the current year, month, and day
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to allow users to select dates
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                        // Listen for events when the user selects a date
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Create the selected date string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                            // Display the selected date in the date_Update EditText
                            date_Update.setText(selectedDate);
                        },
                        // Set default year, month, and day
                        year, month, day);

                // Show the DatePickerDialog to the user
                datePickerDialog.show();
            }
        });

        // Set onClickListener for the update Button to update the activity information
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_Update.getText().toString();
                String location = location_Update.getText().toString();
                String date = date_Update.getText().toString();
                String lengthStr = length_Update.getText().toString();
                String description = description_Update.getText().toString();
                String parkingAvailable = radioGroup.getCheckedRadioButtonId() == R.id.yesRadioButton ? "Yes" : "No";
                String difficultyLevel = "";
                int selectedDifficultyId = radioGroupLevel_Update.getCheckedRadioButtonId();
                if (selectedDifficultyId == R.id.easyRadioButton) {
                    difficultyLevel = "Easy";
                } else if (selectedDifficultyId == R.id.mediumRadioButton) {
                    difficultyLevel = "Medium";
                } else if (selectedDifficultyId == R.id.hardRadioButton) {
                    difficultyLevel = "Hard";
                }

                if (name.isEmpty() || location.isEmpty() || date.isEmpty() || lengthStr.isEmpty()) {
                    Toast.makeText(UpdateActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the activity data in the database
                    boolean isUpdated = dbHelper.updateData(userId, name, location, date, parkingAvailable, Double.parseDouble(lengthStr), difficultyLevel, description);
                    if (isUpdated) {
                        // Display a success message and navigate to HomeActivity
                        Toast.makeText(UpdateActivity.this, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Display a failure message
                        Toast.makeText(UpdateActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Load existing data of the hiking activity from the database
    private void loadDataFromDatabase(int userId) {
        Cursor cursor = dbHelper.getDataById(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String parkingAvailable = cursor.getString(cursor.getColumnIndexOrThrow("parking_available"));
            double length = cursor.getDouble(cursor.getColumnIndexOrThrow("length"));
            String difficultyLevel = cursor.getString(cursor.getColumnIndexOrThrow("difficulty_level"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            // Populate UI elements with existing data
            name_Update.setText(name);
            location_Update.setText(location);
            date_Update.setText(date);
            if (parkingAvailable.equals("Yes")) {
                radioGroup.check(R.id.yesRadioButton);
            } else {
                radioGroup.check(R.id.noRadioButton);
            }
            length_Update.setText(String.valueOf(length));
            if (difficultyLevel.equals("Easy")) {
                radioGroupLevel_Update.check(R.id.easyRadioButton);
            } else if (difficultyLevel.equals("Medium")) {
                radioGroupLevel_Update.check(R.id.mediumRadioButton);
            } else {
                radioGroupLevel_Update.check(R.id.hardRadioButton);
            }
            description_Update.setText(description);

            cursor.close();
        }
    }
}
