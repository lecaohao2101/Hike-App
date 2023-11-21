// Import necessary packages and classes
package com.example.coursework;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

// Define the HomeActivity class, extending AppCompatActivity
public class HomeActivity extends AppCompatActivity {

    // Declare class variables
    ListView display;
    DBHelper dbHelper;
    Button add_nav_at_home, search_nav_at_home, deleteAll;

    // Called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        display = findViewById(R.id.display);
        dbHelper = new DBHelper(this);

        // Retrieve all user data from the database using a cursor
        Cursor cursor = dbHelper.getAllUserData();

        // Create a custom adapter with the cursor and set it to the display
        Adapter cursorAdapter = new Adapter(this, cursor);
        display.setAdapter(cursorAdapter);

        // Initialize the deleteAll button and set its click listener
        deleteAll = findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete all users from the database
                dbHelper.deleteAllUsers();

                // Retrieve updated user data and set it to the adapter
                Cursor newCursor = dbHelper.getAllUserData();
                Adapter cursorAdapter = new Adapter(HomeActivity.this, newCursor);
                display.setAdapter(cursorAdapter);
            }
        });

        // Initialize the search button and set its click listener
        search_nav_at_home = findViewById(R.id.search_nav_at_home);
        search_nav_at_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SearchActivity
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // Initialize the add button and set its click listener
        add_nav_at_home = findViewById(R.id.add_nav_at_home);
        add_nav_at_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
