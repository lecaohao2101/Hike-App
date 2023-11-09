package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class ObservationActivity extends AppCompatActivity {

    private EditText observationEditText, commentsEditText, observationTimeEditText, avatar_id;
    private Button saveButton, backButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

// Sử dụng cursor này để hiển thị dữ liệu trong giao diện người dùng.

        avatar_id = findViewById(R.id.avatar_id);
        observationEditText = findViewById(R.id.observationEditText);
        observationTimeEditText = findViewById(R.id.observationTimeEditText);
        commentsEditText = findViewById(R.id.commentsEditText);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            // Lấy giá trị id từ Intent và đặt vào trường avatar_id
            int userId = intent.getIntExtra("userId", -1);
            avatar_id.setText(String.valueOf(userId));
        } else {
            avatar_id.setText("N/A");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ObservationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        observationTimeEditText.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ObservationActivity.this,

                        //Listen for events when the user selects a date
                        (view, year1, monthOfYear, dayOfMonth) -> {

                            //When the user selects, create the selected date, month, and year string
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                            //Displays the selected date string in the date of birth input field
                            observationTimeEditText.setText(selectedDate);
                        },

                        //Set default day, month, and year
                        year, month, day);

                //Show the calendar dialog to the user
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avt_id = avatar_id .getText().toString();
                String observation = observationEditText.getText().toString();
                String timeof = observationTimeEditText.getText().toString();
                String comment = commentsEditText.getText().toString();

                if (avt_id.isEmpty() || observation.isEmpty() || timeof.isEmpty()){
                    Toast.makeText(ObservationActivity.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                }else {
                    boolean isInserted = dbHelper.insertObservation(Integer.parseInt(avt_id), observation, timeof, comment);
                    if (isInserted) {
                        Toast.makeText(ObservationActivity.this, "The data has been saved to the database", Toast.LENGTH_SHORT).show();
                        // Xóa các trường nhập liệu sau khi lưu dữ liệu thành công
                        clearInputFields();
                    } else {
                        Toast.makeText(ObservationActivity.this, "Saving data to database failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            private void clearInputFields() {
                observationEditText.setText("");
                observationTimeEditText.setText("");
                observationTimeEditText.setText("");
                commentsEditText.setText("");
                // Đặt lại mức độ khó khăn và tình trạng chỗ đậu xe về giá trị mặc định
                // Không cần đặt lại giá trị mặc định cho biến parkingAvailable và selectedDifficultyLevel,
                // chúng sẽ được cập nhật dựa trên lựa chọn mới của người dùng trong lần nhập tiếp theo.
            }

        });

        Button viewButton = findViewById(R.id.view_button_observation);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avatarId = avatar_id.getText().toString();
                if (!avatarId.isEmpty()) {
                    Intent intent = new Intent(ObservationActivity.this, View_Observation.class);
                    intent.putExtra("avatarId", Integer.parseInt(avatarId));
                    startActivity(intent);
                } else {
                    Toast.makeText(ObservationActivity.this, "Vui lòng nhập Avatar_ID trước khi xem quan sát.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
