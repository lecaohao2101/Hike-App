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

public class Adapter extends CursorAdapter {
    DBHelper dbHelper;
    public Adapter(Context context, Cursor c) {
        super(context, c, 0);
        dbHelper = new DBHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name_txt = view.findViewById(R.id.name_txt);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

        name_txt.setText(name);


        final int userId = cursor.getInt(cursor.getColumnIndexOrThrow("_id")); // Thay "id" bằng tên cột id trong database
//        final int activityId = cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")); // Thêm dòng này

        Button moreButton = view.findViewById(R.id.more_button);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở activity_observation.xml khi nhấn nút "More" và truyền dữ liệu cần thiết
                Intent intent = new Intent(context, ObservationActivity.class);
                intent.putExtra("userId", userId);
//                intent.putExtra("activityId", activityId);
//                intent.putExtra("avatarId", cursor.getInt(cursor.getColumnIndexOrThrow("avatar_id")));
                context.startActivity(intent);
            }
        });

        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức xóa người dùng từ database
                dbHelper.deleteUser(userId); // Thay dbHelper bằng tên của lớp truy cập database của bạn

                // Cập nhật ListView sau khi xóa người dùng
                Cursor newCursor = dbHelper.getAllUserData();
                swapCursor(newCursor);
            }
        });

        Button editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở activity chỉnh sửa và truyền dữ liệu cần thiết
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });

    }
}
