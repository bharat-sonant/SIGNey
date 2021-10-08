package com.sonant.dsin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabaseChapterData extends SQLiteOpenHelper {

    private static final String LOCAL_DATABASE_NAME = "chapters";


    public LocalDatabaseChapterData(Context context) {
        super(context, LOCAL_DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + LOCAL_DATABASE_NAME + "(_ID VARCHAR PRIMARY KEY,"
                + "name VARCHAR," +
                "keyname VARCHAR);";


        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCAL_DATABASE_NAME);
        onCreate(db);
    }

    public boolean addChapter(SubjectAdapter subjectAdapter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("_ID", subjectAdapter.getId());
        contentValues.put("name", subjectAdapter.getHeading());
        contentValues.put("keyname", subjectAdapter.getKeyname());


        long result = db.insert(LOCAL_DATABASE_NAME, null, contentValues);

        return result != -1;
    }

    // TODO: 25/8/20 Create a main getter and setter 
    // TODO: 25/8/20 create methods for each page
    // TODO: 25/8/20  modify the complete code

    public List<SubjectAdapter> getAll() {
        List<SubjectAdapter> smsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCAL_DATABASE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            SubjectAdapter subjectAdapter = new SubjectAdapter();
            String id = String.valueOf(cursor.getString(0));
            String name = cursor.getString(1);
            String keyname = cursor.getString(2);

            subjectAdapter.setId(id);
            subjectAdapter.setHeading(name);
            subjectAdapter.setKeyname(keyname);
            smsList.add(subjectAdapter);
        }

        cursor.close();

        return smsList;
    }

    public List<SubjectAdapter> getSpecific(String s) {
        List<SubjectAdapter> smsList = new ArrayList<>();
        String s1 = s;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCAL_DATABASE_NAME + " WHERE keyname=?";
        Cursor cursor = db.rawQuery(query, new String[]{s1});
        while (cursor.moveToNext()) {
            SubjectAdapter subjectAdapter = new SubjectAdapter();
            String id = String.valueOf(cursor.getString(0));
            String name = cursor.getString(1);
            String keyname = cursor.getString(2);

            subjectAdapter.setId(id);
            subjectAdapter.setHeading(name);
            subjectAdapter.setKeyname(keyname);
            smsList.add(subjectAdapter);
        }

        cursor.close();

        return smsList;
    }

    public boolean deleteById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(LOCAL_DATABASE_NAME, "_ID=?", new String[]{id}) > 0;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(LOCAL_DATABASE_NAME, null, null);
    }
    public int getChapterCount() {
        String countQuery = "SELECT  * FROM " + LOCAL_DATABASE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
