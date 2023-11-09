package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Name and Version Database
    private static final String DATABASE_NAME = "hiking_database";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_HIKING_ACTIVITIES = "hiking_activities";
    private static final String TABLE_OBSERVATIONS = "observations";

    public static final String KEY_ID = "id";

    // Column of table hiking_activities
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";
    public static final String KEY_PARKING_AVAILABLE = "parking_available";
    public static final String KEY_LENGTH = "length";
    public static final String KEY_DIFFICULTY_LEVEL = "difficulty_level";
    public static final String KEY_DESCRIPTION = "description";

    // Column of table observations
    public static final String KEY_ACTIVITY_ID = "activity_id";
    public static final String KEY_OBSERVATION = "observation";
    public static final String KEY_OBSERVATION_TIME = "observation_time";
    public static final String KEY_COMMENTS = "comments";

    // Constructor of DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table hiking_activities
        String createHikingActivitiesTable = "CREATE TABLE " + TABLE_HIKING_ACTIVITIES +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT NOT NULL," +
                KEY_LOCATION + " TEXT NOT NULL," +
                KEY_DATE + " TEXT NOT NULL," +
                KEY_PARKING_AVAILABLE + " TEXT NOT NULL," +
                KEY_LENGTH + " REAL NOT NULL," +
                KEY_DIFFICULTY_LEVEL + " TEXT NOT NULL," +
                KEY_DESCRIPTION + " TEXT" +
                ")";

        db.execSQL(createHikingActivitiesTable);

        // Create table observations
        String createObservationsTable = "CREATE TABLE " + TABLE_OBSERVATIONS +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ACTIVITY_ID + " INTEGER NOT NULL," +
                KEY_OBSERVATION + " TEXT NOT NULL," +
                KEY_OBSERVATION_TIME + " TEXT NOT NULL," +
                KEY_COMMENTS + " TEXT," +
                "FOREIGN KEY(" + KEY_ACTIVITY_ID + ") REFERENCES " + TABLE_HIKING_ACTIVITIES + "(" + KEY_ID + ")" +
                ")";
        db.execSQL(createObservationsTable);
    }

    // Delete old table after create table new
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKING_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);

        onCreate(db);
    }

    // Add information to the database
    public boolean insertData(String name, String location, String date, String parkingAvailable, double length, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Put values in table hiking_activities
        values.put(KEY_NAME, name);
        values.put(KEY_LOCATION, location);
        values.put(KEY_DATE, date);
        values.put(KEY_PARKING_AVAILABLE, parkingAvailable);
        values.put(KEY_LENGTH, length);
        values.put(KEY_DIFFICULTY_LEVEL, difficultyLevel);
        values.put(KEY_DESCRIPTION, description);

        // Add data to the table hiking_activities
        long result = db.insert(TABLE_HIKING_ACTIVITIES, null, values);
        return result != -1;
    }

    public Cursor getAllUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query all data in table hiking_activities and return cursor
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_NAME + ", " + KEY_LOCATION + ", " +
                KEY_DATE + ", " + KEY_PARKING_AVAILABLE + ", " + KEY_LENGTH + ", " +
                KEY_DIFFICULTY_LEVEL + " FROM " +
                TABLE_HIKING_ACTIVITIES + " ORDER BY " + KEY_NAME + " ASC";
        return db.rawQuery(query, null);
    }

    public Cursor searchHikesByName(String searchKey) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_ID + " as _id, " + KEY_NAME + ", " + KEY_LOCATION + ", " + KEY_DATE + ", " + KEY_PARKING_AVAILABLE + ", " + KEY_LENGTH + ", " + KEY_DIFFICULTY_LEVEL + ", " + KEY_DESCRIPTION + " FROM " + TABLE_HIKING_ACTIVITIES + " WHERE " + KEY_NAME + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + searchKey + "%"});
    }

    public boolean updateData(int userId, String name, String location, String date, String parkingAvailable, double length, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("location", location);
        contentValues.put("date", date);
        contentValues.put("parking_available", parkingAvailable);
        contentValues.put("length", length);
        contentValues.put("difficulty_level", difficultyLevel);
        contentValues.put("description", description);

        // Câu lệnh WHERE để chỉ cập nhật dữ liệu với id tương ứng
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        // Thực hiện truy vấn cập nhật, hàm update trả về số dòng bị ảnh hưởng (số dòng đã được cập nhật)
        int affectedRows = db.update("hiking_activities", contentValues, whereClause, whereArgs);

        // Đóng kết nối tới cơ sở dữ liệu
        db.close();

        // Nếu có ít nhất một dòng được cập nhật, trả về true, ngược lại trả về false
        return affectedRows > 0;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKING_ACTIVITIES, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKING_ACTIVITIES, null, null);
        db.close();
    }



    public boolean insertObservation(int activityId, String observation, String observationTime, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_ID, activityId);
        values.put(KEY_OBSERVATION, observation);
        values.put(KEY_OBSERVATION_TIME, observationTime);
        values.put(KEY_COMMENTS, comments);

        long result = db.insert(TABLE_OBSERVATIONS, null, values);
        return result != -1;
    }

    public Cursor getAllObservations() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_ACTIVITY_ID + ", " + KEY_OBSERVATION + ", " + KEY_OBSERVATION_TIME + ", " + KEY_COMMENTS + " FROM " + TABLE_OBSERVATIONS + " ORDER BY " + KEY_OBSERVATION + " ASC";


        return db.rawQuery(query, null);
    }

//    public Cursor getObservationsByAvatarId(int avatarId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_ACTIVITY_ID + " = ?";
//        return db.rawQuery(query, new String[]{String.valueOf(avatarId)});
//    }

    public Cursor getObservationsByAvatarId(int avatarId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_ACTIVITY_ID + ", " +
                KEY_OBSERVATION + ", " + KEY_OBSERVATION_TIME + ", " + KEY_COMMENTS +
                " FROM " + TABLE_OBSERVATIONS +
                " WHERE " + KEY_ACTIVITY_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(avatarId)});
    }

    public void deleteObservation(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public void deleteallObservationsByAvatarId(int avatarId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_ACTIVITY_ID + " = ?", new String[]{String.valueOf(avatarId)});
        db.close();
    }

    public Cursor getDataById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HIKING_ACTIVITIES + " WHERE " + KEY_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


    public boolean updateObservation(int observationId, int avatarId, String observation, String observationTime, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activity_id", avatarId);
        values.put("observation", observation);
        values.put("observation_time", observationTime);
        values.put("comments", comments);

        // Thực hiện câu lệnh cập nhật dữ liệu
        int rowsAffected = db.update("observations", values, "id=?", new String[]{String.valueOf(observationId)});

        // Đóng kết nối đến cơ sở dữ liệu
        db.close();

        // Kiểm tra xem có bản ghi nào bị cập nhật hay không và trả về kết quả
        return rowsAffected > 0;
    }

    public Observation getObservationById(int observationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(observationId)});

        if (cursor.moveToFirst()) {
            int activityId = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVITY_ID));
            String observationText = cursor.getString(cursor.getColumnIndex(KEY_OBSERVATION));
            String observationTime = cursor.getString(cursor.getColumnIndex(KEY_OBSERVATION_TIME));
            String comments = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));

            // Tạo đối tượng Observation từ dữ liệu trong Cursor
            Observation observation = new Observation(observationId, activityId, observationText, observationTime, comments);

            // Đóng Cursor và trả về đối tượng Observation
            cursor.close();
            return observation;
        } else {
            // Nếu không có dữ liệu, đóng Cursor và trả về null
            cursor.close();
            return null;
        }
    }

}



