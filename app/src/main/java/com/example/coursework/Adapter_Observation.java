//package com.example.coursework;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CursorAdapter;
//import android.widget.TextView;
//
//public class ObservationsAdapter extends CursorAdapter {
//
//    DBHelper dbHelper;
//    public int avatarId;
//    public ObservationsAdapter(Context context, Cursor c, int avatarId) {
//        super(context, c, 0);
//        dbHelper = new DBHelper(context);
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return LayoutInflater.from(context).inflate(R.layout.observation_item, parent, false);
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        TextView observation_txt = view.findViewById(R.id.observation_txt);
//        TextView observation_time_txt = view.findViewById(R.id.observationTime_txt);
//        TextView comment_txt = view.findViewById(R.id.comment_txt);
//
//        String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
//        String time = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
//        String comment = cursor.getString(cursor.getColumnIndexOrThrow("comments"));
//
//        observation_txt.setText(observation);
//        observation_time_txt.setText(time);
//        comment_txt.setText(comment);
//
//
//
//        final int observationIds = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
//        final String observations = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
//        final String times = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
//        final String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));
//
//        ObservationsAdapter adapter = new ObservationsAdapter(context, cursor, avatarId);
//
//        observation_txt.setText(observation);
//        observation_time_txt.setText(time);
//        comment_txt.setText(comment);
//
//        // Lắng nghe sự kiện khi nút Delete được bấm
//        Button deleteButton = view.findViewById(R.id.delete_observation);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Gọi hàm xóa observation từ database
//                dbHelper.deleteObservation(observationIds);
//                // Cập nhật lại CursorAdapter để hiển thị danh sách observations mới
//                Cursor newCursor = dbHelper.getObservationsByAvatarId(avatarId); // Cần viết hàm này trong DBHelper
//                swapCursor(newCursor);
//            }
//        });
//
//        // Lắng nghe sự kiện khi nút Edit được bấm
//        Button editButton = view.findViewById(R.id.edit_observation);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển dữ liệu observation tới ObservationActivity để chỉnh sửa
//                Intent intent = new Intent(context, ObservationActivity.class);
//                intent.putExtra("observationId", observationIds);
//                intent.putExtra("avatarId", avatarId);
//                intent.putExtra("observation", observations);
//                intent.putExtra("observationTime", times);
//                intent.putExtra("comments", comments);
//                context.startActivity(intent);
//            }
//        });
//    }
//}
//

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

public class Adapter_Observation extends CursorAdapter {
    private DBHelper dbHelper;
    private int avatarId;

    public Adapter_Observation(Context context, Cursor c, int avatarId) {
        super(context, c, 0);
        this.dbHelper = new DBHelper(context);
        this.avatarId = avatarId;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.observation_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView observation_txt = view.findViewById(R.id.observation_txt);
        TextView observation_time_txt = view.findViewById(R.id.observationTime_txt);
        TextView comment_txt = view.findViewById(R.id.comment_txt);

        final int observationId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("observation_time"));
        String comment = cursor.getString(cursor.getColumnIndexOrThrow("comments"));

        observation_txt.setText(observation);
        observation_time_txt.setText(time);
        comment_txt.setText(comment);

        // Lắng nghe sự kiện khi nút Delete được bấm
        Button deleteButton = view.findViewById(R.id.delete_observation);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm xóa observation từ database
                dbHelper.deleteObservation(observationId);
                // Cập nhật lại CursorAdapter để hiển thị danh sách observations mới
                Cursor newCursor = dbHelper.getObservationsByAvatarId(avatarId);
                swapCursor(newCursor);
            }
        });

        Button editButton = view.findViewById(R.id.edit_observation);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gửi thông tin quan sát đến Activity ObservationActivity để chỉnh sửa
                Intent intent = new Intent(context, Update_Observation.class);
                intent.putExtra("observationId", observationId);
                intent.putExtra("avatarId", avatarId);
                context.startActivity(intent);
            }
        });
    }
}

