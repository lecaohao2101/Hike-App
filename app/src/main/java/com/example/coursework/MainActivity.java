package com.example.coursework;

// These lines import the classes and interfaces needed to work with SQLite and Android
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Declare class variables
    Button add, home_nav_at_add, search_nav_at_add;
    DBHelper dbHelper;
    EditText nameInp, locationInp, dateInp, lengthInp, descriptionInp;
    RadioGroup radioGroup_level, radioGroup;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DBHelper and UI components
        dbHelper = new DBHelper(this);
        nameInp = findViewById(R.id.nameInp);
        locationInp = findViewById(R.id.locationInp);
        lengthInp = findViewById(R.id.lengthInp);
        descriptionInp = findViewById(R.id.descriptionInp);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup_level = findViewById(R.id.radioGroup_Level);
        dateInp = findViewById(R.id.dateInp);

        // Set onClickListener for dateInp to open a DatePickerDialog
        dateInp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Calendar object to get the current date
                Calendar calendar = Calendar.getInstance();

                // Get the current year, month, and day
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to allow users to select dates
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Create the selected date string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                            // Display the selected date in the dateInp
                            dateInp.setText(selectedDate);
                        },
                        year, month, day);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Initialize the "add" button and set its onClickListener
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from input fields
                String name = nameInp.getText().toString();
                String location = locationInp.getText().toString();
                String date = dateInp.getText().toString();
                String lengthStr = lengthInp.getText().toString();
                String description = descriptionInp.getText().toString();
                String parkingAvailable = radioGroup.getCheckedRadioButtonId() == R.id.yesRadioButton ? "Yes" : "No";
                String difficultyLevel = "";
                int selectedDifficultyId = radioGroup_level.getCheckedRadioButtonId();

                // Map selected difficulty level
                if (selectedDifficultyId == R.id.easyRadioButton) {
                    difficultyLevel = "Easy";
                } else if (selectedDifficultyId == R.id.mediumRadioButton) {
                    difficultyLevel = "Medium";
                } else if (selectedDifficultyId == R.id.hardRadioButton) {
                    difficultyLevel = "Hard";
                }

                // Validate input fields
                if (name.isEmpty() || location.isEmpty() || date.isEmpty() || lengthStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    // Parse length from string to double
                    double length = Double.parseDouble(lengthStr);

                    // Display confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    String finalDifficultyLevel = difficultyLevel;
                    builder.setTitle("Confirmation")
                            .setMessage("New hike will be added:\n" +
                                    "Name hike: " + name + "\n" +
                                    "Location: " + location + "\n" +
                                    "Date of hike: " + date + "\n" +
                                    "Parking available: " + parkingAvailable + "\n" +
                                    "Length of the hike: " + length + "\n" +
                                    "Difficulty level: " + difficultyLevel + "\n" +
                                    "Description: " + description)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Insert data into the database
                                    boolean isInserted = dbHelper.insertData(name, location, date, parkingAvailable, length, finalDifficultyLevel, description);

                                    // Display a toast message based on the database insertion result
                                    if (isInserted) {
                                        Toast.makeText(MainActivity.this, "The data has been saved to the database", Toast.LENGTH_SHORT).show();
                                        clearInputFields();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Saving data to database failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }

            // Method to clear input fields after successful data insertion
            private void clearInputFields() {
                nameInp.setText("");
                locationInp.setText("");
                dateInp.setText("");
                lengthInp.setText("");
                descriptionInp.setText("");
                radioGroup_level.clearCheck();
                radioGroup.clearCheck();
            }
        });

        // Initialize and set onClickListener for home_nav_at_add button
        home_nav_at_add = findViewById(R.id.home_nav_at_add);
        home_nav_at_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Initialize and set onClickListener for search_nav_at_add button
        search_nav_at_add = findViewById(R.id.search_nav_at_add);
        search_nav_at_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SearchActivity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Check if the activity was started with an intent containing userId
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", -1);
            if (userId != -1) {
                // Load existing data from the database for editing
                loadDataFromDatabase(userId);
            }
        }
    }

    // Method to load data from the database for editing
    private void loadDataFromDatabase(int userId) {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getDataById(userId);

        // Initialize RadioButtons
        RadioButton yesRadioButton = findViewById(R.id.yesRadioButton);
        RadioButton noRadioButton = findViewById(R.id.noRadioButton);
        RadioButton easyRadioButton = findViewById(R.id.easyRadioButton);
        RadioButton mediumRadioButton = findViewById(R.id.mediumRadioButton);
        RadioButton hardRadioButton = findViewById(R.id.hardRadioButton);

        // Check if the cursor is not null and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve data from the cursor
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String parkingAvailable = cursor.getString(cursor.getColumnIndexOrThrow("parking_available"));
            double length = cursor.getDouble(cursor.getColumnIndexOrThrow("length"));
            String difficultyLevel = cursor.getString(cursor.getColumnIndexOrThrow("difficulty_level"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            // Set data to corresponding input fields
            nameInp.setText(name);
            locationInp.setText(location);
            dateInp.setText(date);
            if (parkingAvailable.equals("Yes")) {
                yesRadioButton.setChecked(true);
            } else {
                noRadioButton.setChecked(true);
            }
            lengthInp.setText(String.valueOf(length));
            if (difficultyLevel.equals("Easy")) {
                easyRadioButton.setChecked(true);
            } else if (difficultyLevel.equals("Medium")) {
                mediumRadioButton.setChecked(true);
            } else {
                hardRadioButton.setChecked(true);
            }
            descriptionInp.setText(description);

            // Close the cursor
            cursor.close();
        }

        // Close the DBHelper
        dbHelper.close();
    }
}
