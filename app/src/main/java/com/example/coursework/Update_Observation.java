package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.coursework.Observation;


public class Update_Observation extends AppCompatActivity {

    private EditText observationEditText, commentsEditText, observationTimeEditText, avatarIdEditText;
    private Button updateButton, backButton;
    private DBHelper dbHelper;
    private int observationId, avatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);

        observationEditText = findViewById(R.id.observation_Update);
        observationTimeEditText = findViewById(R.id.observationTime_Update);
        commentsEditText = findViewById(R.id.comments_Update);
        avatarIdEditText = findViewById(R.id.avatar_id);
        updateButton = findViewById(R.id.updateButton);
        backButton = findViewById(R.id.back_update);
        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("observationId") && intent.hasExtra("avatarId")) {
            observationId = intent.getIntExtra("observationId", -1);
            avatarId = intent.getIntExtra("avatarId", -1);
            avatarIdEditText.setText(String.valueOf(avatarId));

            Observation observation = dbHelper.getObservationById(observationId);
            if (observation != null) {
                observationEditText.setText(observation.getObservationText());
                observationTimeEditText.setText(observation.getObservationTime());
                commentsEditText.setText(observation.getComments());
            } else {
                Toast.makeText(this, "Observation not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid observation data", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observationText = observationEditText.getText().toString();
                String observationTime = observationTimeEditText.getText().toString();
                String comments = commentsEditText.getText().toString();

                if (observationText.isEmpty() || observationTime.isEmpty()) {
                    Toast.makeText(Update_Observation.this, "Please enter complete information", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUpdated = dbHelper.updateObservation(observationId, avatarId, observationText, observationTime, comments);
                    if (isUpdated) {
                        Toast.makeText(Update_Observation.this, "Observation updated successfully", Toast.LENGTH_SHORT).show();

                        // Quay lại trang View_Observation và cập nhật danh sách mới
                        Intent intent = new Intent(Update_Observation.this, View_Observation.class);
                        intent.putExtra("avatarId", avatarId);
                        startActivity(intent);

                        // Kết thúc Activity hiện tại để ngăn chặn quay lại nó khi nhấn nút back
                        finish();
                    } else {
                        Toast.makeText(Update_Observation.this, "Failed to update observation", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
