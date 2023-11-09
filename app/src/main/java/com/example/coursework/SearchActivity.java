package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    Button home_nav_at_search, add_nav_at_search,searchButton;
    EditText searchEditText;
    ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.edt_search);
        searchButton = findViewById(R.id.search);
        listView = findViewById(R.id.list_search);
        dbHelper = new DBHelper(this);

        home_nav_at_search = findViewById(R.id.home_nav_at_search);
        home_nav_at_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        add_nav_at_search = findViewById(R.id.add_nav_at_search);
        add_nav_at_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchKey = charSequence.toString().trim();
                if (!searchKey.isEmpty()) {
                    // Thực hiện tìm kiếm và hiển thị kết quả
                    Cursor cursor = dbHelper.searchHikesByName(searchKey);
                    // Hiển thị dữ liệu từ cursor trong ListView hoặc RecyclerView
                    // Ví dụ sử dụng ListView:
                    Adapter_Search cursorAdapter = new Adapter_Search(SearchActivity.this, cursor);
                    listView.setAdapter(cursorAdapter);
                } else {
                    // Nếu chuỗi là rỗng, xóa dữ liệu hiển thị trên ListView hoặc RecyclerView
                    // Ví dụ sử dụng ListView:
                    listView.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần xử lý ở đây
            }
        });



//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Lấy cursor adapter từ ListView
//                CursorAdapter cursorAdapter = (CursorAdapter) parent.getAdapter();
//                // Di chuyển con trỏ đến vị trí được nhấp
//                Cursor cursor = cursorAdapter.getCursor();
//                cursor.moveToPosition(position);
//                // Lấy avatar_id từ cursor
//                int avatarId = cursor.getInt(cursor.getColumnIndexOrThrow("avatar_id"));
//
//                // Chuyển sang Activity ObservationViewActivity và chuyển avatar_id qua Intent
//                Intent intent = new Intent(SearchActivity.this, ObservationView.class);
//                intent.putExtra("AVATAR_ID", avatarId);
//                startActivity(intent);
//            }
//        });
    }
}