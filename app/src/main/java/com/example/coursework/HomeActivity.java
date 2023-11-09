package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    DBHelper dbHelper;
    Button add_nav_at_home ,search_nav_at_home, deleteAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listView);
        dbHelper = new DBHelper(this);

        Cursor cursor = dbHelper.getAllUserData();

        // Create a UserCursorAdapter to display the user data in the 'listView'
        Adapter cursorAdapter = new Adapter(this, cursor);

        // Set the UserCursorAdapter as the adapter for the 'listView'
        listView.setAdapter(cursorAdapter);

        deleteAll = findViewById(R.id.deleteAllButton);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức xóa tất cả người dùng từ database
                dbHelper.deleteAllUsers(); // Thay dbHelper bằng tên của lớp truy cập database của bạn

                // Cập nhật ListView sau khi xóa tất cả người dùng
                Cursor newCursor = dbHelper.getAllUserData();
                // Tạo mới Adapter và thiết lập cho ListView
                Adapter cursorAdapter = new Adapter(HomeActivity.this, newCursor);
                listView.setAdapter(cursorAdapter);
            }
        });
        search_nav_at_home = findViewById(R.id.search_nav_at_home);
        search_nav_at_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        add_nav_at_home = findViewById(R.id.add_nav_at_home);
        add_nav_at_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}