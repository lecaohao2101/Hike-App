package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
        if (intent.hasExtra("avatarId")) {
            int avatarId = intent.getIntExtra("avatarId", -1);
            listView = findViewById(R.id.listObservation);
            dbHelper = new DBHelper(this);

            // Modify the database query to include the primary key column as "_id"
            Cursor cursor = dbHelper.getObservationsByAvatarId(avatarId);

            MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "activity_id", "observation", "observation_time", "comments"});

            // Populate the matrixCursor with data from the original cursor
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    int activityId = cursor.getInt(cursor.getColumnIndexOrThrow("activity_id"));
                    String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
                    String observationTime = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
                    String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

                    matrixCursor.addRow(new Object[]{id, activityId, observation, observationTime, comments});
                } while (cursor.moveToNext());
            }

            adapter = new Adapter_Observation(this, matrixCursor, avatarId);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "There are no observed data to display.", Toast.LENGTH_SHORT).show();
            finish();
        }
        // Thêm nút Back để quay lại trang trước
        Button backButton = findViewById(R.id.backButton_view);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button deleteAllButton = findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy avatarId từ Intent
                int avatarId = getIntent().getIntExtra("avatarId", -1);

                // Xóa các observations có avatarId tương ứng
                dbHelper.deleteallObservationsByAvatarId(avatarId);

                // Cập nhật lại CursorAdapter để hiển thị danh sách observations mới
                Cursor newCursor = dbHelper.getObservationsByAvatarId(avatarId);
                adapter.swapCursor(newCursor);
            }
        });


    }
}


