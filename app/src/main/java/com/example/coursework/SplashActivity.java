package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View myView = findViewById(R.id.my_view); // Thay "my_view" bằng ID của view bạn muốn áp dụng animation

        // Tạo animation fade-in với thời gian 1000ms (1 giây)
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000); // Độ dài của animation (1000ms = 1s)

        // Bắt đầu animation
        fadeIn.start();

        // Sử dụng Handler để chờ thời gian animation hoàn thành trước khi chuyển sang màn hình chính
        int animationDuration = 1000; // Độ dài của animation (được thiết lập trong ObjectAnimator)
        new Handler().postDelayed(() -> {
            // Chuyển sang màn hình chính hoặc Activity chính của ứng dụng
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Kết thúc SplashActivity để người dùng không thể quay lại màn hình Splash
        }, animationDuration);
    }
}
