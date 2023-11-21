// Import necessary packages and classes
package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Define the DBHelper class, extending SQLiteOpenHelper
public class DBHelper extends SQLiteOpenHelper {

    // Define common column names
    public static final String KEY_ID = "id";
    // Define column names for the hiking_activities table
    public static final String KEY_NAME = "name";
    // Define column names for the observations table
    public static final String KEY_ACTIVITY_ID = "activity_id";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_OBSERVATIONS = "observations";
    // Define constants for the database name and version
    private static final String DATABASE_NAME = "hiking_database";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";
    public static final String KEY_PARKING_AVAILABLE = "parking_available";
    public static final String KEY_LENGTH = "length";
    public static final String KEY_DIFFICULTY_LEVEL = "difficulty_level";
    public static final String KEY_DESCRIPTION = "description";
    // Define table names
    private static final String TABLE_HIKING_ACTIVITIES = "hiking_activities";
    public static final String KEY_OBSERVATION = "observation";
    public static final String KEY_OBSERVATION_TIME = "observation_time";
    public static final String KEY_COMMENTS = "comments";

    // Constructor for DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables when the database is first created
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

        // Create table observations with a foreign key reference to hiking_activities
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

    // Handle database upgrades by dropping and recreating tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKING_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        onCreate(db);
    }

    // Insert data into the hiking_activities table
    public boolean insertData(String name, String location, String date, String parkingAvailable, double length, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_LOCATION, location);
        values.put(KEY_DATE, date);
        values.put(KEY_PARKING_AVAILABLE, parkingAvailable);
        values.put(KEY_LENGTH, length);
        values.put(KEY_DIFFICULTY_LEVEL, difficultyLevel);
        values.put(KEY_DESCRIPTION, description);

        long result = db.insert(TABLE_HIKING_ACTIVITIES, null, values);
        return result != -1;
    }

    // Retrieve all data from the hiking_activities table as a cursor
    public Cursor getAllUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_NAME + ", " + KEY_LOCATION + ", " +
                KEY_DATE + ", " + KEY_PARKING_AVAILABLE + ", " + KEY_LENGTH + ", " +
                KEY_DIFFICULTY_LEVEL + " FROM " +
                TABLE_HIKING_ACTIVITIES + " ORDER BY " + KEY_NAME + " ASC";
        return db.rawQuery(query, null);
    }

    // Search for hikes by name and return a cursor with the results
    public Cursor searchHikesByName(String searchKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_NAME + ", " + KEY_LOCATION + ", " +
                KEY_DATE + ", " + KEY_PARKING_AVAILABLE + ", " + KEY_LENGTH + ", " +
                KEY_DIFFICULTY_LEVEL + ", " + KEY_DESCRIPTION + " FROM " +
                TABLE_HIKING_ACTIVITIES + " WHERE " + KEY_NAME + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + searchKey + "%"});
    }

    // Update data in the hiking_activities table
    public boolean updateData(int userId, String name, String location, String date, String parkingAvailable, double length, String difficultyLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_LOCATION, location);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_PARKING_AVAILABLE, parkingAvailable);
        contentValues.put(KEY_LENGTH, length);
        contentValues.put(KEY_DIFFICULTY_LEVEL, difficultyLevel);
        contentValues.put(KEY_DESCRIPTION, description);

        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        int affectedRows = db.update(TABLE_HIKING_ACTIVITIES, contentValues, whereClause, whereArgs);

        db.close();

        return affectedRows > 0;
    }

    // Delete a user from the hiking_activities table
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKING_ACTIVITIES, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    // Delete all users from the hiking_activities table
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKING_ACTIVITIES, null, null);
        db.close();
    }

    // Insert an observation into the observations table
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

    // Retrieve observations for a specific activity by activityId
    public Cursor getObservationsByActivityId(int activityId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " as _id, " + KEY_ACTIVITY_ID + ", " +
                KEY_OBSERVATION + ", " + KEY_OBSERVATION_TIME + ", " + KEY_COMMENTS +
                " FROM " + TABLE_OBSERVATIONS +
                " WHERE " + KEY_ACTIVITY_ID + " = ?" + " ORDER BY " + KEY_OBSERVATION + " ASC";
        return db.rawQuery(query, new String[]{String.valueOf(activityId)});
    }

    // Delete a specific observation by observationId
    public void deleteObservation(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    // Delete all observations for a specific activity by activityId
    public void deleteallObservationsByActivityId(int activityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, KEY_ACTIVITY_ID + " = ?", new String[]{String.valueOf(activityId)});
        db.close();
    }

    // Retrieve data for a specific user by userId
    public Cursor getDataById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HIKING_ACTIVITIES + " WHERE " + KEY_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    // Update an observation in the observations table
    public boolean updateObservation(int observationId, int activityId, String observation, String observationTime, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACTIVITY_ID, activityId);
        values.put(KEY_OBSERVATION, observation);
        values.put(KEY_OBSERVATION_TIME, observationTime);
        values.put(KEY_COMMENTS, comments);

        int rowsAffected = db.update(TABLE_OBSERVATIONS, values, KEY_ID + "=?", new String[]{String.valueOf(observationId)});
        db.close();

        return rowsAffected > 0;
    }

    // Retrieve an observation by observationId
    public Observation getObservationById(int observationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(observationId)});

        if (cursor.moveToFirst()) {
            int activityId = cursor.getInt(cursor.getColumnIndex(KEY_ACTIVITY_ID));
            String observationText = cursor.getString(cursor.getColumnIndex(KEY_OBSERVATION));
            String observationTime = cursor.getString(cursor.getColumnIndex(KEY_OBSERVATION_TIME));
            String comments = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));

            Observation observation = new Observation(observationId, activityId, observationText, observationTime, comments);

            cursor.close();
            return observation;
        } else {
            cursor.close();
            return null;
        }
    }
}
