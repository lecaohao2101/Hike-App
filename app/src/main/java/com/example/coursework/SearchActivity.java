package com.example.coursework;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    // Declare UI elements
    Button home_nav_at_search, add_nav_at_search, searchButton;
    EditText searchInp;
    ListView display;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize UI elements and DBHelper
        searchInp = findViewById(R.id.searchInp);
        searchButton = findViewById(R.id.search);
        display = findViewById(R.id.list_search);
        dbHelper = new DBHelper(this);

        // Set onClickListener for the "Home" button to navigate to HomeActivity
        home_nav_at_search = findViewById(R.id.home_nav_at_search);
        home_nav_at_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the "Add" button to navigate to MainActivity
        add_nav_at_search = findViewById(R.id.add_nav_at_search);
        add_nav_at_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set a TextWatcher for the search EditText to perform live search as the user types
        searchInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the search key from the EditText
                String searchKey = charSequence.toString().trim();
                if (!searchKey.isEmpty()) {
                    // Perform a search in the database based on the search key
                    Cursor cursor = dbHelper.searchHikesByName(searchKey);
                    // Set the search results in the ListView using a custom cursor adapter
                    Adapter_Search cursorAdapter = new Adapter_Search(SearchActivity.this, cursor);
                    display.setAdapter(cursorAdapter);
                } else {
                    // If the search key is empty, clear the ListView
                    display.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
