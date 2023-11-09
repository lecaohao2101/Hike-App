package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button add, home_nav_at_add, search_nav_at_add;
    DBHelper dbHelper;
    EditText nameEditText, locationEditText, dateEditText, lengthEditText, descriptionEditText;
    RadioGroup radioGroup_level, radioGroup;
    private int userId;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        lengthEditText = findViewById(R.id.lengthEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
//        Button chooseImageButton = findViewById(R.id.chooseImageButton);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup_level = findViewById(R.id.radioGroup_Level);
        dateEditText = findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,

                        //Listen for events when the user selects a date
                        (view, year1, monthOfYear, dayOfMonth) -> {

                            //When the user selects, create the selected date, month, and year string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                            //Displays the selected date string in the date of birth input field
                            dateEditText.setText(selectedDate);
                        },

                        //Set default day, month, and year
                        year, month, day);

                //Show the calendar dialog to the user
                datePickerDialog.show();
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập liệu
                String name = nameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String lengthStr = lengthEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String parkingAvailable = radioGroup.getCheckedRadioButtonId() == R.id.yesRadioButton ? "Yes" : "No";
                String difficultyLevel = "";
                int selectedDifficultyId = radioGroup_level.getCheckedRadioButtonId();
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
                    Toast.makeText(MainActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {

                    // Tiếp tục hiển thị hộp thoại xác nhận
                    double length = Double.parseDouble(lengthStr);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    String finalDifficultyLevel = difficultyLevel;
                    builder.setTitle("Confirmation")
                            .setMessage(
                                    "New hike will be added: " + "\n"
                                            + "Name hike: " + name + "\n"
                                            + "Location: " + location + "\n"
                                            + "Date of hike: " + date + "\n"
                                            + "Parking available: " + parkingAvailable + "\n"
                                            + "Length of the hike: " + length + "\n"
                                            + "Difficulty level: " + difficultyLevel + "\n"
                                            + "Description: " + description)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Thêm dữ liệu vào cơ sở dữ liệu
                                    boolean isInserted = dbHelper.insertData(name, location, date, parkingAvailable, length, finalDifficultyLevel, description);

                                    if (isInserted) {
                                        Toast.makeText(MainActivity.this, "The data has been saved to the database", Toast.LENGTH_SHORT).show();
                                        // Xóa các trường nhập liệu sau khi lưu dữ liệu thành công
                                        clearInputFields();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Saving data to database failed", Toast.LENGTH_SHORT).show();
                                    }
//
//                                    boolean isUpdated = dbHelper.updateData(userId, name, location, date, parkingAvailable, length, finalDifficultyLevel, description);
//
//                                    if (isUpdated) {
//                                        Toast.makeText(MainActivity.this, "The data has been updated in the database", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(MainActivity.this, "Updating data in the database failed", Toast.LENGTH_SHORT).show();
//                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); // Đóng hộp thoại nếu người dùng chọn Cancel
                                }
                            })
                            .show();
                }
            }
            private void clearInputFields() {
                nameEditText.setText("");
                locationEditText.setText("");
                dateEditText.setText("");
                lengthEditText.setText("");
                descriptionEditText.setText("");
                // Đặt lại mức độ khó khăn và tình trạng chỗ đậu xe về giá trị mặc định
                radioGroup_level.clearCheck();
                radioGroup.clearCheck();
                // Không cần đặt lại giá trị mặc định cho biến parkingAvailable và selectedDifficultyLevel,
                // chúng sẽ được cập nhật dựa trên lựa chọn mới của người dùng trong lần nhập tiếp theo.
            }
        });

        home_nav_at_add = findViewById(R.id.home_nav_at_add);
        home_nav_at_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        search_nav_at_add = findViewById(R.id.search_nav_at_add);
        search_nav_at_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("userId")) {
            userId = intent.getIntExtra("userId", -1);
            if (userId != -1) {
                // Lấy dữ liệu từ cơ sở dữ liệu bằng userId và điền vào các trường chỉnh sửa
                loadDataFromDatabase(userId);
            }
        }
    }

    private void loadDataFromDatabase(int userId) {
        // Sử dụng DBHelper để lấy dữ liệu từ cơ sở dữ liệu dựa trên userId
        DBHelper dbHelper = new DBHelper(this); // Thay 'this' bằng context tương ứng nếu bạn gọi hàm từ nơi khác
        Cursor cursor = dbHelper.getDataById(userId);

        // Khai báo các RadioButton tại đây
        RadioButton yesRadioButton = findViewById(R.id.yesRadioButton);
        RadioButton noRadioButton = findViewById(R.id.noRadioButton);
        RadioButton easyRadioButton = findViewById(R.id.easyRadioButton);
        RadioButton mediumRadioButton = findViewById(R.id.mediumRadioButton);
        RadioButton hardRadioButton = findViewById(R.id.hardRadioButton);

        // Kiểm tra xem có dữ liệu được trả về không
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy dữ liệu từ Cursor và điền vào các trường chỉnh sửa
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String parkingAvailable = cursor.getString(cursor.getColumnIndexOrThrow("parking_available"));
            double length = cursor.getDouble(cursor.getColumnIndexOrThrow("length"));
            String difficultyLevel = cursor.getString(cursor.getColumnIndexOrThrow("difficulty_level"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            // Điền dữ liệu vào các trường chỉnh sửa
            nameEditText.setText(name);
            locationEditText.setText(location);
            dateEditText.setText(date);
            if (parkingAvailable.equals("Yes")) {
                yesRadioButton.setChecked(true);
            } else {
                noRadioButton.setChecked(true);
            }
            lengthEditText.setText(String.valueOf(length));
            if (difficultyLevel.equals("Easy")) {
                easyRadioButton.setChecked(true);
            } else if (difficultyLevel.equals("Medium")) {
                mediumRadioButton.setChecked(true);
            } else {
                hardRadioButton.setChecked(true);
            }
            descriptionEditText.setText(description);

            // Đóng Cursor sau khi sử dụng
            cursor.close();
        }
        // Đóng kết nối tới database
        dbHelper.close();
    }

}
