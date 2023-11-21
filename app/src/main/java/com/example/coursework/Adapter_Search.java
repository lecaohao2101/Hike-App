// Import necessary packages and classes
package com.example.coursework;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

// Define the Adapter_Search class, extending CursorAdapter
public class Adapter_Search extends CursorAdapter {
    // Constructor for the Adapter_Search class
    public Adapter_Search(Context context, Cursor cursor) {
        // Call the superclass constructor with the provided context, cursor, and flags
        super(context, cursor, 0);
    }

    // Override the newView method to inflate and return a new view for the search item
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the layout for the search item using LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_search, parent, false);
    }

    // Override the bindView method to bind data to the views within the search item view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find the TextView in the search item view with the ID "name_search_txt"
        TextView nameTextView = view.findViewById(R.id.name_search_text);

        // Retrieve the name from the cursor based on the column name
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        // Set the text of the name TextView with the retrieved name
        nameTextView.setText(name);
    }
}
