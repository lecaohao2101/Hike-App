package com.example.coursework;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private int userId;
    private EditText name_Update, location_Update, date_Update, length_Update, description_Update;
    private RadioGroup radioGroup, radioGroupLevel_Update;
    private Button update, back_at_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new DBHelper(this);

        name_Update = findViewById(R.id.name_Update);
        location_Update = findViewById(R.id.location_Update);
        date_Update = findViewById(R.id.date_Update);
        length_Update = findViewById(R.id.length_Update);
        description_Update = findViewById(R.id.description_Update);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroupLevel_Update = findViewById(R.id.radioGroup_level_Update);
        update = findViewById(R.id.update);
        back_at_update = findViewById(R.id.back_at_update);

        // Nhận userId từ Intent
        userId = getIntent().getIntExtra("userId", -1);

        // Load dữ liệu người dùng cần chỉnh sửa từ cơ sở dữ liệu và hiển thị lên giao diện
        loadDataFromDatabase(userId);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường chỉnh sửa
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

                // Kiểm tra xem đã nhập đủ thông tin chưa
                if (name.isEmpty() || location.isEmpty() || date.isEmpty() || lengthStr.isEmpty()) {
                    // Hiển thị thông báo yêu cầu nhập đủ thông tin
                    Toast.makeText(UpdateActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật dữ liệu người dùng trong cơ sở dữ liệu
                    boolean isUpdated = dbHelper.updateData(userId, name, location, date, parkingAvailable, Double.parseDouble(lengthStr), difficultyLevel, description);
                    if (isUpdated) {
                        Toast.makeText(UpdateActivity.this, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Đóng activity sau khi cập nhật thành công
                    } else {
                        Toast.makeText(UpdateActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        back_at_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadDataFromDatabase(int userId) {
        Cursor cursor = dbHelper.getDataById(userId);

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy dữ liệu từ Cursor và điền vào các trường chỉnh sửa
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String parkingAvailable = cursor.getString(cursor.getColumnIndexOrThrow("parking_available"));
            double length = cursor.getDouble(cursor.getColumnIndexOrThrow("length"));
            String difficultyLevel = cursor.getString(cursor.getColumnIndexOrThrow("difficulty_level"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

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
