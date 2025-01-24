package com.example.libraryreservationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

// Klasa DatabaseHelper menaxhon krijimin dhe përditësimin e bazës së të dhënave SQLite.
public class DatabaseHelper extends SQLiteOpenHelper {

    // Informacioni i bazës së të dhënave
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 1;

    // Emrat e tabelave
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USER_DATA = "user_data";

    // Kolonat për TABLE_USERS
    private static final String COL_USER_ID = "ID";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ADDRESS = "address";

    // Kolonat për TABLE_USER_DATA
    private static final String COL_DATA_ID = "ID";
    private static final String COL_NAME = "NAME";

    // Konstruktor për DatabaseHelper, i cili inicializon bazën e të dhënave.
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Krijon tabelat kur baza e të dhënave inicializohet për herë të parë.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Krijimi i tabelës TABLE_USERS
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ADDRESS + " TEXT)";
        db.execSQL(createUsersTable);

        // Krijimi i tabelës TABLE_USER_DATA
        String createUserDataTable = "CREATE TABLE " + TABLE_USER_DATA + " (" +
                COL_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT)";
        db.execSQL(createUserDataTable);
    }

    // Përditëson strukturën e bazës së të dhënave kur versioni i saj ndryshon.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Fshin tabelat ekzistuese nëse ato ekzistojnë
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        // Krijon tabelat e reja
        onCreate(db);
    }

    // Operacione CRUD për TABLE_USER_DATA
    // Metoda për të futur të dhëna në tabelën user_data
    public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        // Futja e të dhënave në tabelë dhe kontrollimi i rezultatit
        long result = db.insert(TABLE_USER_DATA, null, contentValues);
        return result != -1;
    }

    // Metoda për të marrë të gjitha të dhënat nga tabelën user_data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER_DATA, null);
    }

    // Metoda për të përditësuar të dhënat në tabelën user_data
    public boolean updateData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        // Përditësimi i rreshtit me ID e dhënë dhe kthimi i rezultatit
        int result = db.update(TABLE_USER_DATA, contentValues, COL_DATA_ID + " = ?", new String[]{id});
        return result > 0;
    }

    // Metoda për të fshirë të dhënat nga tabelën user_data bazuar në ID
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USER_DATA, COL_DATA_ID + " = ?", new String[]{id});
    }

    // Metodë për të marrë listën e librave të rezervuar si një List<String>
    public List<String> getReservedBooks() {
        List<String> reservedBooks = new ArrayList<>();

        // Përdorim të try-with-resources për të mbyllur automatikisht bazën e të dhënave dhe cursor-in
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_DATA, null)) {

            // Kontrollojmë nëse cursor-i ka të paktën një rresht
            if (cursor.moveToFirst()) {
                // Marrim indeksin e kolonës
                int nameIndex = cursor.getColumnIndex(COL_NAME);

                // Kontrollojmë nëse indeksi i kolonës është valid
                if (nameIndex != -1) {
                    do {
                        String name = cursor.getString(nameIndex);
                        reservedBooks.add(name);
                    } while (cursor.moveToNext());
                } else {
                    Log.e("DatabaseHelper", "Column index for " + COL_NAME + " is -1, column might not exist.");
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving reserved books.", e);
        }

        return reservedBooks;
    }

}
