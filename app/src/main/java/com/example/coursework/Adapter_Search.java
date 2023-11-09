package com.example.coursework;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Adapter_Search extends CursorAdapter {
    public Adapter_Search(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Tạo layout cho một item trong ListView nếu chưa được tạo
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_search, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Bind dữ liệu từ Cursor vào các View trong item của ListView
        TextView nameTextView = view.findViewById(R.id.name_search_txt);
        // Lấy giá trị từ Cursor dựa trên tên cột
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        nameTextView.setText(name);
    }
}

