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

// Define the Adapter class, extending CursorAdapter
public class Adapter extends CursorAdapter {
    // Declare a DBHelper object to handle database operations
    DBHelper dbHelper;

    // Constructor for the Adapter class
    public Adapter(Context context, Cursor c) {
        // Call the superclass constructor with the provided context, cursor, and flags
        super(context, c, 0);

        // Initialize the DBHelper object with the given context
        dbHelper = new DBHelper(context);
    }

    // Override the newView method to inflate and return a new view for the item
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
    }

    // Override the bindView method to bind data to the views within the item view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find the TextView with the ID "name_txt" in the item view
        TextView name_text = view.findViewById(R.id.name_text);

        // Retrieve the name from the cursor based on the column name
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        // Set the text of the name TextView with the retrieved name
        name_text.setText(name);

        // Retrieve the user ID from the cursor based on the column name
        final int userId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

        // Find the "more_button" in the item view and set a click listener
        Button more = view.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the ObservationActivity
                Intent intent = new Intent(context, ObservationActivity.class);
                // Put the user ID as an extra in the Intent
                intent.putExtra("userId", userId);
                // Start the ObservationActivity with the created Intent
                context.startActivity(intent);
            }
        });

        // Find the "delete_button" in the item view and set a click listener
        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the user from the database using the DBHelper
                dbHelper.deleteUser(userId);
                // Retrieve a new cursor with updated data after deletion
                Cursor newCursor = dbHelper.getAllUserData();
                // Swap the old cursor with the new one
                swapCursor(newCursor);
            }
        });

        // Find the "edit_button" in the item view and set a click listener
        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the UpdateActivity
                Intent intent = new Intent(context, UpdateActivity.class);
                // Put the user ID as an extra in the Intent
                intent.putExtra("userId", userId);
                // Start the UpdateActivity with the created Intent
                context.startActivity(intent);
            }
        });
    }
}
